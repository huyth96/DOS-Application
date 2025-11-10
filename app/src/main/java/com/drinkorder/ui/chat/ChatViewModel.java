// Khai báo package com.drinkorder.ui.chat cho toàn bộ lớp.
package com.drinkorder.ui.chat;

// Import android.app.Application để sử dụng các lớp hoặc hàm tương ứng.
import android.app.Application;
// Import android.content.Context để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Context;
// Import android.content.SharedPreferences để sử dụng các lớp hoặc hàm tương ứng.
import android.content.SharedPreferences;
// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.lifecycle.AndroidViewModel để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.AndroidViewModel;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.lifecycle.MediatorLiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.MediatorLiveData;
// Import androidx.lifecycle.MutableLiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.MutableLiveData;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.entity.chat.ChatMessageEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
// Import com.drinkorder.data.db.entity.chat.ChatThreadEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;
// Import com.drinkorder.data.remote.ChatSocketClient để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.remote.ChatSocketClient;
// Import com.drinkorder.data.repo.AuthRepository để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.repo.AuthRepository;
// Import com.drinkorder.data.repo.ChatRepository để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.repo.ChatRepository;

// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.Collections để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Collections;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;

// Định nghĩa lớp ChatViewModel kế thừa AndroidViewModel.
public class ChatViewModel extends AndroidViewModel {

  // Khai báo thuộc tính với phạm vi truy cập: private final ChatRepository repository.
  private final ChatRepository repository;
  // Khai báo thuộc tính với phạm vi truy cập: private final AuthRepository authRepository.
  private final AuthRepository authRepository;
  // Khai báo thuộc tính với phạm vi truy cập: private final MediatorLiveData<List<ChatThreadEntity>> threads = new MediatorLiveData<>().
  private final MediatorLiveData<List<ChatThreadEntity>> threads = new MediatorLiveData<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final MutableLiveData<String> activeThreadId = new MutableLiveData<>().
  private final MutableLiveData<String> activeThreadId = new MutableLiveData<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final MediatorLiveData<ChatThreadEntity> activeThread = new MediatorLiveData<>().
  private final MediatorLiveData<ChatThreadEntity> activeThread = new MediatorLiveData<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final MediatorLiveData<List<ChatMessageEntity>> messages = new MediatorLiveData<>().
  private final MediatorLiveData<List<ChatMessageEntity>> messages = new MediatorLiveData<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final LiveData<List<ChatThreadEntity>> threadsSource.
  private final LiveData<List<ChatThreadEntity>> threadsSource;
  // Khai báo thuộc tính với phạm vi truy cập: private LiveData<List<ChatMessageEntity>> currentMessagesSource.
  private LiveData<List<ChatMessageEntity>> currentMessagesSource;
  // Khai báo thuộc tính với phạm vi truy cập: private final LiveData<ChatSocketClient.ConnectionState> connectionState.
  private final LiveData<ChatSocketClient.ConnectionState> connectionState;
  // Khai báo thuộc tính với phạm vi truy cập: private final String senderRole.
  private final String senderRole;
  // Khai báo thuộc tính với phạm vi truy cập: private final boolean isAdmin.
  private final boolean isAdmin;
  // Khai báo thuộc tính với phạm vi truy cập: private final String defaultThreadId.
  private final String defaultThreadId;
  // Khai báo thuộc tính với phạm vi truy cập: private final String localDisplayName.
  private final String localDisplayName;

  // Định nghĩa phương thức ChatViewModel với phạm vi truy cập tương ứng.
  public ChatViewModel(@NonNull Application application) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(application);.
    super(application);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: AppDatabase db = AppDatabase.get(application);.
    AppDatabase db = AppDatabase.get(application);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: SharedPreferences sp = application.getSharedPreferences("auth", Context.MODE_PRIVATE);.
    SharedPreferences sp = application.getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: authRepository = new AuthRepository(db.userDao(), sp);.
    authRepository = new AuthRepository(db.userDao(), sp);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: repository = new ChatRepository(db, db.chatThreadDao(), db.chatMessageDao(), authRepository);.
    repository = new ChatRepository(db, db.chatThreadDao(), db.chatMessageDao(), authRepository);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String storedRole = authRepository.role();.
    String storedRole = authRepository.role();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: isAdmin = storedRole != null && storedRole.equalsIgnoreCase("admin");.
    isAdmin = storedRole != null && storedRole.equalsIgnoreCase("admin");
    // Thực hiện lời gọi phương thức hoặc khởi tạo: senderRole = isAdmin ? "support" : (storedRole == null ? "customer" : storedRole);.
    senderRole = isAdmin ? "support" : (storedRole == null ? "customer" : storedRole);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: localDisplayName = authRepository.getLoggedUserName();.
    localDisplayName = authRepository.getLoggedUserName();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: defaultThreadId = repository.defaultThreadId();.
    defaultThreadId = repository.defaultThreadId();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!isAdmin && defaultThreadId != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThreadId.setValue(defaultThreadId);.
      activeThreadId.setValue(defaultThreadId);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: repository.ensureLocalThread(defaultThreadId, application.getString(R.string.chat_default_title));.
      repository.ensureLocalThread(defaultThreadId, application.getString(R.string.chat_default_title));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: threads.setValue(Collections.emptyList());.
    threads.setValue(Collections.emptyList());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: messages.setValue(Collections.emptyList());.
    messages.setValue(Collections.emptyList());

