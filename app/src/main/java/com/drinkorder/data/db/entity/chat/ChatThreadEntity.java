// Khai báo package com.drinkorder.data.db.entity.chat cho toàn bộ lớp.
package com.drinkorder.data.db.entity.chat;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.room.Entity để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Entity;
// Import androidx.room.Index để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Index;
// Import androidx.room.PrimaryKey để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.PrimaryKey;

// Áp dụng annotation @Entity( cho phần tử bên dưới.
@Entity(
    // Thực thi câu lệnh: tableName = "chat_threads",.
    tableName = "chat_threads",
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    indices = {
        // Áp dụng annotation @Index("userId"), cho phần tử bên dưới.
        @Index("userId"),
        // Áp dụng annotation @Index("lastTimestamp") cho phần tử bên dưới.
        @Index("lastTimestamp")
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Thực hiện lời gọi phương thức hoặc khởi tạo: ).
)
// Định nghĩa lớp ChatThreadEntity.
public class ChatThreadEntity {
  // Áp dụng annotation @PrimaryKey cho phần tử bên dưới.
  @PrimaryKey
  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull
  // Khai báo thuộc tính với phạm vi truy cập: public String threadId.
  public String threadId;
  // Khai báo thuộc tính với phạm vi truy cập: public int userId.
  public int userId;
  // Khai báo thuộc tính với phạm vi truy cập: public String title.
  public String title;
  // Khai báo thuộc tính với phạm vi truy cập: public String lastMessage.
  public String lastMessage;
  // Khai báo thuộc tính với phạm vi truy cập: public String lastSenderRole.
  public String lastSenderRole;
  // Khai báo thuộc tính với phạm vi truy cập: public long lastTimestamp.
  public long lastTimestamp;
  // Khai báo thuộc tính với phạm vi truy cập: public int unreadCount.
  public int unreadCount;
  // Khai báo thuộc tính với phạm vi truy cập: public long updatedAt.
  public long updatedAt;

  // Định nghĩa phương thức ChatThreadEntity với phạm vi truy cập tương ứng.
  public ChatThreadEntity() {
    // Gán giá trị cho biến hoặc thuộc tính: this.threadId = "".
    this.threadId = "";
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
