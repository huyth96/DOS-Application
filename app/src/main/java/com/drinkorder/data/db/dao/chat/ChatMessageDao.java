package com.drinkorder.data.db.dao.chat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drinkorder.data.db.entity.chat.ChatMessageEntity;

import java.util.List;

@Dao
public interface ChatMessageDao {
  @Query("SELECT * FROM chat_messages WHERE threadId = :threadId ORDER BY sentAt ASC")
  LiveData<List<ChatMessageEntity>> observeMessages(String threadId);

  @Query("SELECT * FROM chat_messages WHERE messageId = :messageId LIMIT 1")
  ChatMessageEntity findById(String messageId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void upsert(ChatMessageEntity entity);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void upsert(List<ChatMessageEntity> entities);

  @Query("UPDATE chat_messages SET deliveredAt = :deliveredAt, isPending = 0 WHERE messageId = :messageId")
  void markDelivered(String messageId, Long deliveredAt);

  @Query("UPDATE chat_messages SET messageId = :newMessageId, deliveredAt = :deliveredAt, isPending = 0 WHERE messageId = :clientMessageId")
  void acknowledge(String clientMessageId, String newMessageId, Long deliveredAt);

  @Query("UPDATE chat_messages SET isPending = 0 WHERE messageId IN(:ids)")
  void clearPending(List<String> ids);

  @Query("DELETE FROM chat_messages WHERE threadId = :threadId AND messageId = :messageId")
  void deleteMessage(String threadId, String messageId);
}
