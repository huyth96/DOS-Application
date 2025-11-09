package com.drinkorder.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {
  @Query("SELECT * FROM categories ORDER BY name")
  LiveData<List<CategoryEntity>> all();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(CategoryEntity category);  // Thêm phương thức insert cho một entity đơn lẻ

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void upsertAll(List<CategoryEntity> list);

  @Update
  void update(CategoryEntity category);

  @Delete
  void delete(CategoryEntity category);
}