package com.drinkorder.data.db;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.drinkorder.data.db.dao.CartDao;
import com.drinkorder.data.db.dao.CategoryDao;
import com.drinkorder.data.db.dao.OrderDao;
import com.drinkorder.data.db.dao.PaymentDao;
import com.drinkorder.data.db.dao.ProductDao;
import com.drinkorder.data.db.dao.UserDao;
import com.drinkorder.data.db.dao.chat.ChatMessageDao;
import com.drinkorder.data.db.dao.chat.ChatThreadDao;
import com.drinkorder.data.db.entity.CartItemEntity;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.db.entity.OrderItemEntity;
import com.drinkorder.data.db.entity.PaymentEntity;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.data.db.entity.UserEntity;
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;

@Database(entities = {
  UserEntity.class,
  CategoryEntity.class,
  ProductEntity.class,
  CartItemEntity.class,
  OrderEntity.class,
  OrderItemEntity.class,
  PaymentEntity.class,
  ChatThreadEntity.class,
  ChatMessageEntity.class
}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
  public abstract UserDao userDao();
  public abstract CategoryDao categoryDao();
  public abstract ProductDao productDao();
  public abstract CartDao cartDao();
  public abstract OrderDao orderDao();
  public abstract PaymentDao paymentDao();
  public abstract ChatThreadDao chatThreadDao();
  public abstract ChatMessageDao chatMessageDao();

  private static volatile AppDatabase INSTANCE;
  public static AppDatabase get(Context ctx){
    if (INSTANCE == null){
      synchronized (AppDatabase.class){
        if (INSTANCE == null){
          INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, "drinkorder.db")
            .addMigrations(MIGRATION_3_4)
            .build();
        }
      }
    }
    return INSTANCE;
  }

  public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase db) {
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
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_threads_userId ON chat_threads(userId)");
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_threads_lastTimestamp ON chat_threads(lastTimestamp)");
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
              "FOREIGN KEY(threadId) REFERENCES chat_threads(threadId) ON UPDATE NO ACTION ON DELETE CASCADE)"
      );
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_threadId ON chat_messages(threadId)");
      db.execSQL("CREATE INDEX IF NOT EXISTS index_chat_messages_sentAt ON chat_messages(sentAt)");
    }
  };
}
