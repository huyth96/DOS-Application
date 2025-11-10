package com.drinkorder.ui.admin;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.dao.UserDao;
import com.drinkorder.data.db.entity.UserEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdminUsersVM extends AndroidViewModel {

  public interface ActionCallback {
    void onSuccess();
    void onError(Throwable throwable);
  }

  private final UserDao userDao;
  private final Executor io = Executors.newSingleThreadExecutor();
  private final Handler main = new Handler(Looper.getMainLooper());
  public final LiveData<List<UserEntity>> users;

  public AdminUsersVM(@NonNull Application application) {
    super(application);
    AppDatabase db = AppDatabase.get(application);
    userDao = db.userDao();
    users = userDao.observeAll();
  }

  public void setBanStatus(@NonNull UserEntity user, boolean ban, @Nullable ActionCallback callback) {
    io.execute(() -> {
      try {
        userDao.updateBanStatus(user.userId, ban);
        if (callback != null) {
          main.post(callback::onSuccess);
        }
      } catch (Throwable throwable) {
        if (callback != null) {
          main.post(() -> callback.onError(throwable));
        }
      }
    });
  }
}
