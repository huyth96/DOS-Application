package com.drinkorder.data.repo;

import android.net.Uri;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.drinkorder.BuildConfig;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.dao.chat.ChatMessageDao;
import com.drinkorder.data.db.dao.chat.ChatThreadDao;
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;
import com.drinkorder.data.remote.ChatSocketClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRepository implements ChatSocketClient.Listener {
  private final AppDatabase database;
  private final ChatThreadDao threadDao;
  private final ChatMessageDao messageDao;
  private final ChatSocketClient socketClient;
  private final AuthRepository authRepository;
  private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
  private final String localRole;
  private final String defaultThreadId;

  public ChatRepository(AppDatabase database, ChatThreadDao threadDao, ChatMessageDao messageDao,
      AuthRepository authRepository) {
    this(database, threadDao, messageDao, authRepository,
        new ChatSocketClient(
            buildSocketUrl(BuildConfig.CHAT_SOCKET_URL, authRepository),
            authRepository != null ? authRepository::getLoggedUserName : null
        ));
  }

  public ChatRepository(AppDatabase database, ChatThreadDao threadDao, ChatMessageDao messageDao,
      AuthRepository authRepository, ChatSocketClient socketClient) {
    this.database = database;
    this.threadDao = threadDao;
    this.messageDao = messageDao;
    this.authRepository = authRepository;
    this.socketClient = socketClient;
    this.socketClient.addListener(this);
    this.localRole = normalizeRole(authRepository != null ? authRepository.role() : null);
    this.defaultThreadId = deriveDefaultThreadId(authRepository);
  }

  public String defaultThreadId() {
    return defaultThreadId;
  }

  public String localRole() {
    return localRole;
  }

  private static String normalizeRole(String rawRole) {
    if (TextUtils.isEmpty(rawRole)) return "customer";
    return rawRole.equalsIgnoreCase("admin") ? "support" : rawRole.toLowerCase();
  }

  private static String deriveDefaultThreadId(AuthRepository authRepository) {
    if (authRepository == null) { return null; }
    String role = authRepository.role();
    if (!TextUtils.isEmpty(role) && role.equalsIgnoreCase("admin")) {
      return null;
    }
    int userId = authRepository.userId();
    if (userId <= 0) { return null; }
    return "thread-" + userId;
  }

  private static String buildSocketUrl(String baseUrl, AuthRepository authRepository) {
    if (authRepository == null || TextUtils.isEmpty(baseUrl)) {
      return baseUrl;
    }
    Uri uri = Uri.parse(baseUrl);
    Uri.Builder builder = uri.buildUpon();
    int userId = authRepository.userId();
    if (userId > 0) {
      builder.appendQueryParameter("userId", String.valueOf(userId));
    }
    String username = authRepository.getLoggedUserName();
    if (!TextUtils.isEmpty(username)) {
      builder.appendQueryParameter("username", username);
    }
    builder.appendQueryParameter("role", normalizeRole(authRepository.role()));
    String threadId = deriveDefaultThreadId(authRepository);
    if (!TextUtils.isEmpty(threadId)) {
      builder.appendQueryParameter("threadId", threadId);
    }
    return builder.build().toString();
  }

  public LiveData<List<ChatThreadEntity>> threads() { return threadDao.observeThreads(); }

  public LiveData<List<ChatMessageEntity>> messages(String threadId) { return messageDao.observeMessages(threadId); }

  public LiveData<ChatSocketClient.ConnectionState> connectionState() { return socketClient.connectionState(); }

  public void connect() { socketClient.connect(); }

  public void disconnect() { socketClient.disconnect(); }

  public void ensureLocalThread(String threadId, String title) {
    if (TextUtils.isEmpty(threadId)) { return; }
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      ChatThreadEntity existing = threadDao.getThread(threadId);
      if (existing != null) { return; }
      ChatThreadEntity entity = new ChatThreadEntity();
      entity.threadId = threadId;
      entity.userId = authRepository != null ? authRepository.userId() : 0;
      entity.title = TextUtils.isEmpty(title) ? "Barista Support" : title;
      entity.lastMessage = "";
      entity.lastSenderRole = localRole;
      long timestamp = System.currentTimeMillis();
      entity.lastTimestamp = timestamp;
      entity.unreadCount = 0;
      entity.updatedAt = timestamp;
      threadDao.upsert(entity);
    }));
  }

  public void sendMessage(String threadId, String body, String senderRole) {
    final String localId = "local-" + UUID.randomUUID();
    final long now = System.currentTimeMillis();
    final ChatMessageEntity entity = new ChatMessageEntity();
    entity.messageId = localId;
    entity.threadId = threadId;
    final String effectiveRole = TextUtils.isEmpty(senderRole) ? localRole : senderRole;
    entity.senderRole = effectiveRole;
    entity.body = body;
    entity.sentAt = now;
    entity.deliveredAt = null;
    entity.isOutgoing = true;
    entity.isPending = true;

    ioExecutor.execute(() -> database.runInTransaction(() -> {
      int userId = authRepository != null ? authRepository.userId() : 0;
      threadDao.touchThread(threadId, userId, body, senderRole, now, 0);
      messageDao.upsert(entity);
    }));

    Map<String, Object> payload = new HashMap<>();
    payload.put("type", "message.send");
    payload.put("threadId", threadId);
    payload.put("body", body);
    payload.put("clientMessageId", localId);
    payload.put("sentAt", now);
    payload.put("senderRole", effectiveRole);
    socketClient.send(payload);
  }

  public void markThreadRead(String threadId) {
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      threadDao.updateUnread(threadId, 0, System.currentTimeMillis());
    }));
    Map<String, Object> payload = new HashMap<>();
    payload.put("type", "thread.read");
    payload.put("threadId", threadId);
    socketClient.send(payload);
  }

  @Override
  public void onMessage(JsonObject message) {
    if (message == null) { return; }
    String type = message.has("type") && message.get("type").isJsonPrimitive()
        ? message.get("type").getAsString()
        : "";
    JsonObject payload = message.has("payload") && message.get("payload").isJsonObject()
        ? message.getAsJsonObject("payload")
        : message;
    switch (type) {
      case "message":
      case "message.new":
        handleIncomingMessage(payload);
        break;
      case "message.ack":
      case "message_ack":
        handleAcknowledgement(payload);
        break;
      case "thread.sync":
      case "thread.list":
        handleThreadSync(payload);
        break;
      case "thread.updated":
        handleThreadUpdate(payload);
        break;
      case "thread.read":
      case "read":
        handleReadReceipt(payload);
        break;
      default:
        if (message.has("messages") && message.get("messages").isJsonArray()) {
          JsonArray arr = message.getAsJsonArray("messages");
          for (JsonElement el : arr) {
            if (el.isJsonObject()) { handleIncomingMessage(el.getAsJsonObject()); }
          }
        }
        break;
    }
  }

  @Override
  public void onClosed() { }

  @Override
  public void onFailure(Throwable t) { }

  private void handleThreadSync(JsonObject payload) {
    if (payload == null) { return; }
    List<ChatThreadEntity> threads = new ArrayList<>();
    if (payload.has("threads") && payload.get("threads").isJsonArray()) {
      JsonArray arr = payload.getAsJsonArray("threads");
      for (JsonElement el : arr) {
        if (el.isJsonObject()) {
          ChatThreadEntity entity = buildThreadEntity(el.getAsJsonObject());
          if (entity != null) { threads.add(entity); }
        }
      }
    } else {
      ChatThreadEntity entity = buildThreadEntity(payload);
      if (entity != null) { threads.add(entity); }
    }
    if (!threads.isEmpty()) {
      ioExecutor.execute(() -> database.runInTransaction(() -> threadDao.upsert(threads)));
    }
  }

  private void handleThreadUpdate(JsonObject payload) {
    ChatThreadEntity entity = buildThreadEntity(payload);
    if (entity == null) { return; }
    ioExecutor.execute(() -> database.runInTransaction(() -> threadDao.upsert(entity)));
  }

  private void handleIncomingMessage(JsonObject payload) {
    if (payload == null) { return; }
    final ChatMessageEntity entity = new ChatMessageEntity();
    entity.messageId = payload.has("messageId") && payload.get("messageId").isJsonPrimitive()
        ? payload.get("messageId").getAsString()
        : UUID.randomUUID().toString();
    entity.threadId = payload.has("threadId") && payload.get("threadId").isJsonPrimitive()
        ? payload.get("threadId").getAsString()
        : "";
    if (entity.threadId == null || entity.threadId.isEmpty()) { return; }
    entity.senderRole = payload.has("senderRole") && payload.get("senderRole").isJsonPrimitive()
        ? payload.get("senderRole").getAsString()
        : null;
    entity.body = payload.has("body") && payload.get("body").isJsonPrimitive()
        ? payload.get("body").getAsString()
        : null;
    entity.sentAt = payload.has("sentAt") && payload.get("sentAt").isJsonPrimitive()
        ? payload.get("sentAt").getAsLong()
        : System.currentTimeMillis();
    entity.deliveredAt = payload.has("deliveredAt") && payload.get("deliveredAt").isJsonPrimitive()
        ? payload.get("deliveredAt").getAsLong()
        : null;
    boolean remoteOutgoing = payload.has("isOutgoing") && payload.get("isOutgoing").isJsonPrimitive() &&
        payload.get("isOutgoing").getAsBoolean();
    boolean roleMatches = !TextUtils.isEmpty(localRole) && !TextUtils.isEmpty(entity.senderRole)
        && localRole.equalsIgnoreCase(entity.senderRole);
    entity.isOutgoing = remoteOutgoing || roleMatches;
    entity.isPending = false;
    final int unreadDelta = entity.isOutgoing ? 0 : 1;
    final int userId = authRepository != null ? authRepository.userId() : 0;
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      threadDao.touchThread(entity.threadId, userId, entity.body, entity.senderRole, entity.sentAt, unreadDelta);
      messageDao.upsert(entity);
    }));
  }

  private void handleAcknowledgement(JsonObject payload) {
    if (payload == null) { return; }
    final String clientId = payload.has("clientMessageId") && payload.get("clientMessageId").isJsonPrimitive()
        ? payload.get("clientMessageId").getAsString()
        : null;
    final String messageId = payload.has("messageId") && payload.get("messageId").isJsonPrimitive()
        ? payload.get("messageId").getAsString()
        : clientId;
    final Long deliveredAt = payload.has("deliveredAt") && payload.get("deliveredAt").isJsonPrimitive()
        ? payload.get("deliveredAt").getAsLong()
        : System.currentTimeMillis();
    if (clientId == null) { return; }
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      messageDao.acknowledge(clientId, messageId, deliveredAt);
      ChatMessageEntity updated = messageDao.findById(messageId);
      if (updated != null) {
        int userId = authRepository != null ? authRepository.userId() : 0;
        threadDao.touchThread(updated.threadId, userId, updated.body, updated.senderRole, updated.sentAt, 0);
      }
    }));
  }

  private void handleReadReceipt(JsonObject payload) {
    if (payload == null) { return; }
    final String threadId = payload.has("threadId") && payload.get("threadId").isJsonPrimitive()
        ? payload.get("threadId").getAsString()
        : null;
    if (threadId == null) { return; }
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      threadDao.updateUnread(threadId, 0, System.currentTimeMillis());
    }));
  }

  private ChatThreadEntity buildThreadEntity(JsonObject obj) {
    if (obj == null) { return null; }
    ChatThreadEntity entity = new ChatThreadEntity();
    if (obj.has("threadId") && obj.get("threadId").isJsonPrimitive()) {
      entity.threadId = obj.get("threadId").getAsString();
    } else {
      return null;
    }
    entity.userId = obj.has("userId") && obj.get("userId").isJsonPrimitive()
        ? obj.get("userId").getAsInt()
        : (authRepository != null ? authRepository.userId() : 0);
    entity.title = obj.has("title") && obj.get("title").isJsonPrimitive()
        ? obj.get("title").getAsString()
        : entity.title;
    entity.lastMessage = obj.has("lastMessage") && obj.get("lastMessage").isJsonPrimitive()
        ? obj.get("lastMessage").getAsString()
        : entity.lastMessage;
    entity.lastSenderRole = obj.has("lastSenderRole") && obj.get("lastSenderRole").isJsonPrimitive()
        ? obj.get("lastSenderRole").getAsString()
        : entity.lastSenderRole;
    entity.lastTimestamp = obj.has("lastTimestamp") && obj.get("lastTimestamp").isJsonPrimitive()
        ? obj.get("lastTimestamp").getAsLong()
        : System.currentTimeMillis();
    entity.unreadCount = obj.has("unreadCount") && obj.get("unreadCount").isJsonPrimitive()
        ? obj.get("unreadCount").getAsInt()
        : entity.unreadCount;
    entity.updatedAt = obj.has("updatedAt") && obj.get("updatedAt").isJsonPrimitive()
        ? obj.get("updatedAt").getAsLong()
        : System.currentTimeMillis();
    return entity;
  }
}
