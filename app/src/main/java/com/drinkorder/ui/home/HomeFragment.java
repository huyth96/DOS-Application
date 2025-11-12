// Đặt package để fragment thuộc nhóm giao diện trang chủ khách hàng.
package com.drinkorder.ui.home;

// Import Context để truy cập SharedPreferences phục vụ lời chào.
import android.content.Context;
// Import Intent để điều hướng sang MapActivity.
import android.content.Intent;
// Import SharedPreferences để lấy thông tin người dùng đăng nhập.
import android.content.SharedPreferences;
// Import Bundle cho vòng đời fragment.
import android.os.Bundle;
// Import TextUtils để kiểm tra chuỗi rỗng.
import android.text.TextUtils;
// Import KeyEvent để bắt phím Enter trong ô tìm kiếm.
import android.view.KeyEvent;
// Import LayoutInflater để tạo view từ XML.
import android.view.LayoutInflater;
// Import View để thao tác giao diện.
import android.view.View;
// Import ViewGroup làm container cho fragment.
import android.view.ViewGroup;
// Import EditorInfo để nhận sự kiện IME search.
import android.view.inputmethod.EditorInfo;
// Import TextView để hiển thị lời chào và badge giỏ hàng.
import android.widget.TextView;
// Import Toast để thông báo các chức năng chưa sẵn sàng.
import android.widget.Toast;

// Import NonNull chú thích tham số không được null.
import androidx.annotation.NonNull;
// Import Nullable chú thích tham số có thể null.
import androidx.annotation.Nullable;
// Import Fragment làm lớp cơ sở cho màn hình trang chủ.
import androidx.fragment.app.Fragment;
// Import ViewModelProvider để lấy ViewModel sản phẩm và giỏ hàng.
import androidx.lifecycle.ViewModelProvider;
// Import LinearLayoutManager để hiển thị danh sách sản phẩm theo chiều dọc.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import RecyclerView để hiển thị danh sách sản phẩm.
import androidx.recyclerview.widget.RecyclerView;

// Import R để truy cập layout và tài nguyên giao diện.
import com.drinkorder.R;
// Import CategoryEntity để hiển thị chip bộ lọc danh mục.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import ProductEntity để mở chi tiết sản phẩm.
import com.drinkorder.data.db.entity.ProductEntity;
// Import CartItemWithProduct để thống kê giỏ hàng.
import com.drinkorder.data.db.pojo.CartItemWithProduct;
// Import ProductDetailFragment để điều hướng tới chi tiết sản phẩm.
import com.drinkorder.ui.detail.ProductDetailFragment;
// Import MapActivity để mở bản đồ cửa hàng.
import com.drinkorder.ui.map.MapActivity;
// Import CartVM để thao tác giỏ hàng.
import com.drinkorder.vm.CartVM;
// Import HomeVM để lấy dữ liệu sản phẩm và danh mục.
import com.drinkorder.vm.HomeVM;
// Import BottomNavigationView để chuyển tab khi người dùng nhấn shortcut.
import com.google.android.material.bottomnavigation.BottomNavigationView;
// Import MaterialCardView cho các shortcut hiển thị nhanh.
import com.google.android.material.card.MaterialCardView;
// Import Chip để hiển thị từng danh mục.
import com.google.android.material.chip.Chip;
// Import ChipGroup để gom các chip danh mục.
import com.google.android.material.chip.ChipGroup;
// Import TextInputEditText cho ô tìm kiếm sản phẩm.
import com.google.android.material.textfield.TextInputEditText;

// Import Calendar để xác định thời điểm trong ngày cho lời chào.
import java.util.Calendar;
// Import List để thao tác danh sách sản phẩm, danh mục, giỏ hàng.
import java.util.List;
// Import Locale để định dạng chuỗi theo ngôn ngữ thiết bị.
import java.util.Locale;

