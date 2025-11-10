// Khai báo package com.drinkorder.data.db cho toàn bộ lớp.
package com.drinkorder.data.db;
// Import android.content.Context để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Context;
// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.room.Database để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Database;
// Import androidx.room.Room để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Room;
// Import androidx.room.RoomDatabase để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.RoomDatabase;
// Import androidx.room.migration.Migration để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.migration.Migration;
// Import androidx.sqlite.db.SupportSQLiteDatabase để sử dụng các lớp hoặc hàm tương ứng.
import androidx.sqlite.db.SupportSQLiteDatabase;
// Import com.drinkorder.data.db.dao.CartDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.CartDao;
// Import com.drinkorder.data.db.dao.CategoryDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.CategoryDao;
// Import com.drinkorder.data.db.dao.OrderDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.OrderDao;
// Import com.drinkorder.data.db.dao.PaymentDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.PaymentDao;
// Import com.drinkorder.data.db.dao.ProductDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.ProductDao;
// Import com.drinkorder.data.db.dao.UserDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.UserDao;
// Import com.drinkorder.data.db.dao.chat.ChatMessageDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.chat.ChatMessageDao;
// Import com.drinkorder.data.db.dao.chat.ChatThreadDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.chat.ChatThreadDao;
// Import com.drinkorder.data.db.entity.CartItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CartItemEntity;
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import com.drinkorder.data.db.entity.OrderEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderEntity;
// Import com.drinkorder.data.db.entity.OrderItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderItemEntity;
// Import com.drinkorder.data.db.entity.PaymentEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.PaymentEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.drinkorder.data.db.entity.UserEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.UserEntity;
// Import com.drinkorder.data.db.entity.chat.ChatMessageEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
// Import com.drinkorder.data.db.entity.chat.ChatThreadEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;

// Áp dụng annotation @Database(entities cho phần tử bên dưới.
@Database(entities = {
  // Thực thi câu lệnh: UserEntity.class,.
  UserEntity.class,
  // Thực thi câu lệnh: CategoryEntity.class,.
  CategoryEntity.class,
  // Thực thi câu lệnh: ProductEntity.class,.
  ProductEntity.class,
  // Thực thi câu lệnh: CartItemEntity.class,.
  CartItemEntity.class,
  // Thực thi câu lệnh: OrderEntity.class,.
  OrderEntity.class,
  // Thực thi câu lệnh: OrderItemEntity.class,.
  OrderItemEntity.class,
  // Thực thi câu lệnh: PaymentEntity.class,.
  PaymentEntity.class,
  // Thực thi câu lệnh: ChatThreadEntity.class,.
  ChatThreadEntity.class,
  // Thực thi câu lệnh: ChatMessageEntity.class.
  ChatMessageEntity.class
// Thực hiện lời gọi phương thức hoặc khởi tạo: }, version = 4, exportSchema = false).
}, version = 4, exportSchema = false)
// Định nghĩa lớp AppDatabase kế thừa RoomDatabase.
public abstract class AppDatabase extends RoomDatabase {
  // Khai báo thuộc tính với phạm vi truy cập: public abstract UserDao userDao().
  public abstract UserDao userDao();
  // Khai báo thuộc tính với phạm vi truy cập: public abstract CategoryDao categoryDao().
  public abstract CategoryDao categoryDao();
  // Khai báo thuộc tính với phạm vi truy cập: public abstract ProductDao productDao().
  public abstract ProductDao productDao();
  // Khai báo thuộc tính với phạm vi truy cập: public abstract CartDao cartDao().
  public abstract CartDao cartDao();
  // Khai báo thuộc tính với phạm vi truy cập: public abstract OrderDao orderDao().
  public abstract OrderDao orderDao();
  // Khai báo thuộc tính với phạm vi truy cập: public abstract PaymentDao paymentDao().
  public abstract PaymentDao paymentDao();
  // Khai báo thuộc tính với phạm vi truy cập: public abstract ChatThreadDao chatThreadDao().
  public abstract ChatThreadDao chatThreadDao();
  // Khai báo thuộc tính với phạm vi truy cập: public abstract ChatMessageDao chatMessageDao().
  public abstract ChatMessageDao chatMessageDao();

