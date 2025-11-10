package com.drinkorder.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChatThreadSummary {
  private final String threadId;
  private final int userId;
  private final String displayName;
  private final String username;
  private final String lastMessage;
  private final long lastMessageAt;
  private final int unreadCount;
  private final boolean online;
  private final ChatMessage.SenderRole lastSenderRole;

  public ChatThreadSummary(@NonNull String threadId, int userId, @NonNull String displayName,
                           @NonNull String username, @Nullable String lastMessage,
                           long lastMessageAt, int unreadCount, boolean online,
                           @NonNull ChatMessage.SenderRole lastSenderRole) {
    this.threadId = threadId;
    this.userId = userId;
    this.displayName = displayName;
    this.username = username;
    this.lastMessage = lastMessage;
    this.lastMessageAt = lastMessageAt;
    this.unreadCount = unreadCount;
    this.online = online;
    this.lastSenderRole = lastSenderRole;
  }

  @NonNull
  public String getThreadId() {
    return threadId;
  }

  public int getUserId() {
    return userId;
  }

  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  @NonNull
  public String getUsername() {
    return username;
  }

  @Nullable
  public String getLastMessage() {
    return lastMessage;
  }

  public long getLastMessageAt() {
    return lastMessageAt;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public boolean isOnline() {
    return online;
  }

  @NonNull
  public ChatMessage.SenderRole getLastSenderRole() {
    return lastSenderRole;
  }
}
