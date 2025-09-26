package com.drinkorder.data.db.pojo;
import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.db.entity.OrderItemEntity;

/**
 * Order + list of items; each item contains its Product via nested relation.
 * Room will resolve nested @Relation to OrderItemWithProduct automatically.
 */
public class OrderWithItems {
    @Embedded public OrderEntity order;

    @Relation(
        entity = OrderItemEntity.class,
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    public List<OrderItemWithProduct> items;
}
