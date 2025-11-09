package com.drinkorder.ui.admin;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.dao.ProductDao;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.db.entity.ProductEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdminProductsVM extends AndroidViewModel {
  private final ProductDao productDao;
  private final Executor io = Executors.newSingleThreadExecutor();
  private final Handler main = new Handler(Looper.getMainLooper());
  public final LiveData<List<ProductEntity>> products;
  public final LiveData<List<CategoryEntity>> categories;

  public AdminProductsVM(@NonNull Application app) {
    super(app);
    AppDatabase db = AppDatabase.get(app);
    productDao = db.productDao();
    products = productDao.all();
    categories = db.categoryDao().all();
  }

  public interface ActionCallback {
    void onSuccess();
    void onError(Throwable throwable);
  }

  public void deleteProduct(ProductEntity product, @Nullable ActionCallback callback) {
    if (product == null) {
      if (callback != null) callback.onError(new IllegalArgumentException("Product is null"));
      return;
    }
    io.execute(() -> {
      try {
        productDao.delete(product);
        if (callback != null) main.post(callback::onSuccess);
      } catch (Throwable t) {
        if (callback != null) main.post(() -> callback.onError(t));
      }
    });
  }
}
