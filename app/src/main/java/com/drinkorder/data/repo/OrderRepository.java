package com.drinkorder.data.repo;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.drinkorder.data.db.dao.CartDao;
import com.drinkorder.data.db.dao.OrderDao;
import com.drinkorder.data.db.dao.PaymentDao;
import com.drinkorder.data.db.dao.ProductDao;
import com.drinkorder.data.db.entity.CartItemEntity;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.db.entity.OrderItemEntity;
import com.drinkorder.data.db.entity.PaymentEntity;
import com.drinkorder.data.db.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Repository xử lý đặt hàng offline (Room).
 * - Tính tổng từ giỏ hàng hiện tại
 * - Chốt giá theo Product tại thời điểm đặt
 * - Tạo Order, OrderItems, Payment
 * - Xoá Cart
 * - Trả kết quả về UI thread qua Callback
 */
public class OrderRepository {

  public interface Callback {
    void onSuccess(long orderId);
    void onError(Throwable t);
  }

  private final OrderDao orderDao;
  private final PaymentDao paymentDao;
  private final CartDao cartDao;
  private final ProductDao productDao;

  private final Executor io;              // chạy nền
  private static final Handler MAIN = new Handler(Looper.getMainLooper()); // trả về UI

  public OrderRepository(OrderDao orderDao,
                         PaymentDao paymentDao,
                         CartDao cartDao,
                         ProductDao productDao) {
    this(orderDao, paymentDao, cartDao, productDao, Executors.newSingleThreadExecutor());
  }

  public OrderRepository(OrderDao orderDao,
                         PaymentDao paymentDao,
                         CartDao cartDao,
                         ProductDao productDao,
                         Executor ioExecutor) {
    this.orderDao = orderDao;
    this.paymentDao = paymentDao;
    this.cartDao = cartDao;
    this.productDao = productDao;
    this.io = ioExecutor == null ? Executors.newSingleThreadExecutor() : ioExecutor;
  }

  /**
   * Tiến hành checkout:
   *  - Đọc toàn bộ Cart ngay lúc gọi (sync)
   *  - Tính tổng, tạo Order/Items/Payment
   *  - Clear cart
   *  - onSuccess/onError luôn được post về Main thread
   */
  public void checkout(int userId, @Nullable String paymentMethod, @Nullable Callback cb) {
    io.execute(() -> {
      try {
        // 1) Lấy items hiện tại trong giỏ
        List<CartItemEntity> cartItems = cartDao.allNow();
        if (cartItems == null || cartItems.isEmpty()) {
          throw new IllegalStateException("Cart is empty");
        }

        // 2) Tính tổng và build danh sách OrderItemEntity
        double total = 0d;
        List<OrderItemEntity> orderItems = new ArrayList<>(cartItems.size());
        for (CartItemEntity ci : cartItems) {
          ProductEntity p = productDao.byIdNow(ci.productId);
          if (p == null) {
            throw new IllegalStateException("Product not found: " + ci.productId);
          }
          double price = p.price; // chốt giá tại thời điểm đặt
          total += price * ci.quantity;

          OrderItemEntity oi = new OrderItemEntity();
          oi.productId = p.productId;
          oi.quantity = ci.quantity;
          oi.unitPrice = price;
          orderItems.add(oi);
        }

        // 3) Tạo Order
        OrderEntity order = new OrderEntity();
        order.userId = userId;
        order.totalAmount = total;
        order.orderStatus = "completed";
        order.paymentStatus = "paid";
        order.createdAt = System.currentTimeMillis();
        long orderId = orderDao.insert(order);

        // 4) Gắn orderId cho từng item và insert
        for (OrderItemEntity oi : orderItems) {
          oi.orderId = (int) orderId;
        }
        orderDao.insertItems(orderItems);

        // 5) Tạo Payment (giả lập thành công)
        PaymentEntity payment = new PaymentEntity();
        payment.orderId = (int) orderId;
        payment.paidAmount = total;
        payment.method = (paymentMethod == null || paymentMethod.isEmpty()) ? "Cash" : paymentMethod;
        payment.status = "success";
        payment.createdAt = System.currentTimeMillis();
        paymentDao.insert(payment);

        // 6) Clear Cart
        cartDao.clear();

        // 7) Trả kết quả về UI thread
        if (cb != null) MAIN.post(() -> cb.onSuccess(orderId));
      } catch (Throwable t) {
        if (cb != null) MAIN.post(() -> cb.onError(t));
      }
    });
  }
}
