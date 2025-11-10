// Khai báo package com.drinkorder.data.db.entity cho toàn bộ lớp.
package com.drinkorder.data.db.entity;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Áp dụng annotation @Entity(tableName="order_items", cho phần tử bên dưới.
@Entity(tableName="order_items",
  // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
  foreignKeys={
    // Áp dụng annotation @ForeignKey(entity=OrderEntity.class, cho phần tử bên dưới.
    @ForeignKey(entity=OrderEntity.class, parentColumns="orderId", childColumns="orderId", onDelete=ForeignKey.CASCADE),
    // Áp dụng annotation @ForeignKey(entity=ProductEntity.class, cho phần tử bên dưới.
    @ForeignKey(entity=ProductEntity.class, parentColumns="productId", childColumns="productId", onDelete=ForeignKey.RESTRICT)
  // Thực thi câu lệnh: },.
  },
  // Thực hiện lời gọi phương thức hoặc khởi tạo: indices={@Index("orderId"), @Index("productId")}).
  indices={@Index("orderId"), @Index("productId")})
// Định nghĩa lớp OrderItemEntity.
public class OrderItemEntity {
  // Áp dụng annotation @PrimaryKey(autoGenerate=true) cho phần tử bên dưới.
  @PrimaryKey(autoGenerate=true) public int orderItemId;
  // Khai báo thuộc tính với phạm vi truy cập: public int orderId.
  public int orderId;
  // Khai báo thuộc tính với phạm vi truy cập: public int productId.
  public int productId;
  // Khai báo thuộc tính với phạm vi truy cập: public int quantity.
  public int quantity;
  // Khai báo thuộc tính với phạm vi truy cập: public double unitPrice.
  public double unitPrice;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
