package com.drinkorder.data.repo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.dao.UserDao;
import com.drinkorder.data.db.entity.UserEntity;
import com.drinkorder.data.model.ChatMessage;
import com.drinkorder.data.model.ChatThreadSummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRepository {

  private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
  private static volatile ChatRepository INSTANCE;

  public static ChatRepository getInstance(@NonNull Context context) {
    if (INSTANCE == null) {
      synchronized (ChatRepository.class) {
        if (INSTANCE == null) {
          INSTANCE = new ChatRepository(AppDatabase.get(context).userDao());
        }
      }
    }
    return INSTANCE;
  }

  private final UserDao userDao;
  private final MediatorLiveData<List<ChatThreadSummary>> threadSummaries = new MediatorLiveData<>();
  private final MutableLiveData<Map<String, List<ChatMessage>>> messageStore = new MutableLiveData<>(new HashMap<>());
  private final MutableLiveData<List<ChatThreadState>> threadStateStore = new MutableLiveData<>(new ArrayList<>());
  private final MutableLiveData<Boolean> connectionState = new MutableLiveData<>(true);

  private List<UserEntity> cachedUsers = new ArrayList<>();

  private ChatRepository(UserDao userDao) {
    this.userDao = userDao;
    threadSummaries.addSource(threadStateStore, states -> combine(states, cachedUsers));
    threadSummaries.addSource(messageStore, map -> combine(threadStateStore.getValue(), cachedUsers));
    threadSummaries.addSource(userDao.observeAll(), users -> {
      cachedUsers = users == null ? new ArrayList<>() : new ArrayList<>(users);
      if (threadStateStore.getValue() == null || threadStateStore.getValue().isEmpty()) {
        seedThreadsForUsers(cachedUsers);
      } else {
        combine(threadStateStore.getValue(), cachedUsers);
      }
    });
  }

  public LiveData<List<ChatThreadSummary>> observeThreads() {
    return threadSummaries;
  }

  public LiveData<ChatThreadSummary> observeThread(@NonNull String threadId) {
    MediatorLiveData<ChatThreadSummary> liveData = new MediatorLiveData<>();
    liveData.addSource(threadSummaries, summaries -> liveData.setValue(findThreadSummary(summaries, threadId)));
    return liveData;
  }

  public LiveData<List<ChatMessage>> observeMessages(@NonNull String threadId) {
    MediatorLiveData<List<ChatMessage>> liveData = new MediatorLiveData<>();
    liveData.addSource(messageStore, map -> {
      List<ChatMessage> messages = map == null ? null : map.get(threadId);
      if (messages == null) {
        liveData.setValue(Collections.emptyList());
      } else {
        liveData.setValue(new ArrayList<>(messages));
      }
    });
    return liveData;
  }

  public LiveData<Boolean> observeConnectionState() {
    return connectionState;
  }

  public void setSocketConnected(boolean connected) {
    connectionState.postValue(connected);
  }

  public void sendAdminMessage(@NonNull String threadId, @NonNull String message) {
    if (message.trim().isEmpty()) return;
    EXECUTOR.execute(() -> {
      long now = System.currentTimeMillis();
      ChatMessage chatMessage = new ChatMessage(UUID.randomUUID().toString(), threadId,
          ChatMessage.SenderRole.ADMIN, message.trim(), now, true);
      appendMessage(threadId, chatMessage, true);
    });
  }

  public void markThreadRead(@NonNull String threadId) {
    EXECUTOR.execute(() -> {
      List<ChatThreadState> states = copyStates();
      boolean updated = false;
      for (ChatThreadState state : states) {
        if (state.threadId.equals(threadId)) {
          if (state.unreadCount > 0) {
            state.unreadCount = 0;
            updated = true;
          }
          break;
        }
      }
      if (updated) {
        postStates(states);
      } else {
        threadSummaries.postValue(buildSummaries(states, cachedUsers));
      }
      Log.d("ChatRepository", "read event sent for " + threadId);
    });
  }

  public void setPresence(@NonNull String threadId, boolean online) {
    EXECUTOR.execute(() -> {
      List<ChatThreadState> states = copyStates();
      boolean changed = false;
      for (ChatThreadState state : states) {
        if (state.threadId.equals(threadId)) {
          if (state.online != online) {
            state.online = online;
            changed = true;
          }
          break;
        }
      }
      if (changed) {
        postStates(states);
      }
    });
  }

  public void simulateConnectionDrop() {
    setSocketConnected(false);
  }

  private void seedThreadsForUsers(List<UserEntity> users) {
    EXECUTOR.execute(() -> {
      List<ChatThreadState> states = new ArrayList<>();
      Map<String, List<ChatMessage>> messages = new LinkedHashMap<>();
      long now = System.currentTimeMillis();
      int index = 0;
      for (UserEntity user : users) {
        if (user == null) continue;
        String role = user.role == null ? "customer" : user.role.toLowerCase(Locale.getDefault());
        if ("admin".equals(role)) continue;
        String threadId = "thread-" + user.userId;
        ChatThreadState state = new ChatThreadState(threadId, user.userId);
        List<ChatMessage> messageList = new ArrayList<>();
        long base = now - index * 45 * 60 * 1000L;
        messageList.add(new ChatMessage(UUID.randomUUID().toString(), threadId,
            ChatMessage.SenderRole.CUSTOMER,
            "Xin chào, tôi muốn hỏi về thức uống nổi bật hôm nay?",
            base - 5 * 60 * 1000L, true));
        messageList.add(new ChatMessage(UUID.randomUUID().toString(), threadId,
            ChatMessage.SenderRole.ADMIN,
            "Chào bạn! Hôm nay chúng tôi gợi ý trà đào cam sả và cold brew đặc biệt.",
            base - 4 * 60 * 1000L, true));
        messageList.add(new ChatMessage(UUID.randomUUID().toString(), threadId,
            ChatMessage.SenderRole.CUSTOMER,
            "Ngon quá, bạn có thể giữ giúp tôi 2 ly cold brew không?",
            base - 2 * 60 * 1000L, false));
        state.lastMessage = messageList.get(messageList.size() - 1).getContent();
        state.lastMessageAt = messageList.get(messageList.size() - 1).getTimestamp();
        state.lastSenderRole = ChatMessage.SenderRole.CUSTOMER;
        state.unreadCount = 1 + (index % 2);
        state.online = index % 3 != 0;
        states.add(state);
        messages.put(threadId, messageList);
        index++;
      }
      if (states.isEmpty()) {
        String threadId = "thread-sample";
        ChatThreadState state = new ChatThreadState(threadId, -1);
        state.lastMessage = "Khách hàng mới sẽ xuất hiện tại đây.";
        state.lastMessageAt = now;
        state.unreadCount = 0;
        state.online = false;
        states.add(state);
        messages.put(threadId, new ArrayList<>());
      }
      threadStateStore.postValue(states);
      messageStore.postValue(messages);
    });
  }

  private void appendMessage(String threadId, ChatMessage message, boolean fromAdmin) {
    Map<String, List<ChatMessage>> current = new LinkedHashMap<>(getMessageMap());
    List<ChatMessage> list = current.get(threadId);
    if (list == null) {
      list = new ArrayList<>();
      current.put(threadId, list);
    } else {
      list = new ArrayList<>(list);
      current.put(threadId, list);
    }
    list.add(message);
    Collections.sort(list, (a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));
    current.put(threadId, list);
    messageStore.postValue(current);

    List<ChatThreadState> states = copyStates();
    for (ChatThreadState state : states) {
      if (state.threadId.equals(threadId)) {
        state.lastMessage = message.getContent();
        state.lastMessageAt = message.getTimestamp();
        state.lastSenderRole = message.getSenderRole();
        if (!fromAdmin && !message.isRead()) {
          state.unreadCount += 1;
        }
        if (fromAdmin) {
          state.unreadCount = 0;
        }
        break;
      }
    }
    postStates(states);
  }

  private synchronized List<ChatThreadState> copyStates() {
    List<ChatThreadState> original = threadStateStore.getValue();
    if (original == null) return new ArrayList<>();
    List<ChatThreadState> copy = new ArrayList<>();
    for (ChatThreadState state : original) {
      copy.add(new ChatThreadState(state));
    }
    return copy;
  }

  private void postStates(List<ChatThreadState> states) {
    threadStateStore.postValue(states);
  }

  private Map<String, List<ChatMessage>> getMessageMap() {
    Map<String, List<ChatMessage>> map = messageStore.getValue();
    if (map == null) {
      map = new LinkedHashMap<>();
    }
    return map;
  }

  private void combine(@Nullable List<ChatThreadState> states, @Nullable List<UserEntity> users) {
    threadSummaries.postValue(buildSummaries(states, users));
  }

  private List<ChatThreadSummary> buildSummaries(@Nullable List<ChatThreadState> states,
                                                 @Nullable List<UserEntity> users) {
    if (states == null) states = new ArrayList<>();
    if (users == null) users = new ArrayList<>();
    Map<Integer, UserEntity> userMap = new HashMap<>();
    for (UserEntity user : users) {
      if (user != null) userMap.put(user.userId, user);
    }
    List<ChatThreadSummary> summaries = new ArrayList<>();
    for (ChatThreadState state : states) {
      UserEntity user = userMap.get(state.userId);
      String name = "Guest";
      String username = "";
      if (user != null) {
        name = (user.fullName != null && !user.fullName.trim().isEmpty()) ? user.fullName : user.username;
        username = user.username;
      }
      summaries.add(new ChatThreadSummary(state.threadId, state.userId, name, username,
          state.lastMessage, state.lastMessageAt, state.unreadCount, state.online,
          state.lastSenderRole));
    }
    Collections.sort(summaries, (a, b) -> Long.compare(b.getLastMessageAt(), a.getLastMessageAt()));
    return summaries;
  }

  @Nullable
  private ChatThreadSummary findThreadSummary(@Nullable List<ChatThreadSummary> summaries, String id) {
    if (summaries == null) return null;
    for (ChatThreadSummary summary : summaries) {
      if (summary.getThreadId().equals(id)) return summary;
    }
    return null;
  }

  private static class ChatThreadState {
    final String threadId;
    final int userId;
    String lastMessage;
    long lastMessageAt;
    int unreadCount;
    boolean online;
    ChatMessage.SenderRole lastSenderRole;

    ChatThreadState(String threadId, int userId) {
      this.threadId = threadId;
      this.userId = userId;
      this.lastMessage = "";
      this.lastMessageAt = System.currentTimeMillis();
      this.unreadCount = 0;
      this.online = false;
      this.lastSenderRole = ChatMessage.SenderRole.CUSTOMER;
    }

    ChatThreadState(ChatThreadState other) {
      this.threadId = other.threadId;
      this.userId = other.userId;
      this.lastMessage = other.lastMessage;
      this.lastMessageAt = other.lastMessageAt;
      this.unreadCount = other.unreadCount;
      this.online = other.online;
      this.lastSenderRole = other.lastSenderRole;
    }
  }
}
