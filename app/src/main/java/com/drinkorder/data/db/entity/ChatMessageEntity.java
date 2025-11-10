package com.drinkorder.data.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "chat_messages",
    foreignKeys = @ForeignKey(
        entity = ChatThreadEntity.class,
        parentColumns = "threadId",
        childColumns = "threadId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {
        @Index("threadId"),
        @Index(value = {"threadId", "isRead"})
    }
)
public class ChatMessageEntity {
  @PrimaryKey(autoGenerate = true)
  public long messageId;
  public long threadId;
  public int senderId;
  public String senderRole;
  public String body;
  public long createdAt;
  @ColumnInfo(defaultValue = "0")
  public boolean isRead;
}
