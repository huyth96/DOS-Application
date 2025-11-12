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

public class AdminCategoriesVM extends AndroidViewModel { // Định nghĩa lớp AdminCategoriesVM kế thừa từ AndroidViewModel, dùng để quản lý dữ liệu danh mục cho giao diện admin.

  private final CategoryDao categoryDao; // Biến final lưu DAO cho danh mục, dùng để tương tác với cơ sở dữ liệu.
  private final Executor io = Executors.newSingleThreadExecutor(); // Biến final lưu Executor cho các tác vụ IO, sử dụng single thread để chạy background.
  private final Handler main = new Handler(Looper.getMainLooper()); // Biến final lưu Handler cho main thread, dùng để post callback về UI thread.
  public final LiveData<List<CategoryEntity>> categories; // Biến public final lưu LiveData danh sách tất cả danh mục, quan sát thay đổi từ database.

  public AdminCategoriesVM(@NonNull Application app) { // Constructor của ViewModel, nhận Application không null.
    super(app); // Gọi constructor của lớp cha AndroidViewModel.
    AppDatabase db = AppDatabase.get(app); // Lấy instance cơ sở dữ liệu từ Application.
    categoryDao = db.categoryDao(); // Lấy DAO cho danh mục từ database.
    categories = categoryDao.all(); // Gán LiveData từ DAO để lấy tất cả danh mục.
  }

  public interface ActionCallback { // Interface định nghĩa callback cho các hành động như save hoặc delete.
    void onSuccess(); // Phương thức gọi khi thành công.
    void onError(Throwable throwable); // Phương thức gọi khi lỗi, nhận Throwable.
  }

  public void save(CategoryEntity category, @Nullable ActionCallback callback) { // Phương thức lưu danh mục, nhận entity và callback có thể null.
    if (category == null) { // Kiểm tra nếu category null.
      if (callback != null) callback.onError(new IllegalArgumentException("Category is null")); // Nếu có callback, gọi onError với exception.
      return; // Thoát phương thức.
    }
    io.execute(() -> { // Chạy tác vụ trên background thread qua Executor.
      try { // Bắt đầu khối try-catch để xử lý lỗi.
        if (category.categoryId > 0) { // Nếu ID > 0, nghĩa là cập nhật.
          categoryDao.update(category); // Gọi update trên DAO.
        } else { // Ngược lại, là insert mới.
          if (category.createdAt <= 0) category.createdAt = System.currentTimeMillis(); // Nếu createdAt <= 0, đặt thời gian hiện tại.
          long newId = categoryDao.insert(category); // Insert và lấy ID mới.
          category.categoryId = (int) newId; // Gán ID mới vào entity (cast long sang int).
        }
        if (callback != null) main.post(callback::onSuccess); // Nếu có callback, post onSuccess về main thread.
      } catch (Throwable t) { // Bắt mọi Throwable.
        if (callback != null) main.post(() -> callback.onError(t)); // Nếu có callback, post onError về main thread.
      }
    });
  }

  public void delete(CategoryEntity category, @Nullable ActionCallback callback) { // Phương thức xóa danh mục, nhận entity và callback có thể null.
    if (category == null) { // Kiểm tra nếu category null.
      if (callback != null) callback.onError(new IllegalArgumentException("Category is null")); // Nếu có callback, gọi onError với exception.
      return; // Thoát phương thức.
    }
    io.execute(() -> { // Chạy tác vụ trên background thread qua Executor.
      try { // Bắt đầu khối try-catch để xử lý lỗi.
        categoryDao.delete(category); // Gọi delete trên DAO.
        if (callback != null) main.post(callback::onSuccess); // Nếu có callback, post onSuccess về main thread.
      } catch (Throwable t) { // Bắt mọi Throwable.
        if (callback != null) main.post(() -> callback.onError(t)); // Nếu có callback, post onError về main thread.
      }
    });
  }
}
