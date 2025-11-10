// Khai báo package com.drinkorder.data.repo cho toàn bộ lớp.
package com.drinkorder.data.repo;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
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

// Định nghĩa lớp CatalogRepository.
public class CatalogRepository {
  // Khai báo thuộc tính với phạm vi truy cập: private final CategoryDao c; private final ProductDao p.
  private final CategoryDao c; private final ProductDao p;
  // Định nghĩa phương thức CatalogRepository với phạm vi truy cập tương ứng.
  public CatalogRepository(CategoryDao c, ProductDao p){ this.c=c; this.p=p; }
  // Định nghĩa phương thức categories với phạm vi truy cập tương ứng.
  public LiveData<java.util.List<CategoryEntity>> categories(){ return c.all(); }
  // Định nghĩa phương thức productsByCategory với phạm vi truy cập tương ứng.
  public LiveData<java.util.List<ProductEntity>> productsByCategory(int cid){ return p.byCategory(cid); }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
