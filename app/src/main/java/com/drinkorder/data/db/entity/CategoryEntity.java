package com.drinkorder.data.db.entity;
import androidx.room.*;
@Entity(tableName="categories")
public class CategoryEntity {
  @PrimaryKey(autoGenerate=true) public int categoryId;
  public String name;
  public String description;
  public long createdAt;
}