  // Khai báo thuộc tính với phạm vi truy cập: private static volatile AppDatabase INSTANCE.
  private static volatile AppDatabase INSTANCE;
  // Định nghĩa phương thức get với phạm vi truy cập tương ứng.
  public static AppDatabase get(Context ctx){
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (INSTANCE == null){
      // Đồng bộ hóa khối lệnh để tránh xung đột luồng.
      synchronized (AppDatabase.class){
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (INSTANCE == null){
          // Thực hiện lời gọi phương thức hoặc khởi tạo: INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, "drinkorder.db").
          INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, "drinkorder.db")
            // Thực hiện lời gọi phương thức hoặc khởi tạo: .addMigrations(MIGRATION_3_4).
            .addMigrations(MIGRATION_3_4)
            // Thực hiện lời gọi phương thức hoặc khởi tạo: .build();.
            .build();
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả INSTANCE;.
    return INSTANCE;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức Migration với phạm vi truy cập tương ứng.
  public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức migrate với phạm vi truy cập tương ứng.
    public void migrate(@NonNull SupportSQLiteDatabase db) {
      // Thực thi câu lệnh: db.execSQL(.
      db.execSQL(
          // Thực thi câu lệnh: "CREATE TABLE IF NOT EXISTS chat_threads (" +.
          "CREATE TABLE IF NOT EXISTS chat_threads (" +
              // Thực thi câu lệnh: "threadId TEXT NOT NULL PRIMARY KEY, " +.
              "threadId TEXT NOT NULL PRIMARY KEY, " +
              // Thực thi câu lệnh: "userId INTEGER NOT NULL, " +.
              "userId INTEGER NOT NULL, " +
              // Thực thi câu lệnh: "title TEXT, " +.
              "title TEXT, " +
              // Thực thi câu lệnh: "lastMessage TEXT, " +.
              "lastMessage TEXT, " +
              // Thực thi câu lệnh: "lastSenderRole TEXT, " +.
              "lastSenderRole TEXT, " +
              // Thực thi câu lệnh: "lastTimestamp INTEGER NOT NULL, " +.
              "lastTimestamp INTEGER NOT NULL, " +
              // Thực thi câu lệnh: "unreadCount INTEGER NOT NULL DEFAULT 0, " +.
              "unreadCount INTEGER NOT NULL DEFAULT 0, " +
              // Thực thi câu lệnh: "updatedAt INTEGER NOT NULL DEFAULT 0)".
              "updatedAt INTEGER NOT NULL DEFAULT 0)"
      // Thực hiện lời gọi phương thức hoặc khởi tạo: );.
      );
      // Thực hiện lời gọi phương thức hoặc khởi tạo: db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_threads_userId ON chat_threads(userId)");.
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_threads_userId ON chat_threads(userId)");
      // Thực hiện lời gọi phương thức hoặc khởi tạo: db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_threads_lastTimestamp ON chat_threads(lastTimestamp)");.
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_threads_lastTimestamp ON chat_threads(lastTimestamp)");
      // Thực thi câu lệnh: db.execSQL(.
      db.execSQL(
          // Thực thi câu lệnh: "CREATE TABLE IF NOT EXISTS chat_messages (" +.
          "CREATE TABLE IF NOT EXISTS chat_messages (" +
              // Thực thi câu lệnh: "messageId TEXT NOT NULL PRIMARY KEY, " +.
              "messageId TEXT NOT NULL PRIMARY KEY, " +
              // Thực thi câu lệnh: "threadId TEXT NOT NULL, " +.
              "threadId TEXT NOT NULL, " +
              // Thực thi câu lệnh: "senderRole TEXT, " +.
              "senderRole TEXT, " +
              // Thực thi câu lệnh: "body TEXT, " +.
              "body TEXT, " +
              // Thực thi câu lệnh: "sentAt INTEGER NOT NULL, " +.
              "sentAt INTEGER NOT NULL, " +
              // Thực thi câu lệnh: "deliveredAt INTEGER, " +.
              "deliveredAt INTEGER, " +
              // Thực thi câu lệnh: "isOutgoing INTEGER NOT NULL, " +.
              "isOutgoing INTEGER NOT NULL, " +
              // Thực thi câu lệnh: "isPending INTEGER NOT NULL, " +.
              "isPending INTEGER NOT NULL, " +
              // Thực thi câu lệnh: "FOREIGN KEY(threadId) REFERENCES chat_threads(threadId) ON UPDATE NO ACTION ON DELETE CASCADE)".
              "FOREIGN KEY(threadId) REFERENCES chat_threads(threadId) ON UPDATE NO ACTION ON DELETE CASCADE)"
      // Thực hiện lời gọi phương thức hoặc khởi tạo: );.
      );
      // Thực hiện lời gọi phương thức hoặc khởi tạo: db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_threadId ON chat_messages(threadId)");.
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_threadId ON chat_messages(threadId)");
      // Thực hiện lời gọi phương thức hoặc khởi tạo: db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_sentAt ON chat_messages(sentAt)");.
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_sentAt ON chat_messages(sentAt)");
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Thực thi câu lệnh: };.
  };
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
