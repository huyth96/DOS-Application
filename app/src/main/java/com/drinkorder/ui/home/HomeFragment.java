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
public class HomeFragment extends Fragment {

  // ViewModel cung cấp dữ liệu sản phẩm và danh mục.
  private HomeVM vm;
  // ViewModel quản lý giỏ hàng để đồng bộ badge và thao tác thêm sản phẩm.
  private CartVM cartVM;
  // Adapter hiển thị danh sách sản phẩm.
  private ProductsAdapter adapter;

  // ChipGroup chứa danh sách danh mục.
  private ChipGroup chipCategories;
  // TextView hiển thị số lượng sản phẩm trong giỏ hàng.
  private TextView tvCartBadge;
  // TextView dòng đầu của lời chào.
  private TextView tvGreetingLine;
  // TextView dòng nhấn mạnh của lời chào.
  private TextView tvGreetingHighlight;
  // Ô tìm kiếm sản phẩm.
  private TextInputEditText edtSearch;

  // Inflate layout trang chủ và ánh xạ các thành phần UI.
  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    // Inflate layout fragment_home chứa danh sách sản phẩm và shortcut.
    View v = inflater.inflate(R.layout.fragment_home, container, false);

    // Thiết lập RecyclerView hiển thị sản phẩm.
    RecyclerView rv = v.findViewById(R.id.rvProducts);
    rv.setLayoutManager(new LinearLayoutManager(requireContext()));
    // Khởi tạo adapter với callback thêm vào giỏ và mở chi tiết.
    adapter = new ProductsAdapter(
        product -> {
          cartVM.add(product);
          Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
        },
        this::openDetail
    );
    rv.setAdapter(adapter);

    // Ánh xạ các thành phần giao diện phụ trợ.
    chipCategories = v.findViewById(R.id.chipCategories);
    tvCartBadge = v.findViewById(R.id.tvCartBadge);
    tvGreetingLine = v.findViewById(R.id.tvGreetingLine);
    tvGreetingHighlight = v.findViewById(R.id.tvGreetingHighlight);
    edtSearch = v.findViewById(R.id.edtSearch);

    View btnMenu = v.findViewById(R.id.btnOpenMenu);
    View btnCartTop = v.findViewById(R.id.btnCartTop);
    MaterialCardView btnShortcutFast = v.findViewById(R.id.btnShortcutFast);
    MaterialCardView btnShortcutLocations = v.findViewById(R.id.btnShortcutLocations);
    MaterialCardView btnShortcutOrders = v.findViewById(R.id.btnShortcutOrders);

    // Thiết lập hành động cho các nút và shortcut.
    btnCartTop.setOnClickListener(view -> switchTab(R.id.tab_cart));
    btnShortcutFast.setOnClickListener(view -> switchTab(R.id.tab_cart));
    btnShortcutOrders.setOnClickListener(view -> switchTab(R.id.tab_orders));
    btnShortcutLocations.setOnClickListener(view -> startActivity(new Intent(requireContext(), MapActivity.class)));
    btnMenu.setOnClickListener(view ->
        Toast.makeText(getContext(), "Quick menu is coming soon", Toast.LENGTH_SHORT).show());

    // Các nút xem tất cả hiển thị thông báo vì tính năng đang phát triển.
    v.findViewById(R.id.btnSeeAllProducts).setOnClickListener(view ->
        Toast.makeText(getContext(), "All products will be visible soon", Toast.LENGTH_SHORT).show());

    v.findViewById(R.id.btnSeeAllCategories).setOnClickListener(view ->
        Toast.makeText(getContext(), "Category details will be added soon", Toast.LENGTH_SHORT).show());

