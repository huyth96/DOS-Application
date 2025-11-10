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
// Import com.drinkorder.data.db.entity.UserEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.UserEntity;
// Import com.google.android.material.dialog.MaterialAlertDialogBuilder để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
// Import com.google.android.material.textfield.TextInputEditText để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.textfield.TextInputEditText;

// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp AdminUsersFragment kế thừa Fragment.
public class AdminUsersFragment extends Fragment {

  // Khai báo thuộc tính với phạm vi truy cập: private AdminUsersVM vm.
  private AdminUsersVM vm;
  // Khai báo thuộc tính với phạm vi truy cập: private AdminUsersAdapter adapter.
  private AdminUsersAdapter adapter;
  // Khai báo thuộc tính với phạm vi truy cập: private RecyclerView recyclerView.
  private RecyclerView recyclerView;
  // Khai báo thuộc tính với phạm vi truy cập: private View emptyState.
  private View emptyState;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvTotal.
  private TextView tvTotal;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvBanned.
  private TextView tvBanned;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvEmptyTitle.
  private TextView tvEmptyTitle;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvEmptySubtitle.
  private TextView tvEmptySubtitle;
  // Khai báo thuộc tính với phạm vi truy cập: private final List<UserEntity> allUsers = new ArrayList<>().
  private final List<UserEntity> allUsers = new ArrayList<>();
  // Khai báo thuộc tính với phạm vi truy cập: private String currentQuery = "".
  private String currentQuery = "";

