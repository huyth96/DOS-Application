// Khai báo package com.drinkorder.ui.home cho toàn bộ lớp.
package com.drinkorder.ui.home;

// Import android.content.Context để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Context;
// Import android.content.Intent để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Intent;
// Import android.content.SharedPreferences để sử dụng các lớp hoặc hàm tương ứng.
import android.content.SharedPreferences;
// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;
// Import android.view.KeyEvent để sử dụng các lớp hoặc hàm tương ứng.
import android.view.KeyEvent;
// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;
// Import android.view.inputmethod.EditorInfo để sử dụng các lớp hoặc hàm tương ứng.
import android.view.inputmethod.EditorInfo;
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
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.drinkorder.data.db.pojo.CartItemWithProduct để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.CartItemWithProduct;
// Import com.drinkorder.ui.detail.ProductDetailFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.detail.ProductDetailFragment;
// Import com.drinkorder.ui.map.MapActivity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.map.MapActivity;
// Import com.drinkorder.vm.CartVM để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.vm.CartVM;
// Import com.drinkorder.vm.HomeVM để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.vm.HomeVM;
// Import com.google.android.material.bottomnavigation.BottomNavigationView để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.bottomnavigation.BottomNavigationView;
// Import com.google.android.material.card.MaterialCardView để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.card.MaterialCardView;
// Import com.google.android.material.chip.Chip để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.chip.Chip;
// Import com.google.android.material.chip.ChipGroup để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.chip.ChipGroup;
// Import com.google.android.material.textfield.TextInputEditText để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.textfield.TextInputEditText;
// Import java.util.Calendar để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Calendar;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp HomeFragment kế thừa Fragment.
public class HomeFragment extends Fragment {

  // Khai báo thuộc tính với phạm vi truy cập: private HomeVM vm.
  private HomeVM vm;
  // Khai báo thuộc tính với phạm vi truy cập: private CartVM cartVM.
  private CartVM cartVM;
  // Khai báo thuộc tính với phạm vi truy cập: private ProductsAdapter adapter.
  private ProductsAdapter adapter;

  // Khai báo thuộc tính với phạm vi truy cập: private ChipGroup chipCategories.
  private ChipGroup chipCategories;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvCartBadge.
  private TextView tvCartBadge;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvGreetingLine.
  private TextView tvGreetingLine;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvGreetingHighlight.
  private TextView tvGreetingHighlight;
  // Khai báo thuộc tính với phạm vi truy cập: private TextInputEditText edtSearch.
  private TextInputEditText edtSearch;

  // Áp dụng annotation @Nullable và @Override cho phần tử bên dưới.
  @Nullable @Override
  // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View v = inflater.inflate(R.layout.fragment_home, container, false);.
    View v = inflater.inflate(R.layout.fragment_home, container, false);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: RecyclerView rv = v.findViewById(R.id.rvProducts);.
    RecyclerView rv = v.findViewById(R.id.rvProducts);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rv.setLayoutManager(new LinearLayoutManager(requireContext()));.
    rv.setLayoutManager(new LinearLayoutManager(requireContext()));
    // Thực thi câu lệnh: adapter = new ProductsAdapter(.
    adapter = new ProductsAdapter(
        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        product -> {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: cartVM.add(product);.
          cartVM.add(product);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();.
        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
        // Thực thi câu lệnh: },.
        },
        // Thực thi câu lệnh: this::openDetail.
        this::openDetail
    // Thực hiện lời gọi phương thức hoặc khởi tạo: );.
    );
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rv.setAdapter(adapter);.
    rv.setAdapter(adapter);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: chipCategories = v.findViewById(R.id.chipCategories);.
    chipCategories = v.findViewById(R.id.chipCategories);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCartBadge = v.findViewById(R.id.tvCartBadge);.
    tvCartBadge = v.findViewById(R.id.tvCartBadge);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvGreetingLine = v.findViewById(R.id.tvGreetingLine);.
    tvGreetingLine = v.findViewById(R.id.tvGreetingLine);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvGreetingHighlight = v.findViewById(R.id.tvGreetingHighlight);.
    tvGreetingHighlight = v.findViewById(R.id.tvGreetingHighlight);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtSearch = v.findViewById(R.id.edtSearch);.
    edtSearch = v.findViewById(R.id.edtSearch);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: View btnMenu = v.findViewById(R.id.btnOpenMenu);.
    View btnMenu = v.findViewById(R.id.btnOpenMenu);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View btnCartTop = v.findViewById(R.id.btnCartTop);.
    View btnCartTop = v.findViewById(R.id.btnCartTop);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: MaterialCardView btnShortcutFast = v.findViewById(R.id.btnShortcutFast);.
    MaterialCardView btnShortcutFast = v.findViewById(R.id.btnShortcutFast);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: MaterialCardView btnShortcutLocations = v.findViewById(R.id.btnShortcutLocations);.
    MaterialCardView btnShortcutLocations = v.findViewById(R.id.btnShortcutLocations);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: MaterialCardView btnShortcutOrders = v.findViewById(R.id.btnShortcutOrders);.
    MaterialCardView btnShortcutOrders = v.findViewById(R.id.btnShortcutOrders);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnCartTop.setOnClickListener(view -> switchTab(R.id.tab_cart));.
    btnCartTop.setOnClickListener(view -> switchTab(R.id.tab_cart));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnShortcutFast.setOnClickListener(view -> switchTab(R.id.tab_cart));.
    btnShortcutFast.setOnClickListener(view -> switchTab(R.id.tab_cart));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnShortcutOrders.setOnClickListener(view -> switchTab(R.id.tab_orders));.
    btnShortcutOrders.setOnClickListener(view -> switchTab(R.id.tab_orders));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnShortcutLocations.setOnClickListener(view -> startActivity(new Intent(requireContext(), MapActivity.class)));.
    btnShortcutLocations.setOnClickListener(view -> startActivity(new Intent(requireContext(), MapActivity.class)));
    // Thực thi câu lệnh: btnMenu.setOnClickListener(view ->.
    btnMenu.setOnClickListener(view ->
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Quick menu is coming soon", Toast.LENGTH_SHORT).show());.
        Toast.makeText(getContext(), "Quick menu is coming soon", Toast.LENGTH_SHORT).show());

