package com.drinkorder.data.db.entity;
import androidx.room.*;
@Entity(tableName="orders",
  indices={@Index("userId"), @Index("createdAt")},
  foreignKeys=@ForeignKey(entity=UserEntity.class, parentColumns="userId", childColumns="userId", onDelete=ForeignKey.RESTRICT))
public class OrderEntity {
  @PrimaryKey(autoGenerate=true) public int orderId;
  public int userId;
  public double totalAmount;
  public String orderStatus;
  public String paymentStatus;
  public long createdAt;
}
