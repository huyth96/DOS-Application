// Khai báo package com.drinkorder.data.repo cho toàn bộ lớp.
package com.drinkorder.data.repo;

// Import android.os.Handler để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Handler;
// Import android.os.Looper để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Looper;

// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;

// Import com.drinkorder.data.db.dao.CartDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.CartDao;
// Import com.drinkorder.data.db.dao.OrderDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.OrderDao;
// Import com.drinkorder.data.db.dao.PaymentDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.PaymentDao;
// Import com.drinkorder.data.db.dao.ProductDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.ProductDao;
// Import com.drinkorder.data.db.entity.CartItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CartItemEntity;
// Import com.drinkorder.data.db.entity.OrderEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderEntity;
// Import com.drinkorder.data.db.entity.OrderItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderItemEntity;
// Import com.drinkorder.data.db.entity.PaymentEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.PaymentEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;

// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.concurrent.Executor để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executor;
// Import java.util.concurrent.Executors để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executors;

/**
 * Repository xử lý đặt hàng offline (Room).
 * - Tính tổng từ giỏ hàng hiện tại
 * - Chốt giá theo Product tại thời điểm đặt
 * - Tạo Order, OrderItems, Payment
 * - Xoá Cart
 * - Trả kết quả về UI thread qua Callback
 */
// Định nghĩa lớp OrderRepository.
public class OrderRepository {

