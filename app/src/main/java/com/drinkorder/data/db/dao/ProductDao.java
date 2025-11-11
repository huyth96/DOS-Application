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

  @Insert(onConflict=OnConflictStrategy.REPLACE)
  void upsertAll(java.util.List<ProductEntity> list);

  @Query("SELECT * FROM products ORDER BY name")
  LiveData<java.util.List<ProductEntity>> all();

  @Query("SELECT * FROM products ORDER BY name")
  java.util.List<ProductEntity> allNow();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(ProductEntity product);

  @Update
  int update(ProductEntity product);

  @Delete
  int delete(ProductEntity product);

  @Query("DELETE FROM products WHERE productId=:productId")
  void deleteById(int productId);

}
