package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.CategoryEntity;
import java.util.List;
@Dao public interface CategoryDao {
  @Query("SELECT * FROM categories ORDER BY name") LiveData<java.util.List<CategoryEntity>> all();
  @Insert(onConflict=OnConflictStrategy.REPLACE) void upsertAll(java.util.List<CategoryEntity> list);
}
