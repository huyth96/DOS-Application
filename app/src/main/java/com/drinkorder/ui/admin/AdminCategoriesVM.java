package com.drinkorder.ui.admin;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.dao.CategoryDao;
import com.drinkorder.data.db.entity.CategoryEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdminCategoriesVM extends AndroidViewModel {

  private final CategoryDao categoryDao;
  private final Executor io = Executors.newSingleThreadExecutor();
  private final Handler main = new Handler(Looper.getMainLooper());
  public final LiveData<List<CategoryEntity>> categories;

  public AdminCategoriesVM(@NonNull Application app) {
    super(app);
    AppDatabase db = AppDatabase.get(app);
    categoryDao = db.categoryDao();
    categories = categoryDao.all();
  }

  public interface ActionCallback {
    void onSuccess();
    void onError(Throwable throwable);
  }

  public void save(CategoryEntity category, @Nullable ActionCallback callback) {
    if (category == null) {
      if (callback != null) callback.onError(new IllegalArgumentException("Category is null"));
      return;
    }
    io.execute(() -> {
      try {
        if (category.categoryId > 0) {
          categoryDao.update(category);
        } else {
          if (category.createdAt <= 0) category.createdAt = System.currentTimeMillis();
          long newId = categoryDao.insert(category);
          category.categoryId = (int) newId;
        }
        if (callback != null) main.post(callback::onSuccess);
      } catch (Throwable t) {
        if (callback != null) main.post(() -> callback.onError(t));
      }
    });
  }

  public void delete(CategoryEntity category, @Nullable ActionCallback callback) {
    if (category == null) {
      if (callback != null) callback.onError(new IllegalArgumentException("Category is null"));
      return;
    }
    io.execute(() -> {
      try {
        categoryDao.delete(category);
        if (callback != null) main.post(callback::onSuccess);
      } catch (Throwable t) {
        if (callback != null) main.post(() -> callback.onError(t));
      }
    });
  }
}
