// Đặt package để fragment thuộc nhóm tính năng quản trị.
package com.drinkorder.ui.admin;

// Import Bundle cho vòng đời fragment.
import android.os.Bundle;
// Import Editable để thao tác chuỗi trong TextWatcher.
import android.text.Editable;
// Import TextWatcher để theo dõi thay đổi ô tìm kiếm.
import android.text.TextWatcher;
// Import LayoutInflater để tạo view từ XML.
import android.view.LayoutInflater;
// Import View để thao tác hiển thị.
import android.view.View;
// Import ViewGroup để làm container cho fragment.
import android.view.ViewGroup;
// Import Button để xử lý nút thêm nhanh trong empty state.
import android.widget.Button;
// Import TextView để hiển thị thông tin tổng quan.
import android.widget.TextView;
// Import Toast để hiển thị phản hồi nhanh.
import android.widget.Toast;

// Import NonNull chú thích tham số không được null.
import androidx.annotation.NonNull;
// Import Nullable chú thích tham số có thể null.
import androidx.annotation.Nullable;
// Import Fragment làm lớp cơ sở cho màn quản trị này.
import androidx.fragment.app.Fragment;
// Import ViewModelProvider để lấy ViewModel.
import androidx.lifecycle.ViewModelProvider;
// Import LinearLayoutManager để sắp xếp danh sách theo chiều dọc.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import RecyclerView để hiển thị danh sách danh mục.
import androidx.recyclerview.widget.RecyclerView;

// Import R để truy cập layout và tài nguyên liên quan.
import com.drinkorder.R;
// Import CategoryEntity để thao tác dữ liệu danh mục.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import MaterialAlertDialogBuilder để hiển thị hộp thoại xác nhận.
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
// Import ExtendedFloatingActionButton để dùng nút thêm danh mục nổi.
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
// Import TextInputEditText để đọc chuỗi tìm kiếm.
import com.google.android.material.textfield.TextInputEditText;

// Import SimpleDateFormat để định dạng thông tin cập nhật cuối.
import java.text.SimpleDateFormat;
// Import ArrayList để lưu danh sách danh mục cục bộ.
import java.util.ArrayList;
// Import Date để chuyển đổi timestamp sang dạng đọc được.
import java.util.Date;
// Import List để khai báo danh sách tổng quát.
import java.util.List;
// Import Locale để định dạng chuỗi phù hợp ngôn ngữ thiết bị.
import java.util.Locale;

// Fragment quản lý danh mục trong màn hình admin.
public class AdminCategoriesFragment extends Fragment {

  // Khai báo ViewModel để thao tác dữ liệu danh mục.
  private AdminCategoriesVM vm;
  // Khai báo adapter để đổ dữ liệu vào RecyclerView.
  private AdminCategoriesAdapter adapter;
  // Khai báo RecyclerView hiển thị danh mục.
  private RecyclerView recyclerView;
  // Khai báo container hiển thị trạng thái rỗng.
  private View emptyStateContainer;
  // Khai báo TextView hiển thị tổng số danh mục.
  private TextView tvCategoriesTotal;
  // Khai báo TextView hiển thị thời gian cập nhật gần nhất.
  private TextView tvCategoriesUpdated;
  // Khai báo TextView tiêu đề cho empty state.
  private TextView tvCategoryEmptyTitle;
  // Khai báo TextView mô tả cho empty state.
  private TextView tvCategoryEmptySubtitle;
  // Danh sách lưu toàn bộ danh mục phục vụ lọc và hiển thị.
  private final List<CategoryEntity> allCategories = new ArrayList<>();
  // Định dạng hiển thị thời gian cập nhật gần nhất.
  private final SimpleDateFormat lastUpdatedFormat =
      new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
  // Lưu truy vấn tìm kiếm hiện tại để áp dụng filter.
  private String currentQuery = "";

