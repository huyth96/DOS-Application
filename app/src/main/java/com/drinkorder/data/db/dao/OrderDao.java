package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.db.entity.OrderItemEntity;
@Dao public interface OrderDao {
  @Insert long insert(OrderEntity o);
  @Insert void insertItems(java.util.List<OrderItemEntity> items);
  @Transaction
  @Query("SELECT * FROM orders WHERE userId=:uid ORDER BY createdAt DESC")
  LiveData<java.util.List<OrderEntity>> byUser(int uid);

  // ====== Auto-added for Order Detail ======
  @androidx.room.Transaction
  @androidx.room.Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
  androidx.lifecycle.LiveData<com.drinkorder.data.db.pojo.OrderWithItems> getOrderWithItems(int orderId);

  @androidx.room.Transaction
  @androidx.room.Query("SELECT * FROM order_items WHERE orderId = :orderId")
  androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.pojo.OrderItemWithProduct>> getItemsWithProduct(int orderId);
  // =========================================

    @androidx.room.Query("SELECT * FROM orders ORDER BY createdAt DESC")
    androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.entity.OrderEntity>> getAllOrders();
}
