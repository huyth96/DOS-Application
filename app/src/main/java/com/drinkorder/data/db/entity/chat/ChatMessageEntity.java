// Khai báo package com.drinkorder.data.db.entity.chat cho toàn bộ lớp.
package com.drinkorder.data.db.entity.chat;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.room.Entity để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Entity;
// Import androidx.room.ForeignKey để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.ForeignKey;
// Import androidx.room.Index để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Index;
// Import androidx.room.PrimaryKey để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.PrimaryKey;

// Áp dụng annotation @Entity( cho phần tử bên dưới.
@Entity(
    // Thực thi câu lệnh: tableName = "chat_messages",.
    tableName = "chat_messages",
    // Thực thi câu lệnh: foreignKeys = @ForeignKey(.
    foreignKeys = @ForeignKey(
        // Thực thi câu lệnh: entity = ChatThreadEntity.class,.
        entity = ChatThreadEntity.class,
        // Thực thi câu lệnh: parentColumns = "threadId",.
        parentColumns = "threadId",
        // Thực thi câu lệnh: childColumns = "threadId",.
        childColumns = "threadId",
        // Thực thi câu lệnh: onDelete = ForeignKey.CASCADE.
        onDelete = ForeignKey.CASCADE
    // Thực thi câu lệnh: ),.
    ),
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    indices = {
        // Áp dụng annotation @Index("threadId"), cho phần tử bên dưới.
        @Index("threadId"),
        // Áp dụng annotation @Index("sentAt") cho phần tử bên dưới.
        @Index("sentAt")
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Thực hiện lời gọi phương thức hoặc khởi tạo: ).
)
// Định nghĩa lớp ChatMessageEntity.
public class ChatMessageEntity {
  // Áp dụng annotation @PrimaryKey cho phần tử bên dưới.
  @PrimaryKey
  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull
  // Khai báo thuộc tính với phạm vi truy cập: public String messageId.
  public String messageId;
  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull
  // Khai báo thuộc tính với phạm vi truy cập: public String threadId.
  public String threadId;
  // Khai báo thuộc tính với phạm vi truy cập: public String senderRole.
  public String senderRole;
  // Khai báo thuộc tính với phạm vi truy cập: public String body.
  public String body;
  // Khai báo thuộc tính với phạm vi truy cập: public long sentAt.
  public long sentAt;
  // Khai báo thuộc tính với phạm vi truy cập: public Long deliveredAt.
  public Long deliveredAt;
  // Khai báo thuộc tính với phạm vi truy cập: public boolean isOutgoing.
  public boolean isOutgoing;
  // Khai báo thuộc tính với phạm vi truy cập: public boolean isPending.
  public boolean isPending;

  // Định nghĩa phương thức ChatMessageEntity với phạm vi truy cập tương ứng.
  public ChatMessageEntity() {
    // Gán giá trị cho biến hoặc thuộc tính: this.messageId = "".
    this.messageId = "";
    // Gán giá trị cho biến hoặc thuộc tính: this.threadId = "".
    this.threadId = "";
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
