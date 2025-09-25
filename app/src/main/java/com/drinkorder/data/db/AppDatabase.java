package com.drinkorder.data.db;
import android.content.Context;
import androidx.room.*;
import com.drinkorder.data.db.dao.*;
import com.drinkorder.data.db.entity.*;

@Database(entities = {
  UserEntity.class, CategoryEntity.class, ProductEntity.class,
  CartItemEntity.class, OrderEntity.class, OrderItemEntity.class, PaymentEntity.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
  public abstract UserDao userDao();
  public abstract CategoryDao categoryDao();
  public abstract ProductDao productDao();
  public abstract CartDao cartDao();
  public abstract OrderDao orderDao();
  public abstract PaymentDao paymentDao();

  private static volatile AppDatabase INSTANCE;
  public static AppDatabase get(Context ctx){
    if (INSTANCE == null){
      synchronized (AppDatabase.class){
        if (INSTANCE == null){
          INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, "drinkorder.db")
            .fallbackToDestructiveMigration()
            .build();
        }
      }
    }
    return INSTANCE;
  }
}
