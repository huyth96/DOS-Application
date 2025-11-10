package com.drinkorder.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.drinkorder.data.db.entity.ChatMessageEntity;
import com.drinkorder.data.db.entity.ChatThreadEntity;

import java.util.List;

@Dao
public interface ChatDao {
  @Query("SELECT * FROM chat_threads WHERE userId = :userId LIMIT 1")
  ChatThreadEntity findThreadByUser(int userId);

  @Insert
  long insertThread(ChatThreadEntity thread);

  @Query("UPDATE chat_threads SET updatedAt = :updatedAt WHERE threadId = :threadId")
  void updateThreadTimestamp(long threadId, long updatedAt);

  @Insert
  long insertMessage(ChatMessageEntity message);

  @Query("SELECT * FROM chat_messages WHERE threadId = :threadId ORDER BY createdAt ASC")
  LiveData<List<ChatMessageEntity>> messagesByThread(long threadId);

  @Query("UPDATE chat_messages SET isRead = 1 WHERE threadId = :threadId AND senderRole != :viewerRole AND isRead = 0")
  void markThreadMessagesRead(long threadId, String viewerRole);

  @Query("SELECT COUNT(*) FROM chat_messages WHERE threadId IN (SELECT threadId FROM chat_threads WHERE userId = :userId) AND senderRole != :viewerRole AND isRead = 0")
  LiveData<Integer> observeUnreadForUser(int userId, String viewerRole);
}
