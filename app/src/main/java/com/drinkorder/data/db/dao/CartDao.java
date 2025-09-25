package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.CartItemEntity;
@Dao public interface CartDao {
  @Query("SELECT * FROM cart_items") LiveData<java.util.List<CartItemEntity>> all();
  @Query("SELECT * FROM cart_items") java.util.List<CartItemEntity> allNow();
  @Insert(onConflict=OnConflictStrategy.REPLACE) void upsert(CartItemEntity e);
  @Query("DELETE FROM cart_items WHERE cartItemId=:id") void remove(int id);
  @Query("DELETE FROM cart_items") void clear();
  @Query("UPDATE cart_items SET quantity=:q WHERE productId=:pid") void setQty(int pid, int q);
}
