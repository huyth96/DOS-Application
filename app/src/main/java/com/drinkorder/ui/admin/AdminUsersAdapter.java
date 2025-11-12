package com.drinkorder.ui.admin;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.UserEntity;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter dùng để hiển thị list người dùng trong màn Admin:
 * - Hiển thị: tên, username, role, trạng thái (Active/Banned), ngày tạo.
 * - Cho phép admin bấm nút Ban / Unban (trừ tài khoản admin).
 */
public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.UserVH> {

  /**
   * Interface để Fragment/Activity nhận sự kiện khi bấm nút Ban/Unban.
   * Adapter chỉ bắn event ra ngoài, không tự xử lý ban/unban trong đây.
   */
  public interface Callback {
    /**
     * Hàm này sẽ được gọi khi admin bấm nút Ban/Unban trong 1 item.
     *
     * @param user user tương ứng với item vừa bấm.
     */
    void onToggleBan(UserEntity user);
  }

  /** Đối tượng callback do Fragment/Activity truyền vào. */
  private final Callback callback;

  /** Danh sách user mà adapter đang dùng để hiển thị. */
  private final List<UserEntity> items = new ArrayList<>();

  /** Format ngày tạo tài khoản để show ra UI, ví dụ "12 Mar 2024, 08:30". */
  private final SimpleDateFormat dateFormat =
          new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

  /** Nhận callback từ bên ngoài để xử lý khi bấm Ban/Unban. */
  public AdminUsersAdapter(Callback callback) {
    this.callback = callback;
  }

  /**
   * Hàm để truyền dữ liệu list user mới vào adapter.
   * Gọi mỗi khi ViewModel trả về danh sách cập nhật.
   */
  public void submit(List<UserEntity> data) {
    items.clear();           // xóa list cũ
    if (data != null) {
      items.addAll(data);    // thêm list mới
    }
    notifyDataSetChanged();  // báo RecyclerView vẽ lại toàn bộ
  }

  /**
   * @Override này thuộc vòng đời RecyclerView:
   * - Được gọi khi RecyclerView cần "tạo mới" 1 ViewHolder.
   * - Thường chỉ inflate layout của item và tạo instance ViewHolder.
   */
  @NonNull
  @Override
  public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Chuyển XML item_admin_user thành View Java.
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_admin_user, parent, false);
    return new UserVH(view);
  }

  /**
   * @Override:
   * - Được gọi mỗi khi RecyclerView muốn "gán dữ liệu" vào 1 hàng (item).
   * - position là vị trí item trong list.
   */
  @Override
  public void onBindViewHolder(@NonNull UserVH holder, int position) {
    // Lấy user tại vị trí tương ứng.
    UserEntity user = items.get(position);

    // Tên hiển thị: ưu tiên fullName, nếu trống thì dùng username.
    holder.tvName.setText(displayName(user));

    // Username hiển thị thêm ký tự "@" phía trước cho đẹp.
    holder.tvUsername.setText("@" + user.username);

    // Role hiển thị dạng chữ đẹp: Admin, Staff, Customer, ...
    holder.tvRole.setText(roleLabel(user.role));

    // Ngày tạo tài khoản: chuyển millis -> Date -> format -> set text.
    holder.tvCreated.setText(holder.itemView.getContext()
            .getString(
                    R.string.admin_user_created_at,
                    dateFormat.format(new Date(user.createdAt))
            ));

    // Trạng thái ban hay không.
    boolean isBanned = user.isBanned;

    // Text: "Banned" hoặc "Active".
    holder.tvStatus.setText(
            isBanned
                    ? R.string.admin_user_status_banned
                    : R.string.admin_user_status_active
    );

    // Màu chữ: Banned -> đỏ, Active -> xanh.
    int statusColor = ContextCompat.getColor(
            holder.itemView.getContext(),
            isBanned ? R.color.brand_error : R.color.brand_success
    );
    holder.tvStatus.setTextColor(statusColor);

    // Nếu là admin thì không cho phép ban.
    boolean isAdminRole = "admin".equalsIgnoreCase(user.role);

    if (isAdminRole) {
      // Disable nút + đổi text + màu xám để biết là không bấm được.
      holder.btnBan.setEnabled(false);
      holder.btnBan.setText(R.string.admin_user_action_protected);
      holder.btnBan.setTextColor(
              ContextCompat.getColor(
                      holder.itemView.getContext(),
                      R.color.brand_muted_text
              )
      );
      holder.btnBan.setOnClickListener(null); // không gắn sự kiện click
    } else {
      // Các role khác (customer/staff/...) thì được phép ban / unban.
      holder.btnBan.setEnabled(true);

      // Nếu đang bị ban -> nút sẽ là "Unban".
      // Nếu đang active -> nút sẽ là "Ban".
      holder.btnBan.setText(
              isBanned
                      ? R.string.admin_user_action_unban
                      : R.string.admin_user_action_ban
      );

      // Màu chữ nút: Ban -> đỏ, Unban -> xanh.
      holder.btnBan.setTextColor(
              ContextCompat.getColor(
                      holder.itemView.getContext(),
                      isBanned ? R.color.brand_success : R.color.brand_error
              )
      );

      // Khi bấm nút:
      // - Adapter gọi callback ra Fragment để xử lý (show dialog + update DB).
      holder.btnBan.setOnClickListener(v -> {
        if (callback != null) {
          callback.onToggleBan(user);
        }
      });
    }
  }

  /**
   * @Override:
   * - Cho RecyclerView biết hiện tại có bao nhiêu item cần hiển thị.
   */
  @Override
  public int getItemCount() {
    return items.size();
  }

  /**
   * ViewHolder: "giữ" các view con trong 1 item.
   * Giúp không phải findViewById nhiều lần khi RecyclerView scroll.
   */
  static class UserVH extends RecyclerView.ViewHolder {
    final TextView tvName;
    final TextView tvUsername;
    final TextView tvRole;
    final TextView tvStatus;
    final TextView tvCreated;
    final MaterialButton btnBan;

    /**
     * @Override constructor của ViewHolder:
     * - Nhận vào 1 View đại diện cho toàn bộ layout item.
     * - Tìm và gán các view con vào biến để dùng lại sau này.
     */
    UserVH(@NonNull View itemView) {
      super(itemView);
      tvName = itemView.findViewById(R.id.tvUserName);
      tvUsername = itemView.findViewById(R.id.tvUserUsername);
      tvRole = itemView.findViewById(R.id.tvUserRole);
      tvStatus = itemView.findViewById(R.id.tvUserStatus);
      tvCreated = itemView.findViewById(R.id.tvUserCreated);
      btnBan = itemView.findViewById(R.id.btnToggleBan);
    }
  }

  /**
   * Lấy tên hiển thị:
   * - Nếu có fullName -> dùng fullName.
   * - Nếu fullName trống -> dùng username.
   */
  private String displayName(UserEntity user) {
    if (!TextUtils.isEmpty(user.fullName)) {
      return user.fullName;
    }
    return user.username;
  }

  /**
   * Chuyển "role" trong DB thành chữ đẹp để show lên UI.
   * - null hoặc rỗng -> "Customer" (mặc định).
   * - "admin"        -> "Admin".
   * - các role khác  -> viết hoa chữ cái đầu (staff -> Staff, manager -> Manager).
   */
  private String roleLabel(String role) {
    if (TextUtils.isEmpty(role)) return "Customer";

    String lower = role.toLowerCase(Locale.getDefault());

    if (lower.equals("admin")) return "Admin";

    return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
  }
}
