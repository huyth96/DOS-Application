// Khai báo package com.drinkorder.ui.cart cho toàn bộ lớp.
package com.drinkorder.ui.cart;

// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
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
// Import android.widget.Toast để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Toast;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.fragment.app.Fragment để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.Fragment;
// Import androidx.lifecycle.ViewModelProvider để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.ViewModelProvider;
// Import androidx.recyclerview.widget.LinearLayoutManager để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.CartItemEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CartItemEntity;
// Import com.drinkorder.data.db.pojo.CartItemWithProduct để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.CartItemWithProduct;
// Import com.drinkorder.vm.CartVM để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.vm.CartVM;
// Import com.drinkorder.vm.OrdersVM để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.vm.OrdersVM;

// Import java.text.NumberFormat để sử dụng các lớp hoặc hàm tương ứng.
import java.text.NumberFormat;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp CartFragment kế thừa Fragment.
public class CartFragment extends Fragment {
  // Khai báo thuộc tính với phạm vi truy cập: private CartVM vm.
  private CartVM vm;
  // Khai báo thuộc tính với phạm vi truy cập: private OrdersVM ordersVM.
  private OrdersVM ordersVM;
  // Khai báo thuộc tính với phạm vi truy cập: private CartAdapter adapter.
  private CartAdapter adapter;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvTotal.
  private TextView tvTotal;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvGrandTotal.
  private TextView tvGrandTotal;
  // Khai báo thuộc tính với phạm vi truy cập: private NumberFormat priceFormat.
  private NumberFormat priceFormat;

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreate với phạm vi truy cập tương ứng.
  public void onCreate(@Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate(savedInstanceState);.
    super.onCreate(savedInstanceState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));.
    priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: priceFormat.setMaximumFractionDigits(0);.
    priceFormat.setMaximumFractionDigits(0);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Nullable cho phần tử bên dưới.
  @Nullable
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
  public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View v = inf.inflate(R.layout.fragment_cart, c, false);.
    View v = inf.inflate(R.layout.fragment_cart, c, false);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: RecyclerView rv = v.findViewById(R.id.rvCart);.
    RecyclerView rv = v.findViewById(R.id.rvCart);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rv.setLayoutManager(new LinearLayoutManager(getContext()));.
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    adapter = new CartAdapter(new CartAdapter.Callback(){
      // Áp dụng annotation @Override và ghi đè phương thức onPlus.
      @Override public void onPlus(CartItemEntity e){ vm.setQty(e.productId, e.quantity+1); }
      // Áp dụng annotation @Override và ghi đè phương thức onMinus.
      @Override public void onMinus(CartItemEntity e){ vm.setQty(e.productId, Math.max(1, e.quantity-1)); }
      // Áp dụng annotation @Override và ghi đè phương thức onRemove.
      @Override public void onRemove(CartItemEntity e){ vm.remove(e.cartItemId); }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rv.setAdapter(adapter);.
    rv.setAdapter(adapter);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTotal = v.findViewById(R.id.tvTotal);.
    tvTotal = v.findViewById(R.id.tvTotal);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvGrandTotal = v.findViewById(R.id.tvGrandTotal);.
    tvGrandTotal = v.findViewById(R.id.tvGrandTotal);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Button btnCheckout = v.findViewById(R.id.btnCheckout);.
    Button btnCheckout = v.findViewById(R.id.btnCheckout);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    btnCheckout.setOnClickListener(vv -> ordersVM.checkout(1, "Cash", new com.drinkorder.data.repo.OrderRepository.Callback(){
      // Áp dụng annotation @Override và ghi đè phương thức onSuccess.
      @Override public void onSuccess(long id){ Toast.makeText(getContext(),"Order "+id+" created",Toast.LENGTH_SHORT).show(); }
      // Áp dụng annotation @Override và ghi đè phương thức onError.
      @Override public void onError(Throwable t){ Toast.makeText(getContext(),"Checkout failed: "+t.getMessage(),Toast.LENGTH_SHORT).show(); }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }));.
    }));
    // Trả về kết quả v;.
    return v;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
  public void onViewCreated(@NonNull View v, @Nullable Bundle b){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(v,b);.
    super.onViewCreated(v,b);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(requireActivity()).get(CartVM.class);.
    vm = new ViewModelProvider(requireActivity()).get(CartVM.class);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ordersVM = new ViewModelProvider(requireActivity()).get(OrdersVM.class);.
    ordersVM = new ViewModelProvider(requireActivity()).get(OrdersVM.class);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.cart.observe(getViewLifecycleOwner(), list -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submit(list);.
      adapter.submit(list);
      // Gán giá trị cho biến hoặc thuộc tính: long totalItems = 0.
      long totalItems = 0;
      // Gán giá trị cho biến hoặc thuộc tính: double totalPrice = 0d.
      double totalPrice = 0d;
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (list != null) {
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (CartItemWithProduct row : list) {
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (row == null || row.item == null) continue;
          // Gán giá trị cho biến hoặc thuộc tính: int qty = row.item.quantity.
          int qty = row.item.quantity;
          // Gán giá trị cho biến hoặc thuộc tính: totalItems += qty.
          totalItems += qty;
          // Gán giá trị cho biến hoặc thuộc tính: double price = row.product != null ? row.product.price : 0d.
          double price = row.product != null ? row.product.price : 0d;
          // Gán giá trị cho biến hoặc thuộc tính: totalPrice += price * qty.
          totalPrice += price * qty;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTotal.setText("Items: " + totalItems);.
      tvTotal.setText("Items: " + totalItems);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (tvGrandTotal != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvGrandTotal.setText(formatPrice(totalPrice));.
        tvGrandTotal.setText(formatPrice(totalPrice));
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức formatPrice với phạm vi truy cập tương ứng.
  private String formatPrice(double amount){
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (priceFormat == null) return "0 VND";
    // Trả về kết quả priceFormat.format(Math.round(amount)) + " VND";.
    return priceFormat.format(Math.round(amount)) + " VND";
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
