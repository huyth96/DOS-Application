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
import com.drinkorder.data.db.dao.ProductDao;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.db.entity.ProductEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdminProductFormVM extends AndroidViewModel {

  private final ProductDao productDao;
  private final CategoryDao categoryDao;
  private final Executor io = Executors.newSingleThreadExecutor();
  private final Handler main = new Handler(Looper.getMainLooper());

  public AdminProductFormVM(@NonNull Application app) {
    super(app);
    AppDatabase db = AppDatabase.get(app);
    productDao = db.productDao();
    categoryDao = db.categoryDao();
  }

  public LiveData<List<CategoryEntity>> categories() {
    return categoryDao.all();
  }

  public LiveData<ProductEntity> product(int productId) {
    return productDao.byId(productId);
  }

  public interface SaveCallback {
    void onSuccess(int productId);
    void onError(Throwable throwable);
  }

  public void save(ProductEntity product, @Nullable SaveCallback callback) {
    io.execute(() -> {
      try {
        if (product.productId > 0) {
          productDao.update(product);
        } else {
          long newId = productDao.insert(product);
          product.productId = (int) newId;
        }
        if (callback != null) main.post(() -> callback.onSuccess(product.productId));
      } catch (Throwable t) {
        if (callback != null) main.post(() -> callback.onError(t));
      }
    });
  }
}
