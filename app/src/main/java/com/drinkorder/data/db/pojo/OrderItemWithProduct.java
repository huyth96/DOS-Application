package com.drinkorder.data.db.pojo;
import androidx.room.Embedded;
import androidx.room.Relation;
import com.drinkorder.data.db.entity.OrderItemEntity;
import com.drinkorder.data.db.entity.ProductEntity;

/** One order item + its product detail */
public class OrderItemWithProduct {
    @Embedded public OrderItemEntity item;

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    public ProductEntity product;
}
