// Khai báo package com.drinkorder.data.repo cho toàn bộ lớp.
package com.drinkorder.data.repo;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;

// Import com.drinkorder.data.db.dao.CartDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.CartDao;
// Import com.drinkorder.data.db.entity.CartItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CartItemEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.drinkorder.data.db.pojo.CartItemWithProduct để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.CartItemWithProduct;

// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.concurrent.Executors để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executors;

// Định nghĩa lớp CartRepository.
public class CartRepository {
  // Khai báo thuộc tính với phạm vi truy cập: private final CartDao cartDao.
  private final CartDao cartDao;
  // Định nghĩa phương thức CartRepository với phạm vi truy cập tương ứng.
  public CartRepository(CartDao cartDao){ this.cartDao=cartDao; }
  // Định nghĩa phương thức cart với phạm vi truy cập tương ứng.
  public LiveData<java.util.List<CartItemWithProduct>> cart(){ return cartDao.allWithProducts(); }
  // Định nghĩa phương thức add với phạm vi truy cập tương ứng.
  public void add(ProductEntity p){
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    Executors.newSingleThreadExecutor().execute(() -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: java.util.List<CartItemEntity> now = cartDao.allNow();.
      java.util.List<CartItemEntity> now = cartDao.allNow();
      // Gán giá trị cho biến hoặc thuộc tính: CartItemEntity ex = null.
      CartItemEntity ex = null;
      // Bắt đầu vòng lặp for để duyệt dữ liệu.
      for (CartItemEntity e: now){ if (e.productId==p.productId){ ex=e; break; } }
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (ex==null){ CartItemEntity e=new CartItemEntity(); e.productId=p.productId; e.quantity=1; e.addedAt=System.currentTimeMillis(); cartDao.upsert(e); }
      // Kết thúc khối lệnh vừa mở phía trên.
      else { cartDao.setQty(p.productId, ex.quantity+1); }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
  // Định nghĩa phương thức setQty với phạm vi truy cập tương ứng.
  public void setQty(int pid, int q){ Executors.newSingleThreadExecutor().execute(() -> cartDao.setQty(pid,q)); }
  // Định nghĩa phương thức remove với phạm vi truy cập tương ứng.
  public void remove(int id){ Executors.newSingleThreadExecutor().execute(() -> cartDao.remove(id)); }
  // Định nghĩa phương thức clear với phạm vi truy cập tương ứng.
  public void clear(){ Executors.newSingleThreadExecutor().execute(cartDao::clear); }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
