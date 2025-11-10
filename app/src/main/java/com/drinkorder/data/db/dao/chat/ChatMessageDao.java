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

// Import com.drinkorder.data.db.entity.chat.ChatMessageEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;

// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;

// Áp dụng annotation @Dao cho phần tử bên dưới.
@Dao
// Định nghĩa interface ChatMessageDao.
public interface ChatMessageDao {
  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM chat_messages WHERE threadId = :threadId ORDER BY sentAt ASC")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<List<ChatMessageEntity>> observeMessages(String threadId);.
  LiveData<List<ChatMessageEntity>> observeMessages(String threadId);

  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM chat_messages WHERE messageId = :messageId LIMIT 1")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatMessageEntity findById(String messageId);.
  ChatMessageEntity findById(String messageId);

  // Áp dụng annotation @Insert(onConflict cho phần tử bên dưới.
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void upsert(ChatMessageEntity entity);.
  void upsert(ChatMessageEntity entity);

  // Áp dụng annotation @Insert(onConflict cho phần tử bên dưới.
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void upsert(List<ChatMessageEntity> entities);.
  void upsert(List<ChatMessageEntity> entities);

  // Áp dụng annotation @Query("UPDATE cho phần tử bên dưới.
  @Query("UPDATE chat_messages SET deliveredAt = :deliveredAt, isPending = 0 WHERE messageId = :messageId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void markDelivered(String messageId, Long deliveredAt);.
  void markDelivered(String messageId, Long deliveredAt);

  // Áp dụng annotation @Query("UPDATE cho phần tử bên dưới.
  @Query("UPDATE chat_messages SET messageId = :newMessageId, deliveredAt = :deliveredAt, isPending = 0 WHERE messageId = :clientMessageId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void acknowledge(String clientMessageId, String newMessageId, Long deliveredAt);.
  void acknowledge(String clientMessageId, String newMessageId, Long deliveredAt);

  // Áp dụng annotation @Query("UPDATE và ghi đè phương thức IN.
  @Query("UPDATE chat_messages SET isPending = 0 WHERE messageId IN(:ids)")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void clearPending(List<String> ids);.
  void clearPending(List<String> ids);

  // Áp dụng annotation @Query("DELETE cho phần tử bên dưới.
  @Query("DELETE FROM chat_messages WHERE threadId = :threadId AND messageId = :messageId")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: void deleteMessage(String threadId, String messageId);.
  void deleteMessage(String threadId, String messageId);
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
