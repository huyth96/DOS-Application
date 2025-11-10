package com.drinkorder.data.db.dao;

import androidx.room.*;
import com.drinkorder.data.db.entity.UserEntity;

@Dao
public interface UserDao {
  @Query("SELECT * FROM users WHERE username = :u LIMIT 1")
  UserEntity findByUsername(String u);

  @Insert(onConflict = OnConflictStrategy.ABORT)
  long insert(UserEntity u);

  @Query("SELECT COUNT(*) FROM users")
  int count();

  // ====== Entries for Edit Profile ======

  // Update full name / email / phone while keeping other columns
  @Query("UPDATE users SET fullName = :full, email = :email, phone = :phone WHERE username = :username")
  void updateProfile(String username, String full, String email, String phone);

  // Change password (only when the user provides a new one)
  @Query("UPDATE users SET passwordHash = :newHash WHERE username = :username")
  void updatePassword(String username, String newHash);

  // (Optional) Update via entity if needed:
  @Update
  int update(UserEntity user);
}
