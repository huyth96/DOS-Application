package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.ProductEntity;
import java.util.List;

@Dao
public interface ProductDao {

  @Query("SELECT * FROM products WHERE categoryId=:cid ORDER BY name")
  LiveData<List<ProductEntity>> byCategory(int cid);

  @Query("SELECT * FROM products WHERE productId=:pid LIMIT 1")
  LiveData<ProductEntity> byId(int pid);

  @Query("SELECT * FROM products WHERE productId=:pid LIMIT 1")
  ProductEntity byIdNow(int pid);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void upsertAll(List<ProductEntity> list);

  @Query("SELECT * FROM products ORDER BY name")
  LiveData<List<ProductEntity>> all();

  @Query("SELECT * FROM products ORDER BY name")
  List<ProductEntity> allNow();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(ProductEntity product);

  @Update
  int update(ProductEntity product);

  @Delete
  int delete(ProductEntity product);

  @Query("DELETE FROM products WHERE productId=:productId")
  void deleteById(int productId);

  // search theo tên không phân biệt hoa thường
  @Query("SELECT * FROM products WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' ORDER BY name")
  LiveData<List<ProductEntity>> searchByName(String query);
}
