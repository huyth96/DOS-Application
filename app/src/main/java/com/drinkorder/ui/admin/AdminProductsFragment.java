// Khai báo package com.drinkorder.ui.admin cho toàn bộ lớp.
package com.drinkorder.ui.admin;

// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.text.Editable để sử dụng các lớp hoặc hàm tương ứng.
import android.text.Editable;
// Import android.text.TextWatcher để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextWatcher;
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
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
// Import com.google.android.material.textfield.TextInputEditText để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.textfield.TextInputEditText;

// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Map để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Map;
// Import java.util.HashMap để sử dụng các lớp hoặc hàm tương ứng.
import java.util.HashMap;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp AdminProductsFragment kế thừa Fragment.
public class AdminProductsFragment extends Fragment {

  // Khai báo thuộc tính với phạm vi truy cập: private AdminProductsVM vm.
  private AdminProductsVM vm;
  // Khai báo thuộc tính với phạm vi truy cập: private AdminProductsAdapter adapter.
  private AdminProductsAdapter adapter;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvProductCount.
  private TextView tvProductCount;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvCategoryCount.
  private TextView tvCategoryCount;
  // Khai báo thuộc tính với phạm vi truy cập: private View emptyStateContainer.
  private View emptyStateContainer;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvEmptyTitle.
  private TextView tvEmptyTitle;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvEmptySubtitle.
  private TextView tvEmptySubtitle;
  // Khai báo thuộc tính với phạm vi truy cập: private RecyclerView recyclerView.
  private RecyclerView recyclerView;
  // Khai báo thuộc tính với phạm vi truy cập: private final List<ProductEntity> allProducts = new ArrayList<>().
  private final List<ProductEntity> allProducts = new ArrayList<>();
  // Khai báo thuộc tính với phạm vi truy cập: private Map<Integer, String> categoryNames = new HashMap<>().
  private Map<Integer, String> categoryNames = new HashMap<>();
  // Khai báo thuộc tính với phạm vi truy cập: private String currentQuery = "".
  private String currentQuery = "";
  // Khai báo thuộc tính với phạm vi truy cập: private TextInputEditText edtSearch.
  private TextInputEditText edtSearch;

