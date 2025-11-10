// Khai báo package com.drinkorder.data.db.entity cho toàn bộ lớp.
package com.drinkorder.data.db.entity;
// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;

// Áp dụng annotation @Entity(tableName="users", và ghi đè phương thức Index.
@Entity(tableName="users", indices=@Index(value="username", unique=true))
// Định nghĩa lớp UserEntity.
public class UserEntity {
  // Áp dụng annotation @PrimaryKey(autoGenerate=true) cho phần tử bên dưới.
  @PrimaryKey(autoGenerate=true) public int userId;
  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull public String username;
  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull public String passwordHash;
  // Khai báo thuộc tính với phạm vi truy cập: public String fullName, email, phone, role.
  public String fullName, email, phone, role;
  // Khai báo thuộc tính với phạm vi truy cập: public long createdAt.
  public long createdAt;
  // Khai báo thuộc tính với phạm vi truy cập: public boolean isBanned.
  public boolean isBanned;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
