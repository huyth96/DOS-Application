package com.drinkorder.data.model;

import androidx.annotation.NonNull;

public class ChatMessage {
  public enum SenderRole { ADMIN, CUSTOMER }

  private final String id;
  private final String threadId;
  private final SenderRole senderRole;
  private final String content;
  private final long timestamp;
  private final boolean read;

  public ChatMessage(@NonNull String id, @NonNull String threadId, @NonNull SenderRole senderRole,
                     @NonNull String content, long timestamp, boolean read) {
    this.id = id;
    this.threadId = threadId;
    this.senderRole = senderRole;
    this.content = content;
    this.timestamp = timestamp;
    this.read = read;
  }

  @NonNull
  public String getId() {
    return id;
  }

  @NonNull
  public String getThreadId() {
    return threadId;
  }

  @NonNull
  public SenderRole getSenderRole() {
    return senderRole;
  }

  @NonNull
  public String getContent() {
    return content;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public boolean isRead() {
    return read;
  }

  public ChatMessage markRead() {
    if (read) return this;
    return new ChatMessage(id, threadId, senderRole, content, timestamp, true);
  }
}
