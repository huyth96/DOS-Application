// Khai báo package com.drinkorder.data.repo cho toàn bộ lớp.
package com.drinkorder.data.repo;

// Import android.net.Uri để sử dụng các lớp hoặc hàm tương ứng.
import android.net.Uri;
// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;

// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;

// Import com.drinkorder.BuildConfig để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.BuildConfig;
// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.dao.chat.ChatMessageDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.chat.ChatMessageDao;
// Import com.drinkorder.data.db.dao.chat.ChatThreadDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.chat.ChatThreadDao;
// Import com.drinkorder.data.db.entity.chat.ChatMessageEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
// Import com.drinkorder.data.db.entity.chat.ChatThreadEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;
// Import com.drinkorder.data.remote.ChatSocketClient để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.remote.ChatSocketClient;
// Import com.google.gson.JsonArray để sử dụng các lớp hoặc hàm tương ứng.
import com.google.gson.JsonArray;
// Import com.google.gson.JsonElement để sử dụng các lớp hoặc hàm tương ứng.
import com.google.gson.JsonElement;
// Import com.google.gson.JsonObject để sử dụng các lớp hoặc hàm tương ứng.
import com.google.gson.JsonObject;

// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.HashMap để sử dụng các lớp hoặc hàm tương ứng.
import java.util.HashMap;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Map để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Map;
// Import java.util.UUID để sử dụng các lớp hoặc hàm tương ứng.
import java.util.UUID;
// Import java.util.concurrent.ExecutorService để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.ExecutorService;
// Import java.util.concurrent.Executors để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executors;

// Định nghĩa lớp ChatRepository triển khai ChatSocketClient.Listener.
public class ChatRepository implements ChatSocketClient.Listener {
  // Khai báo thuộc tính với phạm vi truy cập: private final AppDatabase database.
  private final AppDatabase database;
  // Khai báo thuộc tính với phạm vi truy cập: private final ChatThreadDao threadDao.
  private final ChatThreadDao threadDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final ChatMessageDao messageDao.
  private final ChatMessageDao messageDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final ChatSocketClient socketClient.
  private final ChatSocketClient socketClient;
  // Khai báo thuộc tính với phạm vi truy cập: private final AuthRepository authRepository.
  private final AuthRepository authRepository;
  // Khai báo thuộc tính với phạm vi truy cập: private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor().
  private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
  // Khai báo thuộc tính với phạm vi truy cập: private final String localRole.
  private final String localRole;
  // Khai báo thuộc tính với phạm vi truy cập: private final String defaultThreadId.
  private final String defaultThreadId;

