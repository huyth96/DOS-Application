// Khai báo package com.drinkorder.ui.cart cho toàn bộ lớp.
package com.drinkorder.ui.cart;

// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;
// Import android.widget.Button để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Button;
// Import android.widget.TextView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.TextView;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.CartItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CartItemEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.drinkorder.data.db.pojo.CartItemWithProduct để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.CartItemWithProduct;

// Import java.text.NumberFormat để sử dụng các lớp hoặc hàm tương ứng.
import java.text.NumberFormat;
// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp CartAdapter kế thừa RecyclerView.Adapter<CartAdapter.VH>.
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
  // Định nghĩa interface Callback.
  public interface Callback { void onPlus(CartItemEntity e); void onMinus(CartItemEntity e); void onRemove(CartItemEntity e); }

  // Khai báo thuộc tính với phạm vi truy cập: private final Callback cb.
  private final Callback cb;
  // Khai báo thuộc tính với phạm vi truy cập: private final List<CartItemWithProduct> data = new ArrayList<>().
  private final List<CartItemWithProduct> data = new ArrayList<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN")).
  private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

  // Định nghĩa phương thức CartAdapter với phạm vi truy cập tương ứng.
  public CartAdapter(Callback cb){
    // Gán giá trị cho biến hoặc thuộc tính: this.cb = cb.
    this.cb = cb;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: priceFormat.setMaximumFractionDigits(0);.
    priceFormat.setMaximumFractionDigits(0);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức submit với phạm vi truy cập tương ứng.
  public void submit(List<CartItemWithProduct> list){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: data.clear();.
    data.clear();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (list != null) data.addAll(list);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
    notifyDataSetChanged();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @NonNull và @Override và ghi đè phương thức onCreateViewHolder.
  @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vt){
    // Trả về kết quả new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_cart, p, false));.
    return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_cart, p, false));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override và ghi đè phương thức onBindViewHolder.
  @Override public void onBindViewHolder(@NonNull VH h, int position){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: CartItemWithProduct row = data.get(position);.
    CartItemWithProduct row = data.get(position);
    // Gán giá trị cho biến hoặc thuộc tính: CartItemEntity item = row.item.
    CartItemEntity item = row.item;
    // Gán giá trị cho biến hoặc thuộc tính: ProductEntity product = row.product.
    ProductEntity product = row.product;

    // Thực hiện lời gọi phương thức hoặc khởi tạo: String title = (product != null && product.name != null && !product.name.isEmpty()).
    String title = (product != null && product.name != null && !product.name.isEmpty())
        // Thực thi câu lệnh: ? product.name.
        ? product.name
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : "Product #" + (item != null ? item.productId : "");.
        : "Product #" + (item != null ? item.productId : "");
    // Gán giá trị cho biến hoặc thuộc tính: double unitPrice = product != null ? product.price : 0d.
    double unitPrice = product != null ? product.price : 0d;
    // Gán giá trị cho biến hoặc thuộc tính: int qty = item != null ? item.quantity : 0.
    int qty = item != null ? item.quantity : 0;

    // Thực hiện lời gọi phương thức hoặc khởi tạo: h.title.setText(title);.
    h.title.setText(title);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: h.price.setText(formatPrice(unitPrice));.
    h.price.setText(formatPrice(unitPrice));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: h.qty.setText(String.valueOf(qty));.
    h.qty.setText(String.valueOf(qty));

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    h.btnPlus.setOnClickListener(v -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (item != null) cb.onPlus(item);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    h.btnMinus.setOnClickListener(v -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (item != null) cb.onMinus(item);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    h.btnRemove.setOnClickListener(v -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (item != null) cb.onRemove(item);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override và ghi đè phương thức getItemCount.
  @Override public int getItemCount(){ return data.size(); }

  // Định nghĩa phương thức formatPrice với phạm vi truy cập tương ứng.
  private String formatPrice(double price){
    // Trả về kết quả priceFormat.format(Math.round(price)) + " VND";.
    return priceFormat.format(Math.round(price)) + " VND";
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa lớp VH kế thừa RecyclerView.ViewHolder.
  static class VH extends RecyclerView.ViewHolder{
    // Thực thi câu lệnh: final TextView title;.
    final TextView title;
    // Thực thi câu lệnh: final TextView qty;.
    final TextView qty;
    // Thực thi câu lệnh: final TextView price;.
    final TextView price;
    // Thực thi câu lệnh: final Button btnPlus;.
    final Button btnPlus;
    // Thực thi câu lệnh: final Button btnMinus;.
    final Button btnMinus;
    // Thực thi câu lệnh: final Button btnRemove;.
    final Button btnRemove;
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    VH(View v){
      // Thực hiện lời gọi phương thức hoặc khởi tạo: super(v);.
      super(v);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: title = v.findViewById(R.id.title);.
      title = v.findViewById(R.id.title);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: qty = v.findViewById(R.id.qty);.
      qty = v.findViewById(R.id.qty);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: price = v.findViewById(R.id.tvPrice);.
      price = v.findViewById(R.id.tvPrice);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnPlus = v.findViewById(R.id.btnPlus);.
      btnPlus = v.findViewById(R.id.btnPlus);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnMinus = v.findViewById(R.id.btnMinus);.
      btnMinus = v.findViewById(R.id.btnMinus);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnRemove = v.findViewById(R.id.btnRemove);.
      btnRemove = v.findViewById(R.id.btnRemove);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
