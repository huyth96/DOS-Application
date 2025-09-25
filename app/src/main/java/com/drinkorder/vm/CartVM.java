package com.drinkorder.vm;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.CartItemEntity;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.data.repo.CartRepository;

public class CartVM extends AndroidViewModel {
  private final CartRepository repo;
  public LiveData<java.util.List<CartItemEntity>> cart;
  public CartVM(@NonNull Application app){
    super(app);
    AppDatabase db = AppDatabase.get(app);
    repo = new CartRepository(db.cartDao());
    cart = repo.cart();
  }
  public void add(ProductEntity p){ repo.add(p); }
  public void setQty(int pid, int q){ repo.setQty(pid, q); }
  public void remove(int id){ repo.remove(id); }
  public void clear(){ repo.clear(); }
}
