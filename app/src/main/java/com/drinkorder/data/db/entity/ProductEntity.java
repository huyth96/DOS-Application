// Khai báo package com.drinkorder.data.db.entity cho toàn bộ lớp.
package com.drinkorder.data.db.entity;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Áp dụng annotation @Entity(tableName="products", cho phần tử bên dưới.
@Entity(tableName="products",
  // Thực thi câu lệnh: foreignKeys=@ForeignKey(entity=CategoryEntity.class, parentColumns="categoryId", childColumns="categoryId", onDelete=ForeignKey.RESTRICT),.
  foreignKeys=@ForeignKey(entity=CategoryEntity.class, parentColumns="categoryId", childColumns="categoryId", onDelete=ForeignKey.RESTRICT),
  // Thực hiện lời gọi phương thức hoặc khởi tạo: indices={@Index("categoryId"), @Index("name")}).
  indices={@Index("categoryId"), @Index("name")})
// Định nghĩa lớp ProductEntity.
public class ProductEntity {
  // Áp dụng annotation @PrimaryKey(autoGenerate=true) cho phần tử bên dưới.
  @PrimaryKey(autoGenerate=true) public int productId;
  // Khai báo thuộc tính với phạm vi truy cập: public String name.
  public String name;
  // Khai báo thuộc tính với phạm vi truy cập: public String description.
  public String description;
  // Khai báo thuộc tính với phạm vi truy cập: public double price.
  public double price;
  // Khai báo thuộc tính với phạm vi truy cập: public Integer stock.
  public Integer stock;
  // Khai báo thuộc tính với phạm vi truy cập: public String imageUrl.
  public String imageUrl;
  // Khai báo thuộc tính với phạm vi truy cập: public Double rating.
  public Double rating;
  // Khai báo thuộc tính với phạm vi truy cập: public int categoryId.
  public int categoryId;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
