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

public class AdminProductsVM extends AndroidViewModel { // Định nghĩa lớp AdminProductsVM kế thừa từ AndroidViewModel, dùng để quản lý dữ liệu sản phẩm cho giao diện admin.

  private final ProductDao productDao; // Biến final lưu DAO cho sản phẩm, dùng để tương tác với cơ sở dữ liệu sản phẩm.
  private final Executor io = Executors.newSingleThreadExecutor(); // Biến final lưu Executor cho các tác vụ IO, sử dụng single thread để chạy background.
  private final Handler main = new Handler(Looper.getMainLooper()); // Biến final lưu Handler cho main thread, dùng để post callback về UI thread.
  public final LiveData<List<ProductEntity>> products; // Biến public final lưu LiveData danh sách tất cả sản phẩm, quan sát thay đổi từ database.
  public final LiveData<List<CategoryEntity>> categories; // Biến public final lưu LiveData danh sách tất cả danh mục, quan sát thay đổi từ database.

  public AdminProductsVM(@NonNull Application app) { // Constructor của ViewModel, nhận Application không null.
    super(app); // Gọi constructor của lớp cha AndroidViewModel.
    AppDatabase db = AppDatabase.get(app); // Lấy instance cơ sở dữ liệu từ Application.
    productDao = db.productDao(); // Lấy DAO cho sản phẩm từ database.
    products = productDao.all(); // Gán LiveData từ DAO để lấy tất cả sản phẩm.
    categories = db.categoryDao().all(); // Gán LiveData từ DAO để lấy tất cả danh mục.
  }

  public interface ActionCallback { // Interface định nghĩa callback cho các hành động như delete.
    void onSuccess(); // Phương thức gọi khi thành công.
    void onError(Throwable throwable); // Phương thức gọi khi lỗi, nhận Throwable.
  }

  public void deleteProduct(ProductEntity product, @Nullable ActionCallback callback) { // Phương thức xóa sản phẩm, nhận entity và callback có thể null.
    if (product == null) { // Kiểm tra nếu product null.
      if (callback != null) callback.onError(new IllegalArgumentException("Product is null")); // Nếu có callback, gọi onError với exception.
      return; // Thoát phương thức.
    }
    io.execute(() -> { // Chạy tác vụ trên background thread qua Executor.
      try { // Bắt đầu khối try-catch để xử lý lỗi.
        productDao.delete(product); // Gọi delete trên DAO để xóa sản phẩm.
        if (callback != null) main.post(callback::onSuccess); // Nếu có callback, post onSuccess về main thread.
      } catch (Throwable t) { // Bắt mọi Throwable.
        if (callback != null) main.post(() -> callback.onError(t)); // Nếu có callback, post onError về main thread.
      }
    });
  }
}
