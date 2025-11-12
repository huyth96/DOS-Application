package com.drinkorder.data.db.entity;
import androidx.room.*;
@Entity(tableName="orders",
  indices={@Index("userId"), @Index("createdAt")}// tạo index để tăng tốc truy vấn theo userId và createdAt
        , foreignKeys=@ForeignKey(// thiết lập khóa ngoại với bảng users
        entity=UserEntity.class,// bảng cha là UserEntity
        parentColumns="userId",// cột khóa chính của bảng cha
        childColumns="userId", // cột trong bảng orders tham chiếu
        onDelete=ForeignKey.RESTRICT))// nếu user bị xóa, orders vẫn giữ nguyên
public class OrderEntity {
  @PrimaryKey(autoGenerate=true) public int orderId;
  public int userId;
  public double totalAmount;
  public String orderStatus;
  public String paymentStatus;
  public long createdAt;
}
