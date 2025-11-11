// Đặt package để fragment thuộc khu vực quản trị sản phẩm.
package com.drinkorder.ui.admin;

// Import Bundle phục vụ vòng đời fragment.
import android.os.Bundle;
// Import Editable để xử lý văn bản trong TextWatcher.
import android.text.Editable;
// Import TextWatcher để theo dõi nội dung ô tìm kiếm.
import android.text.TextWatcher;
// Import LayoutInflater để tạo view từ XML.
import android.view.LayoutInflater;
// Import View để thao tác với các phần tử giao diện.
import android.view.View;
// Import ViewGroup làm container cho fragment.
import android.view.ViewGroup;
// Import Button để xử lý nút hành động trong empty state.
import android.widget.Button;
// Import TextView để hiển thị các chỉ số tổng quan.
import android.widget.TextView;
// Import Toast để thông báo nhanh cho người quản trị.
import android.widget.Toast;

// Import NonNull để chú thích tham số không được null.
import androidx.annotation.NonNull;
// Import Nullable để chú thích tham số có thể null.
import androidx.annotation.Nullable;
// Import Fragment làm lớp cơ sở cho màn quản lý sản phẩm.
import androidx.fragment.app.Fragment;
// Import ViewModelProvider để lấy ViewModel tương ứng.
import androidx.lifecycle.ViewModelProvider;
// Import LinearLayoutManager để sắp xếp danh sách sản phẩm theo chiều dọc.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import RecyclerView để hiển thị danh sách sản phẩm.
import androidx.recyclerview.widget.RecyclerView;

// Import R để truy cập layout và tài nguyên giao diện.
import com.drinkorder.R;
// Import CategoryEntity để ánh xạ ID danh mục sang tên hiển thị.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import ProductEntity để làm việc với dữ liệu đồ uống.
import com.drinkorder.data.db.entity.ProductEntity;
// Import ExtendedFloatingActionButton cho nút thêm sản phẩm nổi.
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
// Import TextInputEditText để đọc truy vấn tìm kiếm.
import com.google.android.material.textfield.TextInputEditText;

// Import ArrayList để lưu danh sách sản phẩm cục bộ.
import java.util.ArrayList;
// Import List để khai báo cấu trúc danh sách tổng quát.
import java.util.List;
// Import Map để lưu bảng ánh xạ danh mục.
import java.util.Map;
// Import HashMap để hiện thực Map lưu tên danh mục.
import java.util.HashMap;
// Import Locale để chuẩn hóa văn bản theo ngôn ngữ thiết bị.
import java.util.Locale;

// Fragment quản lý sản phẩm trong dashboard admin.
public class AdminProductsFragment extends Fragment {

  // ViewModel điều phối dữ liệu sản phẩm và danh mục.
  private AdminProductsVM vm;
  // Adapter hiển thị danh sách sản phẩm.
  private AdminProductsAdapter adapter;
  // TextView hiển thị tổng số sản phẩm.
  private TextView tvProductCount;
  // TextView hiển thị tổng số danh mục.
  private TextView tvCategoryCount;
  // View chứa giao diện khi danh sách rỗng.
  private View emptyStateContainer;
  // TextView tiêu đề empty state.
  private TextView tvEmptyTitle;
  // TextView mô tả empty state.
  private TextView tvEmptySubtitle;
  // RecyclerView chính hiển thị danh sách sản phẩm.
  private RecyclerView recyclerView;
  // Danh sách giữ toàn bộ sản phẩm hiện có để lọc.
  private final List<ProductEntity> allProducts = new ArrayList<>();
  // Bảng ánh xạ categoryId sang tên danh mục hiển thị.
  private Map<Integer, String> categoryNames = new HashMap<>();
  // Chuỗi truy vấn tìm kiếm hiện tại.
  private String currentQuery = "";
  // Ô tìm kiếm sản phẩm theo tên hoặc danh mục.
  private TextInputEditText edtSearch;

