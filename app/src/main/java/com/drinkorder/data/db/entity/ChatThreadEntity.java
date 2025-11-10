package com.drinkorder.data.db.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_threads", indices = @Index(value = {"userId"}, unique = true))
public class ChatThreadEntity {
  @PrimaryKey(autoGenerate = true)
  public long threadId;
  public int userId;
  public long createdAt;
  public long updatedAt;
}
