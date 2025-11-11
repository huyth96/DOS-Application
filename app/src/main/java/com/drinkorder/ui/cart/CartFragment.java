// Đặt package để fragment thuộc nhóm chức năng giỏ hàng.
package com.drinkorder.ui.cart;

// Import Bundle phục vụ vòng đời fragment.
import android.os.Bundle;
// Import LayoutInflater để tạo view từ XML.
import android.view.LayoutInflater;
// Import View để thao tác với giao diện.
import android.view.View;
// Import ViewGroup làm container cho fragment.
import android.view.ViewGroup;
// Import Button để xử lý hành động thanh toán.
import android.widget.Button;
// Import TextView để hiển thị tổng số lượng và tổng tiền.
import android.widget.ImageButton;
import android.widget.TextView;
// Import Toast để thông báo kết quả thanh toán cho người dùng.
import android.widget.Toast;

// Import NonNull chú thích tham số bắt buộc.
import androidx.annotation.NonNull;
// Import Nullable chú thích tham số có thể rỗng.
import androidx.annotation.Nullable;
// Import Fragment làm lớp cơ sở cho màn giỏ hàng.
import androidx.fragment.app.Fragment;
// Import ViewModelProvider để lấy ViewModel chia sẻ với Activity.
import androidx.lifecycle.ViewModelProvider;
// Import LinearLayoutManager để sắp xếp danh sách sản phẩm trong giỏ.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import RecyclerView để hiển thị danh sách giỏ hàng.
import androidx.recyclerview.widget.RecyclerView;

// Import R để truy cập layout và id view của giỏ hàng.
import com.drinkorder.R;
// Import CartItemEntity để thao tác với bản ghi giỏ hàng.
import com.drinkorder.data.db.entity.CartItemEntity;
// Import CartItemWithProduct để có thêm thông tin sản phẩm kèm item.
import com.drinkorder.data.db.pojo.CartItemWithProduct;
// Import CartVM để quản lý dữ liệu giỏ hàng.
import com.drinkorder.vm.CartVM;
// Import OrdersVM để thực hiện thao tác checkout.
import com.drinkorder.vm.OrdersVM;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Import NumberFormat để định dạng giá tiền theo locale.
import java.text.NumberFormat;
// Import Locale để định nghĩa định dạng Việt Nam.
import java.util.Locale;

// Fragment hiển thị và cho phép thao tác trên giỏ hàng của khách hàng.
public class CartFragment extends Fragment {
  // ViewModel quản lý danh sách giỏ hàng.
  private CartVM vm;
  // ViewModel xử lý việc tạo đơn hàng từ giỏ.
  private OrdersVM ordersVM;
  // Adapter hiển thị danh sách các item trong giỏ.
  private CartAdapter adapter;
  // TextView hiển thị tổng số lượng sản phẩm.
  private TextView tvTotal;
  // TextView hiển thị tổng số tiền phải thanh toán.
  private TextView tvGrandTotal;
  // Định dạng giá tiền theo chuẩn Việt Nam.
  private NumberFormat priceFormat;

