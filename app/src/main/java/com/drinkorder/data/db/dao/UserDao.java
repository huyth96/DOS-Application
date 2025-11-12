package com.drinkorder.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.drinkorder.data.db.entity.UserEntity;

import java.util.List;

/**
 * UserDao
 * ===========================
 * Data Access Object (DAO) cho bảng `users` trong Room Database.
 *
 * 💡 Nhiệm vụ:
 * - Cung cấp các hàm truy vấn, thêm, cập nhật dữ liệu user.
 * - Là tầng trung gian giữa Room (SQLite) và các lớp Repository/ViewModel.
 *
 * ⚙️ Room sẽ tự động tạo phần triển khai thật khi build dự án.
 */
@Dao
public interface UserDao {

  /**
   * Tìm người dùng theo username.
   * ------------------------------
   * - Trả về duy nhất 1 kết quả (LIMIT 1).
   * - Thường được dùng trong quá trình đăng nhập.
   *
   * @param u username cần tìm.
   * @return UserEntity nếu tồn tại, ngược lại null.
   */
  @Query("SELECT * FROM users WHERE username = :u LIMIT 1")
  UserEntity findByUsername(String u);

  /**
   * Thêm người dùng mới vào bảng.
   * ------------------------------
   * - Dùng annotation @Insert để Room tự động sinh SQL tương ứng.
   * - OnConflictStrategy.ABORT: nếu username đã tồn tại (unique), Room sẽ báo lỗi và rollback.
   *
   * @param u user cần thêm.
   * @return ID của user mới thêm (Room tự sinh auto-increment).
   */
  @Insert(onConflict = OnConflictStrategy.ABORT)
  long insert(UserEntity u);

  /**
   * Đếm tổng số lượng người dùng hiện có trong hệ thống.
   * ------------------------------
   * @return số lượng user (COUNT(*)).
   */
  @Query("SELECT COUNT(*) FROM users")
  int count();

  /**
   * Theo dõi danh sách toàn bộ người dùng dưới dạng LiveData.
   * ------------------------------
   * - LiveData giúp UI tự động cập nhật khi DB thay đổi (Reactive).
   * - Dữ liệu được sắp xếp theo thời gian tạo mới nhất (DESC).
   *
   * @return LiveData<List<UserEntity>> để dùng trong ViewModel.
   */
  @Query("SELECT * FROM users ORDER BY createdAt DESC")
  LiveData<List<UserEntity>> observeAll();

  /**
   * Cập nhật trạng thái ban/unban cho user.
   * ------------------------------
   * - Dùng trong Admin panel để khóa/mở khóa tài khoản.
   * - Chỉ cập nhật trường `isBanned` dựa trên userId.
   *
   * @param userId ID người dùng cần cập nhật.
   * @param banned true = ban, false = unban.
   */
  @Query("UPDATE users SET isBanned = :banned WHERE userId = :userId")
  void updateBanStatus(int userId, boolean banned);

  // ====== PHẦN DÀNH CHO CHỈNH SỬA HỒ SƠ (Edit Profile) ======

  /**
   * Cập nhật thông tin cá nhân (họ tên, email, số điện thoại).
   * ------------------------------
   * - Không thay đổi mật khẩu hoặc role.
   * - Xác định user dựa vào username.
   *
   * @param username username của người dùng.
   * @param full     họ tên mới.
   * @param email    email mới.
   * @param phone    số điện thoại mới.
   */
  @Query("UPDATE users SET fullName = :full, email = :email, phone = :phone WHERE username = :username")
  void updateProfile(String username, String full, String email, String phone);

  /**
   * Đổi mật khẩu cho user.
   * ------------------------------
   * - Chỉ thay đổi trường passwordHash.
   * - Chỉ dùng khi người dùng thực sự nhập mật khẩu mới.
   *
   * @param username username của người dùng.
   * @param newHash  mật khẩu mới (hiện demo chưa hash).
   */
  @Query("UPDATE users SET passwordHash = :newHash WHERE username = :username")
  void updatePassword(String username, String newHash);

  /**
   * Cập nhật thông tin user bằng cách truyền vào toàn bộ entity.
   * ------------------------------
   * - Dùng @Update (Room sẽ tự xác định hàng nào cần update dựa theo khóa chính userId).
   * - Trả về số dòng bị ảnh hưởng (1 nếu thành công).
   *
   * ⚠️ Lưu ý: chỉ nên dùng khi bạn đã có entity đầy đủ (bao gồm userId).
   *
   * @param user UserEntity cần cập nhật.
   * @return số dòng được cập nhật.
   */
  @Update
  int update(UserEntity user);
}
