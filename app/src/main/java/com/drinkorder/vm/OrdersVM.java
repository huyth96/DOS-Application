package com.drinkorder.vm;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.repo.OrderRepository;

public class OrdersVM extends AndroidViewModel {
  private final AppDatabase db;
  private final OrderRepository orderRepo;
  public LiveData<java.util.List<OrderEntity>> orders;

  public OrdersVM(@NonNull Application app){
    super(app);
    db = AppDatabase.get(app);
    orderRepo = new OrderRepository(db.orderDao(), db.paymentDao(), db.cartDao(), db.productDao());
  }
  public void load(int userId){ orders = db.orderDao().byUser(userId); }
  public void checkout(int userId, String method, OrderRepository.Callback cb){ orderRepo.checkout(userId, method, cb); }
}