    // Xử lý sự kiện tìm kiếm khi người dùng nhấn Enter hoặc IME action.
    edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
      if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
        performSearch(textView.getText());
        return true;
      }
      return false;
    });

    // Thiết lập lời chào tùy thời điểm và người dùng.
    setupGreeting();

    // Trả về view đã cấu hình.
    return v;
  }

  // Thiết lập ViewModel và observer sau khi view sẵn sàng.
  @Override
  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState){
    super.onViewCreated(v, savedInstanceState);
    // Lấy HomeVM để quan sát dữ liệu sản phẩm và danh mục.
    vm = new ViewModelProvider(this).get(HomeVM.class);
    // Lấy CartVM dùng chung với Activity để giữ dữ liệu khi chuyển tab.
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);

    // Quan sát danh sách sản phẩm và cập nhật adapter.
    vm.products.observe(getViewLifecycleOwner(), adapter::submit);
    // Quan sát danh sách danh mục để render chip.
    vm.categories.observe(getViewLifecycleOwner(), this::renderCategories);
    // Quan sát danh mục được chọn để cập nhật trạng thái checked của chip.
    vm.selectedCategory.observe(getViewLifecycleOwner(), this::highlightSelectedCategory);

    // Quan sát giỏ hàng để cập nhật badge số lượng.
    cartVM.cart.observe(getViewLifecycleOwner(), list -> {
      int total = 0;
      if (list != null) {
        for (CartItemWithProduct row : list) {
          if (row == null || row.item == null) continue;
          total += row.item.quantity;
        }
      }
      if (total > 0) {
        tvCartBadge.setVisibility(View.VISIBLE);
        tvCartBadge.setText(String.valueOf(total));
      } else {
        tvCartBadge.setVisibility(View.GONE);
      }
    });
  }

  // Tạo chip danh mục và thiết lập sự kiện chọn.
  private void renderCategories(List<CategoryEntity> categories) {
    if (chipCategories == null) return;
    chipCategories.removeAllViews();
    LayoutInflater inflater = LayoutInflater.from(requireContext());
    Integer selected = vm.selectedCategory.getValue();

//    if (categories != null && !categories.isEmpty() && selected == null) {
//      vm.selectedCategory.setValue(categories.get(0).categoryId);
//      selected = categories.get(0).categoryId;
//    }

    if (categories != null) {
      for (CategoryEntity cat : categories) {
        Chip chip = (Chip) inflater.inflate(R.layout.item_category_chip, chipCategories, false);
        chip.setText(cat.name);
        chip.setTag(cat.categoryId);
        chip.setChecked(selected != null && selected == cat.categoryId);

        chip.setOnClickListener(view -> {
          Integer current = vm.selectedCategory.getValue();
          if (current != null && current.equals(cat.categoryId)) {
            // 🔹 Nếu bấm lại chính chip đang chọn -> bỏ chọn -> hiển thị tất cả
            vm.selectedCategory.setValue(null);
          } else {
            // 🔹 Ngược lại: chọn danh mục mới
            vm.selectedCategory.setValue(cat.categoryId);
          }
        });

        chipCategories.addView(chip);
      }
    }
  }

  // Cập nhật trạng thái checked cho các chip khi danh mục được chọn thay đổi.
  private void highlightSelectedCategory(Integer selectedId) {
    if (chipCategories == null) return;
    for (int i = 0; i < chipCategories.getChildCount(); i++) {
      View child = chipCategories.getChildAt(i);
      if (child instanceof Chip chip) {
        Object tag = chip.getTag();
        boolean isSelected = tag instanceof Integer && ((Integer) tag) == selectedId;
        chip.setChecked(isSelected);
      }
    }
  }

  // Tạo lời chào cá nhân hóa dựa trên thời điểm và username.
  private void setupGreeting() {
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    String period;
    if (hour < 11) period = "morning";
    else if (hour < 17) period = "afternoon";
    else period = "evening";

    tvGreetingLine.setText(String.format(Locale.getDefault(), "Good %s,", period));

    SharedPreferences sp = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
    String username = sp.getString("username", null);
    if (TextUtils.isEmpty(username)) {
      tvGreetingHighlight.setText("What drink are you craving today?");
    } else {
      tvGreetingHighlight.setText(String.format(Locale.getDefault(), "%s, what sounds good today?", username));
    }
  }

  // Tạm thời chỉ hiển thị thông báo cho hành động tìm kiếm.
  private void performSearch(CharSequence query) {
    if (TextUtils.isEmpty(query)) {
      Toast.makeText(getContext(), "Enter a drink name to search", Toast.LENGTH_SHORT).show();
      return;
    }
    Toast.makeText(getContext(), "Search is coming soon!", Toast.LENGTH_SHORT).show();
  }

  // Điều hướng sang màn chi tiết sản phẩm khi người dùng chọn.
  private void openDetail(ProductEntity item){
    Fragment f = ProductDetailFragment.newInstance(item.productId);
    requireActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, f)
        .addToBackStack("product_detail")
        .commit();
  }

  // Chuyển tab của BottomNavigationView dựa trên id tab truyền vào.
  private void switchTab(int tabId) {
    BottomNavigationView nav = requireActivity().findViewById(R.id.bottomNav);
    if (nav != null) {
      nav.setSelectedItemId(tabId);
    }
  }
}
