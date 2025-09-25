package com.drinkorder.data.db.entity;
import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(tableName="users", indices=@Index(value="username", unique=true))
public class UserEntity {
  @PrimaryKey(autoGenerate=true) public int userId;
  @NonNull public String username;
  @NonNull public String passwordHash;
  public String fullName, email, phone, role;
  public long createdAt;
}