  // Khởi tạo các thành phần không phụ thuộc view.
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Tạo NumberFormat với locale Việt Nam để định dạng tiền tệ quen thuộc.
    priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    // Bỏ phần thập phân để phù hợp hiển thị VND.
    priceFormat.setMaximumFractionDigits(0);
  }

  // Inflate layout và cấu hình RecyclerView cùng các nút hành động.
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b){
    // Inflate layout fragment_cart chứa danh sách và nút thanh toán.
    View v = inf.inflate(R.layout.fragment_cart, c, false);
    // Ánh xạ RecyclerView hiển thị danh sách giỏ hàng.
    RecyclerView rv = v.findViewById(R.id.rvCart);
    // Sử dụng LinearLayoutManager dọc để sản phẩm dễ đọc.
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    // Khởi tạo adapter với callback xử lý tăng/giảm/xóa sản phẩm.
    adapter = new CartAdapter(new CartAdapter.Callback(){
      // Khi người dùng tăng số lượng sản phẩm.
      @Override public void onPlus(CartItemEntity e){ vm.setQty(e.productId, e.quantity+1); }
      // Khi người dùng giảm số lượng nhưng không thấp hơn 1.
      @Override public void onMinus(CartItemEntity e){ vm.setQty(e.productId, Math.max(1, e.quantity-1)); }
      // Khi người dùng muốn xóa sản phẩm khỏi giỏ.
      @Override public void onRemove(CartItemEntity e){ vm.remove(e.cartItemId); }
    });
    // Gắn adapter vào RecyclerView.
    rv.setAdapter(adapter);
    // Ánh xạ TextView hiển thị tổng số lượng.
    tvTotal = v.findViewById(R.id.tvTotal);
    // Ánh xạ TextView hiển thị tổng tiền.
    tvGrandTotal = v.findViewById(R.id.tvGrandTotal);
    // Ánh xạ nút back
    ImageButton btnBack = v.findViewById(R.id.btnBack);
    btnBack.setOnClickListener(view -> {
      BottomNavigationView nav = requireActivity().findViewById(R.id.bottomNav);
      if (nav != null) nav.setSelectedItemId(R.id.tab_home);
    });
    // Ánh xạ nút thanh toán để thực hiện checkout.
    Button btnCheckout = v.findViewById(R.id.btnCheckout);
    // Khi bấm thanh toán sẽ gọi OrdersVM tạo đơn hàng với phương thức mặc định.
    btnCheckout.setOnClickListener(vv -> ordersVM.checkout(1, "Cash", new com.drinkorder.data.repo.OrderRepository.Callback(){
      // Thông báo thành công khi đơn hàng được tạo.
      @Override public void onSuccess(long id){ Toast.makeText(getContext(),"Order "+id+" created",Toast.LENGTH_SHORT).show(); }
      // Thông báo lỗi khi quá trình checkout thất bại.
      @Override public void onError(Throwable t){ Toast.makeText(getContext(),"Checkout failed: "+t.getMessage(),Toast.LENGTH_SHORT).show(); }
    }));
    // Trả về view đã cấu hình.
    return v;
  }

  // Thiết lập ViewModel và observer sau khi view được tạo.
  @Override
  public void onViewCreated(@NonNull View v, @Nullable Bundle b){
    super.onViewCreated(v,b);
    // Lấy CartVM dùng chung với Activity để giữ dữ liệu khi đổi tab.
    vm = new ViewModelProvider(requireActivity()).get(CartVM.class);
    // Lấy OrdersVM để thực hiện thanh toán.
    ordersVM = new ViewModelProvider(requireActivity()).get(OrdersVM.class);
    // Quan sát LiveData giỏ hàng để cập nhật UI theo thời gian thực.
    vm.cart.observe(getViewLifecycleOwner(), list -> {
      // Đưa danh sách mới vào adapter để hiển thị.
      adapter.submit(list);
      // Biến đếm tổng số sản phẩm trong giỏ.
      long totalItems = 0;
      // Biến cộng dồn tổng tiền.
      double totalPrice = 0d;
      // Duyệt danh sách để tính số lượng và tổng tiền.
      if (list != null) {
        for (CartItemWithProduct row : list) {
          if (row == null || row.item == null) continue;
          int qty = row.item.quantity;
          totalItems += qty;
          double price = row.product != null ? row.product.price : 0d;
          totalPrice += price * qty;
        }
      }
      // Cập nhật tổng số lượng lên TextView.
      tvTotal.setText("Items: " + totalItems);
      // Hiển thị tổng tiền đã định dạng nếu view tồn tại.
      if (tvGrandTotal != null) {
        tvGrandTotal.setText(formatPrice(totalPrice));
      }
    });
  }

  // Định dạng giá tiền thành chuỗi có đơn vị VND.
  private String formatPrice(double amount){
    if (priceFormat == null) return "0 VND";
    return priceFormat.format(Math.round(amount)) + " VND";
  }
}
