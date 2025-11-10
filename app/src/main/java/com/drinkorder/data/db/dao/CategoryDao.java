// Khai báo package com.drinkorder.data.db.dao cho toàn bộ lớp.
package com.drinkorder.data.db.dao;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Định nghĩa interface CategoryDao.
@Dao public interface CategoryDao {
  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM categories ORDER BY name")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<List<CategoryEntity>> all();.
  LiveData<List<CategoryEntity>> all();

  // Áp dụng annotation @Insert(onConflict cho phần tử bên dưới.
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void upsertAll(List<CategoryEntity> list);.
  void upsertAll(List<CategoryEntity> list);

  // Áp dụng annotation @Insert(onConflict cho phần tử bên dưới.
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: long insert(CategoryEntity category);.
  long insert(CategoryEntity category);

  // Áp dụng annotation @Update cho phần tử bên dưới.
  @Update
  // Thực hiện lời gọi phương thức hoặc khởi tạo: int update(CategoryEntity category);.
  int update(CategoryEntity category);

  // Áp dụng annotation @Delete cho phần tử bên dưới.
  @Delete
  // Thực hiện lời gọi phương thức hoặc khởi tạo: int delete(CategoryEntity category);.
  int delete(CategoryEntity category);

  // Áp dụng annotation @Query("DELETE cho phần tử bên dưới.
  @Query("DELETE FROM categories WHERE categoryId=:categoryId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void deleteById(int categoryId);.
  void deleteById(int categoryId);
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