  // Định nghĩa interface Callback.
  public interface Callback {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onSuccess(long orderId);.
    void onSuccess(long orderId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onError(Throwable t);.
    void onError(Throwable t);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private final OrderDao orderDao.
  private final OrderDao orderDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final PaymentDao paymentDao.
  private final PaymentDao paymentDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final CartDao cartDao.
  private final CartDao cartDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final ProductDao productDao.
  private final ProductDao productDao;

  // Thực thi câu lệnh: private final Executor io;              // chạy nền.
  private final Executor io;              // chạy nền
  // Định nghĩa phương thức Handler với phạm vi truy cập tương ứng.
  private static final Handler MAIN = new Handler(Looper.getMainLooper()); // trả về UI

  // Định nghĩa phương thức OrderRepository với phạm vi truy cập tương ứng.
  public OrderRepository(OrderDao orderDao,
                         // Thực thi câu lệnh: PaymentDao paymentDao,.
                         PaymentDao paymentDao,
                         // Thực thi câu lệnh: CartDao cartDao,.
                         CartDao cartDao,
                         // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
                         ProductDao productDao) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: this(orderDao, paymentDao, cartDao, productDao, Executors.newSingleThreadExecutor());.
    this(orderDao, paymentDao, cartDao, productDao, Executors.newSingleThreadExecutor());
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức OrderRepository với phạm vi truy cập tương ứng.
  public OrderRepository(OrderDao orderDao,
                         // Thực thi câu lệnh: PaymentDao paymentDao,.
                         PaymentDao paymentDao,
                         // Thực thi câu lệnh: CartDao cartDao,.
                         CartDao cartDao,
                         // Thực thi câu lệnh: ProductDao productDao,.
                         ProductDao productDao,
                         // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
                         Executor ioExecutor) {
    // Gán giá trị cho biến hoặc thuộc tính: this.orderDao = orderDao.
    this.orderDao = orderDao;
    // Gán giá trị cho biến hoặc thuộc tính: this.paymentDao = paymentDao.
    this.paymentDao = paymentDao;
    // Gán giá trị cho biến hoặc thuộc tính: this.cartDao = cartDao.
    this.cartDao = cartDao;
    // Gán giá trị cho biến hoặc thuộc tính: this.productDao = productDao.
    this.productDao = productDao;
    // Gán giá trị cho biến hoặc thuộc tính: this.io = ioExecutor == null ? Executors.newSingleThreadExecutor() : ioExecutor.
    this.io = ioExecutor == null ? Executors.newSingleThreadExecutor() : ioExecutor;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  /**
   * Tiến hành checkout:
   *  - Đọc toàn bộ Cart ngay lúc gọi (sync)
   *  - Tính tổng, tạo Order/Items/Payment
   *  - Clear cart
   *  - onSuccess/onError luôn được post về Main thread
   */
  // Định nghĩa phương thức checkout với phạm vi truy cập tương ứng.
  public void checkout(int userId, @Nullable String paymentMethod, @Nullable Callback cb) {
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    io.execute(() -> {
      // Bắt đầu khối try để bắt lỗi có thể phát sinh.
      try {
        // 1) Lấy items hiện tại trong giỏ
        // Thực hiện lời gọi phương thức hoặc khởi tạo: List<CartItemEntity> cartItems = cartDao.allNow();.
        List<CartItemEntity> cartItems = cartDao.allNow();
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (cartItems == null || cartItems.isEmpty()) {
          // Ném ngoại lệ để thông báo lỗi ra bên ngoài.
          throw new IllegalStateException("Cart is empty");
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }

        // 2) Tính tổng và build danh sách OrderItemEntity
        // Gán giá trị cho biến hoặc thuộc tính: double total = 0d.
        double total = 0d;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: List<OrderItemEntity> orderItems = new ArrayList<>(cartItems.size());.
        List<OrderItemEntity> orderItems = new ArrayList<>(cartItems.size());
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (CartItemEntity ci : cartItems) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: ProductEntity p = productDao.byIdNow(ci.productId);.
          ProductEntity p = productDao.byIdNow(ci.productId);
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (p == null) {
            // Ném ngoại lệ để thông báo lỗi ra bên ngoài.
            throw new IllegalStateException("Product not found: " + ci.productId);
          // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
          }
          // Thực thi câu lệnh: double price = p.price; // chốt giá tại thời điểm đặt.
          double price = p.price; // chốt giá tại thời điểm đặt
          // Gán giá trị cho biến hoặc thuộc tính: total += price * ci.quantity.
          total += price * ci.quantity;

          // Thực hiện lời gọi phương thức hoặc khởi tạo: OrderItemEntity oi = new OrderItemEntity();.
          OrderItemEntity oi = new OrderItemEntity();
          // Gán giá trị cho biến hoặc thuộc tính: oi.productId = p.productId.
          oi.productId = p.productId;
          // Gán giá trị cho biến hoặc thuộc tính: oi.quantity = ci.quantity.
          oi.quantity = ci.quantity;
          // Gán giá trị cho biến hoặc thuộc tính: oi.unitPrice = price.
          oi.unitPrice = price;
          // Thực hiện lời gọi phương thức hoặc khởi tạo: orderItems.add(oi);.
          orderItems.add(oi);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }

        // 3) Tạo Order
        // Thực hiện lời gọi phương thức hoặc khởi tạo: OrderEntity order = new OrderEntity();.
        OrderEntity order = new OrderEntity();
        // Gán giá trị cho biến hoặc thuộc tính: order.userId = userId.
        order.userId = userId;
        // Gán giá trị cho biến hoặc thuộc tính: order.totalAmount = total.
        order.totalAmount = total;
        // Gán giá trị cho biến hoặc thuộc tính: order.orderStatus = "completed".
        order.orderStatus = "completed";
        // Gán giá trị cho biến hoặc thuộc tính: order.paymentStatus = "paid".
        order.paymentStatus = "paid";
        // Thực hiện lời gọi phương thức hoặc khởi tạo: order.createdAt = System.currentTimeMillis();.
        order.createdAt = System.currentTimeMillis();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: long orderId = orderDao.insert(order);.
        long orderId = orderDao.insert(order);

        // 4) Gắn orderId cho từng item và insert
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (OrderItemEntity oi : orderItems) {
          // Gán giá trị cho biến hoặc thuộc tính: oi.orderId = (int) orderId.
          oi.orderId = (int) orderId;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Thực hiện lời gọi phương thức hoặc khởi tạo: orderDao.insertItems(orderItems);.
        orderDao.insertItems(orderItems);

        // 5) Tạo Payment (giả lập thành công)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: PaymentEntity payment = new PaymentEntity();.
        PaymentEntity payment = new PaymentEntity();
        // Gán giá trị cho biến hoặc thuộc tính: payment.orderId = (int) orderId.
        payment.orderId = (int) orderId;
        // Gán giá trị cho biến hoặc thuộc tính: payment.paidAmount = total.
        payment.paidAmount = total;
        // Gán giá trị cho biến hoặc thuộc tính: payment.method = (paymentMethod == null || paymentMethod.isEmpty()) ? "Cash" : paymentMethod.
        payment.method = (paymentMethod == null || paymentMethod.isEmpty()) ? "Cash" : paymentMethod;
        // Gán giá trị cho biến hoặc thuộc tính: payment.status = "success".
        payment.status = "success";
        // Thực hiện lời gọi phương thức hoặc khởi tạo: payment.createdAt = System.currentTimeMillis();.
        payment.createdAt = System.currentTimeMillis();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: paymentDao.insert(payment);.
        paymentDao.insert(payment);

        // 6) Clear Cart
        // Thực hiện lời gọi phương thức hoặc khởi tạo: cartDao.clear();.
        cartDao.clear();

        // 7) Trả kết quả về UI thread
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (cb != null) MAIN.post(() -> cb.onSuccess(orderId));
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      } catch (Throwable t) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (cb != null) MAIN.post(() -> cb.onError(t));
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