  // Tạo view cho fragment từ layout XML.
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate layout quản trị danh mục để hiển thị giao diện.
    return inflater.inflate(R.layout.fragment_admin_categories, container, false);
  }

  // Thiết lập logic sau khi view được tạo xong.
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // Gọi super để hoàn tất chu trình mặc định.
    super.onViewCreated(view, savedInstanceState);

    // Ánh xạ RecyclerView để hiển thị dữ liệu danh mục.
    recyclerView = view.findViewById(R.id.rvAdminCategories);
    // Ánh xạ container empty state để điều khiển trạng thái trống.
    emptyStateContainer = view.findViewById(R.id.categoryEmptyState);
    // Ánh xạ TextView tổng số danh mục để cập nhật số liệu.
    tvCategoriesTotal = view.findViewById(R.id.tvCategoriesTotal);
    // Ánh xạ TextView thời gian cập nhật để hiển thị mốc thời gian gần nhất.
    tvCategoriesUpdated = view.findViewById(R.id.tvCategoriesUpdated);
    // Ánh xạ TextView tiêu đề empty state để tùy biến thông điệp.
    tvCategoryEmptyTitle = view.findViewById(R.id.tvCategoryEmptyTitle);
    // Ánh xạ TextView mô tả empty state để hướng dẫn người dùng.
    tvCategoryEmptySubtitle = view.findViewById(R.id.tvCategoryEmptySubtitle);

    // Thiết lập layout manager dọc để hiển thị danh sách theo cột.
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    // Khởi tạo adapter với callback để xử lý thao tác người dùng.
    adapter = new AdminCategoriesAdapter(new AdminCategoriesAdapter.Callback() {
      // Xử lý khi admin chọn sửa danh mục.
      @Override
      public void onEdit(CategoryEntity category) {
        // Mở dialog chỉnh sửa với dữ liệu hiện tại.
        showCategoryDialog(category);
      }

      // Xử lý khi admin chọn xóa danh mục.
      @Override
      public void onDelete(CategoryEntity category) {
        // Hiển thị hộp thoại xác nhận xóa.
        confirmDelete(category);
      }
    });
    // Gắn adapter vào RecyclerView để hiển thị dữ liệu.
    recyclerView.setAdapter(adapter);

    // Thiết lập ô tìm kiếm để lọc danh mục theo từ khóa.
    TextInputEditText edtSearch = view.findViewById(R.id.edtSearchCategories);
    // Kiểm tra null để tránh crash nếu view không tồn tại.
    if (edtSearch != null) {
      // Lắng nghe thay đổi văn bản để cập nhật bộ lọc tức thời.
      edtSearch.addTextChangedListener(new TextWatcher() {
        // Không cần xử lý trước khi thay đổi.
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        // Cập nhật truy vấn tìm kiếm mỗi khi người dùng nhập.
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          // Lưu chuỗi tìm kiếm hiện tại đã loại bỏ khoảng trắng.
          currentQuery = s == null ? "" : s.toString().trim();
          // Áp dụng bộ lọc để làm mới danh sách theo từ khóa.
          applyCategoryFilters();
        }
        // Không cần xử lý sau khi thay đổi.
        @Override public void afterTextChanged(Editable s) {}
      });
    }

    // Lấy nút nổi thêm danh mục để mở form tạo mới.
    ExtendedFloatingActionButton fab = view.findViewById(R.id.fabAddCategory);
    // Khi bấm nút nổi sẽ mở dialog tạo danh mục.
    fab.setOnClickListener(v -> openCreateCategoryDialog());

    // Lấy nút thêm trong trạng thái rỗng để hỗ trợ thao tác nhanh.
    Button btnAddCategory = view.findViewById(R.id.btnAddCategoryFromEmpty);
    // Kiểm tra null vì view chỉ tồn tại trong layout empty state.
    if (btnAddCategory != null) {
      // Khi bấm nút sẽ mở dialog tạo danh mục.
      btnAddCategory.setOnClickListener(v -> openCreateCategoryDialog());
    }

    // Lấy ViewModel dùng chung trong fragment để theo dõi dữ liệu.
    vm = new ViewModelProvider(this).get(AdminCategoriesVM.class);
    // Quan sát danh sách danh mục để cập nhật giao diện mỗi khi dữ liệu đổi.
    vm.categories.observe(getViewLifecycleOwner(), list -> {
      // Xóa danh sách cũ để đồng bộ dữ liệu mới.
      allCategories.clear();
      // Thêm toàn bộ danh mục nhận được vào bộ nhớ tạm.
      if (list != null) allCategories.addAll(list);
      // Cập nhật thông tin tổng quan như số lượng và thời gian cập nhật.
      updateSummary(list);
      // Áp dụng bộ lọc hiện tại để hiển thị đúng danh sách.
      applyCategoryFilters();
    });
  }

  // Hiển thị dialog tạo hoặc chỉnh sửa danh mục.
  private void showCategoryDialog(@Nullable CategoryEntity editing) {
    // Tạo dialog với dữ liệu hiện tại nếu đang chỉnh sửa.
    AdminCategoryDialog dialog = AdminCategoryDialog.newInstance(editing);
    // Lắng nghe kết quả từ dialog để lưu danh mục.
    dialog.setListener((name, description) -> saveCategory(name, description, editing));
    // Hiển thị dialog sử dụng FragmentManager con.
    dialog.show(getChildFragmentManager(), "category_dialog");
  }

  // Mở dialog ở chế độ tạo mới danh mục.
  private void openCreateCategoryDialog() {
    // Truyền null để dialog hiểu rằng cần tạo danh mục mới.
    showCategoryDialog(null);
  }

  // Lưu danh mục mới hoặc cập nhật danh mục có sẵn.
  private void saveCategory(String name, String description, @Nullable CategoryEntity editing) {
    // Tạo entity mới để chuẩn bị ghi xuống cơ sở dữ liệu.
    CategoryEntity entity = new CategoryEntity();
    // Nếu đang chỉnh sửa thì giữ nguyên id và thời gian tạo.
    if (editing != null) {
      entity.categoryId = editing.categoryId;
      entity.createdAt = editing.createdAt;
    } else {
      // Nếu tạo mới thì gán thời gian tạo hiện tại.
      entity.createdAt = System.currentTimeMillis();
    }
    // Gán tên danh mục với dữ liệu người dùng nhập.
    entity.name = name;
    // Gán mô tả danh mục với dữ liệu người dùng nhập.
    entity.description = description;

    // Yêu cầu ViewModel lưu entity và nhận callback kết quả.
    vm.save(entity, new AdminCategoriesVM.ActionCallback() {
      // Callback chạy khi lưu thành công.
      @Override
      public void onSuccess() {
        // Chỉ phản hồi nếu fragment vẫn gắn với activity.
        if (!isAdded()) return;
        // Thông báo cho admin biết hành động đã hoàn tất.
        Toast.makeText(requireContext(), editing == null ? "Category created" : "Category updated", Toast.LENGTH_SHORT).show();
      }

      // Callback chạy khi lưu thất bại.
      @Override
      public void onError(Throwable throwable) {
        // Tránh hiển thị khi fragment đã tách.
        if (!isAdded()) return;
        // Thông báo lỗi để admin biết nguyên nhân.
        Toast.makeText(requireContext(), "Unable to save: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  // Hiển thị hộp thoại xác nhận trước khi xóa danh mục.
  private void confirmDelete(@Nullable CategoryEntity category) {
    // Nếu thiếu dữ liệu hoặc context không sẵn sàng thì dừng lại.
    if (category == null || getContext() == null) return;
    // Tạo dialog xác nhận bằng Material Design.
    new MaterialAlertDialogBuilder(requireContext())
        // Đặt tiêu đề cho hộp thoại xác nhận.
        .setTitle("Delete category")
        // Hiển thị thông điệp kèm tên danh mục.
        .setMessage("Are you sure you want to delete \"" + category.name + "\"?")
        // Khi xác nhận xóa sẽ gọi phương thức deleteCategory.
        .setPositiveButton("Delete", (dialog, which) -> deleteCategory(category))
        // Cho phép hủy thao tác mà không làm gì thêm.
        .setNegativeButton("Cancel", null)
        // Hiển thị dialog lên màn hình.
        .show();
  }

  // Xóa danh mục thông qua ViewModel và phản hồi kết quả.
  private void deleteCategory(CategoryEntity category) {
    // Gọi ViewModel thực hiện xóa và nhận callback.
    vm.delete(category, new AdminCategoriesVM.ActionCallback() {
      // Thông báo khi xóa thành công.
      @Override
      public void onSuccess() {
        // Đảm bảo fragment vẫn gắn với activity trước khi hiển thị Toast.
        if (!isAdded()) return;
        // Cho admin biết danh mục đã bị xóa.
        Toast.makeText(requireContext(), "Category removed", Toast.LENGTH_SHORT).show();
      }

      // Thông báo khi xóa thất bại.
      @Override
      public void onError(Throwable throwable) {
        // Kiểm tra fragment đã gắn để tránh crash.
        if (!isAdded()) return;
        // Thông báo lỗi chi tiết giúp admin biết nguyên nhân.
        Toast.makeText(requireContext(), "Delete failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  // Áp dụng bộ lọc tìm kiếm và cập nhật giao diện tương ứng.
  private void applyCategoryFilters() {
    // Nếu chưa có dữ liệu thì hiển thị empty state ngay.
    if (allCategories.isEmpty()) {
      // Đưa danh sách trống cho adapter để dọn giao diện.
      adapter.submit(allCategories);
      // Bật empty state và đánh dấu không phải do tìm kiếm.
      toggleEmptyState(true, false);
      return;
    }
    // Chuẩn hóa truy vấn tìm kiếm để so khớp không phân biệt hoa thường.
    String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());
    // Nếu không có truy vấn thì hiển thị toàn bộ danh mục.
    if (query.isEmpty()) {
      // Nạp lại danh sách đầy đủ vào adapter.
      adapter.submit(allCategories);
      // Tắt empty state vì có dữ liệu.
      toggleEmptyState(false, false);
      return;
    }
    // Tạo danh sách mới chứa các danh mục phù hợp.
    List<CategoryEntity> filtered = new ArrayList<>();
    // Duyệt từng danh mục để kiểm tra điều kiện tìm kiếm.
    for (CategoryEntity category : allCategories) {
      // Chuẩn hóa tên để so sánh với truy vấn.
      String name = category.name == null ? "" : category.name.toLowerCase(Locale.getDefault());
      // Chuẩn hóa mô tả để so sánh với truy vấn.
      String desc = category.description == null ? "" : category.description.toLowerCase(Locale.getDefault());
      // Nếu tên hoặc mô tả chứa truy vấn thì thêm vào danh sách kết quả.
      if (name.contains(query) || desc.contains(query)) {
        filtered.add(category);
      }
    }
    // Cập nhật adapter với danh sách đã lọc.
    adapter.submit(filtered);
    // Điều chỉnh empty state dựa trên kết quả lọc.
    toggleEmptyState(filtered.isEmpty(), !query.isEmpty());
  }

  // Hiển thị hoặc ẩn empty state tùy theo dữ liệu hiện có.
  private void toggleEmptyState(boolean showEmpty, boolean fromSearch) {
    // Nếu thiếu view cần thiết thì không làm gì để tránh lỗi.
    if (emptyStateContainer == null || recyclerView == null) return;
    // Điều chỉnh hiển thị của empty state và danh sách.
    emptyStateContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    // Nếu không ở trạng thái rỗng hoặc thiếu TextView thì dừng lại.
    if (!showEmpty || tvCategoryEmptyTitle == null || tvCategoryEmptySubtitle == null) return;
    // Khi không có danh mục nào hãy hướng dẫn tạo mới.
    if (allCategories.isEmpty()) {
      tvCategoryEmptyTitle.setText("No categories yet");
      tvCategoryEmptySubtitle.setText("Organize drinks by creating a category.");
    } else if (fromSearch) {
      // Khi tìm kiếm không ra kết quả hãy gợi ý đổi từ khóa.
      tvCategoryEmptyTitle.setText("Nothing matches your search");
      tvCategoryEmptySubtitle.setText("Try another keyword or clear the filter.");
    } else {
      // Trường hợp khác hiển thị thông điệp mặc định.
      tvCategoryEmptyTitle.setText("No categories available");
      tvCategoryEmptySubtitle.setText("Use the button below to add one.");
    }
  }

  // Cập nhật các chỉ số tổng quan ở đầu màn hình.
  private void updateSummary(@Nullable List<CategoryEntity> list) {
    // Tính tổng số danh mục hiện có.
    int count = list == null ? 0 : list.size();
    // Nếu view tổng số tồn tại thì hiển thị số lượng cùng đơn vị.
    if (tvCategoriesTotal != null) {
      tvCategoriesTotal.setText(String.format(Locale.getDefault(), "%d %s", count,
          count == 1 ? "category" : "categories"));
    }
    // Nếu view thời gian cập nhật tồn tại thì hiển thị giá trị phù hợp.
    if (tvCategoriesUpdated != null) {
      // Khi danh sách rỗng hiển thị placeholder.
      if (list == null || list.isEmpty()) {
        tvCategoriesUpdated.setText("--");
      } else {
        // Duyệt tìm thời điểm tạo mới nhất trong danh sách.
        long lastUpdated = 0;
        for (CategoryEntity c : list) {
          if (c.createdAt > lastUpdated) lastUpdated = c.createdAt;
        }
        // Định dạng thời gian hoặc đặt placeholder nếu không hợp lệ.
        String formatted = lastUpdated <= 0
            ? "--"
            : lastUpdatedFormat.format(new Date(lastUpdated));
        // Hiển thị thời gian cập nhật cuối cùng.
        tvCategoriesUpdated.setText(formatted);
      }
    }
  }
}