  // Áp dụng annotation @Nullable cho phần tử bên dưới.
  @Nullable
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           // Áp dụng annotation @Nullable cho phần tử bên dưới.
                           @Nullable Bundle savedInstanceState) {
    // Trả về kết quả inflater.inflate(R.layout.fragment_admin_users, container, false);.
    return inflater.inflate(R.layout.fragment_admin_users, container, false);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(view, savedInstanceState);.
    super.onViewCreated(view, savedInstanceState);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView = view.findViewById(R.id.rvAdminUsers);.
    recyclerView = view.findViewById(R.id.rvAdminUsers);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: emptyState = view.findViewById(R.id.userEmptyState);.
    emptyState = view.findViewById(R.id.userEmptyState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTotal = view.findViewById(R.id.tvUsersTotal);.
    tvTotal = view.findViewById(R.id.tvUsersTotal);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvBanned = view.findViewById(R.id.tvUsersBanned);.
    tvBanned = view.findViewById(R.id.tvUsersBanned);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyTitle = view.findViewById(R.id.tvUserEmptyTitle);.
    tvEmptyTitle = view.findViewById(R.id.tvUserEmptyTitle);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptySubtitle = view.findViewById(R.id.tvUserEmptySubtitle);.
    tvEmptySubtitle = view.findViewById(R.id.tvUserEmptySubtitle);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));.
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter = new AdminUsersAdapter(this::confirmBanToggle);.
    adapter = new AdminUsersAdapter(this::confirmBanToggle);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setAdapter(adapter);.
    recyclerView.setAdapter(adapter);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: TextInputEditText edtSearch = view.findViewById(R.id.edtSearchUsers);.
    TextInputEditText edtSearch = view.findViewById(R.id.edtSearchUsers);
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

    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(this).get(AdminUsersVM.class);.
    vm = new ViewModelProvider(this).get(AdminUsersVM.class);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.users.observe(getViewLifecycleOwner(), list -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: allUsers.clear();.
      allUsers.clear();
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (list != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: allUsers.addAll(list);.
        allUsers.addAll(list);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: updateSummary();.
      updateSummary();
      // Thực hiện lời gọi phương thức hoặc khởi tạo: applyFilters();.
      applyFilters();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức updateSummary với phạm vi truy cập tương ứng.
  private void updateSummary() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: int total = allUsers.size();.
    int total = allUsers.size();
    // Gán giá trị cho biến hoặc thuộc tính: int banned = 0.
    int banned = 0;
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (UserEntity user : allUsers) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (user.isBanned) banned++;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (tvTotal != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTotal.setText(String.valueOf(total));.
      tvTotal.setText(String.valueOf(total));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (tvBanned != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvBanned.setText(String.valueOf(banned));.
      tvBanned.setText(String.valueOf(banned));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức applyFilters với phạm vi truy cập tương ứng.
  private void applyFilters() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());.
    String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (allUsers.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submit(allUsers);.
      adapter.submit(allUsers);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: toggleEmptyState(true, false);.
      toggleEmptyState(true, false);
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (query.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submit(allUsers);.
      adapter.submit(allUsers);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: toggleEmptyState(false, false);.
      toggleEmptyState(false, false);
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: List<UserEntity> filtered = new ArrayList<>();.
    List<UserEntity> filtered = new ArrayList<>();
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (UserEntity user : allUsers) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String name = safeLower(user.fullName);.
      String name = safeLower(user.fullName);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String username = safeLower(user.username);.
      String username = safeLower(user.username);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String email = safeLower(user.email);.
      String email = safeLower(user.email);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String phone = safeLower(user.phone);.
      String phone = safeLower(user.phone);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (name.contains(query) || username.contains(query) || email.contains(query) || phone.contains(query)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: filtered.add(user);.
        filtered.add(user);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submit(filtered);.
    adapter.submit(filtered);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: toggleEmptyState(filtered.isEmpty(), true);.
    toggleEmptyState(filtered.isEmpty(), true);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức safeLower với phạm vi truy cập tương ứng.
  private String safeLower(String value) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (value == null) return "";
    // Trả về kết quả value.toLowerCase(Locale.getDefault());.
    return value.toLowerCase(Locale.getDefault());
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức toggleEmptyState với phạm vi truy cập tương ứng.
  private void toggleEmptyState(boolean showEmpty, boolean fromSearch) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (recyclerView == null || emptyState == null) return;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);.
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: emptyState.setVisibility(showEmpty ? View.VISIBLE : View.GONE);.
    emptyState.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!showEmpty || tvEmptyTitle == null || tvEmptySubtitle == null) return;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (allUsers.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyTitle.setText(R.string.admin_user_empty_all);.
      tvEmptyTitle.setText(R.string.admin_user_empty_all);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptySubtitle.setText(R.string.admin_user_empty_all_subtitle);.
      tvEmptySubtitle.setText(R.string.admin_user_empty_all_subtitle);
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (fromSearch) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyTitle.setText(R.string.admin_user_empty_filtered);.
      tvEmptyTitle.setText(R.string.admin_user_empty_filtered);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptySubtitle.setText(R.string.admin_user_empty_filtered_subtitle);.
      tvEmptySubtitle.setText(R.string.admin_user_empty_filtered_subtitle);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức confirmBanToggle với phạm vi truy cập tương ứng.
  private void confirmBanToggle(UserEntity user) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (user == null || getContext() == null) return;
    // Gán giá trị cho biến hoặc thuộc tính: boolean ban = !user.isBanned.
    boolean ban = !user.isBanned;
    // Gán giá trị cho biến hoặc thuộc tính: int title = ban ? R.string.admin_user_confirm_ban_title : R.string.admin_user_confirm_unban_title.
    int title = ban ? R.string.admin_user_confirm_ban_title : R.string.admin_user_confirm_unban_title;
    // Gán giá trị cho biến hoặc thuộc tính: int message = ban ? R.string.admin_user_confirm_ban_message : R.string.admin_user_confirm_unban_message.
    int message = ban ? R.string.admin_user_confirm_ban_message : R.string.admin_user_confirm_unban_message;
    // Khởi tạo đối tượng mới với biểu thức new MaterialAlertDialogBuilder(requireContext()).
    new MaterialAlertDialogBuilder(requireContext())
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setTitle(title).
        .setTitle(title)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setMessage(message).
        .setMessage(message)
        // Thực thi câu lệnh: .setPositiveButton(ban ? R.string.admin_user_action_ban : R.string.admin_user_action_unban,.
        .setPositiveButton(ban ? R.string.admin_user_action_ban : R.string.admin_user_action_unban,
            // Thực hiện lời gọi phương thức hoặc khởi tạo: (dialog, which) -> updateBanStatus(user, ban)).
            (dialog, which) -> updateBanStatus(user, ban))
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setNegativeButton(android.R.string.cancel, null).
        .setNegativeButton(android.R.string.cancel, null)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .show();.
        .show();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức updateBanStatus với phạm vi truy cập tương ứng.
  private void updateBanStatus(UserEntity user, boolean ban) {
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.setBanStatus(user, ban, new AdminUsersVM.ActionCallback() {
      // Áp dụng annotation @Override và ghi đè phương thức onSuccess.
      @Override public void onSuccess() {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (!isAdded()) return;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(requireContext(), R.string.admin_user_ban_success, Toast.LENGTH_SHORT).show();.
        Toast.makeText(requireContext(), R.string.admin_user_ban_success, Toast.LENGTH_SHORT).show();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }

      // Áp dụng annotation @Override và ghi đè phương thức onError.
      @Override public void onError(Throwable throwable) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (!isAdded()) return;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: String msg = throwable == null ? "Unknown error" : throwable.getMessage();.
        String msg = throwable == null ? "Unknown error" : throwable.getMessage();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(requireContext(), getString(R.string.admin_user_ban_error, msg), Toast.LENGTH_SHORT).show();.
        Toast.makeText(requireContext(), getString(R.string.admin_user_ban_error, msg), Toast.LENGTH_SHORT).show();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
