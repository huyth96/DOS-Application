package com.drinkorder.data.db.entity;
import androidx.room.*;
@Entity(tableName="order_items",
  foreignKeys={
    @ForeignKey(entity=OrderEntity.class, parentColumns="orderId", childColumns="orderId",
            onDelete=ForeignKey.CASCADE),//nếu order bị xóa, tất cả item của order cũng xóa theo
    @ForeignKey(entity=ProductEntity.class, parentColumns="productId", childColumns="productId",
            onDelete=ForeignKey.RESTRICT) // không cho xóa product nếu còn order item tham chiếu
  },
  indices={@Index("orderId"), @Index("productId")})// tạo index để tra cứu nhanh theo orderId hoặc productId
public class OrderItemEntity {
  @PrimaryKey(autoGenerate=true) public int orderItemId;
  public int orderId;
  public int productId;
  public int quantity;
  public double unitPrice;
}
