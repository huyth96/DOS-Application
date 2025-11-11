package com.drinkorder.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.drinkorder.data.db.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
  /** Tìm người dùng theo username (tối đa 1 kết quả). */
  @Query("SELECT * FROM users WHERE username = :u LIMIT 1")
  UserEntity findByUsername(String u);

  /** Thêm người dùng mới. OnConflictStrategy.ABORT để báo lỗi khi trùng username (unique index). */
  @Insert(onConflict = OnConflictStrategy.ABORT)
  long insert(UserEntity u);

  /** Đếm tổng số người dùng. */
  @Query("SELECT COUNT(*) FROM users")
  int count();

  /** Theo dõi (LiveData) danh sách người dùng, sắp xếp theo thời điểm tạo mới nhất. */
  @Query("SELECT * FROM users ORDER BY createdAt DESC")
  LiveData<List<UserEntity>> observeAll();

  /** Cập nhật trạng thái ban/unban cho user theo userId. */
  @Query("UPDATE users SET isBanned = :banned WHERE userId = :userId")
  void updateBanStatus(int userId, boolean banned);

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
