package com.drinkorder.data.db.pojo;
import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.db.entity.OrderItemEntity;

/**
 * POJO đại diện cho một đơn hàng kèm danh sách item.
 * Mỗi item sẽ tự động chứa thông tin sản phẩm nhờ nested @Relation.
 * Room sẽ tự resolve các @Relation lồng nhau:
 * Order -> OrderItem -> Product
 */
public class OrderWithItems {
    // @Embedded nhúng toàn bộ cột của OrderEntity vào đây
    @Embedded public OrderEntity order;

    // @Relation tạo mối quan hệ 1-n: 1 order có nhiều order items (List)
    // parentColumn = orderId của OrderEntity
    // entityColumn = orderId của OrderItemEntity
    // Room sẽ tự động map từng OrderItemWithProduct vào list items
    @Relation(
        entity = OrderItemEntity.class,
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    public List<OrderItemWithProduct> items;
}
