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

  // ====== Thêm mới cho Edit Profile ======

  // Cập nhật Họ tên / Email / SĐT (giữ nguyên các cột khác)
  @Query("UPDATE users SET fullName = :full, email = :email, phone = :phone WHERE username = :username")
  void updateProfile(String username, String full, String email, String phone);

  // Đổi mật khẩu (chỉ gọi khi người dùng nhập mật khẩu mới)
  @Query("UPDATE users SET passwordHash = :newHash WHERE username = :username")
  void updatePassword(String username, String newHash);

  // (tuỳ chọn) Nếu bạn muốn cập nhật theo entity:
  @Update
  int update(UserEntity user);
}
