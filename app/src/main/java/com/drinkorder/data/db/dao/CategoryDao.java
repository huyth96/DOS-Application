package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.CategoryEntity;
import java.util.List;
@Dao public interface CategoryDao {
  @Query("SELECT * FROM categories ORDER BY name")
  LiveData<List<CategoryEntity>> all();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void upsertAll(List<CategoryEntity> list);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(CategoryEntity category);

  @Update
  int update(CategoryEntity category);

  @Delete
  int delete(CategoryEntity category);

  @Query("DELETE FROM categories WHERE categoryId=:categoryId")
  void deleteById(int categoryId);
}
