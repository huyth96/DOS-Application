// Khai báo package com.drinkorder.data.db.entity cho toàn bộ lớp.
package com.drinkorder.data.db.entity;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Áp dụng annotation @Entity(tableName="payments", cho phần tử bên dưới.
@Entity(tableName="payments",
  // Thực thi câu lệnh: foreignKeys=@ForeignKey(entity=OrderEntity.class, parentColumns="orderId", childColumns="orderId", onDelete=ForeignKey.CASCADE),.
  foreignKeys=@ForeignKey(entity=OrderEntity.class, parentColumns="orderId", childColumns="orderId", onDelete=ForeignKey.CASCADE),
  // Thực hiện lời gọi phương thức hoặc khởi tạo: indices=@Index("orderId")).
  indices=@Index("orderId"))
// Định nghĩa lớp PaymentEntity.
public class PaymentEntity {
  // Áp dụng annotation @PrimaryKey(autoGenerate=true) cho phần tử bên dưới.
  @PrimaryKey(autoGenerate=true) public int paymentId;
  // Khai báo thuộc tính với phạm vi truy cập: public int orderId.
  public int orderId;
  // Khai báo thuộc tính với phạm vi truy cập: public double paidAmount.
  public double paidAmount;
  // Khai báo thuộc tính với phạm vi truy cập: public String method.
  public String method;
  // Khai báo thuộc tính với phạm vi truy cập: public String status.
  public String status;
  // Khai báo thuộc tính với phạm vi truy cập: public long createdAt.
  public long createdAt;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
