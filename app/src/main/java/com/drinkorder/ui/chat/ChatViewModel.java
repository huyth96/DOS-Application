package com.drinkorder.ui.chat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;
import com.drinkorder.data.remote.ChatSocketClient;
import com.drinkorder.data.repo.AuthRepository;
import com.drinkorder.data.repo.ChatRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {

  private final ChatRepository repository;
  private final AuthRepository authRepository;
  private final MediatorLiveData<List<ChatThreadEntity>> threads = new MediatorLiveData<>();
  private final MutableLiveData<String> activeThreadId = new MutableLiveData<>();
  private final MediatorLiveData<ChatThreadEntity> activeThread = new MediatorLiveData<>();
  private final MediatorLiveData<List<ChatMessageEntity>> messages = new MediatorLiveData<>();
  private final LiveData<List<ChatThreadEntity>> threadsSource;
  private LiveData<List<ChatMessageEntity>> currentMessagesSource;
  private final LiveData<ChatSocketClient.ConnectionState> connectionState;
  private final String senderRole;
  private final boolean isAdmin;
  private final String defaultThreadId;
  private final String localDisplayName;

  public ChatViewModel(@NonNull Application application) {
    super(application);
    AppDatabase db = AppDatabase.get(application);
    SharedPreferences sp = application.getSharedPreferences("auth", Context.MODE_PRIVATE);
    authRepository = new AuthRepository(db.userDao(), sp);
    repository = new ChatRepository(db, db.chatThreadDao(), db.chatMessageDao(), authRepository);
    String storedRole = authRepository.role();
    isAdmin = storedRole != null && storedRole.equalsIgnoreCase("admin");
    senderRole = isAdmin ? "support" : (storedRole == null ? "customer" : storedRole);
    localDisplayName = authRepository.getLoggedUserName();
    defaultThreadId = repository.defaultThreadId();
    if (!isAdmin && defaultThreadId != null) {
      activeThreadId.setValue(defaultThreadId);
      repository.ensureLocalThread(defaultThreadId, application.getString(R.string.chat_default_title));
    }

    threads.setValue(Collections.emptyList());
    messages.setValue(Collections.emptyList());

    threadsSource = repository.threads();
    threads.addSource(threadsSource, list -> {
      List<ChatThreadEntity> filtered = filterThreads(list);
      threads.setValue(filtered);
      ensureActiveThread(filtered);
      updateActiveThreadEntity();
    });

    activeThread.addSource(activeThreadId, ignored -> updateActiveThreadEntity());
    activeThread.addSource(threads, ignored -> updateActiveThreadEntity());

    messages.addSource(activeThreadId, this::switchMessageSource);

    connectionState = repository.connectionState();
  }

  private List<ChatThreadEntity> filterThreads(List<ChatThreadEntity> list) {
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }
    if (isAdmin || TextUtils.isEmpty(defaultThreadId)) {
      return list;
    }
    for (ChatThreadEntity entity : list) {
      if (entity != null && TextUtils.equals(defaultThreadId, entity.threadId)) {
        ArrayList<ChatThreadEntity> onlyDefault = new ArrayList<>(1);
        onlyDefault.add(entity);
        return onlyDefault;
      }
    }
    return Collections.emptyList();
  }

  private void ensureActiveThread(List<ChatThreadEntity> list) {
    if (list == null || list.isEmpty()) {
      if (defaultThreadId != null) {
        activeThreadId.setValue(defaultThreadId);
      } else {
        activeThreadId.setValue(null);
      }
      return;
    }
    String current = activeThreadId.getValue();
    if (!TextUtils.isEmpty(defaultThreadId)) {
      for (ChatThreadEntity entity : list) {
        if (entity != null && TextUtils.equals(defaultThreadId, entity.threadId)) {
          if (!TextUtils.equals(defaultThreadId, current)) {
            activeThreadId.setValue(defaultThreadId);
          }
          return;
        }
      }
    }
    if (current != null) {
      for (ChatThreadEntity entity : list) {
        if (entity != null && current.equals(entity.threadId)) {
          return;
        }
      }
    }
    ChatThreadEntity first = list.get(0);
    if (first != null) {
      activeThreadId.setValue(first.threadId);
    }
  }

  private void updateActiveThreadEntity() {
    List<ChatThreadEntity> list = threads.getValue();
    String id = activeThreadId.getValue();
    if (list == null || id == null || id.isEmpty()) {
      activeThread.setValue(null);
      return;
    }
    for (ChatThreadEntity entity : list) {
      if (entity != null && id.equals(entity.threadId)) {
        activeThread.setValue(entity);
        return;
      }
    }
    activeThread.setValue(null);
  }

  private void switchMessageSource(String threadId) {
    if (currentMessagesSource != null) {
      messages.removeSource(currentMessagesSource);
      currentMessagesSource = null;
    }
    if (threadId == null || threadId.isEmpty()) {
      messages.setValue(Collections.emptyList());
      return;
    }
    LiveData<List<ChatMessageEntity>> source = repository.messages(threadId);
    currentMessagesSource = source;
    messages.addSource(source, messages::setValue);
  }

  public void connect() {
    repository.connect();
  }

  public void disconnect() {
    repository.disconnect();
  }

  public LiveData<List<ChatThreadEntity>> getThreads() {
    return threads;
  }

  public LiveData<ChatThreadEntity> getActiveThread() {
    return activeThread;
  }

  public LiveData<String> getActiveThreadId() {
    return activeThreadId;
  }

  public LiveData<List<ChatMessageEntity>> getMessages() {
    return messages;
  }

  public LiveData<ChatSocketClient.ConnectionState> getConnectionState() {
    return connectionState;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public String getSenderRole() {
    return senderRole;
  }

  public String getLocalDisplayName() {
    if (TextUtils.isEmpty(localDisplayName)) {
      return getApplication().getString(R.string.chat_role_you);
    }
    return localDisplayName;
  }

  public void selectThread(String threadId) {
    if (threadId == null || threadId.isEmpty()) {
      activeThreadId.setValue(null);
      return;
    }
    activeThreadId.setValue(threadId);
    repository.markThreadRead(threadId);
  }

  public void markCurrentThreadRead() {
    String id = activeThreadId.getValue();
    if (id != null && !id.isEmpty()) {
      repository.markThreadRead(id);
    }
  }

  public void sendMessage(String body) {
    String threadId = activeThreadId.getValue();
    if (threadId == null || threadId.isEmpty()) {
      return;
    }
    if (body == null) {
      return;
    }
    String trimmed = body.trim();
    if (trimmed.isEmpty()) {
      return;
    }
    repository.sendMessage(threadId, trimmed, senderRole);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    repository.disconnect();
    threads.removeSource(threadsSource);
    if (currentMessagesSource != null) {
      messages.removeSource(currentMessagesSource);
      currentMessagesSource = null;
    }
  }
}
