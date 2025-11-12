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

public class AdminProductFormVM extends AndroidViewModel { // Định nghĩa lớp AdminProductFormVM kế thừa từ AndroidViewModel, dùng để quản lý dữ liệu cho form sản phẩm admin.

  private final ProductDao productDao; // Biến final lưu DAO cho sản phẩm, dùng để tương tác với cơ sở dữ liệu sản phẩm.
  private final CategoryDao categoryDao; // Biến final lưu DAO cho danh mục, dùng để tương tác với cơ sở dữ liệu danh mục.
  private final Executor io = Executors.newSingleThreadExecutor(); // Biến final lưu Executor cho các tác vụ IO, sử dụng single thread để chạy background.
  private final Handler main = new Handler(Looper.getMainLooper()); // Biến final lưu Handler cho main thread, dùng để post callback về UI thread.

  public AdminProductFormVM(@NonNull Application app) { // Constructor của ViewModel, nhận Application không null.
    super(app); // Gọi constructor của lớp cha AndroidViewModel.
    AppDatabase db = AppDatabase.get(app); // Lấy instance cơ sở dữ liệu từ Application.
    productDao = db.productDao(); // Lấy DAO cho sản phẩm từ database.
    categoryDao = db.categoryDao(); // Lấy DAO cho danh mục từ database.
  }

  public LiveData<List<CategoryEntity>> categories() { // Phương thức trả về LiveData danh sách tất cả danh mục.
    return categoryDao.all(); // Trả về LiveData từ DAO để lấy tất cả danh mục.
  }

  public LiveData<ProductEntity> product(int productId) { // Phương thức trả về LiveData sản phẩm dựa trên ID.
    return productDao.byId(productId); // Trả về LiveData từ DAO để lấy sản phẩm theo ID.
  }

  public interface SaveCallback { // Interface định nghĩa callback cho hành động lưu sản phẩm.
    void onSuccess(int productId); // Phương thức gọi khi thành công, nhận ID sản phẩm.
    void onError(Throwable throwable); // Phương thức gọi khi lỗi, nhận Throwable.
  }

  public void save(ProductEntity product, @Nullable SaveCallback callback) { // Phương thức lưu sản phẩm, nhận entity và callback có thể null.
    io.execute(() -> { // Chạy tác vụ trên background thread qua Executor.
      try { // Bắt đầu khối try-catch để xử lý lỗi.
        if (product.productId > 0) { // Nếu ID > 0, nghĩa là cập nhật.
          productDao.update(product); // Gọi update trên DAO cho sản phẩm.
        } else { // Ngược lại, là insert mới.
          long newId = productDao.insert(product); // Insert và lấy ID mới.
          product.productId = (int) newId; // Gán ID mới vào entity (cast long sang int).
        }
        if (callback != null) main.post(() -> callback.onSuccess(product.productId)); // Nếu có callback, post onSuccess về main thread với ID sản phẩm.
      } catch (Throwable t) { // Bắt mọi Throwable.
        if (callback != null) main.post(() -> callback.onError(t)); // Nếu có callback, post onError về main thread.
      }
    });
  }
}
