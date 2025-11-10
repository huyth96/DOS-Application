// Khai báo package com.drinkorder.ui.admin cho toàn bộ lớp.
package com.drinkorder.ui.admin;

// Import android.app.Application để sử dụng các lớp hoặc hàm tương ứng.
import android.app.Application;
// Import android.os.Handler để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Handler;
// Import android.os.Looper để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Looper;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.lifecycle.AndroidViewModel để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.AndroidViewModel;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;

// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.dao.CategoryDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.CategoryDao;
// Import com.drinkorder.data.db.dao.ProductDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.ProductDao;
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;

// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.concurrent.Executor để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executor;
// Import java.util.concurrent.Executors để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executors;

// Định nghĩa lớp AdminProductFormVM kế thừa AndroidViewModel.
public class AdminProductFormVM extends AndroidViewModel {

  // Khai báo thuộc tính với phạm vi truy cập: private final ProductDao productDao.
  private final ProductDao productDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final CategoryDao categoryDao.
  private final CategoryDao categoryDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final Executor io = Executors.newSingleThreadExecutor().
  private final Executor io = Executors.newSingleThreadExecutor();
  // Khai báo thuộc tính với phạm vi truy cập: private final Handler main = new Handler(Looper.getMainLooper()).
  private final Handler main = new Handler(Looper.getMainLooper());

  // Định nghĩa phương thức AdminProductFormVM với phạm vi truy cập tương ứng.
  public AdminProductFormVM(@NonNull Application app) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(app);.
    super(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: AppDatabase db = AppDatabase.get(app);.
    AppDatabase db = AppDatabase.get(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: productDao = db.productDao();.
    productDao = db.productDao();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryDao = db.categoryDao();.
    categoryDao = db.categoryDao();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức categories với phạm vi truy cập tương ứng.
  public LiveData<List<CategoryEntity>> categories() {
    // Trả về kết quả categoryDao.all();.
    return categoryDao.all();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức product với phạm vi truy cập tương ứng.
  public LiveData<ProductEntity> product(int productId) {
    // Trả về kết quả productDao.byId(productId);.
    return productDao.byId(productId);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa interface SaveCallback.
  public interface SaveCallback {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onSuccess(int productId);.
    void onSuccess(int productId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onError(Throwable throwable);.
    void onError(Throwable throwable);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức save với phạm vi truy cập tương ứng.
  public void save(ProductEntity product, @Nullable SaveCallback callback) {
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    io.execute(() -> {
      // Bắt đầu khối try để bắt lỗi có thể phát sinh.
      try {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (product.productId > 0) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: productDao.update(product);.
          productDao.update(product);
        // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
        } else {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: long newId = productDao.insert(product);.
          long newId = productDao.insert(product);
          // Gán giá trị cho biến hoặc thuộc tính: product.productId = (int) newId.
          product.productId = (int) newId;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (callback != null) main.post(() -> callback.onSuccess(product.productId));
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      } catch (Throwable t) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (callback != null) main.post(() -> callback.onError(t));
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
