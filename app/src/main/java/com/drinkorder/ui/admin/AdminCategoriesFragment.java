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
// Import com.google.android.material.dialog.MaterialAlertDialogBuilder để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
// Import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
// Import com.google.android.material.textfield.TextInputEditText để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.textfield.TextInputEditText;

// Import java.text.SimpleDateFormat để sử dụng các lớp hoặc hàm tương ứng.
import java.text.SimpleDateFormat;
// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.Date để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Date;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp AdminCategoriesFragment kế thừa Fragment.
public class AdminCategoriesFragment extends Fragment {

  // Khai báo thuộc tính với phạm vi truy cập: private AdminCategoriesVM vm.
  private AdminCategoriesVM vm;
  // Khai báo thuộc tính với phạm vi truy cập: private AdminCategoriesAdapter adapter.
  private AdminCategoriesAdapter adapter;
  // Khai báo thuộc tính với phạm vi truy cập: private RecyclerView recyclerView.
  private RecyclerView recyclerView;
  // Khai báo thuộc tính với phạm vi truy cập: private View emptyStateContainer.
  private View emptyStateContainer;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvCategoriesTotal.
  private TextView tvCategoriesTotal;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvCategoriesUpdated.
  private TextView tvCategoriesUpdated;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvCategoryEmptyTitle.
  private TextView tvCategoryEmptyTitle;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvCategoryEmptySubtitle.
  private TextView tvCategoryEmptySubtitle;
  // Khai báo thuộc tính với phạm vi truy cập: private final List<CategoryEntity> allCategories = new ArrayList<>().
  private final List<CategoryEntity> allCategories = new ArrayList<>();
  // Thực thi câu lệnh: private final SimpleDateFormat lastUpdatedFormat =.
  private final SimpleDateFormat lastUpdatedFormat =
      // Khởi tạo đối tượng mới với biểu thức new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());.
      new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
  // Khai báo thuộc tính với phạm vi truy cập: private String currentQuery = "".
  private String currentQuery = "";

