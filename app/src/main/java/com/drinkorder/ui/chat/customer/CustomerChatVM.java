package com.drinkorder.ui.chat.customer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.ChatMessageEntity;
import com.drinkorder.data.repo.AuthRepository;
import com.drinkorder.data.repo.ChatRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerChatVM extends AndroidViewModel {

  public enum ConnectionState {
    CONNECTING,
    READY,
    ERROR
  }

  public enum SendState {
    IDLE,
    SENDING,
    ERROR
  }

  private final ChatRepository chatRepository;
  private final AuthRepository authRepository;
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final MutableLiveData<Long> threadIdLiveData = new MutableLiveData<>();
  private final MutableLiveData<Integer> userIdLiveData = new MutableLiveData<>();
  private final MediatorLiveData<List<CustomerChatMessageItem>> messageItems =
      new MediatorLiveData<>();
  private final MutableLiveData<ConnectionState> connectionState =
      new MutableLiveData<>(ConnectionState.CONNECTING);
  private final MutableLiveData<SendState> sendState = new MutableLiveData<>(SendState.IDLE);
  private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
  private final MutableLiveData<String> connectionErrorMessage = new MutableLiveData<>();
  private final LiveData<Integer> unreadCount;
  private final LiveData<List<ChatMessageEntity>> rawMessages;

  private int currentUserId;

  public CustomerChatVM(@NonNull Application app) {
    super(app);
    AppDatabase db = AppDatabase.get(app);
    SharedPreferences sp = app.getSharedPreferences("auth", Context.MODE_PRIVATE);
    authRepository = new AuthRepository(db.userDao(), sp);
    chatRepository = new ChatRepository(db.chatDao());

    currentUserId = authRepository.userId();
    userIdLiveData.setValue(currentUserId);

    unreadCount = Transformations.switchMap(
        userIdLiveData,
        id -> {
          MutableLiveData<Integer> zero = new MutableLiveData<>();
          if (id == null || id <= 0) {
            zero.setValue(0);
            return zero;
          }
          return Transformations.map(
              chatRepository.unreadCount(id, "customer"),
              count -> count == null ? 0 : count
          );
        });

    rawMessages = Transformations.switchMap(
        threadIdLiveData,
        threadId -> {
          MutableLiveData<List<ChatMessageEntity>> empty = new MutableLiveData<>();
          if (threadId == null) {
            empty.setValue(new ArrayList<>());
            return empty;
          }
          return chatRepository.messages(threadId);
        });

    messageItems.addSource(rawMessages, list -> {
      messageItems.setValue(mapMessages(list));
      if (list != null && !list.isEmpty()) {
        for (ChatMessageEntity entity : list) {
          if (entity == null) continue;
          if (!isMine(entity) && !entity.isRead) {
            markThreadAsReadInternal();
            break;
          }
        }
      }
    });

    if (currentUserId <= 0) {
      connectionState.setValue(ConnectionState.ERROR);
      connectionErrorMessage.setValue(app.getString(R.string.chat_connection_failed_not_logged_in));
    } else {
      initializeThread();
    }
  }

  public LiveData<List<CustomerChatMessageItem>> getMessages() {
    return messageItems;
  }

  public LiveData<ConnectionState> getConnectionState() {
    return connectionState;
  }

  public LiveData<SendState> getSendState() {
    return sendState;
  }

  public LiveData<String> getErrorMessage() {
    return errorMessage;
  }

  public LiveData<String> getConnectionErrorMessage() {
    return connectionErrorMessage;
  }

  public LiveData<Integer> getUnreadCount() {
    return unreadCount;
  }

  public boolean sendMessage(String body) {
    if (TextUtils.isEmpty(body)) {
      errorMessage.setValue(getApplication().getString(R.string.chat_empty_message));
      return false;
    }
    if (currentUserId <= 0) {
      errorMessage.setValue(getApplication().getString(R.string.chat_connection_failed_not_logged_in));
      return false;
    }
    Long threadId = threadIdLiveData.getValue();
    if (threadId == null) {
      errorMessage.setValue(getApplication().getString(R.string.chat_not_ready));
      return false;
    }
    sendState.setValue(SendState.SENDING);
    chatRepository.sendMessage(
        threadId,
        body,
        currentUserId,
        "customer",
        new ChatRepository.SendCallback() {
          @Override
          public void onSuccess() {
            sendState.setValue(SendState.IDLE);
          }

          @Override
          public void onError(Throwable t) {
            sendState.setValue(SendState.ERROR);
            if (t != null && !TextUtils.isEmpty(t.getMessage())) {
              errorMessage.setValue(t.getMessage());
            } else {
              errorMessage.setValue(getApplication().getString(R.string.chat_send_failed));
            }
          }
        });
    return true;
  }

  public void retryConnection() {
    if (currentUserId <= 0) {
      connectionState.setValue(ConnectionState.ERROR);
      connectionErrorMessage.setValue(
          getApplication().getString(R.string.chat_connection_failed_not_logged_in));
      return;
    }
    initializeThread();
  }

  public void markThreadAsRead() {
    markThreadAsReadInternal();
  }

  public void clearErrorMessage() {
    errorMessage.setValue(null);
  }

  public void resetSendState() {
    sendState.setValue(SendState.IDLE);
  }

  private void initializeThread() {
    connectionState.setValue(ConnectionState.CONNECTING);
    connectionErrorMessage.setValue(null);
    executor.execute(() -> {
      try {
        long threadId = chatRepository.ensureThread(currentUserId);
        threadIdLiveData.postValue(threadId);
        connectionState.postValue(ConnectionState.READY);
      } catch (Exception e) {
        connectionState.postValue(ConnectionState.ERROR);
        connectionErrorMessage.postValue(
            getApplication().getString(R.string.chat_connection_failed_generic));
      }
    });
  }

  private List<CustomerChatMessageItem> mapMessages(List<ChatMessageEntity> list) {
    List<CustomerChatMessageItem> items = new ArrayList<>();
    if (list == null) {
      return items;
    }
    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
    for (ChatMessageEntity entity : list) {
      if (entity == null) continue;
      boolean mine = isMine(entity);
      String text = entity.body == null ? "" : entity.body;
      String time = format.format(new Date(entity.createdAt));
      items.add(new CustomerChatMessageItem(entity.messageId, text, time, mine));
    }
    return items;
  }

  private boolean isMine(ChatMessageEntity entity) {
    if (entity == null) return false;
    return "customer".equalsIgnoreCase(entity.senderRole) && entity.senderId == currentUserId;
  }

  private void markThreadAsReadInternal() {
    Long threadId = threadIdLiveData.getValue();
    if (threadId == null) return;
    chatRepository.markThreadAsRead(threadId, "customer");
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    executor.shutdown();
    chatRepository.shutdown();
  }
}
