package com.drinkorder.data.repo;
import com.drinkorder.data.db.dao.*;
import com.drinkorder.data.db.entity.*;
import java.util.*;
import java.util.concurrent.Executors;

public class OrderRepository {
  private final OrderDao orderDao; private final PaymentDao paymentDao; private final CartDao cartDao; private final ProductDao productDao;
  public OrderRepository(OrderDao o, PaymentDao p, CartDao c, ProductDao prod){ orderDao=o; paymentDao=p; cartDao=c; productDao=prod; }
  public interface Callback { void onSuccess(long orderId); void onError(Throwable t); }
  public void checkout(int userId, String paymentMethod, Callback cb){
    Executors.newSingleThreadExecutor().execute(() -> {
      try {
        java.util.List<CartItemEntity> items = cartDao.allNow();
        if (items.isEmpty()) throw new IllegalStateException("Cart is empty");
        double total = 0; java.util.List<OrderItemEntity> orderItems = new ArrayList<>();
        for (CartItemEntity ci: items){
          ProductEntity p = productDao.byIdNow(ci.productId);
          if (p==null) throw new IllegalStateException("Product not found: "+ci.productId);
          total += p.price * ci.quantity;
          OrderItemEntity oi = new OrderItemEntity(); oi.productId=p.productId; oi.quantity=ci.quantity; oi.unitPrice=p.price; orderItems.add(oi);
        }
        OrderEntity order = new OrderEntity(); order.userId=userId; order.totalAmount=total; order.orderStatus="completed"; order.paymentStatus="paid"; order.createdAt=System.currentTimeMillis();
        long orderId = orderDao.insert(order);
        for (OrderItemEntity oi: orderItems) oi.orderId=(int)orderId; orderDao.insertItems(orderItems);
        PaymentEntity pay=new PaymentEntity(); pay.orderId=(int)orderId; pay.paidAmount=total; pay.method=paymentMethod==null?"Cash":paymentMethod; pay.status="success"; pay.createdAt=System.currentTimeMillis(); paymentDao.insert(pay);
        cartDao.clear();
        if (cb!=null) cb.onSuccess(orderId);
      } catch (Throwable t){ if (cb!=null) cb.onError(t); }
    });
  }
}
