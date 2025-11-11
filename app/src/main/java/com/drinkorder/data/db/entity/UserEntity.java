package com.drinkorder.data.db.entity;
import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(tableName="users", indices=@Index(value="username", unique=true))
public class UserEntity {
  /** Khóa chính tăng tự động. */
  @PrimaryKey(autoGenerate=true) public int userId;
  /** Tên đăng nhập (duy nhất). */
  @NonNull public String username;
  /** Mật khẩu (demo: lưu thẳng; thực tế cần hash/salt). */
  @NonNull public String passwordHash;
  /** Họ tên, email, số điện thoại, vai trò (vd: customer/admin). */
  public String fullName, email, phone, role;
  /** Thời điểm tạo tài khoản (epoch millis). */
  public long createdAt;
  /** Đánh dấu tài khoản bị khóa (ban). */
  public boolean isBanned;
}