  // Áp dụng annotation @Nullable cho phần tử bên dưới.
  @Nullable
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Trả về kết quả inflater.inflate(R.layout.fragment_admin_categories, container, false);.
    return inflater.inflate(R.layout.fragment_admin_categories, container, false);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(view, savedInstanceState);.
    super.onViewCreated(view, savedInstanceState);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView = view.findViewById(R.id.rvAdminCategories);.
    recyclerView = view.findViewById(R.id.rvAdminCategories);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: emptyStateContainer = view.findViewById(R.id.categoryEmptyState);.
    emptyStateContainer = view.findViewById(R.id.categoryEmptyState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoriesTotal = view.findViewById(R.id.tvCategoriesTotal);.
    tvCategoriesTotal = view.findViewById(R.id.tvCategoriesTotal);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoriesUpdated = view.findViewById(R.id.tvCategoriesUpdated);.
    tvCategoriesUpdated = view.findViewById(R.id.tvCategoriesUpdated);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryEmptyTitle = view.findViewById(R.id.tvCategoryEmptyTitle);.
    tvCategoryEmptyTitle = view.findViewById(R.id.tvCategoryEmptyTitle);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryEmptySubtitle = view.findViewById(R.id.tvCategoryEmptySubtitle);.
    tvCategoryEmptySubtitle = view.findViewById(R.id.tvCategoryEmptySubtitle);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));.
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    adapter = new AdminCategoriesAdapter(new AdminCategoriesAdapter.Callback() {
      // Áp dụng annotation @Override cho phần tử bên dưới.
      @Override
      // Định nghĩa phương thức onEdit với phạm vi truy cập tương ứng.
      public void onEdit(CategoryEntity category) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: showCategoryDialog(category);.
        showCategoryDialog(category);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }

      // Áp dụng annotation @Override cho phần tử bên dưới.
      @Override
      // Định nghĩa phương thức onDelete với phạm vi truy cập tương ứng.
      public void onDelete(CategoryEntity category) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: confirmDelete(category);.
        confirmDelete(category);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setAdapter(adapter);.
    recyclerView.setAdapter(adapter);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: TextInputEditText edtSearch = view.findViewById(R.id.edtSearchCategories);.
    TextInputEditText edtSearch = view.findViewById(R.id.edtSearchCategories);
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
          // Thực hiện lời gọi phương thức hoặc khởi tạo: applyCategoryFilters();.
          applyCategoryFilters();
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Áp dụng annotation @Override và ghi đè phương thức afterTextChanged.
        @Override public void afterTextChanged(Editable s) {}
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: ExtendedFloatingActionButton fab = view.findViewById(R.id.fabAddCategory);.
    ExtendedFloatingActionButton fab = view.findViewById(R.id.fabAddCategory);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: fab.setOnClickListener(v -> openCreateCategoryDialog());.
    fab.setOnClickListener(v -> openCreateCategoryDialog());

    // Thực hiện lời gọi phương thức hoặc khởi tạo: Button btnAddCategory = view.findViewById(R.id.btnAddCategoryFromEmpty);.
    Button btnAddCategory = view.findViewById(R.id.btnAddCategoryFromEmpty);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (btnAddCategory != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnAddCategory.setOnClickListener(v -> openCreateCategoryDialog());.
      btnAddCategory.setOnClickListener(v -> openCreateCategoryDialog());
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(this).get(AdminCategoriesVM.class);.
    vm = new ViewModelProvider(this).get(AdminCategoriesVM.class);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.categories.observe(getViewLifecycleOwner(), list -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: allCategories.clear();.
      allCategories.clear();
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (list != null) allCategories.addAll(list);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: updateSummary(list);.
      updateSummary(list);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: applyCategoryFilters();.
      applyCategoryFilters();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức showCategoryDialog với phạm vi truy cập tương ứng.
  private void showCategoryDialog(@Nullable CategoryEntity editing) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: AdminCategoryDialog dialog = AdminCategoryDialog.newInstance(editing);.
    AdminCategoryDialog dialog = AdminCategoryDialog.newInstance(editing);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: dialog.setListener((name, description) -> saveCategory(name, description, editing));.
    dialog.setListener((name, description) -> saveCategory(name, description, editing));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: dialog.show(getChildFragmentManager(), "category_dialog");.
    dialog.show(getChildFragmentManager(), "category_dialog");
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức openCreateCategoryDialog với phạm vi truy cập tương ứng.
  private void openCreateCategoryDialog() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: showCategoryDialog(null);.
    showCategoryDialog(null);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức saveCategory với phạm vi truy cập tương ứng.
  private void saveCategory(String name, String description, @Nullable CategoryEntity editing) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: CategoryEntity entity = new CategoryEntity();.
    CategoryEntity entity = new CategoryEntity();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (editing != null) {
      // Gán giá trị cho biến hoặc thuộc tính: entity.categoryId = editing.categoryId.
      entity.categoryId = editing.categoryId;
      // Gán giá trị cho biến hoặc thuộc tính: entity.createdAt = editing.createdAt.
      entity.createdAt = editing.createdAt;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: entity.createdAt = System.currentTimeMillis();.
      entity.createdAt = System.currentTimeMillis();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Gán giá trị cho biến hoặc thuộc tính: entity.name = name.
    entity.name = name;
    // Gán giá trị cho biến hoặc thuộc tính: entity.description = description.
    entity.description = description;

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.save(entity, new AdminCategoriesVM.ActionCallback() {
      // Áp dụng annotation @Override cho phần tử bên dưới.
      @Override
      // Định nghĩa phương thức onSuccess với phạm vi truy cập tương ứng.
      public void onSuccess() {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (!isAdded()) return;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(requireContext(), editing == null ? "Category created" : "Category updated", Toast.LENGTH_SHORT).show();.
        Toast.makeText(requireContext(), editing == null ? "Category created" : "Category updated", Toast.LENGTH_SHORT).show();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }

      // Áp dụng annotation @Override cho phần tử bên dưới.
      @Override
      // Định nghĩa phương thức onError với phạm vi truy cập tương ứng.
      public void onError(Throwable throwable) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (!isAdded()) return;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(requireContext(), "Unable to save: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();.
        Toast.makeText(requireContext(), "Unable to save: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức confirmDelete với phạm vi truy cập tương ứng.
  private void confirmDelete(@Nullable CategoryEntity category) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (category == null || getContext() == null) return;
    // Khởi tạo đối tượng mới với biểu thức new MaterialAlertDialogBuilder(requireContext()).
    new MaterialAlertDialogBuilder(requireContext())
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setTitle("Delete category").
        .setTitle("Delete category")
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setMessage("Are you sure you want to delete \"" + category.name + "\"?").
        .setMessage("Are you sure you want to delete \"" + category.name + "\"?")
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setPositiveButton("Delete", (dialog, which) -> deleteCategory(category)).
        .setPositiveButton("Delete", (dialog, which) -> deleteCategory(category))
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setNegativeButton("Cancel", null).
        .setNegativeButton("Cancel", null)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .show();.
        .show();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức deleteCategory với phạm vi truy cập tương ứng.
  private void deleteCategory(CategoryEntity category) {
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.delete(category, new AdminCategoriesVM.ActionCallback() {
      // Áp dụng annotation @Override cho phần tử bên dưới.
      @Override
      // Định nghĩa phương thức onSuccess với phạm vi truy cập tương ứng.
      public void onSuccess() {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (!isAdded()) return;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(requireContext(), "Category removed", Toast.LENGTH_SHORT).show();.
        Toast.makeText(requireContext(), "Category removed", Toast.LENGTH_SHORT).show();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }

      // Áp dụng annotation @Override cho phần tử bên dưới.
      @Override
      // Định nghĩa phương thức onError với phạm vi truy cập tương ứng.
      public void onError(Throwable throwable) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (!isAdded()) return;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(requireContext(), "Delete failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();.
        Toast.makeText(requireContext(), "Delete failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức applyCategoryFilters với phạm vi truy cập tương ứng.
  private void applyCategoryFilters() {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (allCategories.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submit(allCategories);.
      adapter.submit(allCategories);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: toggleEmptyState(true, false);.
      toggleEmptyState(true, false);
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());.
    String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (query.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submit(allCategories);.
      adapter.submit(allCategories);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: toggleEmptyState(false, false);.
      toggleEmptyState(false, false);
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: List<CategoryEntity> filtered = new ArrayList<>();.
    List<CategoryEntity> filtered = new ArrayList<>();
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (CategoryEntity category : allCategories) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String name = category.name == null ? "" : category.name.toLowerCase(Locale.getDefault());.
      String name = category.name == null ? "" : category.name.toLowerCase(Locale.getDefault());
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String desc = category.description == null ? "" : category.description.toLowerCase(Locale.getDefault());.
      String desc = category.description == null ? "" : category.description.toLowerCase(Locale.getDefault());
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (name.contains(query) || desc.contains(query)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: filtered.add(category);.
        filtered.add(category);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submit(filtered);.
    adapter.submit(filtered);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: toggleEmptyState(filtered.isEmpty(), !query.isEmpty());.
    toggleEmptyState(filtered.isEmpty(), !query.isEmpty());
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức toggleEmptyState với phạm vi truy cập tương ứng.
  private void toggleEmptyState(boolean showEmpty, boolean fromSearch) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (emptyStateContainer == null || recyclerView == null) return;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: emptyStateContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);.
    emptyStateContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);.
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!showEmpty || tvCategoryEmptyTitle == null || tvCategoryEmptySubtitle == null) return;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (allCategories.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryEmptyTitle.setText("No categories yet");.
      tvCategoryEmptyTitle.setText("No categories yet");
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryEmptySubtitle.setText("Organize drinks by creating a category.");.
      tvCategoryEmptySubtitle.setText("Organize drinks by creating a category.");
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (fromSearch) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryEmptyTitle.setText("Nothing matches your search");.
      tvCategoryEmptyTitle.setText("Nothing matches your search");
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryEmptySubtitle.setText("Try another keyword or clear the filter.");.
      tvCategoryEmptySubtitle.setText("Try another keyword or clear the filter.");
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryEmptyTitle.setText("No categories available");.
      tvCategoryEmptyTitle.setText("No categories available");
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoryEmptySubtitle.setText("Use the button below to add one.");.
      tvCategoryEmptySubtitle.setText("Use the button below to add one.");
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức updateSummary với phạm vi truy cập tương ứng.
  private void updateSummary(@Nullable List<CategoryEntity> list) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: int count = list == null ? 0 : list.size();.
    int count = list == null ? 0 : list.size();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (tvCategoriesTotal != null) {
      // Thực thi câu lệnh: tvCategoriesTotal.setText(String.format(Locale.getDefault(), "%d %s", count,.
      tvCategoriesTotal.setText(String.format(Locale.getDefault(), "%d %s", count,
          // Thực hiện lời gọi phương thức hoặc khởi tạo: count == 1 ? "category" : "categories"));.
          count == 1 ? "category" : "categories"));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (tvCategoriesUpdated != null) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (list == null || list.isEmpty()) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoriesUpdated.setText("--");.
        tvCategoriesUpdated.setText("--");
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Gán giá trị cho biến hoặc thuộc tính: long lastUpdated = 0.
        long lastUpdated = 0;
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (CategoryEntity c : list) {
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (c.createdAt > lastUpdated) lastUpdated = c.createdAt;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Thực thi câu lệnh: String formatted = lastUpdated <= 0.
        String formatted = lastUpdated <= 0
            // Thực thi câu lệnh: ? "--".
            ? "--"
            // Thực hiện lời gọi phương thức hoặc khởi tạo: : lastUpdatedFormat.format(new Date(lastUpdated));.
            : lastUpdatedFormat.format(new Date(lastUpdated));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategoriesUpdated.setText(formatted);.
        tvCategoriesUpdated.setText(formatted);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
