package com.drinkorder.data.db.dao;
import androidx.room.*;
import com.drinkorder.data.db.entity.UserEntity;
@Dao public interface UserDao {
  @Query("SELECT * FROM users WHERE username=:u LIMIT 1") UserEntity findByUsername(String u);
  @Insert(onConflict=OnConflictStrategy.ABORT) long insert(UserEntity u);
  @Query("SELECT COUNT(*) FROM users") int count();
}
