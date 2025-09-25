package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.ProductEntity;
import java.util.List;
@Dao public interface ProductDao {
  @Query("SELECT * FROM products WHERE categoryId=:cid ORDER BY name")
  LiveData<java.util.List<ProductEntity>> byCategory(int cid);
  @Query("SELECT * FROM products WHERE productId=:pid LIMIT 1")
  LiveData<ProductEntity> byId(int pid);
  @Query("SELECT * FROM products WHERE productId=:pid LIMIT 1")
  ProductEntity byIdNow(int pid);
  @Insert(onConflict=OnConflictStrategy.REPLACE) void upsertAll(java.util.List<ProductEntity> list);
}
