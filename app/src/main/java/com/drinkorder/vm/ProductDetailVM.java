package com.drinkorder.vm;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.ProductEntity;

public class ProductDetailVM extends AndroidViewModel { // Định nghĩa lớp ProductDetailVM kế thừa từ AndroidViewModel, dùng để quản lý dữ liệu chi tiết sản phẩm.

  private final AppDatabase db; // Biến final lưu instance cơ sở dữ liệu AppDatabase.

  public ProductDetailVM(@NonNull Application app) { // Constructor của ViewModel, nhận Application không null.
    super(app); // Gọi constructor của lớp cha AndroidViewModel.
    db = AppDatabase.get(app); // Lấy và gán instance cơ sở dữ liệu từ Application.
  }

  public LiveData<ProductEntity> productLive(int productId){ // Phương thức trả về LiveData sản phẩm dựa trên ID.
    return db.productDao().byId(productId); // Trả về LiveData từ ProductDao để lấy sản phẩm theo ID.
  }
}
