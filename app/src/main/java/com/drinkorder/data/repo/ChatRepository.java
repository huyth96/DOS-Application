package com.drinkorder.data.repo;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.drinkorder.data.db.dao.ChatDao;
import com.drinkorder.data.db.entity.ChatMessageEntity;
import com.drinkorder.data.db.entity.ChatThreadEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRepository {
  private final ChatDao chatDao;
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final Handler mainHandler = new Handler(Looper.getMainLooper());

  public ChatRepository(ChatDao chatDao) {
    this.chatDao = chatDao;
  }

  public long ensureThread(int userId) {
    ChatThreadEntity existing = chatDao.findThreadByUser(userId);
    if (existing != null) {
      return existing.threadId;
    }
    ChatThreadEntity thread = new ChatThreadEntity();
    long now = System.currentTimeMillis();
    thread.userId = userId;
    thread.createdAt = now;
    thread.updatedAt = now;
    return chatDao.insertThread(thread);
  }

  public LiveData<List<ChatMessageEntity>> messages(long threadId) {
    return chatDao.messagesByThread(threadId);
  }

  public LiveData<Integer> unreadCount(int userId, String viewerRole) {
    return chatDao.observeUnreadForUser(userId, viewerRole);
  }

  public void markThreadAsRead(long threadId, String viewerRole) {
    executor.execute(() -> chatDao.markThreadMessagesRead(threadId, viewerRole));
  }

  public void sendMessage(long threadId, String body, int senderId, String senderRole, SendCallback callback) {
    executor.execute(() -> {
      try {
        ChatMessageEntity message = new ChatMessageEntity();
        long now = System.currentTimeMillis();
        message.threadId = threadId;
        message.senderId = senderId;
        message.senderRole = senderRole;
        message.body = body;
        message.createdAt = now;
        message.isRead = "customer".equalsIgnoreCase(senderRole);
        chatDao.insertMessage(message);
        chatDao.updateThreadTimestamp(threadId, now);
        if (callback != null) {
          mainHandler.post(callback::onSuccess);
        }
      } catch (Exception e) {
        if (callback != null) {
          mainHandler.post(() -> callback.onError(e));
        }
      }
    });
  }

  public void shutdown() {
    executor.shutdown();
  }

  public interface SendCallback {
    void onSuccess();
    void onError(Throwable t);
  }
}
