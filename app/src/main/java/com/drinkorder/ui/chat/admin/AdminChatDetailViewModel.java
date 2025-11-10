package com.drinkorder.ui.chat.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.drinkorder.data.model.ChatMessage;
import com.drinkorder.data.model.ChatThreadSummary;
import com.drinkorder.data.repo.ChatRepository;

import java.util.List;

public class AdminChatDetailViewModel extends AndroidViewModel {

  private final ChatRepository repository;
  private LiveData<List<ChatMessage>> messages;
  private LiveData<ChatThreadSummary> threadSummary;
  private String threadId;
  private boolean initialized = false;

  public AdminChatDetailViewModel(@NonNull Application application) {
    super(application);
    repository = ChatRepository.getInstance(application);
  }

  public void init(@NonNull String threadId) {
    if (initialized) return;
    this.threadId = threadId;
    messages = repository.observeMessages(threadId);
    threadSummary = repository.observeThread(threadId);
    repository.markThreadRead(threadId);
    initialized = true;
  }

  public LiveData<List<ChatMessage>> getMessages() {
    return messages;
  }

  public LiveData<ChatThreadSummary> getThreadSummary() {
    return threadSummary;
  }

  public LiveData<Boolean> observeConnectionState() {
    return repository.observeConnectionState();
  }

  public void sendMessage(String content) {
    if (threadId == null) return;
    repository.sendAdminMessage(threadId, content);
  }
}
