package com.drinkorder.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.UserEntity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Màn hình quản lý người dùng cho admin:
 * - Xem danh sách tất cả user.
 * - Tìm kiếm theo tên, username, email hoặc số điện thoại.
 * - Ban / Unban tài khoản người dùng (trừ admin).
 */
public class AdminUsersFragment extends Fragment {

  private AdminUsersVM vm;                 // ViewModel xử lý dữ liệu
  private AdminUsersAdapter adapter;       // Adapter hiển thị danh sách user
  private RecyclerView recyclerView;       // Danh sách người dùng
  private View emptyState;                 // View hiển thị khi danh sách trống
  private TextView tvTotal, tvBanned;      // Số lượng user tổng và bị ban
  private TextView tvEmptyTitle, tvEmptySubtitle;
  private final List<UserEntity> allUsers = new ArrayList<>(); // Tất cả user
  private String currentQuery = "";        // Từ khóa tìm kiếm hiện tại

  /** Tạo layout cho fragment. */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    // Inflate layout XML thành View hiển thị
    return inflater.inflate(R.layout.fragment_admin_users, container, false);
  }

  /** Khi view đã được tạo xong, ta thiết lập giao diện và dữ liệu. */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Gán các view từ layout
    recyclerView = view.findViewById(R.id.rvAdminUsers);
    emptyState = view.findViewById(R.id.userEmptyState);
    tvTotal = view.findViewById(R.id.tvUsersTotal);
    tvBanned = view.findViewById(R.id.tvUsersBanned);
    tvEmptyTitle = view.findViewById(R.id.tvUserEmptyTitle);
    tvEmptySubtitle = view.findViewById(R.id.tvUserEmptySubtitle);

    // Thiết lập danh sách hiển thị theo chiều dọc
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

    // Khởi tạo adapter và gắn vào RecyclerView
    adapter = new AdminUsersAdapter(this::confirmBanToggle);
    recyclerView.setAdapter(adapter);

    // Thiết lập tìm kiếm người dùng
    TextInputEditText edtSearch = view.findViewById(R.id.edtSearchUsers);
    if (edtSearch != null) {
      // Theo dõi khi người dùng nhập vào ô tìm kiếm
      edtSearch.addTextChangedListener(new TextWatcher() {
        /** Trước khi text thay đổi – không cần làm gì. */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        /** Khi người dùng đang gõ, ta cập nhật từ khóa tìm kiếm. */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          currentQuery = (s == null) ? "" : s.toString().trim();
          applyFilters(); // lọc danh sách theo từ khóa
        }

        /** Sau khi text thay đổi – không cần làm gì thêm. */
        @Override
        public void afterTextChanged(Editable s) {}
      });
    }

    // Khởi tạo ViewModel
    vm = new ViewModelProvider(this).get(AdminUsersVM.class);

    // Quan sát dữ liệu user từ ViewModel (LiveData)
    vm.users.observe(getViewLifecycleOwner(), list -> {
      allUsers.clear();
      if (list != null) allUsers.addAll(list);
      updateSummary(); // cập nhật tổng số user và số bị ban
      applyFilters();  // áp dụng tìm kiếm hiện tại
    });
  }

  /** Cập nhật số lượng tổng user và số user bị ban. */
  private void updateSummary() {
    int total = allUsers.size();
    int banned = 0;
    for (UserEntity u : allUsers) {
      if (u.isBanned) banned++;
    }
    tvTotal.setText(String.valueOf(total));
    tvBanned.setText(String.valueOf(banned));
  }

  /** Lọc danh sách người dùng theo từ khóa đang nhập. */
  private void applyFilters() {
    String query = (currentQuery == null) ? "" : currentQuery.toLowerCase(Locale.getDefault());

    // Nếu chưa có dữ liệu
    if (allUsers.isEmpty()) {
      adapter.submit(allUsers);
      toggleEmptyState(true, false);
      return;
    }

    // Nếu không có từ khóa -> hiển thị toàn bộ user
    if (query.isEmpty()) {
      adapter.submit(allUsers);
      toggleEmptyState(false, false);
      return;
    }

    // Lọc danh sách user
    List<UserEntity> filtered = new ArrayList<>();
    for (UserEntity u : allUsers) {
      String name = safeLower(u.fullName);
      String username = safeLower(u.username);
      String email = safeLower(u.email);
      String phone = safeLower(u.phone);

      if (name.contains(query) || username.contains(query)
              || email.contains(query) || phone.contains(query)) {
        filtered.add(u);
      }
    }

    adapter.submit(filtered);
    toggleEmptyState(filtered.isEmpty(), true);
  }

  /** Tránh lỗi null khi so sánh chuỗi, đồng thời chuyển về chữ thường. */
  private String safeLower(String value) {
    if (value == null) return "";
    return value.toLowerCase(Locale.getDefault());
  }

  /**
   * Hiển thị/ẩn giao diện “trống”.
   * @param showEmpty true nếu cần hiển thị giao diện trống.
   * @param fromSearch true nếu danh sách trống là do không có kết quả tìm kiếm.
   */
  private void toggleEmptyState(boolean showEmpty, boolean fromSearch) {
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    emptyState.setVisibility(showEmpty ? View.VISIBLE : View.GONE);

    if (!showEmpty) return;

    // Cập nhật nội dung thông báo phù hợp
    if (allUsers.isEmpty()) {
      tvEmptyTitle.setText(R.string.admin_user_empty_all);
      tvEmptySubtitle.setText(R.string.admin_user_empty_all_subtitle);
    } else if (fromSearch) {
      tvEmptyTitle.setText(R.string.admin_user_empty_filtered);
      tvEmptySubtitle.setText(R.string.admin_user_empty_filtered_subtitle);
    }
  }

  /**
   * Hiển thị hộp thoại xác nhận trước khi Ban/Unban người dùng.
   * (gọi từ adapter khi admin bấm nút)
   */
  private void confirmBanToggle(UserEntity user) {
    if (user == null || getContext() == null) return;

    boolean ban = !user.isBanned; // nếu đang active => chuẩn bị ban, ngược lại unban

    int title = ban ? R.string.admin_user_confirm_ban_title
            : R.string.admin_user_confirm_unban_title;
    int message = ban ? R.string.admin_user_confirm_ban_message
            : R.string.admin_user_confirm_unban_message;

    new MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            // Khi admin xác nhận ban/unban
            .setPositiveButton(ban ? R.string.admin_user_action_ban
                            : R.string.admin_user_action_unban,
                    (dialog, which) -> updateBanStatus(user, ban))
            .setNegativeButton(android.R.string.cancel, null) // Hủy bỏ
            .show();
  }

  /**
   * Gọi ViewModel để thay đổi trạng thái Ban/Unban,
   * rồi hiển thị thông báo kết quả cho admin.
   */
  private void updateBanStatus(UserEntity user, boolean ban) {
    vm.setBanStatus(user, ban, new AdminUsersVM.ActionCallback() {

      /** Thành công: hiển thị thông báo Toast. */
      @Override
      public void onSuccess() {
        if (!isAdded()) return;
        Toast.makeText(requireContext(),
                R.string.admin_user_ban_success, Toast.LENGTH_SHORT).show();
      }

      /** Lỗi: hiển thị thông báo lỗi. */
      @Override
      public void onError(Throwable throwable) {
        if (!isAdded()) return;
        String msg = (throwable == null) ? "Unknown error" : throwable.getMessage();
        Toast.makeText(requireContext(),
                getString(R.string.admin_user_ban_error, msg), Toast.LENGTH_SHORT).show();
      }
    });
  }
}
