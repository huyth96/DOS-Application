// Khai báo package com.drinkorder.data.db.entity cho toàn bộ lớp.
package com.drinkorder.data.db.entity;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Áp dụng annotation @Entity(tableName="cart_items", cho phần tử bên dưới.
@Entity(tableName="cart_items",
  // Thực thi câu lệnh: indices=@Index(value={"productId"}, unique=true),.
  indices=@Index(value={"productId"}, unique=true),
  // Thực hiện lời gọi phương thức hoặc khởi tạo: foreignKeys=@ForeignKey(entity=ProductEntity.class, parentColumns="productId", childColumns="productId", onDelete=ForeignKey.RESTRICT)).
  foreignKeys=@ForeignKey(entity=ProductEntity.class, parentColumns="productId", childColumns="productId", onDelete=ForeignKey.RESTRICT))
// Định nghĩa lớp CartItemEntity.
public class CartItemEntity {
  // Áp dụng annotation @PrimaryKey(autoGenerate=true) cho phần tử bên dưới.
  @PrimaryKey(autoGenerate=true) public int cartItemId;
  // Khai báo thuộc tính với phạm vi truy cập: public int productId.
  public int productId;
  // Khai báo thuộc tính với phạm vi truy cập: public int quantity.
  public int quantity;
  // Khai báo thuộc tính với phạm vi truy cập: public long addedAt.
  public long addedAt;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