  // Định nghĩa phương thức ChatRepository với phạm vi truy cập tương ứng.
  public ChatRepository(AppDatabase database, ChatThreadDao threadDao, ChatMessageDao messageDao,
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      AuthRepository authRepository) {
    // Thực thi câu lệnh: this(database, threadDao, messageDao, authRepository,.
    this(database, threadDao, messageDao, authRepository,
        // Khởi tạo đối tượng mới với biểu thức new ChatSocketClient(.
        new ChatSocketClient(
            // Thực thi câu lệnh: buildSocketUrl(BuildConfig.CHAT_SOCKET_URL, authRepository),.
            buildSocketUrl(BuildConfig.CHAT_SOCKET_URL, authRepository),
            // Thực thi câu lệnh: authRepository != null ? authRepository::getLoggedUserName : null.
            authRepository != null ? authRepository::getLoggedUserName : null
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ));.
        ));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức ChatRepository với phạm vi truy cập tương ứng.
  public ChatRepository(AppDatabase database, ChatThreadDao threadDao, ChatMessageDao messageDao,
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      AuthRepository authRepository, ChatSocketClient socketClient) {
    // Gán giá trị cho biến hoặc thuộc tính: this.database = database.
    this.database = database;
    // Gán giá trị cho biến hoặc thuộc tính: this.threadDao = threadDao.
    this.threadDao = threadDao;
    // Gán giá trị cho biến hoặc thuộc tính: this.messageDao = messageDao.
    this.messageDao = messageDao;
    // Gán giá trị cho biến hoặc thuộc tính: this.authRepository = authRepository.
    this.authRepository = authRepository;
    // Gán giá trị cho biến hoặc thuộc tính: this.socketClient = socketClient.
    this.socketClient = socketClient;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: this.socketClient.addListener(this);.
    this.socketClient.addListener(this);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: this.localRole = normalizeRole(authRepository != null ? authRepository.role() : null);.
    this.localRole = normalizeRole(authRepository != null ? authRepository.role() : null);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: this.defaultThreadId = deriveDefaultThreadId(authRepository);.
    this.defaultThreadId = deriveDefaultThreadId(authRepository);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức defaultThreadId với phạm vi truy cập tương ứng.
  public String defaultThreadId() {
    // Trả về kết quả defaultThreadId;.
    return defaultThreadId;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức localRole với phạm vi truy cập tương ứng.
  public String localRole() {
    // Trả về kết quả localRole;.
    return localRole;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức normalizeRole với phạm vi truy cập tương ứng.
  private static String normalizeRole(String rawRole) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(rawRole)) return "customer";
    // Trả về kết quả rawRole.equalsIgnoreCase("admin") ? "support" : rawRole.toLowerCase();.
    return rawRole.equalsIgnoreCase("admin") ? "support" : rawRole.toLowerCase();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức deriveDefaultThreadId với phạm vi truy cập tương ứng.
  private static String deriveDefaultThreadId(AuthRepository authRepository) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (authRepository == null) { return null; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String role = authRepository.role();.
    String role = authRepository.role();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.isEmpty(role) && role.equalsIgnoreCase("admin")) {
      // Trả về kết quả null;.
      return null;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: int userId = authRepository.userId();.
    int userId = authRepository.userId();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (userId <= 0) { return null; }
    // Trả về kết quả "thread-" + userId;.
    return "thread-" + userId;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức buildSocketUrl với phạm vi truy cập tương ứng.
  private static String buildSocketUrl(String baseUrl, AuthRepository authRepository) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (authRepository == null || TextUtils.isEmpty(baseUrl)) {
      // Trả về kết quả baseUrl;.
      return baseUrl;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Uri uri = Uri.parse(baseUrl);.
    Uri uri = Uri.parse(baseUrl);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Uri.Builder builder = uri.buildUpon();.
    Uri.Builder builder = uri.buildUpon();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: int userId = authRepository.userId();.
    int userId = authRepository.userId();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (userId > 0) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: builder.appendQueryParameter("userId", String.valueOf(userId));.
      builder.appendQueryParameter("userId", String.valueOf(userId));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String username = authRepository.getLoggedUserName();.
    String username = authRepository.getLoggedUserName();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.isEmpty(username)) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: builder.appendQueryParameter("username", username);.
      builder.appendQueryParameter("username", username);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: builder.appendQueryParameter("role", normalizeRole(authRepository.role()));.
    builder.appendQueryParameter("role", normalizeRole(authRepository.role()));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String threadId = deriveDefaultThreadId(authRepository);.
    String threadId = deriveDefaultThreadId(authRepository);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.isEmpty(threadId)) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: builder.appendQueryParameter("threadId", threadId);.
      builder.appendQueryParameter("threadId", threadId);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả builder.build().toString();.
    return builder.build().toString();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức threads với phạm vi truy cập tương ứng.
  public LiveData<List<ChatThreadEntity>> threads() { return threadDao.observeThreads(); }

  // Định nghĩa phương thức messages với phạm vi truy cập tương ứng.
  public LiveData<List<ChatMessageEntity>> messages(String threadId) { return messageDao.observeMessages(threadId); }

  // Định nghĩa phương thức connectionState với phạm vi truy cập tương ứng.
  public LiveData<ChatSocketClient.ConnectionState> connectionState() { return socketClient.connectionState(); }

  // Định nghĩa phương thức connect với phạm vi truy cập tương ứng.
  public void connect() { socketClient.connect(); }

  // Định nghĩa phương thức disconnect với phạm vi truy cập tương ứng.
  public void disconnect() { socketClient.disconnect(); }

  // Định nghĩa phương thức ensureLocalThread với phạm vi truy cập tương ứng.
  public void ensureLocalThread(String threadId, String title) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(threadId)) { return; }
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity existing = threadDao.getThread(threadId);.
      ChatThreadEntity existing = threadDao.getThread(threadId);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (existing != null) { return; }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity entity = new ChatThreadEntity();.
      ChatThreadEntity entity = new ChatThreadEntity();
      // Gán giá trị cho biến hoặc thuộc tính: entity.threadId = threadId.
      entity.threadId = threadId;
      // Gán giá trị cho biến hoặc thuộc tính: entity.userId = authRepository != null ? authRepository.userId() : 0.
      entity.userId = authRepository != null ? authRepository.userId() : 0;
      // Gán giá trị cho biến hoặc thuộc tính: entity.title = TextUtils.isEmpty(title) ? "Barista Support" : title.
      entity.title = TextUtils.isEmpty(title) ? "Barista Support" : title;
      // Gán giá trị cho biến hoặc thuộc tính: entity.lastMessage = "".
      entity.lastMessage = "";
      // Gán giá trị cho biến hoặc thuộc tính: entity.lastSenderRole = localRole.
      entity.lastSenderRole = localRole;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: long timestamp = System.currentTimeMillis();.
      long timestamp = System.currentTimeMillis();
      // Gán giá trị cho biến hoặc thuộc tính: entity.lastTimestamp = timestamp.
      entity.lastTimestamp = timestamp;
      // Gán giá trị cho biến hoặc thuộc tính: entity.unreadCount = 0.
      entity.unreadCount = 0;
      // Gán giá trị cho biến hoặc thuộc tính: entity.updatedAt = timestamp.
      entity.updatedAt = timestamp;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: threadDao.upsert(entity);.
      threadDao.upsert(entity);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }));.
    }));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức sendMessage với phạm vi truy cập tương ứng.
  public void sendMessage(String threadId, String body, String senderRole) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: final String localId = "local-" + UUID.randomUUID();.
    final String localId = "local-" + UUID.randomUUID();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: final long now = System.currentTimeMillis();.
    final long now = System.currentTimeMillis();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: final ChatMessageEntity entity = new ChatMessageEntity();.
    final ChatMessageEntity entity = new ChatMessageEntity();
    // Gán giá trị cho biến hoặc thuộc tính: entity.messageId = localId.
    entity.messageId = localId;
    // Gán giá trị cho biến hoặc thuộc tính: entity.threadId = threadId.
    entity.threadId = threadId;
    // Gán giá trị cho biến hoặc thuộc tính: final String effectiveRole = TextUtils.isEmpty(senderRole) ? localRole : senderRole.
    final String effectiveRole = TextUtils.isEmpty(senderRole) ? localRole : senderRole;
    // Gán giá trị cho biến hoặc thuộc tính: entity.senderRole = effectiveRole.
    entity.senderRole = effectiveRole;
    // Gán giá trị cho biến hoặc thuộc tính: entity.body = body.
    entity.body = body;
    // Gán giá trị cho biến hoặc thuộc tính: entity.sentAt = now.
    entity.sentAt = now;
    // Gán giá trị cho biến hoặc thuộc tính: entity.deliveredAt = null.
    entity.deliveredAt = null;
    // Gán giá trị cho biến hoặc thuộc tính: entity.isOutgoing = true.
    entity.isOutgoing = true;
    // Gán giá trị cho biến hoặc thuộc tính: entity.isPending = true.
    entity.isPending = true;

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      // Gán giá trị cho biến hoặc thuộc tính: int userId = authRepository != null ? authRepository.userId() : 0.
      int userId = authRepository != null ? authRepository.userId() : 0;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: threadDao.touchThread(threadId, userId, body, senderRole, now, 0);.
      threadDao.touchThread(threadId, userId, body, senderRole, now, 0);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: messageDao.upsert(entity);.
      messageDao.upsert(entity);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }));.
    }));

    // Thực hiện lời gọi phương thức hoặc khởi tạo: Map<String, Object> payload = new HashMap<>();.
    Map<String, Object> payload = new HashMap<>();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.put("type", "message.send");.
    payload.put("type", "message.send");
    // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.put("threadId", threadId);.
    payload.put("threadId", threadId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.put("body", body);.
    payload.put("body", body);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.put("clientMessageId", localId);.
    payload.put("clientMessageId", localId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.put("sentAt", now);.
    payload.put("sentAt", now);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.put("senderRole", effectiveRole);.
    payload.put("senderRole", effectiveRole);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: socketClient.send(payload);.
    socketClient.send(payload);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức markThreadRead với phạm vi truy cập tương ứng.
  public void markThreadRead(String threadId) {
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: threadDao.updateUnread(threadId, 0, System.currentTimeMillis());.
      threadDao.updateUnread(threadId, 0, System.currentTimeMillis());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }));.
    }));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Map<String, Object> payload = new HashMap<>();.
    Map<String, Object> payload = new HashMap<>();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.put("type", "thread.read");.
    payload.put("type", "thread.read");
    // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.put("threadId", threadId);.
    payload.put("threadId", threadId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: socketClient.send(payload);.
    socketClient.send(payload);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onMessage với phạm vi truy cập tương ứng.
  public void onMessage(JsonObject message) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (message == null) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String type = message.has("type") && message.get("type").isJsonPrimitive().
    String type = message.has("type") && message.get("type").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? message.get("type").getAsString().
        ? message.get("type").getAsString()
        // Thực thi câu lệnh: : "";.
        : "";
    // Thực hiện lời gọi phương thức hoặc khởi tạo: JsonObject payload = message.has("payload") && message.get("payload").isJsonObject().
    JsonObject payload = message.has("payload") && message.get("payload").isJsonObject()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? message.getAsJsonObject("payload").
        ? message.getAsJsonObject("payload")
        // Thực thi câu lệnh: : message;.
        : message;
    // Tạo cấu trúc switch để rẽ nhánh theo giá trị.
    switch (type) {
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "message":
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "message.new":
        // Thực hiện lời gọi phương thức hoặc khởi tạo: handleIncomingMessage(payload);.
        handleIncomingMessage(payload);
        // Thoát khỏi vòng lặp hoặc switch hiện tại.
        break;
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "message.ack":
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "message_ack":
        // Thực hiện lời gọi phương thức hoặc khởi tạo: handleAcknowledgement(payload);.
        handleAcknowledgement(payload);
        // Thoát khỏi vòng lặp hoặc switch hiện tại.
        break;
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "thread.sync":
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "thread.list":
        // Thực hiện lời gọi phương thức hoặc khởi tạo: handleThreadSync(payload);.
        handleThreadSync(payload);
        // Thoát khỏi vòng lặp hoặc switch hiện tại.
        break;
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "thread.updated":
        // Thực hiện lời gọi phương thức hoặc khởi tạo: handleThreadUpdate(payload);.
        handleThreadUpdate(payload);
        // Thoát khỏi vòng lặp hoặc switch hiện tại.
        break;
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "thread.read":
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case "read":
        // Thực hiện lời gọi phương thức hoặc khởi tạo: handleReadReceipt(payload);.
        handleReadReceipt(payload);
        // Thoát khỏi vòng lặp hoặc switch hiện tại.
        break;
      // Định nghĩa nhánh mặc định cho switch.
      default:
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (message.has("messages") && message.get("messages").isJsonArray()) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: JsonArray arr = message.getAsJsonArray("messages");.
          JsonArray arr = message.getAsJsonArray("messages");
          // Bắt đầu vòng lặp for để duyệt dữ liệu.
          for (JsonElement el : arr) {
            // Kiểm tra điều kiện if để quyết định luồng xử lý.
            if (el.isJsonObject()) { handleIncomingMessage(el.getAsJsonObject()); }
          // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
          }
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Thoát khỏi vòng lặp hoặc switch hiện tại.
        break;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onClosed với phạm vi truy cập tương ứng.
  public void onClosed() { }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onFailure với phạm vi truy cập tương ứng.
  public void onFailure(Throwable t) { }

  // Định nghĩa phương thức handleThreadSync với phạm vi truy cập tương ứng.
  private void handleThreadSync(JsonObject payload) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (payload == null) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: List<ChatThreadEntity> threads = new ArrayList<>();.
    List<ChatThreadEntity> threads = new ArrayList<>();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (payload.has("threads") && payload.get("threads").isJsonArray()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: JsonArray arr = payload.getAsJsonArray("threads");.
      JsonArray arr = payload.getAsJsonArray("threads");
      // Bắt đầu vòng lặp for để duyệt dữ liệu.
      for (JsonElement el : arr) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (el.isJsonObject()) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity entity = buildThreadEntity(el.getAsJsonObject());.
          ChatThreadEntity entity = buildThreadEntity(el.getAsJsonObject());
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (entity != null) { threads.add(entity); }
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity entity = buildThreadEntity(payload);.
      ChatThreadEntity entity = buildThreadEntity(payload);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (entity != null) { threads.add(entity); }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!threads.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: ioExecutor.execute(() -> database.runInTransaction(() -> threadDao.upsert(threads)));.
      ioExecutor.execute(() -> database.runInTransaction(() -> threadDao.upsert(threads)));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức handleThreadUpdate với phạm vi truy cập tương ứng.
  private void handleThreadUpdate(JsonObject payload) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity entity = buildThreadEntity(payload);.
    ChatThreadEntity entity = buildThreadEntity(payload);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (entity == null) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ioExecutor.execute(() -> database.runInTransaction(() -> threadDao.upsert(entity)));.
    ioExecutor.execute(() -> database.runInTransaction(() -> threadDao.upsert(entity)));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức handleIncomingMessage với phạm vi truy cập tương ứng.
  private void handleIncomingMessage(JsonObject payload) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (payload == null) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: final ChatMessageEntity entity = new ChatMessageEntity();.
    final ChatMessageEntity entity = new ChatMessageEntity();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.messageId = payload.has("messageId") && payload.get("messageId").isJsonPrimitive().
    entity.messageId = payload.has("messageId") && payload.get("messageId").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("messageId").getAsString().
        ? payload.get("messageId").getAsString()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : UUID.randomUUID().toString();.
        : UUID.randomUUID().toString();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.threadId = payload.has("threadId") && payload.get("threadId").isJsonPrimitive().
    entity.threadId = payload.has("threadId") && payload.get("threadId").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("threadId").getAsString().
        ? payload.get("threadId").getAsString()
        // Thực thi câu lệnh: : "";.
        : "";
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (entity.threadId == null || entity.threadId.isEmpty()) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.senderRole = payload.has("senderRole") && payload.get("senderRole").isJsonPrimitive().
    entity.senderRole = payload.has("senderRole") && payload.get("senderRole").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("senderRole").getAsString().
        ? payload.get("senderRole").getAsString()
        // Thực thi câu lệnh: : null;.
        : null;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.body = payload.has("body") && payload.get("body").isJsonPrimitive().
    entity.body = payload.has("body") && payload.get("body").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("body").getAsString().
        ? payload.get("body").getAsString()
        // Thực thi câu lệnh: : null;.
        : null;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.sentAt = payload.has("sentAt") && payload.get("sentAt").isJsonPrimitive().
    entity.sentAt = payload.has("sentAt") && payload.get("sentAt").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("sentAt").getAsLong().
        ? payload.get("sentAt").getAsLong()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : System.currentTimeMillis();.
        : System.currentTimeMillis();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.deliveredAt = payload.has("deliveredAt") && payload.get("deliveredAt").isJsonPrimitive().
    entity.deliveredAt = payload.has("deliveredAt") && payload.get("deliveredAt").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("deliveredAt").getAsLong().
        ? payload.get("deliveredAt").getAsLong()
        // Thực thi câu lệnh: : null;.
        : null;
    // Thực thi câu lệnh: boolean remoteOutgoing = payload.has("isOutgoing") && payload.get("isOutgoing").isJsonPrimitive() &&.
    boolean remoteOutgoing = payload.has("isOutgoing") && payload.get("isOutgoing").isJsonPrimitive() &&
        // Thực hiện lời gọi phương thức hoặc khởi tạo: payload.get("isOutgoing").getAsBoolean();.
        payload.get("isOutgoing").getAsBoolean();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: boolean roleMatches = !TextUtils.isEmpty(localRole) && !TextUtils.isEmpty(entity.senderRole).
    boolean roleMatches = !TextUtils.isEmpty(localRole) && !TextUtils.isEmpty(entity.senderRole)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: && localRole.equalsIgnoreCase(entity.senderRole);.
        && localRole.equalsIgnoreCase(entity.senderRole);
    // Gán giá trị cho biến hoặc thuộc tính: entity.isOutgoing = remoteOutgoing || roleMatches.
    entity.isOutgoing = remoteOutgoing || roleMatches;
    // Gán giá trị cho biến hoặc thuộc tính: entity.isPending = false.
    entity.isPending = false;
    // Gán giá trị cho biến hoặc thuộc tính: final int unreadDelta = entity.isOutgoing ? 0 : 1.
    final int unreadDelta = entity.isOutgoing ? 0 : 1;
    // Gán giá trị cho biến hoặc thuộc tính: final int userId = authRepository != null ? authRepository.userId() : 0.
    final int userId = authRepository != null ? authRepository.userId() : 0;
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: threadDao.touchThread(entity.threadId, userId, entity.body, entity.senderRole, entity.sentAt, unreadDelta);.
      threadDao.touchThread(entity.threadId, userId, entity.body, entity.senderRole, entity.sentAt, unreadDelta);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: messageDao.upsert(entity);.
      messageDao.upsert(entity);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }));.
    }));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức handleAcknowledgement với phạm vi truy cập tương ứng.
  private void handleAcknowledgement(JsonObject payload) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (payload == null) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: final String clientId = payload.has("clientMessageId") && payload.get("clientMessageId").isJsonPrimitive().
    final String clientId = payload.has("clientMessageId") && payload.get("clientMessageId").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("clientMessageId").getAsString().
        ? payload.get("clientMessageId").getAsString()
        // Thực thi câu lệnh: : null;.
        : null;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: final String messageId = payload.has("messageId") && payload.get("messageId").isJsonPrimitive().
    final String messageId = payload.has("messageId") && payload.get("messageId").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("messageId").getAsString().
        ? payload.get("messageId").getAsString()
        // Thực thi câu lệnh: : clientId;.
        : clientId;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: final Long deliveredAt = payload.has("deliveredAt") && payload.get("deliveredAt").isJsonPrimitive().
    final Long deliveredAt = payload.has("deliveredAt") && payload.get("deliveredAt").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("deliveredAt").getAsLong().
        ? payload.get("deliveredAt").getAsLong()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : System.currentTimeMillis();.
        : System.currentTimeMillis();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (clientId == null) { return; }
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: messageDao.acknowledge(clientId, messageId, deliveredAt);.
      messageDao.acknowledge(clientId, messageId, deliveredAt);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatMessageEntity updated = messageDao.findById(messageId);.
      ChatMessageEntity updated = messageDao.findById(messageId);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (updated != null) {
        // Gán giá trị cho biến hoặc thuộc tính: int userId = authRepository != null ? authRepository.userId() : 0.
        int userId = authRepository != null ? authRepository.userId() : 0;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: threadDao.touchThread(updated.threadId, userId, updated.body, updated.senderRole, updated.sentAt, 0);.
        threadDao.touchThread(updated.threadId, userId, updated.body, updated.senderRole, updated.sentAt, 0);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }));.
    }));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức handleReadReceipt với phạm vi truy cập tương ứng.
  private void handleReadReceipt(JsonObject payload) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (payload == null) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: final String threadId = payload.has("threadId") && payload.get("threadId").isJsonPrimitive().
    final String threadId = payload.has("threadId") && payload.get("threadId").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? payload.get("threadId").getAsString().
        ? payload.get("threadId").getAsString()
        // Thực thi câu lệnh: : null;.
        : null;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (threadId == null) { return; }
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    ioExecutor.execute(() -> database.runInTransaction(() -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: threadDao.updateUnread(threadId, 0, System.currentTimeMillis());.
      threadDao.updateUnread(threadId, 0, System.currentTimeMillis());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }));.
    }));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức buildThreadEntity với phạm vi truy cập tương ứng.
  private ChatThreadEntity buildThreadEntity(JsonObject obj) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (obj == null) { return null; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity entity = new ChatThreadEntity();.
    ChatThreadEntity entity = new ChatThreadEntity();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (obj.has("threadId") && obj.get("threadId").isJsonPrimitive()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.threadId = obj.get("threadId").getAsString();.
      entity.threadId = obj.get("threadId").getAsString();
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Trả về kết quả null;.
      return null;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.userId = obj.has("userId") && obj.get("userId").isJsonPrimitive().
    entity.userId = obj.has("userId") && obj.get("userId").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? obj.get("userId").getAsInt().
        ? obj.get("userId").getAsInt()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : (authRepository != null ? authRepository.userId() : 0);.
        : (authRepository != null ? authRepository.userId() : 0);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.title = obj.has("title") && obj.get("title").isJsonPrimitive().
    entity.title = obj.has("title") && obj.get("title").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? obj.get("title").getAsString().
        ? obj.get("title").getAsString()
        // Thực thi câu lệnh: : entity.title;.
        : entity.title;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.lastMessage = obj.has("lastMessage") && obj.get("lastMessage").isJsonPrimitive().
    entity.lastMessage = obj.has("lastMessage") && obj.get("lastMessage").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? obj.get("lastMessage").getAsString().
        ? obj.get("lastMessage").getAsString()
        // Thực thi câu lệnh: : entity.lastMessage;.
        : entity.lastMessage;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.lastSenderRole = obj.has("lastSenderRole") && obj.get("lastSenderRole").isJsonPrimitive().
    entity.lastSenderRole = obj.has("lastSenderRole") && obj.get("lastSenderRole").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? obj.get("lastSenderRole").getAsString().
        ? obj.get("lastSenderRole").getAsString()
        // Thực thi câu lệnh: : entity.lastSenderRole;.
        : entity.lastSenderRole;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.lastTimestamp = obj.has("lastTimestamp") && obj.get("lastTimestamp").isJsonPrimitive().
    entity.lastTimestamp = obj.has("lastTimestamp") && obj.get("lastTimestamp").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? obj.get("lastTimestamp").getAsLong().
        ? obj.get("lastTimestamp").getAsLong()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : System.currentTimeMillis();.
        : System.currentTimeMillis();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.unreadCount = obj.has("unreadCount") && obj.get("unreadCount").isJsonPrimitive().
    entity.unreadCount = obj.has("unreadCount") && obj.get("unreadCount").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? obj.get("unreadCount").getAsInt().
        ? obj.get("unreadCount").getAsInt()
        // Thực thi câu lệnh: : entity.unreadCount;.
        : entity.unreadCount;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.updatedAt = obj.has("updatedAt") && obj.get("updatedAt").isJsonPrimitive().
    entity.updatedAt = obj.has("updatedAt") && obj.get("updatedAt").isJsonPrimitive()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? obj.get("updatedAt").getAsLong().
        ? obj.get("updatedAt").getAsLong()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : System.currentTimeMillis();.
        : System.currentTimeMillis();
    // Trả về kết quả entity;.
    return entity;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
