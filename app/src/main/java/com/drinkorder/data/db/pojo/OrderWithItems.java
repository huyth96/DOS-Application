// Khai báo package com.drinkorder.data.db.pojo cho toàn bộ lớp.
package com.drinkorder.data.db.pojo;
// Import androidx.room.Embedded để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Embedded;
// Import androidx.room.Relation để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Relation;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import com.drinkorder.data.db.entity.OrderEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderEntity;
// Import com.drinkorder.data.db.entity.OrderItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderItemEntity;

/**
 * Order + list of items; each item contains its Product via nested relation.
 * Room will resolve nested @Relation to OrderItemWithProduct automatically.
 */
// Định nghĩa lớp OrderWithItems.
public class OrderWithItems {
    // Áp dụng annotation @Embedded cho phần tử bên dưới.
    @Embedded public OrderEntity order;

    // Áp dụng annotation @Relation( cho phần tử bên dưới.
    @Relation(
        // Thực thi câu lệnh: entity = OrderItemEntity.class,.
        entity = OrderItemEntity.class,
        // Thực thi câu lệnh: parentColumn = "orderId",.
        parentColumn = "orderId",
        // Thực thi câu lệnh: entityColumn = "orderId".
        entityColumn = "orderId"
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ).
    )
    // Khai báo thuộc tính với phạm vi truy cập: public List<OrderItemWithProduct> items.
    public List<OrderItemWithProduct> items;
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
