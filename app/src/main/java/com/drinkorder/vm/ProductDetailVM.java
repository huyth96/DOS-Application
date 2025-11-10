// Khai báo package com.drinkorder.vm cho toàn bộ lớp.
package com.drinkorder.vm;

// Import android.app.Application để sử dụng các lớp hoặc hàm tương ứng.
import android.app.Application;
// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.lifecycle.AndroidViewModel để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.AndroidViewModel;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;

// Định nghĩa lớp ProductDetailVM kế thừa AndroidViewModel.
public class ProductDetailVM extends AndroidViewModel {
  // Khai báo thuộc tính với phạm vi truy cập: private final AppDatabase db.
  private final AppDatabase db;
  // Định nghĩa phương thức ProductDetailVM với phạm vi truy cập tương ứng.
  public ProductDetailVM(@NonNull Application app) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(app);.
    super(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: db = AppDatabase.get(app);.
    db = AppDatabase.get(app);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
  // Định nghĩa phương thức productLive với phạm vi truy cập tương ứng.
  public LiveData<ProductEntity> productLive(int productId){
    // Trả về kết quả db.productDao().byId(productId);.
    return db.productDao().byId(productId);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
