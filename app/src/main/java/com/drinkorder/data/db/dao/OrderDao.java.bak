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
}
