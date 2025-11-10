// Khai báo package com.drinkorder.data.db.pojo cho toàn bộ lớp.
package com.drinkorder.data.db.pojo;

// Import androidx.room.Embedded để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Embedded;
// Import androidx.room.Relation để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Relation;

// Import com.drinkorder.data.db.entity.CartItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CartItemEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;

/**
 * Represents one cart row along with the associated product.
 */
// Định nghĩa lớp CartItemWithProduct.
public class CartItemWithProduct {
  // Áp dụng annotation @Embedded cho phần tử bên dưới.
  @Embedded public CartItemEntity item;

  // Áp dụng annotation @Relation( cho phần tử bên dưới.
  @Relation(
      // Thực thi câu lệnh: parentColumn = "productId",.
      parentColumn = "productId",
      // Thực thi câu lệnh: entityColumn = "productId".
      entityColumn = "productId"
  // Thực hiện lời gọi phương thức hoặc khởi tạo: ).
  )
  // Khai báo thuộc tính với phạm vi truy cập: public ProductEntity product.
  public ProductEntity product;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
