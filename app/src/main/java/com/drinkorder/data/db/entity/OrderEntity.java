// Khai báo package com.drinkorder.data.db.entity cho toàn bộ lớp.
package com.drinkorder.data.db.entity;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Áp dụng annotation @Entity(tableName="orders", cho phần tử bên dưới.
@Entity(tableName="orders",
  // Thực thi câu lệnh: indices={@Index("userId"), @Index("createdAt")},.
  indices={@Index("userId"), @Index("createdAt")},
  // Thực hiện lời gọi phương thức hoặc khởi tạo: foreignKeys=@ForeignKey(entity=UserEntity.class, parentColumns="userId", childColumns="userId", onDelete=ForeignKey.RESTRICT)).
  foreignKeys=@ForeignKey(entity=UserEntity.class, parentColumns="userId", childColumns="userId", onDelete=ForeignKey.RESTRICT))
// Định nghĩa lớp OrderEntity.
public class OrderEntity {
  // Áp dụng annotation @PrimaryKey(autoGenerate=true) cho phần tử bên dưới.
  @PrimaryKey(autoGenerate=true) public int orderId;
  // Khai báo thuộc tính với phạm vi truy cập: public int userId.
  public int userId;
  // Khai báo thuộc tính với phạm vi truy cập: public double totalAmount.
  public double totalAmount;
  // Khai báo thuộc tính với phạm vi truy cập: public String orderStatus.
  public String orderStatus;
  // Khai báo thuộc tính với phạm vi truy cập: public String paymentStatus.
  public String paymentStatus;
  // Khai báo thuộc tính với phạm vi truy cập: public long createdAt.
  public long createdAt;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
