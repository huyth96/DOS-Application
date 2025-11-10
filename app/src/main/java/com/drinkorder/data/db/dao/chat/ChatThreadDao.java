package com.drinkorder.data.db.dao.chat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.drinkorder.data.db.entity.chat.ChatThreadEntity;

import java.util.List;

@Dao
public interface ChatThreadDao {
  @Query("SELECT * FROM chat_threads ORDER BY lastTimestamp DESC")
  LiveData<List<ChatThreadEntity>> observeThreads();

  @Query("SELECT * FROM chat_threads WHERE threadId = :threadId LIMIT 1")
  ChatThreadEntity getThread(String threadId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void upsert(ChatThreadEntity entity);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void upsert(List<ChatThreadEntity> entities);

  @Query(
      "UPDATE chat_threads SET lastMessage = :lastMessage, lastSenderRole = :lastSenderRole, " +
      "lastTimestamp = :lastTimestamp, updatedAt = :updatedAt WHERE threadId = :threadId")
  void updateLastMessage(String threadId, String lastMessage, String lastSenderRole, long lastTimestamp, long updatedAt);

  @Query("UPDATE chat_threads SET unreadCount = :count, updatedAt = :updatedAt WHERE threadId = :threadId")
  void updateUnread(String threadId, int count, long updatedAt);

  @Transaction
  default void touchThread(String threadId, int userId, String lastMessage, String lastSenderRole, long timestamp, int unreadDelta) {
    ChatThreadEntity existing = getThread(threadId);
    ChatThreadEntity target = existing == null ? new ChatThreadEntity() : existing;
    target.threadId = threadId;
    target.userId = existing == null ? userId : existing.userId;
    target.lastMessage = lastMessage;
    target.lastSenderRole = lastSenderRole;
    target.lastTimestamp = timestamp;
    target.updatedAt = System.currentTimeMillis();
    target.unreadCount = Math.max(0, (existing == null ? 0 : existing.unreadCount) + unreadDelta);
    upsert(target);
  }
}
