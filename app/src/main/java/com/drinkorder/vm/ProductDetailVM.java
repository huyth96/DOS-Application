package com.drinkorder.vm;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.ProductEntity;

public class ProductDetailVM extends AndroidViewModel {
  private final AppDatabase db;
  public ProductDetailVM(@NonNull Application app) {
    super(app);
    db = AppDatabase.get(app);
  }
  public LiveData<ProductEntity> productLive(int productId){
    return db.productDao().byId(productId);
  }
}
