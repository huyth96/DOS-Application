package com.drinkorder.data.repo;
import androidx.lifecycle.LiveData;
import com.drinkorder.data.db.dao.CategoryDao;
import com.drinkorder.data.db.dao.ProductDao;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.db.entity.ProductEntity;
import java.util.List;

public class CatalogRepository {
  private final CategoryDao c; private final ProductDao p;
  public CatalogRepository(CategoryDao c, ProductDao p){ this.c=c; this.p=p; }
  public LiveData<java.util.List<CategoryEntity>> categories(){ return c.all(); }
  public LiveData<java.util.List<ProductEntity>> productsByCategory(int cid){ return p.byCategory(cid); }
}
