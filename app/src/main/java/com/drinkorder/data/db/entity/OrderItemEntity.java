package com.drinkorder.data.db.entity;
import androidx.room.*;
@Entity(tableName="order_items",
  foreignKeys={
    @ForeignKey(entity=OrderEntity.class, parentColumns="orderId", childColumns="orderId", onDelete=ForeignKey.CASCADE),
    @ForeignKey(entity=ProductEntity.class, parentColumns="productId", childColumns="productId", onDelete=ForeignKey.RESTRICT)
  },
  indices={@Index("orderId"), @Index("productId")})
public class OrderItemEntity {
  @PrimaryKey(autoGenerate=true) public int orderItemId;
  public int orderId;
  public int productId;
  public int quantity;
  public double unitPrice;
}