    // Thực thi câu lệnh: v.findViewById(R.id.btnSeeAllProducts).setOnClickListener(view ->.
    v.findViewById(R.id.btnSeeAllProducts).setOnClickListener(view ->
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "All products will be visible soon", Toast.LENGTH_SHORT).show());.
        Toast.makeText(getContext(), "All products will be visible soon", Toast.LENGTH_SHORT).show());

    // Thực thi câu lệnh: v.findViewById(R.id.btnSeeAllCategories).setOnClickListener(view ->.
    v.findViewById(R.id.btnSeeAllCategories).setOnClickListener(view ->
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Category details will be added soon", Toast.LENGTH_SHORT).show());.
        Toast.makeText(getContext(), "Category details will be added soon", Toast.LENGTH_SHORT).show());

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: performSearch(textView.getText());.
        performSearch(textView.getText());
        // Trả về kết quả true;.
        return true;
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Trả về kết quả false;.
      return false;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });

    // Thực hiện lời gọi phương thức hoặc khởi tạo: setupGreeting();.
    setupGreeting();

    // Trả về kết quả v;.
    return v;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(v, savedInstanceState);.
    super.onViewCreated(v, savedInstanceState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(this).get(HomeVM.class);.
    vm = new ViewModelProvider(this).get(HomeVM.class);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);.
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.products.observe(getViewLifecycleOwner(), adapter::submit);.
    vm.products.observe(getViewLifecycleOwner(), adapter::submit);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.categories.observe(getViewLifecycleOwner(), this::renderCategories);.
    vm.categories.observe(getViewLifecycleOwner(), this::renderCategories);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.selectedCategory.observe(getViewLifecycleOwner(), this::highlightSelectedCategory);.
    vm.selectedCategory.observe(getViewLifecycleOwner(), this::highlightSelectedCategory);

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    cartVM.cart.observe(getViewLifecycleOwner(), list -> {
      // Gán giá trị cho biến hoặc thuộc tính: int total = 0.
      int total = 0;
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (list != null) {
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (CartItemWithProduct row : list) {
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (row == null || row.item == null) continue;
          // Gán giá trị cho biến hoặc thuộc tính: total += row.item.quantity.
          total += row.item.quantity;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (total > 0) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCartBadge.setVisibility(View.VISIBLE);.
        tvCartBadge.setVisibility(View.VISIBLE);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCartBadge.setText(String.valueOf(total));.
        tvCartBadge.setText(String.valueOf(total));
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCartBadge.setVisibility(View.GONE);.
        tvCartBadge.setVisibility(View.GONE);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức renderCategories với phạm vi truy cập tương ứng.
  private void renderCategories(List<CategoryEntity> categories) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (chipCategories == null) return;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: chipCategories.removeAllViews();.
    chipCategories.removeAllViews();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: LayoutInflater inflater = LayoutInflater.from(requireContext());.
    LayoutInflater inflater = LayoutInflater.from(requireContext());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Integer selected = vm.selectedCategory.getValue();.
    Integer selected = vm.selectedCategory.getValue();

    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (categories != null && !categories.isEmpty() && selected == null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.selectedCategory.setValue(categories.get(0).categoryId);.
      vm.selectedCategory.setValue(categories.get(0).categoryId);
      // Gán giá trị cho biến hoặc thuộc tính: selected = categories.get(0).categoryId.
      selected = categories.get(0).categoryId;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (categories != null) {
      // Bắt đầu vòng lặp for để duyệt dữ liệu.
      for (CategoryEntity cat : categories) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Chip chip = (Chip) inflater.inflate(R.layout.item_category_chip, chipCategories, false);.
        Chip chip = (Chip) inflater.inflate(R.layout.item_category_chip, chipCategories, false);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: chip.setText(cat.name);.
        chip.setText(cat.name);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: chip.setTag(cat.categoryId);.
        chip.setTag(cat.categoryId);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: chip.setChecked(selected != null && selected == cat.categoryId);.
        chip.setChecked(selected != null && selected == cat.categoryId);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: chip.setOnClickListener(view -> vm.selectedCategory.setValue(cat.categoryId));.
        chip.setOnClickListener(view -> vm.selectedCategory.setValue(cat.categoryId));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: chipCategories.addView(chip);.
        chipCategories.addView(chip);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức highlightSelectedCategory với phạm vi truy cập tương ứng.
  private void highlightSelectedCategory(Integer selectedId) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (chipCategories == null) return;
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (int i = 0; i < chipCategories.getChildCount(); i++) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: View child = chipCategories.getChildAt(i);.
      View child = chipCategories.getChildAt(i);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (child instanceof Chip chip) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Object tag = chip.getTag();.
        Object tag = chip.getTag();
        // Gán giá trị cho biến hoặc thuộc tính: boolean isSelected = tag instanceof Integer && ((Integer) tag) == selectedId.
        boolean isSelected = tag instanceof Integer && ((Integer) tag) == selectedId;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: chip.setChecked(isSelected);.
        chip.setChecked(isSelected);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setupGreeting với phạm vi truy cập tương ứng.
  private void setupGreeting() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Calendar calendar = Calendar.getInstance();.
    Calendar calendar = Calendar.getInstance();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: int hour = calendar.get(Calendar.HOUR_OF_DAY);.
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    // Thực thi câu lệnh: String period;.
    String period;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (hour < 11) period = "morning";
    // Gán giá trị cho biến hoặc thuộc tính: else if (hour < 17) period = "afternoon".
    else if (hour < 17) period = "afternoon";
    // Gán giá trị cho biến hoặc thuộc tính: else period = "evening".
    else period = "evening";

    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvGreetingLine.setText(String.format(Locale.getDefault(), "Good %s,", period));.
    tvGreetingLine.setText(String.format(Locale.getDefault(), "Good %s,", period));

    // Thực hiện lời gọi phương thức hoặc khởi tạo: SharedPreferences sp = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);.
    SharedPreferences sp = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String username = sp.getString("username", null);.
    String username = sp.getString("username", null);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(username)) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvGreetingHighlight.setText("What drink are you craving today?");.
      tvGreetingHighlight.setText("What drink are you craving today?");
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvGreetingHighlight.setText(String.format(Locale.getDefault(), "%s, what sounds good today?", username));.
      tvGreetingHighlight.setText(String.format(Locale.getDefault(), "%s, what sounds good today?", username));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức performSearch với phạm vi truy cập tương ứng.
  private void performSearch(CharSequence query) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(query)) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Enter a drink name to search", Toast.LENGTH_SHORT).show();.
      Toast.makeText(getContext(), "Enter a drink name to search", Toast.LENGTH_SHORT).show();
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Search is coming soon!", Toast.LENGTH_SHORT).show();.
    Toast.makeText(getContext(), "Search is coming soon!", Toast.LENGTH_SHORT).show();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức openDetail với phạm vi truy cập tương ứng.
  private void openDetail(ProductEntity item){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Fragment f = ProductDetailFragment.newInstance(item.productId);.
    Fragment f = ProductDetailFragment.newInstance(item.productId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: requireActivity().getSupportFragmentManager().
    requireActivity().getSupportFragmentManager()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .beginTransaction().
        .beginTransaction()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .replace(R.id.container, f).
        .replace(R.id.container, f)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .addToBackStack("product_detail").
        .addToBackStack("product_detail")
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .commit();.
        .commit();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức switchTab với phạm vi truy cập tương ứng.
  private void switchTab(int tabId) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: BottomNavigationView nav = requireActivity().findViewById(R.id.bottomNav);.
    BottomNavigationView nav = requireActivity().findViewById(R.id.bottomNav);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (nav != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: nav.setSelectedItemId(tabId);.
      nav.setSelectedItemId(tabId);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
