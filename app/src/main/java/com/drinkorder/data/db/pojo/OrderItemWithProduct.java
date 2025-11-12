package com.drinkorder.data.db.pojo;
import androidx.room.Embedded;
import androidx.room.Relation;
import com.drinkorder.data.db.entity.OrderItemEntity;
import com.drinkorder.data.db.entity.ProductEntity;
// Kết quả truy vấn có join dữ liệu: OrderItem + Product
/** Một order item kèm chi tiết sản phẩm tương ứng */
public class OrderItemWithProduct {
    // @Embedded nhúng toàn bộ cột của OrderItemEntity vào POJO này
    @Embedded public OrderItemEntity item;

    // @Relation giúp Room tự động join ProductEntity dựa trên productId
    // parentColumn = cột trong OrderItemEntity
    // entityColumn = cột trong ProductEntity
    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    public ProductEntity product;
}
