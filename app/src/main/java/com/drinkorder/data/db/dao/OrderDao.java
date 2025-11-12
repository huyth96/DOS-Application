package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.db.entity.OrderItemEntity;
@Dao public interface OrderDao {
  // Chèn 1 order vào bảng orders, trả về id của order vừa chèn
  @Insert long insert(OrderEntity o);
  // Chèn nhiều order items vào bảng order_items
  @Insert void insertItems(java.util.List<OrderItemEntity> items);

  // Truy vấn tất cả đơn của 1 user theo userId, sắp xếp theo createdAt giảm dần
  //@Transaction bảo đảm rằng nếu OrderEntity có quan hệ với các bảng khác (ví dụ OrderItemEntity)
  // và Room phải join nhiều bảng để tạo OrderWithItems, toàn bộ truy vấn sẽ được thực hiện
  // trong một transaction duy nhất, tránh dữ liệu bị “nửa vời” nếu có vấn đề xảy ra.
  @Transaction
  @Query("SELECT * FROM orders WHERE userId=:uid ORDER BY createdAt DESC")
  LiveData<java.util.List<OrderEntity>> byUser(int uid);

  // ====== Auto-added for Order Detail ======

  // Lấy chi tiết 1 order cùng với tất cả các item liên quan
  @androidx.room.Transaction
  @androidx.room.Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
  androidx.lifecycle.LiveData<com.drinkorder.data.db.pojo.OrderWithItems> getOrderWithItems(int orderId);

  // Lấy tất cả items của 1 order, kèm thông tin product tương ứng
  @androidx.room.Transaction
  @androidx.room.Query("SELECT * FROM order_items WHERE orderId = :orderId")
  androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.pojo.OrderItemWithProduct>> getItemsWithProduct(int orderId);
  // =========================================

  // Lấy tất cả orders, sắp xếp theo ngày tạo giảm dần
    @androidx.room.Query("SELECT * FROM orders ORDER BY createdAt DESC")
    androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.entity.OrderEntity>> getAllOrders();
}
