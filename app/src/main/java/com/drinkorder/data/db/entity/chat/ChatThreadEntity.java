package com.drinkorder.data.db.entity.chat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "chat_threads",
    indices = {
        @Index("userId"),
        @Index("lastTimestamp")
    }
)
public class ChatThreadEntity {
  @PrimaryKey
  @NonNull
  public String threadId;
  public int userId;
  public String title;
  public String lastMessage;
  public String lastSenderRole;
  public long lastTimestamp;
  public int unreadCount;
  public long updatedAt;

  public ChatThreadEntity() {
    this.threadId = "";
  }
}