  // Tạo view cho fragment từ layout XML tương ứng.
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    // Inflate layout quản trị sản phẩm để sử dụng trong fragment.
    return inflater.inflate(R.layout.fragment_admin_products, container, false);
  }

  // Thiết lập giao diện và dữ liệu sau khi view được tạo xong.
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // Gọi super để bảo toàn xử lý mặc định của Fragment.
    super.onViewCreated(view, savedInstanceState);

    // Ánh xạ TextView tổng số sản phẩm để cập nhật thống kê.
    tvProductCount = view.findViewById(R.id.tvProductCount);
    // Ánh xạ TextView tổng số danh mục để hiển thị số liệu hỗ trợ.
    tvCategoryCount = view.findViewById(R.id.tvCategoryCount);
    // Ánh xạ container empty state để điều chỉnh hiển thị khi không có dữ liệu.
    emptyStateContainer = view.findViewById(R.id.emptyStateContainer);
    // Ánh xạ TextView tiêu đề empty state.
    tvEmptyTitle = view.findViewById(R.id.tvEmptyState);
    // Ánh xạ TextView mô tả empty state.
    tvEmptySubtitle = view.findViewById(R.id.tvEmptySubtitle);
    // Ánh xạ RecyclerView hiển thị danh sách sản phẩm.
    recyclerView = view.findViewById(R.id.rvAdminProducts);
    // Sử dụng LinearLayoutManager dọc để hiển thị danh sách dễ đọc.
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    // Khởi tạo adapter với callback xử lý thao tác chỉnh sửa và xóa.
    adapter = new AdminProductsAdapter(new AdminProductsAdapter.Callback() {
      // Callback chỉnh sửa sản phẩm khi người dùng chọn.
      @Override public void onEdit(ProductEntity product) {
        // Đảm bảo context tồn tại trước khi mở form chỉnh sửa.
        if (getContext() == null) return;
        // Mở Activity form sản phẩm với ID cần chỉnh sửa.
        AdminProductFormActivity.start(getContext(), product.productId);
      }

      // Callback xóa sản phẩm khi người dùng chọn.
      @Override public void onDelete(ProductEntity product) {
        // Hiển thị hộp thoại xác nhận xóa.
        confirmDelete(product);
      }
    });
    // Gắn adapter vào RecyclerView để hiển thị danh sách.
    recyclerView.setAdapter(adapter);

    // Ánh xạ ô tìm kiếm để lọc danh sách sản phẩm.
    edtSearch = view.findViewById(R.id.edtSearchProducts);
    // Kiểm tra tồn tại để tránh NullPointerException trên layout khác.
    if (edtSearch != null) {
      // Lắng nghe thay đổi văn bản để cập nhật bộ lọc ngay lập tức.
      edtSearch.addTextChangedListener(new TextWatcher() {
        // Không cần xử lý trước khi thay đổi.
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        // Cập nhật chuỗi tìm kiếm mỗi khi người dùng nhập dữ liệu.
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          // Lưu truy vấn hiện tại đã loại bỏ khoảng trắng dư.
          currentQuery = s == null ? "" : s.toString().trim();
          // Áp dụng bộ lọc để làm mới danh sách hiển thị.
          applyFilters();
        }
        // Không cần xử lý sau khi thay đổi.
        @Override public void afterTextChanged(Editable s) {}
      });
    }

    // Ánh xạ nút nổi thêm sản phẩm.
    ExtendedFloatingActionButton fab = view.findViewById(R.id.fabAddProduct);
    // Khi nhấn nút nổi thì mở form tạo sản phẩm mới.
    fab.setOnClickListener(v -> openProductForm());

    // Ánh xạ nút thêm trong empty state để tạo sản phẩm khi danh sách rỗng.
    Button btnAddFromEmpty = view.findViewById(R.id.btnAddProductFromEmpty);
    // Kiểm tra null vì phần tử chỉ xuất hiện khi layout hỗ trợ.
    if (btnAddFromEmpty != null) {
      // Khi nhấn nút sẽ mở form tạo sản phẩm.
      btnAddFromEmpty.setOnClickListener(v -> openProductForm());
    }

    // Lấy ViewModel cung cấp dữ liệu sản phẩm và danh mục.
    vm = new ViewModelProvider(this).get(AdminProductsVM.class);
    // Quan sát danh sách sản phẩm để cập nhật UI khi dữ liệu thay đổi.
    vm.products.observe(getViewLifecycleOwner(), list -> {
      // Dọn danh sách cũ để đồng bộ dữ liệu mới nhất.
      allProducts.clear();
      // Sao chép dữ liệu mới vào bộ nhớ tạm phục vụ lọc.
      if (list != null) allProducts.addAll(list);
      // Cập nhật bộ đếm tổng số sản phẩm.
      updateProductCount(allProducts.size());
      // Áp dụng bộ lọc hiện thời để làm mới danh sách.
      applyFilters();
    });
    // Quan sát danh sách danh mục để cập nhật bảng ánh xạ tên.
    vm.categories.observe(getViewLifecycleOwner(), list -> {
      // Chuyển danh sách danh mục sang map để tra cứu nhanh.
      categoryNames = toCategoryMap(list);
      // Thông báo adapter về bảng tên danh mục mới.
      adapter.setCategoryNames(categoryNames);
      // Tính tổng số danh mục để hiển thị thống kê.
      int count = list == null ? 0 : list.size();
      // Cập nhật TextView số danh mục với dạng số + đơn vị.
      tvCategoryCount.setText(String.format(Locale.getDefault(), "%d %s", count,
          count == 1 ? "category" : "categories"));
      // Áp dụng lại bộ lọc để phản ánh thay đổi tên danh mục.
      applyFilters();
    });
  }

  // Hiển thị hộp thoại xác nhận xóa sản phẩm.
  private void confirmDelete(ProductEntity product) {
    // Nếu thiếu dữ liệu hoặc context thì không tiếp tục.
    if (product == null || getContext() == null) return;
    // Tạo dialog xác nhận bằng AlertDialog của AppCompat.
    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
        // Đặt tiêu đề cho dialog xóa sản phẩm.
        .setTitle("Remove product")
        // Đặt thông điệp chứa tên sản phẩm để người dùng xác nhận.
        .setMessage("Are you sure you want to delete \"" + product.name + "\"?")
        // Khi bấm Delete sẽ gọi ViewModel xóa sản phẩm.
        .setPositiveButton("Delete", (dialog, which) ->
            vm.deleteProduct(product, new AdminProductsVM.ActionCallback() {
              // Thông báo khi xóa thành công.
              @Override public void onSuccess() {
                Toast.makeText(getContext(), "Product removed", Toast.LENGTH_SHORT).show();
              }

              // Thông báo khi xóa thất bại.
              @Override public void onError(Throwable throwable) {
                Toast.makeText(getContext(), "Delete failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }))
        // Nút Cancel chỉ đóng dialog mà không làm gì thêm.
        .setNegativeButton("Cancel", null)
        // Hiển thị dialog ra màn hình.
        .show();
  }

  // Chuyển danh sách danh mục sang map để adapter tra cứu nhanh.
  private Map<Integer, String> toCategoryMap(List<CategoryEntity> list) {
    // Tạo HashMap mới để lưu kết quả.
    Map<Integer, String> map = new HashMap<>();
    // Nếu danh sách danh mục không rỗng thì duyệt từng phần tử.
    if (list != null) {
      for (CategoryEntity c : list) {
        // Lưu cặp key-value giữa id và tên danh mục.
        map.put(c.categoryId, c.name);
      }
    }
    // Trả về map đã xây dựng.
    return map;
  }

  // Áp dụng bộ lọc dựa trên truy vấn tìm kiếm và tên danh mục.
  private void applyFilters() {
    // Tạo danh sách mới để chứa các sản phẩm phù hợp.
    List<ProductEntity> filtered = new ArrayList<>();
    // Chuẩn hóa truy vấn thành chữ thường để so sánh không phân biệt hoa thường.
    String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());
    // Duyệt từng sản phẩm trong danh sách gốc.
    for (ProductEntity p : allProducts) {
      // Nếu không có truy vấn thì thêm toàn bộ sản phẩm.
      if (query.isEmpty()) {
        filtered.add(p);
      } else {
        // Lấy tên sản phẩm dạng chữ thường để so sánh.
        String name = p.name == null ? "" : p.name.toLowerCase(Locale.getDefault());
        // Lấy tên danh mục tương ứng dạng chữ thường để so sánh.
        String category = categoryNames.getOrDefault(p.categoryId, "")
            .toLowerCase(Locale.getDefault());
        // Nếu tên sản phẩm hoặc tên danh mục chứa truy vấn thì giữ lại.
        if (name.contains(query) || category.contains(query)) {
          filtered.add(p);
        }
      }
    }
    // Cập nhật adapter với danh sách đã lọc.
    adapter.submit(filtered);
    // Cập nhật empty state dựa trên kết quả lọc.
    updateEmptyState(filtered.isEmpty(), !query.isEmpty());
  }

  // Cập nhật tổng số sản phẩm hiển thị trên dashboard.
  private void updateProductCount(int total) {
    // Nếu view thống kê không tồn tại thì dừng lại.
    if (tvProductCount == null) return;
    // Hiển thị số lượng kèm đơn vị số ít hoặc số nhiều.
    tvProductCount.setText(String.format(Locale.getDefault(), "%d %s", total,
        total == 1 ? "product" : "products"));
  }

  // Điều chỉnh giao diện empty state tùy theo điều kiện hiển thị.
  private void updateEmptyState(boolean showEmpty, boolean isFiltering) {
    // Nếu thiếu view cần thiết thì không xử lý.
    if (recyclerView == null || emptyStateContainer == null) return;
    // Ẩn danh sách khi rỗng và hiển thị container empty state.
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    emptyStateContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    // Nếu có dữ liệu thì không cần tùy chỉnh thông điệp.
    if (!showEmpty) return;
    // Kiểm tra TextView để tránh lỗi null.
    if (tvEmptyTitle == null || tvEmptySubtitle == null) return;
    // Khi danh sách chưa có sản phẩm nào hãy hướng dẫn tạo mới.
    if (allProducts.isEmpty()) {
      tvEmptyTitle.setText("No products yet");
      tvEmptySubtitle.setText("Tap the button below to add your first drink.");
    } else if (isFiltering) {
      // Khi lọc không ra kết quả hãy gợi ý đổi từ khóa.
      tvEmptyTitle.setText("No products match your search");
      tvEmptySubtitle.setText("Try another keyword or clear the filter.");
    } else {
      // Trường hợp khác hiển thị thông điệp mặc định.
      tvEmptyTitle.setText("No products found");
      tvEmptySubtitle.setText("Add a new product to get started.");
    }
  }

  // Mở form tạo hoặc chỉnh sửa sản phẩm tùy theo ID truyền vào.
  private void openProductForm() {
    // Chỉ thực hiện khi context sẵn sàng.
    if (getContext() != null) {
      // Truyền -1 để form hiểu là tạo sản phẩm mới.
      AdminProductFormActivity.start(getContext(), -1);
    }
  }
}
