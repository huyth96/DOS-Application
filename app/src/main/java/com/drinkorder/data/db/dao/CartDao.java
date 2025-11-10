// Khai báo package com.drinkorder.data.db.dao cho toàn bộ lớp.
package com.drinkorder.data.db.dao;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.room.Dao để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Dao;
// Import androidx.room.Insert để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Insert;
// Import androidx.room.OnConflictStrategy để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.OnConflictStrategy;
// Import androidx.room.Query để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Query;
// Import androidx.room.Transaction để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.Transaction;

// Import com.drinkorder.data.db.entity.CartItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CartItemEntity;
// Import com.drinkorder.data.db.pojo.CartItemWithProduct để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.CartItemWithProduct;
// Định nghĩa interface CartDao.
@Dao public interface CartDao {
  // Áp dụng annotation @Query("SELECT và ghi đè phương thức all.
  @Query("SELECT * FROM cart_items") LiveData<java.util.List<CartItemEntity>> all();
  // Áp dụng annotation @Query("SELECT và ghi đè phương thức allNow.
  @Query("SELECT * FROM cart_items") java.util.List<CartItemEntity> allNow();
  // Áp dụng annotation @Insert(onConflict=OnConflictStrategy.REPLACE) và ghi đè phương thức upsert.
  @Insert(onConflict=OnConflictStrategy.REPLACE) void upsert(CartItemEntity e);
  // Áp dụng annotation @Query("DELETE và ghi đè phương thức remove.
  @Query("DELETE FROM cart_items WHERE cartItemId=:id") void remove(int id);
  // Áp dụng annotation @Query("DELETE và ghi đè phương thức clear.
  @Query("DELETE FROM cart_items") void clear();
  // Áp dụng annotation @Query("UPDATE và ghi đè phương thức setQty.
  @Query("UPDATE cart_items SET quantity=:q WHERE productId=:pid") void setQty(int pid, int q);
  // Áp dụng annotation @Transaction cho phần tử bên dưới.
  @Transaction
  // Áp dụng annotation @Query("SELECT cho phần tử bên dưới.
  @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
  // Thực hiện lời gọi phương thức hoặc khởi tạo: LiveData<java.util.List<CartItemWithProduct>> allWithProducts();.
  LiveData<java.util.List<CartItemWithProduct>> allWithProducts();
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