// Fragment trang chủ hiển thị sản phẩm, danh mục và shortcut hỗ trợ khách hàng.
public class HomeFragment extends Fragment { // Định nghĩa lớp HomeFragment kế thừa từ Fragment, dùng để hiển thị trang chủ với sản phẩm và shortcut.

  // ViewModel cung cấp dữ liệu sản phẩm và danh mục.
  private HomeVM vm; // Biến lưu ViewModel HomeVM để lấy dữ liệu sản phẩm và danh mục.
  // ViewModel quản lý giỏ hàng để đồng bộ badge và thao tác thêm sản phẩm.
  private CartVM cartVM; // Biến lưu ViewModel CartVM để quản lý giỏ hàng.
  // Adapter hiển thị danh sách sản phẩm.
  private ProductsAdapter adapter; // Biến lưu adapter để hiển thị danh sách sản phẩm trong RecyclerView.

  // ChipGroup chứa danh sách danh mục.
  private ChipGroup chipCategories; // Biến lưu ChipGroup để chứa các chip danh mục.
  // TextView hiển thị số lượng sản phẩm trong giỏ hàng.
  private TextView tvCartBadge; // Biến lưu TextView để hiển thị badge số lượng trong giỏ hàng.
  // TextView dòng đầu của lời chào.
  private TextView tvGreetingLine; // Biến lưu TextView cho dòng đầu lời chào (như "Good morning,").
  // TextView dòng nhấn mạnh của lời chào.
  private TextView tvGreetingHighlight; // Biến lưu TextView cho dòng nhấn mạnh lời chào (tên người dùng và câu hỏi).
  // Ô tìm kiếm sản phẩm.
  private TextInputEditText edtSearch; // Biến lưu TextInputEditText cho ô tìm kiếm sản phẩm.

