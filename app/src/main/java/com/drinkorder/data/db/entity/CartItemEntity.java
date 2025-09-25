package com.drinkorder.data.db.entity;
import androidx.room.*;
@Entity(tableName="cart_items",
  indices=@Index(value={"productId"}, unique=true),
  foreignKeys=@ForeignKey(entity=ProductEntity.class, parentColumns="productId", childColumns="productId", onDelete=ForeignKey.RESTRICT))
public class CartItemEntity {
  @PrimaryKey(autoGenerate=true) public int cartItemId;
  public int productId;
  public int quantity;
  public long addedAt;
}
