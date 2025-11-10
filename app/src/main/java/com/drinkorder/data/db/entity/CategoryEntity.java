// Khai báo package com.drinkorder.data.db.entity cho toàn bộ lớp.
package com.drinkorder.data.db.entity;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Áp dụng annotation @Entity(tableName="categories") cho phần tử bên dưới.
@Entity(tableName="categories")
// Định nghĩa lớp CategoryEntity.
public class CategoryEntity {
  // Áp dụng annotation @PrimaryKey(autoGenerate=true) cho phần tử bên dưới.
  @PrimaryKey(autoGenerate=true) public int categoryId;
  // Khai báo thuộc tính với phạm vi truy cập: public String name.
  public String name;
  // Khai báo thuộc tính với phạm vi truy cập: public String description.
  public String description;
  // Khai báo thuộc tính với phạm vi truy cập: public long createdAt.
  public long createdAt;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
