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
// Import com.drinkorder.data.db.pojo.CartItemWithProduct để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.CartItemWithProduct;
// Import com.drinkorder.data.repo.CartRepository để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.repo.CartRepository;

// Định nghĩa lớp CartVM kế thừa AndroidViewModel.
public class CartVM extends AndroidViewModel {
  // Khai báo thuộc tính với phạm vi truy cập: private final CartRepository repo.
  private final CartRepository repo;
  // Khai báo thuộc tính với phạm vi truy cập: public LiveData<java.util.List<CartItemWithProduct>> cart.
  public LiveData<java.util.List<CartItemWithProduct>> cart;
  // Định nghĩa phương thức CartVM với phạm vi truy cập tương ứng.
  public CartVM(@NonNull Application app){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(app);.
    super(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: AppDatabase db = AppDatabase.get(app);.
    AppDatabase db = AppDatabase.get(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: repo = new CartRepository(db.cartDao());.
    repo = new CartRepository(db.cartDao());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: cart = repo.cart();.
    cart = repo.cart();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
  // Định nghĩa phương thức add với phạm vi truy cập tương ứng.
  public void add(ProductEntity p){ repo.add(p); }
  // Định nghĩa phương thức setQty với phạm vi truy cập tương ứng.
  public void setQty(int pid, int q){ repo.setQty(pid, q); }
  // Định nghĩa phương thức remove với phạm vi truy cập tương ứng.
  public void remove(int id){ repo.remove(id); }
  // Định nghĩa phương thức clear với phạm vi truy cập tương ứng.
  public void clear(){ repo.clear(); }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
