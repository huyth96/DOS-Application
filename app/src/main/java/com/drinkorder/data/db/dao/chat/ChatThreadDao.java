// Khai báo package com.drinkorder.data.db.dao.chat cho toàn bộ lớp.
package com.drinkorder.data.db.dao.chat;

// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.room.Dao để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Dao;
// Import androidx.room.Insert để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Insert;
// Import androidx.room.OnConflictStrategy để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.OnConflictStrategy;
// Import androidx.room.Query để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Query;
// Import androidx.room.Transaction để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Transaction;

// Import com.drinkorder.data.db.entity.chat.ChatThreadEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;

// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;

// Áp dụng annotation @Dao cho phần tử bên dưới.
@Dao
// Định nghĩa interface ChatThreadDao.
public interface ChatThreadDao {
  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM chat_threads ORDER BY lastTimestamp DESC")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<List<ChatThreadEntity>> observeThreads();.
  LiveData<List<ChatThreadEntity>> observeThreads();

  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM chat_threads WHERE threadId = :threadId LIMIT 1")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity getThread(String threadId);.
  ChatThreadEntity getThread(String threadId);

  // Áp dụng annotation @Insert(onConflict cho phần tử bên dưới.
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void upsert(ChatThreadEntity entity);.
  void upsert(ChatThreadEntity entity);

  // Áp dụng annotation @Insert(onConflict cho phần tử bên dưới.
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void upsert(List<ChatThreadEntity> entities);.
  void upsert(List<ChatThreadEntity> entities);

  // Áp dụng annotation @Query( cho phần tử bên dưới.
  @Query(
      // Thực thi câu lệnh: "UPDATE chat_threads SET lastMessage = :lastMessage, lastSenderRole = :lastSenderRole, " +.
      "UPDATE chat_threads SET lastMessage = :lastMessage, lastSenderRole = :lastSenderRole, " +
      // Thực hiện lời gọi phương thức hoặc khởi tạo: "lastTimestamp = :lastTimestamp, updatedAt = :updatedAt WHERE threadId = :threadId").
      "lastTimestamp = :lastTimestamp, updatedAt = :updatedAt WHERE threadId = :threadId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void updateLastMessage(String threadId, String lastMessage, String lastSenderRole, long lastTimestamp, long updatedAt);.
  void updateLastMessage(String threadId, String lastMessage, String lastSenderRole, long lastTimestamp, long updatedAt);

  // Áp dụng annotation @Query("UPDATE cho phần tử bên dưới.
  @Query("UPDATE chat_threads SET unreadCount = :count, updatedAt = :updatedAt WHERE threadId = :threadId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void updateUnread(String threadId, int count, long updatedAt);.
  void updateUnread(String threadId, int count, long updatedAt);

  // Áp dụng annotation @Transaction cho phần tử bên dưới.
  @Transaction
  // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
  default void touchThread(String threadId, int userId, String lastMessage, String lastSenderRole, long timestamp, int unreadDelta) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity existing = getThread(threadId);.
    ChatThreadEntity existing = getThread(threadId);
    // Gán giá trị cho biến hoặc thuộc tính: ChatThreadEntity target = existing == null ? new ChatThreadEntity() : existing.
    ChatThreadEntity target = existing == null ? new ChatThreadEntity() : existing;
    // Gán giá trị cho biến hoặc thuộc tính: target.threadId = threadId.
    target.threadId = threadId;
    // Gán giá trị cho biến hoặc thuộc tính: target.userId = existing == null ? userId : existing.userId.
    target.userId = existing == null ? userId : existing.userId;
    // Gán giá trị cho biến hoặc thuộc tính: target.lastMessage = lastMessage.
    target.lastMessage = lastMessage;
    // Gán giá trị cho biến hoặc thuộc tính: target.lastSenderRole = lastSenderRole.
    target.lastSenderRole = lastSenderRole;
    // Gán giá trị cho biến hoặc thuộc tính: target.lastTimestamp = timestamp.
    target.lastTimestamp = timestamp;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: target.updatedAt = System.currentTimeMillis();.
    target.updatedAt = System.currentTimeMillis();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: target.unreadCount = Math.max(0, (existing == null ? 0 : existing.unreadCount) + unreadDelta);.
    target.unreadCount = Math.max(0, (existing == null ? 0 : existing.unreadCount) + unreadDelta);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: upsert(target);.
    upsert(target);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
