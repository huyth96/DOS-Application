package com.drinkorder.ui.chat.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.drinkorder.data.model.ChatThreadSummary;
import com.drinkorder.data.repo.ChatRepository;

import java.util.List;

public class AdminChatViewModel extends AndroidViewModel {

  private final ChatRepository repository;
  private final MutableLiveData<String> selectedThreadId = new MutableLiveData<>();
  private final LiveData<List<ChatThreadSummary>> threads;
  private final LiveData<Boolean> connectionState;

  public AdminChatViewModel(@NonNull Application application) {
    super(application);
    repository = ChatRepository.getInstance(application);
    threads = repository.observeThreads();
    connectionState = repository.observeConnectionState();
  }

  public LiveData<List<ChatThreadSummary>> getThreads() {
    return threads;
  }

  public LiveData<Boolean> getConnectionState() {
    return connectionState;
  }

  public LiveData<String> getSelectedThreadId() {
    return selectedThreadId;
  }

  public void selectThread(String threadId) {
    selectedThreadId.setValue(threadId);
    repository.markThreadRead(threadId);
  }

  public LiveData<ChatThreadSummary> observeThread(String threadId) {
    return repository.observeThread(threadId);
  }

  public void refreshPresence(String threadId, boolean online) {
    repository.setPresence(threadId, online);
  }
}
