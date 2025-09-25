package com.drinkorder.data.db.entity;
import androidx.room.*;
@Entity(tableName="payments",
  foreignKeys=@ForeignKey(entity=OrderEntity.class, parentColumns="orderId", childColumns="orderId", onDelete=ForeignKey.CASCADE),
  indices=@Index("orderId"))
public class PaymentEntity {
  @PrimaryKey(autoGenerate=true) public int paymentId;
  public int orderId;
  public double paidAmount;
  public String method;
  public String status;
  public long createdAt;
}
