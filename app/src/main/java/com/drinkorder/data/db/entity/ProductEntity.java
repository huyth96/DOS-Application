package com.drinkorder.data.db.entity;
import androidx.room.*;
@Entity(tableName="products",
  foreignKeys=@ForeignKey(entity=CategoryEntity.class, parentColumns="categoryId", childColumns="categoryId", onDelete=ForeignKey.RESTRICT),
  indices={@Index("categoryId"), @Index("name")})
public class ProductEntity {
  @PrimaryKey(autoGenerate=true) public int productId;
  public String name;
  public String description;
  public double price;
  public Integer stock;
  public String imageUrl;
  public int categoryId;
}
