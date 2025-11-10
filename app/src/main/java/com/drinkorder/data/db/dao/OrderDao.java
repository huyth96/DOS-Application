// Khai báo package com.drinkorder.data.db.dao cho toàn bộ lớp.
package com.drinkorder.data.db.dao;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Import com.drinkorder.data.db.entity.OrderEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderEntity;
// Import com.drinkorder.data.db.entity.OrderItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderItemEntity;
// Định nghĩa interface OrderDao.
@Dao public interface OrderDao {
  // Áp dụng annotation @Insert và ghi đè phương thức insert.
  @Insert long insert(OrderEntity o);
  // Áp dụng annotation @Insert và ghi đè phương thức insertItems.
  @Insert void insertItems(java.util.List<OrderItemEntity> items);
  // Áp dụng annotation @Transaction cho phần tử bên dưới.
  @Transaction
  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM orders WHERE userId=:uid ORDER BY createdAt DESC")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<java.util.List<OrderEntity>> byUser(int uid);.
  LiveData<java.util.List<OrderEntity>> byUser(int uid);

  // ====== Auto-added for Order Detail ======
  // Áp dụng annotation @androidx.room.Transaction cho phần tử bên dưới.
  @androidx.room.Transaction
  // Áp dụng annotation @androidx.room.Query("SELECT cho phần tử bên dưới.
  @androidx.room.Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: androidx.lifecycle.LiveData<com.drinkorder.data.db.pojo.OrderWithItems> getOrderWithItems(int orderId);.
  androidx.lifecycle.LiveData<com.drinkorder.data.db.pojo.OrderWithItems> getOrderWithItems(int orderId);

  // Áp dụng annotation @androidx.room.Transaction cho phần tử bên dưới.
  @androidx.room.Transaction
  // Áp dụng annotation @androidx.room.Query("SELECT cho phần tử bên dưới.
  @androidx.room.Query("SELECT * FROM order_items WHERE orderId = :orderId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.pojo.OrderItemWithProduct>> getItemsWithProduct(int orderId);.
  androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.pojo.OrderItemWithProduct>> getItemsWithProduct(int orderId);
  // =========================================

    // Áp dụng annotation @androidx.room.Query("SELECT cho phần tử bên dưới.
    @androidx.room.Query("SELECT * FROM orders ORDER BY createdAt DESC")
    // Thực hiện lời gọi phương thức hoặc khởi tạo: androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.entity.OrderEntity>> getAllOrders();.
    androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.entity.OrderEntity>> getAllOrders();
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
