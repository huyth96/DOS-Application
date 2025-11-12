package com.drinkorder.data.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.drinkorder.data.db.dao.*;
import com.drinkorder.data.db.dao.chat.ChatMessageDao;
import com.drinkorder.data.db.dao.chat.ChatThreadDao;
import com.drinkorder.data.db.entity.*;
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;

/**
 * AppDatabase
 * ====================================
 * Lớp trung tâm của Room Database – định nghĩa toàn bộ cấu trúc và entry point
 * để truy cập các bảng (Entity) thông qua các DAO.
 *
 * 💡 Vai trò:
 * - Quản lý toàn bộ các bảng trong cơ sở dữ liệu (qua `entities`).
 * - Cung cấp các DAO giúp truy vấn / thêm / sửa / xóa dữ liệu.
 * - Đảm bảo chỉ tồn tại 1 instance duy nhất trong toàn app (singleton).
 */
@Database(
        entities = {
                UserEntity.class,        // Bảng người dùng
                CategoryEntity.class,    // Bảng danh mục sản phẩm
                ProductEntity.class,     // Bảng sản phẩm
                CartItemEntity.class,    // Bảng giỏ hàng
                OrderEntity.class,       // Bảng đơn hàng
                OrderItemEntity.class,   // Bảng chi tiết đơn hàng
                PaymentEntity.class,     // Bảng thanh toán
                ChatThreadEntity.class,  // Bảng danh sách đoạn chat
                ChatMessageEntity.class  // Bảng tin nhắn trong đoạn chat
        },
        version = 4,                 // Version DB – phải tăng khi thay đổi schema
        exportSchema = false         // Tắt export file JSON schema (chỉ để dev dùng)
)
public abstract class AppDatabase extends RoomDatabase {

  // ====== KHAI BÁO DAO ======
  // Mỗi DAO tương ứng với 1 nhóm bảng / chức năng riêng.
  public abstract UserDao userDao();
  public abstract CategoryDao categoryDao();
  public abstract ProductDao productDao();
  public abstract CartDao cartDao();
  public abstract OrderDao orderDao();
  public abstract PaymentDao paymentDao();
  public abstract ChatThreadDao chatThreadDao();
  public abstract ChatMessageDao chatMessageDao();

  // ====== SINGLETON INSTANCE ======
  // Dùng mô hình Singleton để toàn app chỉ có 1 kết nối tới DB,
  // tránh tạo nhiều instance gây tốn tài nguyên.
  private static volatile AppDatabase INSTANCE;

  /**
   * Hàm khởi tạo (singleton)
   * ----------------------------
   * - Nếu INSTANCE null, tạo mới bằng Room.databaseBuilder.
   * - Dùng synchronized để đảm bảo an toàn khi truy cập đa luồng.
   * - Database được tạo trong ApplicationContext để tránh leak.
   */
  public static AppDatabase get(Context ctx) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(
                          ctx.getApplicationContext(),
                          AppDatabase.class,
                          "drinkorder.db"               // Tên file SQLite thực tế
                  )
                  // Thêm migration khi nâng version DB
                  .addMigrations(MIGRATION_3_4)
                  // Nếu đang dev có thể thêm .fallbackToDestructiveMigration()
                  .build();
        }
      }
    }
    return INSTANCE;
  }

  // ====== MIGRATION: NÂNG CẤP DB ======
  /**
   * MIGRATION_3_4
   * ----------------------------
   * Migration định nghĩa cách chuyển schema từ version 3 → 4
   * mà KHÔNG mất dữ liệu hiện có.
   *
   * 💡 Ở đây: thêm 2 bảng mới `chat_threads` và `chat_messages`
   * dùng cho hệ thống chat giữa admin và người dùng.
   *
   * Lưu ý:
   * - Room sẽ tự động gọi `migrate()` khi phát hiện version DB thay đổi.
   * - Nếu không có migration phù hợp mà version thay đổi → app sẽ crash
   *   trừ khi dùng `fallbackToDestructiveMigration()` (xoá toàn DB).
   */
  public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase db) {
      // Tạo bảng lưu danh sách cuộc trò chuyện
      db.execSQL(
              "CREATE TABLE IF NOT EXISTS chat_threads (" +
                      "threadId TEXT NOT NULL PRIMARY KEY, " +
                      "userId INTEGER NOT NULL, " +
                      "title TEXT, " +
                      "lastMessage TEXT, " +
                      "lastSenderRole TEXT, " +
                      "lastTimestamp INTEGER NOT NULL, " +
                      "unreadCount INTEGER NOT NULL DEFAULT 0, " +
                      "updatedAt INTEGER NOT NULL DEFAULT 0)"
      );

      // Tạo index giúp truy vấn nhanh hơn
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_threads_userId ON chat_threads(userId)");
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_threads_lastTimestamp ON chat_threads(lastTimestamp)");

      // Tạo bảng lưu tin nhắn
      db.execSQL(
              "CREATE TABLE IF NOT EXISTS chat_messages (" +
                      "messageId TEXT NOT NULL PRIMARY KEY, " +
                      "threadId TEXT NOT NULL, " +
                      "senderRole TEXT, " +
                      "body TEXT, " +
                      "sentAt INTEGER NOT NULL, " +
                      "deliveredAt INTEGER, " +
                      "isOutgoing INTEGER NOT NULL, " +
                      "isPending INTEGER NOT NULL, " +
                      // Ràng buộc khóa ngoại → khi thread bị xóa thì tin nhắn cũng xóa
                      "FOREIGN KEY(threadId) REFERENCES chat_threads(threadId) " +
                      "ON UPDATE NO ACTION ON DELETE CASCADE)"
      );

      // Tạo index cho chat_messages
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_threadId ON chat_messages(threadId)");
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_sentAt ON chat_messages(sentAt)");
    }
  };
}
