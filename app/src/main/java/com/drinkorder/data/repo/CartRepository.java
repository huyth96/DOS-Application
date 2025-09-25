package com.drinkorder.data.repo;
import androidx.lifecycle.LiveData;
import com.drinkorder.data.db.dao.CartDao;
import com.drinkorder.data.db.entity.CartItemEntity;
import com.drinkorder.data.db.entity.ProductEntity;
import java.util.List;
import java.util.concurrent.Executors;

public class CartRepository {
  private final CartDao cartDao;
  public CartRepository(CartDao cartDao){ this.cartDao=cartDao; }
  public LiveData<java.util.List<CartItemEntity>> cart(){ return cartDao.all(); }
  public void add(ProductEntity p){
    Executors.newSingleThreadExecutor().execute(() -> {
      java.util.List<CartItemEntity> now = cartDao.allNow();
      CartItemEntity ex = null;
      for (CartItemEntity e: now){ if (e.productId==p.productId){ ex=e; break; } }
      if (ex==null){ CartItemEntity e=new CartItemEntity(); e.productId=p.productId; e.quantity=1; e.addedAt=System.currentTimeMillis(); cartDao.upsert(e); }
      else { cartDao.setQty(p.productId, ex.quantity+1); }
    });
  }
  public void setQty(int pid, int q){ Executors.newSingleThreadExecutor().execute(() -> cartDao.setQty(pid,q)); }
  public void remove(int id){ Executors.newSingleThreadExecutor().execute(() -> cartDao.remove(id)); }
  public void clear(){ Executors.newSingleThreadExecutor().execute(cartDao::clear); }
}
