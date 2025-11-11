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
 * AdminUsersFragment
 * - Màn hình quản trị người dùng: xem danh sách, thống kê và ban/unban tài khoản.
 * - Tìm kiếm theo họ tên, username, email, số điện thoại.
 */
public class AdminUsersFragment extends Fragment {

  private AdminUsersVM vm;
  private AdminUsersAdapter adapter;
  private RecyclerView recyclerView;
  private View emptyState;
  private TextView tvTotal;
  private TextView tvBanned;
  private TextView tvEmptyTitle;
  private TextView tvEmptySubtitle;
  private final List<UserEntity> allUsers = new ArrayList<>();
  private String currentQuery = "";

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_admin_users, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    recyclerView = view.findViewById(R.id.rvAdminUsers);
    emptyState = view.findViewById(R.id.userEmptyState);
    tvTotal = view.findViewById(R.id.tvUsersTotal);
    tvBanned = view.findViewById(R.id.tvUsersBanned);
    tvEmptyTitle = view.findViewById(R.id.tvUserEmptyTitle);
    tvEmptySubtitle = view.findViewById(R.id.tvUserEmptySubtitle);

    // Danh sách người dùng
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    adapter = new AdminUsersAdapter(this::confirmBanToggle);
    recyclerView.setAdapter(adapter);

    TextInputEditText edtSearch = view.findViewById(R.id.edtSearchUsers);
    if (edtSearch != null) {
      edtSearch.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          currentQuery = s == null ? "" : s.toString().trim();
          applyFilters();
        }
        @Override public void afterTextChanged(Editable s) {}
      });
    }

    // Quan sát danh sách người dùng từ ViewModel (LiveData)
    vm = new ViewModelProvider(this).get(AdminUsersVM.class);
    vm.users.observe(getViewLifecycleOwner(), list -> {
      allUsers.clear();
      if (list != null) {
        allUsers.addAll(list);
      }
      updateSummary();
      applyFilters();
    });
  }

  /** Cập nhật thống kê tổng số user và số user bị ban. */
  private void updateSummary() {
    int total = allUsers.size();
    int banned = 0;
    for (UserEntity user : allUsers) {
      if (user.isBanned) banned++;
    }
    if (tvTotal != null) {
      tvTotal.setText(String.valueOf(total));
    }
    if (tvBanned != null) {
      tvBanned.setText(String.valueOf(banned));
    }
  }

  /** Áp dụng tìm kiếm/lọc theo từ khóa hiện tại. */
  private void applyFilters() {
    String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());
    if (allUsers.isEmpty()) {
      adapter.submit(allUsers);
      toggleEmptyState(true, false);
      return;
    }

    if (query.isEmpty()) {
      adapter.submit(allUsers);
      toggleEmptyState(false, false);
      return;
    }

    List<UserEntity> filtered = new ArrayList<>();
    for (UserEntity user : allUsers) {
      String name = safeLower(user.fullName);
      String username = safeLower(user.username);
      String email = safeLower(user.email);
      String phone = safeLower(user.phone);
      if (name.contains(query) || username.contains(query) || email.contains(query) || phone.contains(query)) {
        filtered.add(user);
      }
    }
    adapter.submit(filtered);
    toggleEmptyState(filtered.isEmpty(), true);
  }

  /** Trả về chuỗi thường, an toàn khi null. */
  private String safeLower(String value) {
    if (value == null) return "";
    return value.toLowerCase(Locale.getDefault());
  }

  /** Hiển thị/ẩn empty-state với nội dung phù hợp theo ngữ cảnh. */
  private void toggleEmptyState(boolean showEmpty, boolean fromSearch) {
    if (recyclerView == null || emptyState == null) return;
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    emptyState.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    if (!showEmpty || tvEmptyTitle == null || tvEmptySubtitle == null) return;
    if (allUsers.isEmpty()) {
      tvEmptyTitle.setText(R.string.admin_user_empty_all);
      tvEmptySubtitle.setText(R.string.admin_user_empty_all_subtitle);
    } else if (fromSearch) {
      tvEmptyTitle.setText(R.string.admin_user_empty_filtered);
      tvEmptySubtitle.setText(R.string.admin_user_empty_filtered_subtitle);
    }
  }

  /** Hiển thị hộp thoại xác nhận trước khi ban/unban. */
  private void confirmBanToggle(UserEntity user) {
    if (user == null || getContext() == null) return;
    boolean ban = !user.isBanned;
    int title = ban ? R.string.admin_user_confirm_ban_title : R.string.admin_user_confirm_unban_title;
    int message = ban ? R.string.admin_user_confirm_ban_message : R.string.admin_user_confirm_unban_message;
    new MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(ban ? R.string.admin_user_action_ban : R.string.admin_user_action_unban,
            (dialog, which) -> updateBanStatus(user, ban))
        .setNegativeButton(android.R.string.cancel, null)
        .show();
  }

  /** Gọi ViewModel để cập nhật trạng thái ban/unban và hiển thị kết quả. */
  private void updateBanStatus(UserEntity user, boolean ban) {
    vm.setBanStatus(user, ban, new AdminUsersVM.ActionCallback() {
      @Override public void onSuccess() {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), R.string.admin_user_ban_success, Toast.LENGTH_SHORT).show();
      }

      @Override public void onError(Throwable throwable) {
        if (!isAdded()) return;
        String msg = throwable == null ? "Unknown error" : throwable.getMessage();
        Toast.makeText(requireContext(), getString(R.string.admin_user_ban_error, msg), Toast.LENGTH_SHORT).show();
      }
    });
  }
}
