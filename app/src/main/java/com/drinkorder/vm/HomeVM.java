package com.drinkorder.vm;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.repo.CatalogRepository;

public class HomeVM extends AndroidViewModel {
  private final CatalogRepository repo;
  public LiveData<java.util.List<CategoryEntity>> categories;
  public MutableLiveData<Integer> selectedCategory = new MutableLiveData<>(null);

  public LiveData<java.util.List<ProductEntity>> products;

  public HomeVM(@NonNull Application app){
    super(app);
    AppDatabase db = AppDatabase.get(app);
    repo = new CatalogRepository(db.categoryDao(), db.productDao());
    categories = repo.categories();
    products = Transformations.switchMap(selectedCategory, cid -> {
      if (cid == null) {
        return repo.allProducts(); // 🔹 Nếu không có danh mục được chọn → hiện toàn bộ sản phẩm
      } else {
        return repo.productsByCategory(cid);
      }
    });
  }
}