  // Áp dụng annotation @Nullable cho phần tử bên dưới.
  @Nullable
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           // Áp dụng annotation @Nullable cho phần tử bên dưới.
                           @Nullable Bundle savedInstanceState) {
    // Trả về kết quả inflater.inflate(R.layout.fragment_admin_products, container, false);.
    return inflater.inflate(R.layout.fragment_admin_products, container, false);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(view, savedInstanceState);.
    super.onViewCreated(view, savedInstanceState);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvProductCount = view.findViewById(R.id.tvProductCount);.
    tvProductCount = view.findViewById(R.id.tvProductCount);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryCount = view.findViewById(R.id.tvCategoryCount);.
    tvCategoryCount = view.findViewById(R.id.tvCategoryCount);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: emptyStateContainer = view.findViewById(R.id.emptyStateContainer);.
    emptyStateContainer = view.findViewById(R.id.emptyStateContainer);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyTitle = view.findViewById(R.id.tvEmptyState);.
    tvEmptyTitle = view.findViewById(R.id.tvEmptyState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptySubtitle = view.findViewById(R.id.tvEmptySubtitle);.
    tvEmptySubtitle = view.findViewById(R.id.tvEmptySubtitle);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView = view.findViewById(R.id.rvAdminProducts);.
    recyclerView = view.findViewById(R.id.rvAdminProducts);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));.
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    adapter = new AdminProductsAdapter(new AdminProductsAdapter.Callback() {
      // Áp dụng annotation @Override và ghi đè phương thức onEdit.
      @Override public void onEdit(ProductEntity product) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (getContext() == null) return;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: AdminProductFormActivity.start(getContext(), product.productId);.
        AdminProductFormActivity.start(getContext(), product.productId);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }

      // Áp dụng annotation @Override và ghi đè phương thức onDelete.
      @Override public void onDelete(ProductEntity product) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: confirmDelete(product);.
        confirmDelete(product);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setAdapter(adapter);.
    recyclerView.setAdapter(adapter);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtSearch = view.findViewById(R.id.edtSearchProducts);.
    edtSearch = view.findViewById(R.id.edtSearchProducts);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (edtSearch != null) {
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      edtSearch.addTextChangedListener(new TextWatcher() {
        // Áp dụng annotation @Override và ghi đè phương thức beforeTextChanged.
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        // Áp dụng annotation @Override và ghi đè phương thức onTextChanged.
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: currentQuery = s == null ? "" : s.toString().trim();.
          currentQuery = s == null ? "" : s.toString().trim();
          // Thực hiện lời gọi phương thức hoặc khởi tạo: applyFilters();.
          applyFilters();
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Áp dụng annotation @Override và ghi đè phương thức afterTextChanged.
        @Override public void afterTextChanged(Editable s) {}
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: ExtendedFloatingActionButton fab = view.findViewById(R.id.fabAddProduct);.
    ExtendedFloatingActionButton fab = view.findViewById(R.id.fabAddProduct);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: fab.setOnClickListener(v -> openProductForm());.
    fab.setOnClickListener(v -> openProductForm());

    // Thực hiện lời gọi phương thức hoặc khởi tạo: Button btnAddFromEmpty = view.findViewById(R.id.btnAddProductFromEmpty);.
    Button btnAddFromEmpty = view.findViewById(R.id.btnAddProductFromEmpty);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (btnAddFromEmpty != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnAddFromEmpty.setOnClickListener(v -> openProductForm());.
      btnAddFromEmpty.setOnClickListener(v -> openProductForm());
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(this).get(AdminProductsVM.class);.
    vm = new ViewModelProvider(this).get(AdminProductsVM.class);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.products.observe(getViewLifecycleOwner(), list -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: allProducts.clear();.
      allProducts.clear();
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (list != null) allProducts.addAll(list);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: updateProductCount(allProducts.size());.
      updateProductCount(allProducts.size());
      // Thực hiện lời gọi phương thức hoặc khởi tạo: applyFilters();.
      applyFilters();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.categories.observe(getViewLifecycleOwner(), list -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryNames = toCategoryMap(list);.
      categoryNames = toCategoryMap(list);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.setCategoryNames(categoryNames);.
      adapter.setCategoryNames(categoryNames);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: int count = list == null ? 0 : list.size();.
      int count = list == null ? 0 : list.size();
      // Thực thi câu lệnh: tvCategoryCount.setText(String.format(Locale.getDefault(), "%d %s", count,.
      tvCategoryCount.setText(String.format(Locale.getDefault(), "%d %s", count,
          // Thực hiện lời gọi phương thức hoặc khởi tạo: count == 1 ? "category" : "categories"));.
          count == 1 ? "category" : "categories"));
      // Thực hiện lời gọi phương thức hoặc khởi tạo: applyFilters();.
      applyFilters();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức confirmDelete với phạm vi truy cập tương ứng.
  private void confirmDelete(ProductEntity product) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (product == null || getContext() == null) return;
    // Khởi tạo đối tượng mới với biểu thức new androidx.appcompat.app.AlertDialog.Builder(requireContext()).
    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setTitle("Remove product").
        .setTitle("Remove product")
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setMessage("Are you sure you want to delete \"" + product.name + "\"?").
        .setMessage("Are you sure you want to delete \"" + product.name + "\"?")
        // Thực thi câu lệnh: .setPositiveButton("Delete", (dialog, which) ->.
        .setPositiveButton("Delete", (dialog, which) ->
            // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
            vm.deleteProduct(product, new AdminProductsVM.ActionCallback() {
              // Áp dụng annotation @Override và ghi đè phương thức onSuccess.
              @Override public void onSuccess() {
                // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Product removed", Toast.LENGTH_SHORT).show();.
                Toast.makeText(getContext(), "Product removed", Toast.LENGTH_SHORT).show();
              // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
              }

              // Áp dụng annotation @Override và ghi đè phương thức onError.
              @Override public void onError(Throwable throwable) {
                // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Delete failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();.
                Toast.makeText(getContext(), "Delete failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
              // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
              }
            // Thực hiện lời gọi phương thức hoặc khởi tạo: })).
            }))
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setNegativeButton("Cancel", null).
        .setNegativeButton("Cancel", null)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .show();.
        .show();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức toCategoryMap với phạm vi truy cập tương ứng.
  private Map<Integer, String> toCategoryMap(List<CategoryEntity> list) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Map<Integer, String> map = new HashMap<>();.
    Map<Integer, String> map = new HashMap<>();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (list != null) {
      // Bắt đầu vòng lặp for để duyệt dữ liệu.
      for (CategoryEntity c : list) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: map.put(c.categoryId, c.name);.
        map.put(c.categoryId, c.name);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả map;.
    return map;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức applyFilters với phạm vi truy cập tương ứng.
  private void applyFilters() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: List<ProductEntity> filtered = new ArrayList<>();.
    List<ProductEntity> filtered = new ArrayList<>();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());.
    String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (ProductEntity p : allProducts) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (query.isEmpty()) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: filtered.add(p);.
        filtered.add(p);
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: String name = p.name == null ? "" : p.name.toLowerCase(Locale.getDefault());.
        String name = p.name == null ? "" : p.name.toLowerCase(Locale.getDefault());
        // Thực hiện lời gọi phương thức hoặc khởi tạo: String category = categoryNames.getOrDefault(p.categoryId, "").
        String category = categoryNames.getOrDefault(p.categoryId, "")
            // Thực hiện lời gọi phương thức hoặc khởi tạo: .toLowerCase(Locale.getDefault());.
            .toLowerCase(Locale.getDefault());
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (name.contains(query) || category.contains(query)) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: filtered.add(p);.
          filtered.add(p);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submit(filtered);.
    adapter.submit(filtered);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: updateEmptyState(filtered.isEmpty(), !query.isEmpty());.
    updateEmptyState(filtered.isEmpty(), !query.isEmpty());
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức updateProductCount với phạm vi truy cập tương ứng.
  private void updateProductCount(int total) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (tvProductCount == null) return;
    // Thực thi câu lệnh: tvProductCount.setText(String.format(Locale.getDefault(), "%d %s", total,.
    tvProductCount.setText(String.format(Locale.getDefault(), "%d %s", total,
        // Thực hiện lời gọi phương thức hoặc khởi tạo: total == 1 ? "product" : "products"));.
        total == 1 ? "product" : "products"));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức updateEmptyState với phạm vi truy cập tương ứng.
  private void updateEmptyState(boolean showEmpty, boolean isFiltering) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (recyclerView == null || emptyStateContainer == null) return;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);.
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: emptyStateContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);.
    emptyStateContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!showEmpty) return;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (tvEmptyTitle == null || tvEmptySubtitle == null) return;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (allProducts.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyTitle.setText("No products yet");.
      tvEmptyTitle.setText("No products yet");
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptySubtitle.setText("Tap the button below to add your first drink.");.
      tvEmptySubtitle.setText("Tap the button below to add your first drink.");
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (isFiltering) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyTitle.setText("No products match your search");.
      tvEmptyTitle.setText("No products match your search");
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptySubtitle.setText("Try another keyword or clear the filter.");.
      tvEmptySubtitle.setText("Try another keyword or clear the filter.");
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyTitle.setText("No products found");.
      tvEmptyTitle.setText("No products found");
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptySubtitle.setText("Add a new product to get started.");.
      tvEmptySubtitle.setText("Add a new product to get started.");
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức openProductForm với phạm vi truy cập tương ứng.
  private void openProductForm() {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (getContext() != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: AdminProductFormActivity.start(getContext(), -1);.
      AdminProductFormActivity.start(getContext(), -1);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}

