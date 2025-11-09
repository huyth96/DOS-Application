package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.drinkorder.data.db.entity.CartItemEntity;
import com.drinkorder.data.db.pojo.CartItemWithProduct;
@Dao public interface CartDao {
  @Query("SELECT * FROM cart_items") LiveData<java.util.List<CartItemEntity>> all();
  @Query("SELECT * FROM cart_items") java.util.List<CartItemEntity> allNow();
  @Insert(onConflict=OnConflictStrategy.REPLACE) void upsert(CartItemEntity e);
  @Query("DELETE FROM cart_items WHERE cartItemId=:id") void remove(int id);
  @Query("DELETE FROM cart_items") void clear();
  @Query("UPDATE cart_items SET quantity=:q WHERE productId=:pid") void setQty(int pid, int q);
  @Transaction
  @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
  LiveData<java.util.List<CartItemWithProduct>> allWithProducts();
}
