package com.drinkorder.data.db.entity.chat;

import androidx.annotation.NonNull;
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
        @Index("sentAt")
    }
)
public class ChatMessageEntity {
  @PrimaryKey
  @NonNull
  public String messageId;
  @NonNull
  public String threadId;
  public String senderRole;
  public String body;
  public long sentAt;
  public Long deliveredAt;
  public boolean isOutgoing;
  public boolean isPending;

  public ChatMessageEntity() {
    this.messageId = "";
    this.threadId = "";
  }
}
