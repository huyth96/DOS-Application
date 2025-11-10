// Khai báo package com.drinkorder.vm cho toàn bộ lớp.
package com.drinkorder.vm;
// Import android.app.Application để sử dụng các lớp hoặc hàm tương ứng.
import android.app.Application;
// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.lifecycle.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.*;
// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import com.drinkorder.data.repo.CatalogRepository để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.repo.CatalogRepository;

// Định nghĩa lớp HomeVM kế thừa AndroidViewModel.
public class HomeVM extends AndroidViewModel {
  // Khai báo thuộc tính với phạm vi truy cập: private final CatalogRepository repo.
  private final CatalogRepository repo;
  // Khai báo thuộc tính với phạm vi truy cập: public LiveData<java.util.List<CategoryEntity>> categories.
  public LiveData<java.util.List<CategoryEntity>> categories;
  // Khai báo thuộc tính với phạm vi truy cập: public MutableLiveData<Integer> selectedCategory = new MutableLiveData<>(1).
  public MutableLiveData<Integer> selectedCategory = new MutableLiveData<>(1);
  // Khai báo thuộc tính với phạm vi truy cập: public LiveData<java.util.List<ProductEntity>> products.
  public LiveData<java.util.List<ProductEntity>> products;

  // Định nghĩa phương thức HomeVM với phạm vi truy cập tương ứng.
  public HomeVM(@NonNull Application app){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(app);.
    super(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: AppDatabase db = AppDatabase.get(app);.
    AppDatabase db = AppDatabase.get(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: repo = new CatalogRepository(db.categoryDao(), db.productDao());.
    repo = new CatalogRepository(db.categoryDao(), db.productDao());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categories = repo.categories();.
    categories = repo.categories();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: products = Transformations.switchMap(selectedCategory, cid -> repo.productsByCategory(cid==null?1:cid));.
    products = Transformations.switchMap(selectedCategory, cid -> repo.productsByCategory(cid==null?1:cid));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
