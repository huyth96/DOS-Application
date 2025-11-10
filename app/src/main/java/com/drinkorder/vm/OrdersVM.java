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
// Import com.drinkorder.data.db.entity.OrderEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderEntity;
// Import com.drinkorder.data.repo.OrderRepository để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.repo.OrderRepository;

// Định nghĩa lớp OrdersVM kế thừa AndroidViewModel.
public class OrdersVM extends AndroidViewModel {
  // Khai báo thuộc tính với phạm vi truy cập: private final AppDatabase db.
  private final AppDatabase db;
  // Khai báo thuộc tính với phạm vi truy cập: private final OrderRepository orderRepo.
  private final OrderRepository orderRepo;
  // Khai báo thuộc tính với phạm vi truy cập: public LiveData<java.util.List<OrderEntity>> orders.
  public LiveData<java.util.List<OrderEntity>> orders;

  // Định nghĩa phương thức OrdersVM với phạm vi truy cập tương ứng.
  public OrdersVM(@NonNull Application app){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(app);.
    super(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: db = AppDatabase.get(app);.
    db = AppDatabase.get(app);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: orderRepo = new OrderRepository(db.orderDao(), db.paymentDao(), db.cartDao(), db.productDao());.
    orderRepo = new OrderRepository(db.orderDao(), db.paymentDao(), db.cartDao(), db.productDao());
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
  // Định nghĩa phương thức load với phạm vi truy cập tương ứng.
  public void load(int userId){ orders = db.orderDao().byUser(userId); }
  // Định nghĩa phương thức checkout với phạm vi truy cập tương ứng.
  public void checkout(int userId, String method, OrderRepository.Callback cb){ orderRepo.checkout(userId, method, cb); }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