    // Thực hiện lời gọi phương thức hoặc khởi tạo: threadsSource = repository.threads();.
    threadsSource = repository.threads();
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    threads.addSource(threadsSource, list -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: List<ChatThreadEntity> filtered = filterThreads(list);.
      List<ChatThreadEntity> filtered = filterThreads(list);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: threads.setValue(filtered);.
      threads.setValue(filtered);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: ensureActiveThread(filtered);.
      ensureActiveThread(filtered);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: updateActiveThreadEntity();.
      updateActiveThreadEntity();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });

    // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThread.addSource(activeThreadId, ignored -> updateActiveThreadEntity());.
    activeThread.addSource(activeThreadId, ignored -> updateActiveThreadEntity());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThread.addSource(threads, ignored -> updateActiveThreadEntity());.
    activeThread.addSource(threads, ignored -> updateActiveThreadEntity());

    // Thực hiện lời gọi phương thức hoặc khởi tạo: messages.addSource(activeThreadId, this::switchMessageSource);.
    messages.addSource(activeThreadId, this::switchMessageSource);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: connectionState = repository.connectionState();.
    connectionState = repository.connectionState();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức filterThreads với phạm vi truy cập tương ứng.
  private List<ChatThreadEntity> filterThreads(List<ChatThreadEntity> list) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (list == null || list.isEmpty()) {
      // Trả về kết quả Collections.emptyList();.
      return Collections.emptyList();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (isAdmin || TextUtils.isEmpty(defaultThreadId)) {
      // Trả về kết quả list;.
      return list;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (ChatThreadEntity entity : list) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (entity != null && TextUtils.equals(defaultThreadId, entity.threadId)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ArrayList<ChatThreadEntity> onlyDefault = new ArrayList<>(1);.
        ArrayList<ChatThreadEntity> onlyDefault = new ArrayList<>(1);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: onlyDefault.add(entity);.
        onlyDefault.add(entity);
        // Trả về kết quả onlyDefault;.
        return onlyDefault;
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả Collections.emptyList();.
    return Collections.emptyList();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức ensureActiveThread với phạm vi truy cập tương ứng.
  private void ensureActiveThread(List<ChatThreadEntity> list) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (list == null || list.isEmpty()) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (defaultThreadId != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThreadId.setValue(defaultThreadId);.
        activeThreadId.setValue(defaultThreadId);
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThreadId.setValue(null);.
        activeThreadId.setValue(null);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String current = activeThreadId.getValue();.
    String current = activeThreadId.getValue();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.isEmpty(defaultThreadId)) {
      // Bắt đầu vòng lặp for để duyệt dữ liệu.
      for (ChatThreadEntity entity : list) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (entity != null && TextUtils.equals(defaultThreadId, entity.threadId)) {
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (!TextUtils.equals(defaultThreadId, current)) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThreadId.setValue(defaultThreadId);.
            activeThreadId.setValue(defaultThreadId);
          // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
          }
          // Trả về kết quả ;.
          return;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (current != null) {
      // Bắt đầu vòng lặp for để duyệt dữ liệu.
      for (ChatThreadEntity entity : list) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (entity != null && current.equals(entity.threadId)) {
          // Trả về kết quả ;.
          return;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity first = list.get(0);.
    ChatThreadEntity first = list.get(0);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (first != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThreadId.setValue(first.threadId);.
      activeThreadId.setValue(first.threadId);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức updateActiveThreadEntity với phạm vi truy cập tương ứng.
  private void updateActiveThreadEntity() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: List<ChatThreadEntity> list = threads.getValue();.
    List<ChatThreadEntity> list = threads.getValue();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String id = activeThreadId.getValue();.
    String id = activeThreadId.getValue();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (list == null || id == null || id.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThread.setValue(null);.
      activeThread.setValue(null);
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (ChatThreadEntity entity : list) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (entity != null && id.equals(entity.threadId)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThread.setValue(entity);.
        activeThread.setValue(entity);
        // Trả về kết quả ;.
        return;
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThread.setValue(null);.
    activeThread.setValue(null);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức switchMessageSource với phạm vi truy cập tương ứng.
  private void switchMessageSource(String threadId) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (currentMessagesSource != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: messages.removeSource(currentMessagesSource);.
      messages.removeSource(currentMessagesSource);
      // Gán giá trị cho biến hoặc thuộc tính: currentMessagesSource = null.
      currentMessagesSource = null;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (threadId == null || threadId.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: messages.setValue(Collections.emptyList());.
      messages.setValue(Collections.emptyList());
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<List<ChatMessageEntity>> source = repository.messages(threadId);.
    LiveData<List<ChatMessageEntity>> source = repository.messages(threadId);
    // Gán giá trị cho biến hoặc thuộc tính: currentMessagesSource = source.
    currentMessagesSource = source;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: messages.addSource(source, messages::setValue);.
    messages.addSource(source, messages::setValue);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức connect với phạm vi truy cập tương ứng.
  public void connect() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: repository.connect();.
    repository.connect();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức disconnect với phạm vi truy cập tương ứng.
  public void disconnect() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: repository.disconnect();.
    repository.disconnect();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getThreads với phạm vi truy cập tương ứng.
  public LiveData<List<ChatThreadEntity>> getThreads() {
    // Trả về kết quả threads;.
    return threads;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getActiveThread với phạm vi truy cập tương ứng.
  public LiveData<ChatThreadEntity> getActiveThread() {
    // Trả về kết quả activeThread;.
    return activeThread;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getActiveThreadId với phạm vi truy cập tương ứng.
  public LiveData<String> getActiveThreadId() {
    // Trả về kết quả activeThreadId;.
    return activeThreadId;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getMessages với phạm vi truy cập tương ứng.
  public LiveData<List<ChatMessageEntity>> getMessages() {
    // Trả về kết quả messages;.
    return messages;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getConnectionState với phạm vi truy cập tương ứng.
  public LiveData<ChatSocketClient.ConnectionState> getConnectionState() {
    // Trả về kết quả connectionState;.
    return connectionState;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức isAdmin với phạm vi truy cập tương ứng.
  public boolean isAdmin() {
    // Trả về kết quả isAdmin;.
    return isAdmin;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getSenderRole với phạm vi truy cập tương ứng.
  public String getSenderRole() {
    // Trả về kết quả senderRole;.
    return senderRole;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getLocalDisplayName với phạm vi truy cập tương ứng.
  public String getLocalDisplayName() {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(localDisplayName)) {
      // Trả về kết quả getApplication().getString(R.string.chat_role_you);.
      return getApplication().getString(R.string.chat_role_you);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả localDisplayName;.
    return localDisplayName;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức selectThread với phạm vi truy cập tương ứng.
  public void selectThread(String threadId) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (threadId == null || threadId.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThreadId.setValue(null);.
      activeThreadId.setValue(null);
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: activeThreadId.setValue(threadId);.
    activeThreadId.setValue(threadId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: repository.markThreadRead(threadId);.
    repository.markThreadRead(threadId);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức markCurrentThreadRead với phạm vi truy cập tương ứng.
  public void markCurrentThreadRead() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String id = activeThreadId.getValue();.
    String id = activeThreadId.getValue();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (id != null && !id.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: repository.markThreadRead(id);.
      repository.markThreadRead(id);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức sendMessage với phạm vi truy cập tương ứng.
  public void sendMessage(String body) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String threadId = activeThreadId.getValue();.
    String threadId = activeThreadId.getValue();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (threadId == null || threadId.isEmpty()) {
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (body == null) {
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String trimmed = body.trim();.
    String trimmed = body.trim();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (trimmed.isEmpty()) {
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: repository.sendMessage(threadId, trimmed, senderRole);.
    repository.sendMessage(threadId, trimmed, senderRole);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCleared với phạm vi truy cập tương ứng.
  protected void onCleared() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCleared();.
    super.onCleared();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: repository.disconnect();.
    repository.disconnect();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: threads.removeSource(threadsSource);.
    threads.removeSource(threadsSource);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (currentMessagesSource != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: messages.removeSource(currentMessagesSource);.
      messages.removeSource(currentMessagesSource);
      // Gán giá trị cho biến hoặc thuộc tính: currentMessagesSource = null.
      currentMessagesSource = null;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