  // Inflate layout trang chủ và ánh xạ các thành phần UI.
  @Nullable @Override // Ghi đè phương thức onCreateView từ Fragment, có thể trả về null.
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){ // Phương thức tạo view cho fragment, nhận inflater, container và savedInstanceState.
    // Inflate layout fragment_home chứa danh sách sản phẩm và shortcut.
    View v = inflater.inflate(R.layout.fragment_home, container, false); // Inflate layout từ resource R.layout.fragment_home và lưu vào biến v.

    // Thiết lập RecyclerView hiển thị sản phẩm.
    RecyclerView rv = v.findViewById(R.id.rvProducts); // Tìm và lưu RecyclerView từ view bằng ID rvProducts.
    rv.setLayoutManager(new LinearLayoutManager(requireContext())); // Thiết lập LayoutManager là LinearLayoutManager theo chiều dọc từ context.
    // Khởi tạo adapter với callback thêm vào giỏ và mở chi tiết.
    adapter = new ProductsAdapter( // Khởi tạo adapter ProductsAdapter với hai callback.
            product -> { // Callback OnAdd: thêm sản phẩm vào giỏ hàng.
              cartVM.add(product); // Gọi add từ CartVM để thêm sản phẩm.
              Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show(); // Hiển thị toast xác nhận thêm vào giỏ.
            },
            this::openDetail // Callback OnClick: mở chi tiết sản phẩm bằng phương thức openDetail.
    );
    rv.setAdapter(adapter); // Gán adapter vào RecyclerView.

    // Ánh xạ các thành phần giao diện phụ trợ.
    chipCategories = v.findViewById(R.id.chipCategories); // Tìm và lưu ChipGroup danh mục.
    tvCartBadge = v.findViewById(R.id.tvCartBadge); // Tìm và lưu TextView badge giỏ hàng.
    tvGreetingLine = v.findViewById(R.id.tvGreetingLine); // Tìm và lưu TextView dòng lời chào đầu.
    tvGreetingHighlight = v.findViewById(R.id.tvGreetingHighlight); // Tìm và lưu TextView dòng lời chào nhấn mạnh.
    edtSearch = v.findViewById(R.id.edtSearch); // Tìm và lưu TextInputEditText tìm kiếm.

    View btnMenu = v.findViewById(R.id.btnOpenMenu); // Tìm và lưu nút menu.
    View btnCartTop = v.findViewById(R.id.btnCartTop); // Tìm và lưu nút giỏ hàng trên cùng.
    MaterialCardView btnShortcutFast = v.findViewById(R.id.btnShortcutFast); // Tìm và lưu shortcut fast delivery.
    MaterialCardView btnShortcutLocations = v.findViewById(R.id.btnShortcutLocations); // Tìm và lưu shortcut locations.
    MaterialCardView btnShortcutOrders = v.findViewById(R.id.btnShortcutOrders); // Tìm và lưu shortcut orders.

    // Thiết lập hành động cho các nút và shortcut.
    btnCartTop.setOnClickListener(view -> switchTab(R.id.tab_cart)); // Listener cho nút giỏ hàng: chuyển sang tab cart.
    btnShortcutFast.setOnClickListener(view -> switchTab(R.id.tab_cart)); // Listener cho shortcut fast: chuyển sang tab cart.
    btnShortcutOrders.setOnClickListener(view -> switchTab(R.id.tab_orders)); // Listener cho shortcut orders: chuyển sang tab orders.
    btnShortcutLocations.setOnClickListener(view -> startActivity(new Intent(requireContext(), MapActivity.class))); // Listener cho shortcut locations: mở MapActivity.
    btnMenu.setOnClickListener(view -> // Listener cho nút menu.
            Toast.makeText(getContext(), "Quick menu is coming soon", Toast.LENGTH_SHORT).show()); // Hiển thị toast thông báo tính năng sắp có.

    // Các nút xem tất cả hiển thị thông báo vì tính năng đang phát triển.
    v.findViewById(R.id.btnSeeAllProducts).setOnClickListener(view -> // Listener cho nút xem tất cả sản phẩm.
            Toast.makeText(getContext(), "All products will be visible soon", Toast.LENGTH_SHORT).show()); // Hiển thị toast thông báo sắp có.

    v.findViewById(R.id.btnSeeAllCategories).setOnClickListener(view -> // Listener cho nút xem tất cả danh mục.
            Toast.makeText(getContext(), "Category details will be added soon", Toast.LENGTH_SHORT).show()); // Hiển thị toast thông báo sắp có.

    // Xử lý sự kiện tìm kiếm khi người dùng nhấn Enter hoặc IME action.
    edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> { // Thiết lập listener cho editor action của edtSearch.
      if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) { // Kiểm tra nếu là action search hoặc phím Enter.
        performSearch(textView.getText()); // Gọi performSearch với text từ TextView.
        return true; // Trả về true để xử lý sự kiện.
      }
      return false; // Trả về false nếu không xử lý.
    });

    // Thiết lập lời chào tùy thời điểm và người dùng.
    setupGreeting(); // Gọi phương thức setupGreeting để thiết lập lời chào.

    // Trả về view đã cấu hình.
    return v; // Trả về view đã inflate và cấu hình.
  }

  // Thiết lập ViewModel và observer sau khi view sẵn sàng.
  @Override // Ghi đè phương thức onViewCreated từ Fragment.
  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState){ // Phương thức gọi sau khi view được tạo, nhận view và savedInstanceState.
    super.onViewCreated(v, savedInstanceState); // Gọi onViewCreated của lớp cha.
    // Lấy HomeVM để quan sát dữ liệu sản phẩm và danh mục.
    vm = new ViewModelProvider(this).get(HomeVM.class); // Khởi tạo HomeVM từ ViewModelProvider cho fragment này.
    // Lấy CartVM dùng chung với Activity để giữ dữ liệu khi chuyển tab.
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class); // Khởi tạo CartVM từ ViewModelProvider cho activity.

    // Quan sát danh sách sản phẩm và cập nhật adapter.
    vm.products.observe(getViewLifecycleOwner(), adapter::submit); // Quan sát LiveData products và gọi submit của adapter khi thay đổi.
    // Quan sát danh sách danh mục để render chip.
    vm.categories.observe(getViewLifecycleOwner(), this::renderCategories); // Quan sát LiveData categories và gọi renderCategories khi thay đổi.
    // Quan sát danh mục được chọn để cập nhật trạng thái checked của chip.
    vm.selectedCategory.observe(getViewLifecycleOwner(), this::highlightSelectedCategory); // Quan sát LiveData selectedCategory và gọi highlightSelectedCategory khi thay đổi.

    // Quan sát giỏ hàng để cập nhật badge số lượng.
    cartVM.cart.observe(getViewLifecycleOwner(), list -> { // Quan sát LiveData cart và xử lý khi thay đổi.
      int total = 0; // Biến lưu tổng số lượng sản phẩm trong giỏ.
      if (list != null) { // Nếu list không null.
        for (CartItemWithProduct row : list) { // Duyệt qua từng item trong giỏ.
          if (row == null || row.item == null) continue; // Bỏ qua nếu row hoặc item null.
          total += row.item.quantity; // Cộng quantity vào total.
        }
      }
      if (total > 0) { // Nếu total > 0.
        tvCartBadge.setVisibility(View.VISIBLE); // Hiển thị badge.
        tvCartBadge.setText(String.valueOf(total)); // Đặt text là total.
      } else { // Ngược lại.
        tvCartBadge.setVisibility(View.GONE); // Ẩn badge.
      }
    });
  }

  // Tạo chip danh mục và thiết lập sự kiện chọn.
  private void renderCategories(List<CategoryEntity> categories) { // Phương thức render các chip danh mục từ list categories.
    if (chipCategories == null) return; // Thoát nếu chipCategories null.
    chipCategories.removeAllViews(); // Xóa tất cả view con trong ChipGroup.
    LayoutInflater inflater = LayoutInflater.from(requireContext()); // Lấy LayoutInflater từ context.
    Integer selected = vm.selectedCategory.getValue(); // Lấy danh mục đang chọn từ ViewModel.

//    if (categories != null && !categories.isEmpty() && selected == null) {
//      vm.selectedCategory.setValue(categories.get(0).categoryId);
//      selected = categories.get(0).categoryId;
//    }

    if (categories != null) { // Nếu categories không null.
      for (CategoryEntity cat : categories) { // Duyệt qua từng danh mục.
        Chip chip = (Chip) inflater.inflate(R.layout.item_category_chip, chipCategories, false); // Inflate chip từ layout item_category_chip.
        chip.setText(cat.name); // Đặt text là tên danh mục.
        chip.setTag(cat.categoryId); // Đặt tag là ID danh mục.
        chip.setChecked(selected != null && selected == cat.categoryId); // Đặt checked nếu là danh mục đang chọn.

        chip.setOnClickListener(view -> { // Thiết lập listener click cho chip.
          Integer current = vm.selectedCategory.getValue(); // Lấy danh mục hiện tại.
          if (current != null && current.equals(cat.categoryId)) { // Nếu click lại chip đang chọn.
            // 🔹 Nếu bấm lại chính chip đang chọn -> bỏ chọn -> hiển thị tất cả
            vm.selectedCategory.setValue(null); // Bỏ chọn danh mục.
          } else { // Ngược lại.
            // 🔹 Ngược lại: chọn danh mục mới
            vm.selectedCategory.setValue(cat.categoryId); // Chọn danh mục mới.
          }
        });

        chipCategories.addView(chip); // Thêm chip vào ChipGroup.
      }
    }
  }

  // Cập nhật trạng thái checked cho các chip khi danh mục được chọn thay đổi.
  private void highlightSelectedCategory(Integer selectedId) { // Phương thức cập nhật checked cho chip dựa trên selectedId.
    if (chipCategories == null) return; // Thoát nếu chipCategories null.
    for (int i = 0; i < chipCategories.getChildCount(); i++) { // Duyệt qua từng child trong ChipGroup.
      View child = chipCategories.getChildAt(i); // Lấy child tại vị trí i.
      if (child instanceof Chip chip) { // Nếu child là Chip.
        Object tag = chip.getTag(); // Lấy tag của chip.
        boolean isSelected = tag instanceof Integer && ((Integer) tag) == selectedId; // Kiểm tra nếu tag là Integer và bằng selectedId.
        chip.setChecked(isSelected); // Đặt checked theo kết quả kiểm tra.
      }
    }
  }

  // Tạo lời chào cá nhân hóa dựa trên thời điểm và username.
  private void setupGreeting() { // Phương thức thiết lập lời chào dựa trên thời gian và username.
    Calendar calendar = Calendar.getInstance(); // Lấy instance Calendar hiện tại.
    int hour = calendar.get(Calendar.HOUR_OF_DAY); // Lấy giờ trong ngày (0-23).
    String period; // Biến lưu chuỗi thời gian (morning, afternoon, evening).
    if (hour < 11) period = "morning"; // Nếu giờ < 11, là morning.
    else if (hour < 17) period = "afternoon"; // Nếu giờ < 17, là afternoon.
    else period = "evening"; // Ngược lại, là evening.

    tvGreetingLine.setText(String.format(Locale.getDefault(), "Good %s,", period)); // Đặt text cho tvGreetingLine với "Good [period],".

    SharedPreferences sp = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE); // Lấy SharedPreferences tên "auth" mode private.
    String username = sp.getString("username", null); // Lấy username từ SharedPreferences, mặc định null.
    if (TextUtils.isEmpty(username)) { // Nếu username rỗng.
      tvGreetingHighlight.setText("What drink are you craving today?"); // Đặt text mặc định không có tên.
    } else { // Ngược lại.
      tvGreetingHighlight.setText(String.format(Locale.getDefault(), "%s, what sounds good today?", username)); // Đặt text với tên người dùng.
    }
  }

  // Tạm thời chỉ hiển thị thông báo cho hành động tìm kiếm.
  private void performSearch(CharSequence query) { // Phương thức xử lý tìm kiếm với query.
    if (TextUtils.isEmpty(query)) { // Nếu query rỗng.
      Toast.makeText(getContext(), "Enter a drink name to search", Toast.LENGTH_SHORT).show(); // Hiển thị toast yêu cầu nhập tên.
      return; // Thoát.
    }
    Toast.makeText(getContext(), "Search is coming soon!", Toast.LENGTH_SHORT).show(); // Hiển thị toast thông báo tính năng sắp có.
  }

  // Điều hướng sang màn chi tiết sản phẩm khi người dùng chọn.
  private void openDetail(ProductEntity item){ // Phương thức mở chi tiết sản phẩm với item.
    Fragment f = ProductDetailFragment.newInstance(item.productId); // Tạo instance ProductDetailFragment với ID sản phẩm.
    requireActivity().getSupportFragmentManager() // Lấy FragmentManager từ activity.
            .beginTransaction() // Bắt đầu transaction.
            .replace(R.id.container, f) // Thay thế fragment trong container bằng f.
            .addToBackStack("product_detail") // Thêm vào back stack với tag "product_detail".
            .commit(); // Commit transaction.
  }

  // Chuyển tab của BottomNavigationView dựa trên id tab truyền vào.
  private void switchTab(int tabId) { // Phương thức chuyển tab với tabId.
    BottomNavigationView nav = requireActivity().findViewById(R.id.bottomNav); // Tìm BottomNavigationView từ activity bằng ID.
    if (nav != null) { // Nếu nav không null.
      nav.setSelectedItemId(tabId); // Đặt item được chọn theo tabId.
    }
  }
}