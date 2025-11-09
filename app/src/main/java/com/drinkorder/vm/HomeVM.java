package com.drinkorder.vm;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.repo.CatalogRepository;
import java.util.List;
public class HomeVM extends AndroidViewModel {
  private final CatalogRepository repo;
  public LiveData<java.util.List<CategoryEntity>> categories;
  public MutableLiveData<Integer> selectedCategory = new MutableLiveData<>(1);
  public LiveData<java.util.List<ProductEntity>> products;

  // Thêm cho tìm kiếm
  public MutableLiveData<String> searchQuery = new MutableLiveData<>("");
  public LiveData<List<ProductEntity>> searchResults;

  public HomeVM(@NonNull Application app){
    super(app);
    AppDatabase db = AppDatabase.get(app);
    repo = new CatalogRepository(db.categoryDao(), db.productDao());
    categories = repo.categories();
    products = Transformations.switchMap(selectedCategory, cid -> repo.productsByCategory(cid==null?1:cid));
    // SwitchMap cho tìm kiếm: Nếu query không rỗng, tìm theo tên; nếu rỗng, dùng list theo category
    searchResults = Transformations.switchMap(searchQuery, query -> {
      if (query == null || query.isEmpty()) {
        return products;  // Quay về list theo category
      } else {
        return db.productDao().searchByName(query);  // Tìm theo tên
      }
    });
  }
}
