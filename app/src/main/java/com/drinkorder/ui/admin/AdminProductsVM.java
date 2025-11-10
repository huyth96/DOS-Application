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

// Định nghĩa lớp AdminProductsVM kế thừa AndroidViewModel.
public class AdminProductsVM extends AndroidViewModel {
  // Khai báo thuộc tính với phạm vi truy cập: private final ProductDao productDao.
  private final ProductDao productDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final Executor io = Executors.newSingleThreadExecutor().
  private final Executor io = Executors.newSingleThreadExecutor();
  // Khai báo thuộc tính với phạm vi truy cập: private final Handler main = new Handler(Looper.getMainLooper()).
  private final Handler main = new Handler(Looper.getMainLooper());
  // Khai báo thuộc tính với phạm vi truy cập: public final LiveData<List<ProductEntity>> products.
  public final LiveData<List<ProductEntity>> products;
  // Khai báo thuộc tính với phạm vi truy cập: public final LiveData<List<CategoryEntity>> categories.
  public final LiveData<List<CategoryEntity>> categories;

  // Định nghĩa phương thức AdminProductsVM với phạm vi truy cập tương ứng.
  public AdminProductsVM(@NonNull Application app) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(app);.
    super(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: AppDatabase db = AppDatabase.get(app);.
    AppDatabase db = AppDatabase.get(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: productDao = db.productDao();.
    productDao = db.productDao();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: products = productDao.all();.
    products = productDao.all();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categories = db.categoryDao().all();.
    categories = db.categoryDao().all();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa interface ActionCallback.
  public interface ActionCallback {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onSuccess();.
    void onSuccess();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onError(Throwable throwable);.
    void onError(Throwable throwable);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức deleteProduct với phạm vi truy cập tương ứng.
  public void deleteProduct(ProductEntity product, @Nullable ActionCallback callback) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (product == null) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (callback != null) callback.onError(new IllegalArgumentException("Product is null"));
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    io.execute(() -> {
      // Bắt đầu khối try để bắt lỗi có thể phát sinh.
      try {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: productDao.delete(product);.
        productDao.delete(product);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (callback != null) main.post(callback::onSuccess);
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
