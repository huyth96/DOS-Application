// Khai báo package com.drinkorder.data.db.dao cho toàn bộ lớp.
package com.drinkorder.data.db.dao;

// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;

// Import com.drinkorder.data.db.entity.UserEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.UserEntity;

// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;

// Áp dụng annotation @Dao cho phần tử bên dưới.
@Dao
// Định nghĩa interface UserDao.
public interface UserDao {
  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM users WHERE username = :u LIMIT 1")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: UserEntity findByUsername(String u);.
  UserEntity findByUsername(String u);

  // Áp dụng annotation @Insert(onConflict cho phần tử bên dưới.
  @Insert(onConflict = OnConflictStrategy.ABORT)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: long insert(UserEntity u);.
  long insert(UserEntity u);

  // Áp dụng annotation @Query("SELECT và ghi đè phương thức COUNT.
  @Query("SELECT COUNT(*) FROM users")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: int count();.
  int count();

  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM users ORDER BY createdAt DESC")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<List<UserEntity>> observeAll();.
  LiveData<List<UserEntity>> observeAll();

  // Áp dụng annotation @Query("UPDATE cho phần tử bên dưới.
  @Query("UPDATE users SET isBanned = :banned WHERE userId = :userId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void updateBanStatus(int userId, boolean banned);.
  void updateBanStatus(int userId, boolean banned);

  // ====== Entries for Edit Profile ======

  // Update full name / email / phone while keeping other columns
  // Áp dụng annotation @Query("UPDATE cho phần tử bên dưới.
  @Query("UPDATE users SET fullName = :full, email = :email, phone = :phone WHERE username = :username")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void updateProfile(String username, String full, String email, String phone);.
  void updateProfile(String username, String full, String email, String phone);

  // Change password (only when the user provides a new one)
  // Áp dụng annotation @Query("UPDATE cho phần tử bên dưới.
  @Query("UPDATE users SET passwordHash = :newHash WHERE username = :username")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void updatePassword(String username, String newHash);.
  void updatePassword(String username, String newHash);

  // (Optional) Update via entity if needed:
  // Áp dụng annotation @Update cho phần tử bên dưới.
  @Update
  // Thực hiện lời gọi phương thức hoặc khởi tạo: int update(UserEntity user);.
  int update(UserEntity user);
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
