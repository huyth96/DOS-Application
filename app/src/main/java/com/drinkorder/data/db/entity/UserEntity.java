package com.drinkorder.data.db.entity;

import androidx.annotation.NonNull;
import androidx.room.*;

/**
 * UserEntity
 * ===================================
 * Lớp đại diện cho bảng `users` trong Room Database.
 *
 * 💡 Vai trò:
 * - Mỗi đối tượng UserEntity tương ứng với 1 dòng (record) trong bảng `users`.
 * - Room sẽ tự động map các trường (field) trong class thành cột (column) của bảng.
 *
 * ⚙️ Được dùng trong:
 * - UserDao để truy vấn / thêm / cập nhật thông tin người dùng.
 * - AuthRepository để xác thực đăng nhập / đăng ký.
 * - AdminUsersFragment để hiển thị danh sách user.
 */
@Entity(
        tableName = "users",
        indices = @Index(value = "username", unique = true) // username là duy nhất
)
public class UserEntity {

  /**
   * 🔑 Khóa chính (Primary Key)
   * ----------------------------
   * - `autoGenerate = true` nghĩa là Room sẽ tự động tăng userId cho mỗi bản ghi mới.
   * - Dùng để phân biệt các user trong database.
   */
  @PrimaryKey(autoGenerate = true)
  public int userId;

  /**
   * 🧍 Username (duy nhất)
   * ----------------------------
   * - Dùng để đăng nhập.
   * - Được đánh dấu @NonNull vì không được phép null.
   * - Có unique index trong @Entity (đảm bảo không trùng nhau).
   */
  @NonNull
  public String username;

  /**
   * 🔒 Mật khẩu người dùng
   * ----------------------------
   * - Trong bản demo đang lưu plaintext (passwordHash).
   * - Khi triển khai thật, nên lưu hash + salt để bảo mật.
   */
  @NonNull
  public String passwordHash;

  /**
   * 👤 Thông tin cá nhân mở rộng
   * ----------------------------
   * - fullName: Họ tên hiển thị.
   * - email: Địa chỉ email liên hệ.
   * - phone: Số điện thoại.
   * - role: Vai trò của tài khoản (ví dụ: "customer", "admin", "staff").
   */
  public String fullName;
  public String email;
  public String phone;
  public String role;

  /**
   * 🕓 Thời điểm tạo tài khoản
   * ----------------------------
   * - Lưu dưới dạng epoch time (System.currentTimeMillis()).
   * - Dùng để sắp xếp danh sách theo thời gian tạo.
   */
  public long createdAt;

  /**
   * 🚫 Trạng thái bị khóa tài khoản
   * ----------------------------
   * - true  → bị ban (không cho đăng nhập).
   * - false → hoạt động bình thường.
   * - Được admin thay đổi trong Admin panel.
   */
  public boolean isBanned;
}
