package com.drinkorder.vm;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.repo.OrderRepository;

/**
 * ViewModel quản lý danh sách đơn hàng của một user
 * và xử lý thao tác checkout offline.
 */
public class OrdersVM extends AndroidViewModel {
  private final AppDatabase db;
  // Repository xử lý logic đặt hàng, thanh toán
  private final OrderRepository orderRepo;
  // LiveData chứa danh sách đơn hàng của user, UI sẽ observe để cập nhật
  public LiveData<java.util.List<OrderEntity>> orders;

  // Constructor khởi tạo database và repository
  public OrdersVM(@NonNull Application app){
    super(app);
    db = AppDatabase.get(app);
    orderRepo = new OrderRepository(db.orderDao(), db.paymentDao(), db.cartDao(), db.productDao());
  }

  /**
   * Load danh sách đơn hàng của user theo userId.
   * Gán LiveData từ DAO cho orders.
   * UI fragment/activity có thể observe trực tiếp.
   */
  public void load(int userId){ orders = db.orderDao().byUser(userId); }

  /**
   * Thực hiện checkout cho user.
   * @param userId id người dùng
   * @param method phương thức thanh toán (ví dụ: "Cash", "Card")
   * @param cb callback trả về kết quả thành công hoặc lỗi
   */
  public void checkout(int userId, String method, OrderRepository.Callback cb){ orderRepo.checkout(userId, method, cb); }
}
