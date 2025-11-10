// Khai báo package com.drinkorder.data.db.dao cho toàn bộ lớp.
package com.drinkorder.data.db.dao;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Định nghĩa interface ProductDao.
@Dao public interface ProductDao {
  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM products WHERE categoryId=:cid ORDER BY name")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<java.util.List<ProductEntity>> byCategory(int cid);.
  LiveData<java.util.List<ProductEntity>> byCategory(int cid);

  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM products WHERE productId=:pid LIMIT 1")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<ProductEntity> byId(int pid);.
  LiveData<ProductEntity> byId(int pid);

  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM products WHERE productId=:pid LIMIT 1")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: ProductEntity byIdNow(int pid);.
  ProductEntity byIdNow(int pid);

  // Áp dụng annotation @Insert(onConflict=OnConflictStrategy.REPLACE) cho phần tử bên dưới.
  @Insert(onConflict=OnConflictStrategy.REPLACE)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void upsertAll(java.util.List<ProductEntity> list);.
  void upsertAll(java.util.List<ProductEntity> list);

  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM products ORDER BY name")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<java.util.List<ProductEntity>> all();.
  LiveData<java.util.List<ProductEntity>> all();

  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM products ORDER BY name")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: java.util.List<ProductEntity> allNow();.
  java.util.List<ProductEntity> allNow();

  // Áp dụng annotation @Insert(onConflict cho phần tử bên dưới.
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: long insert(ProductEntity product);.
  long insert(ProductEntity product);

  // Áp dụng annotation @Update cho phần tử bên dưới.
  @Update
  // Thực hiện lời gọi phương thức hoặc khởi tạo: int update(ProductEntity product);.
  int update(ProductEntity product);

  // Áp dụng annotation @Delete cho phần tử bên dưới.
  @Delete
  // Thực hiện lời gọi phương thức hoặc khởi tạo: int delete(ProductEntity product);.
  int delete(ProductEntity product);

  // Áp dụng annotation @Query("DELETE cho phần tử bên dưới.
  @Query("DELETE FROM products WHERE productId=:productId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void deleteById(int productId);.
  void deleteById(int productId);
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
