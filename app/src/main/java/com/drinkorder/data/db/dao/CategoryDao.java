package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.CategoryEntity;
import java.util.List;
/**
 * DAO cho bảng categories trong Room, hỗ trợ CRUD trên CategoryEntity.
 */
@Dao
public interface CategoryDao {

  /**
   * Lấy tất cả danh mục sắp xếp theo tên, dưới dạng LiveData.
   *
   * @return LiveData danh sách CategoryEntity.
   */
  @Query("SELECT * FROM categories ORDER BY name")
  LiveData<List<CategoryEntity>> all();

  /**
   * Upsert danh sách danh mục (thay thế nếu xung đột).
   *
   * @param list Danh sách CategoryEntity.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void upsertAll(List<CategoryEntity> list);

  /**
   * Chèn danh mục mới, trả về ID.
   *
   * @param category CategoryEntity cần chèn.
   * @return ID của danh mục.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(CategoryEntity category);

  /**
   * Cập nhật danh mục, trả về số hàng ảnh hưởng.
   *
   * @param category CategoryEntity cần cập nhật.
   * @return Số hàng cập nhật.
   */
  @Update
  int update(CategoryEntity category);

  /**
   * Xóa danh mục, trả về số hàng ảnh hưởng.
   *
   * @param category CategoryEntity cần xóa.
   * @return Số hàng xóa.
   */
  @Delete
  int delete(CategoryEntity category);

  /**
   * Xóa danh mục theo ID.
   *
   * @param categoryId ID cần xóa.
   */
  @Query("DELETE FROM categories WHERE categoryId=:categoryId")
  void deleteById(int categoryId);
}
