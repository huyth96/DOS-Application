// Khai báo package com.drinkorder.ui.admin cho toàn bộ lớp.
package com.drinkorder.ui.admin;

// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;
// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;
// Import android.widget.TextView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.TextView;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.core.content.ContextCompat để sử dụng các lớp hoặc hàm tương ứng.
import androidx.core.content.ContextCompat;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.UserEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.UserEntity;
// Import com.google.android.material.button.MaterialButton để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.button.MaterialButton;

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

// Định nghĩa lớp AdminUsersAdapter kế thừa RecyclerView.Adapter<AdminUsersAdapter.UserVH>.
public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.UserVH> {

  // Định nghĩa interface Callback.
  public interface Callback {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onToggleBan(UserEntity user);.
    void onToggleBan(UserEntity user);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private final Callback callback.
  private final Callback callback;
  // Khai báo thuộc tính với phạm vi truy cập: private final List<UserEntity> items = new ArrayList<>().
  private final List<UserEntity> items = new ArrayList<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

  // Định nghĩa phương thức AdminUsersAdapter với phạm vi truy cập tương ứng.
  public AdminUsersAdapter(Callback callback) {
    // Gán giá trị cho biến hoặc thuộc tính: this.callback = callback.
    this.callback = callback;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức submit với phạm vi truy cập tương ứng.
  public void submit(List<UserEntity> data) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: items.clear();.
    items.clear();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (data != null) items.addAll(data);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
    notifyDataSetChanged();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateViewHolder với phạm vi truy cập tương ứng.
  public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View view = LayoutInflater.from(parent.getContext()).
    View view = LayoutInflater.from(parent.getContext())
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .inflate(R.layout.item_admin_user, parent, false);.
        .inflate(R.layout.item_admin_user, parent, false);
    // Trả về kết quả new UserVH(view);.
    return new UserVH(view);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onBindViewHolder với phạm vi truy cập tương ứng.
  public void onBindViewHolder(@NonNull UserVH holder, int position) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: UserEntity user = items.get(position);.
    UserEntity user = items.get(position);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvName.setText(displayName(user));.
    holder.tvName.setText(displayName(user));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvUsername.setText("@" + user.username);.
    holder.tvUsername.setText("@" + user.username);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvRole.setText(roleLabel(user.role));.
    holder.tvRole.setText(roleLabel(user.role));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvCreated.setText(holder.itemView.getContext().
    holder.tvCreated.setText(holder.itemView.getContext()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .getString(R.string.admin_user_created_at, dateFormat.format(new Date(user.createdAt))));.
        .getString(R.string.admin_user_created_at, dateFormat.format(new Date(user.createdAt))));

    // Gán giá trị cho biến hoặc thuộc tính: boolean isBanned = user.isBanned.
    boolean isBanned = user.isBanned;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvStatus.setText(isBanned ? R.string.admin_user_status_banned : R.string.admin_user_status_active);.
    holder.tvStatus.setText(isBanned ? R.string.admin_user_status_banned : R.string.admin_user_status_active);
    // Thực thi câu lệnh: int statusColor = ContextCompat.getColor(holder.itemView.getContext(),.
    int statusColor = ContextCompat.getColor(holder.itemView.getContext(),
        // Thực hiện lời gọi phương thức hoặc khởi tạo: isBanned ? R.color.brand_error : R.color.brand_success);.
        isBanned ? R.color.brand_error : R.color.brand_success);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvStatus.setTextColor(statusColor);.
    holder.tvStatus.setTextColor(statusColor);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: boolean isAdminRole = "admin".equalsIgnoreCase(user.role);.
    boolean isAdminRole = "admin".equalsIgnoreCase(user.role);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (isAdminRole) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.btnBan.setEnabled(false);.
      holder.btnBan.setEnabled(false);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.btnBan.setText(R.string.admin_user_action_protected);.
      holder.btnBan.setText(R.string.admin_user_action_protected);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.btnBan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.brand_muted_text));.
      holder.btnBan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.brand_muted_text));
      // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.btnBan.setOnClickListener(null);.
      holder.btnBan.setOnClickListener(null);
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.btnBan.setEnabled(true);.
      holder.btnBan.setEnabled(true);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.btnBan.setText(isBanned ? R.string.admin_user_action_unban : R.string.admin_user_action_ban);.
      holder.btnBan.setText(isBanned ? R.string.admin_user_action_unban : R.string.admin_user_action_ban);
      // Thực thi câu lệnh: holder.btnBan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),.
      holder.btnBan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
          // Thực hiện lời gọi phương thức hoặc khởi tạo: isBanned ? R.color.brand_success : R.color.brand_error));.
          isBanned ? R.color.brand_success : R.color.brand_error));
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      holder.btnBan.setOnClickListener(v -> {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (callback != null) callback.onToggleBan(user);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức getItemCount với phạm vi truy cập tương ứng.
  public int getItemCount() {
    // Trả về kết quả items.size();.
    return items.size();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa lớp UserVH kế thừa RecyclerView.ViewHolder.
  static class UserVH extends RecyclerView.ViewHolder {
    // Thực thi câu lệnh: final TextView tvName;.
    final TextView tvName;
    // Thực thi câu lệnh: final TextView tvUsername;.
    final TextView tvUsername;
    // Thực thi câu lệnh: final TextView tvRole;.
    final TextView tvRole;
    // Thực thi câu lệnh: final TextView tvStatus;.
    final TextView tvStatus;
    // Thực thi câu lệnh: final TextView tvCreated;.
    final TextView tvCreated;
    // Thực thi câu lệnh: final MaterialButton btnBan;.
    final MaterialButton btnBan;

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    UserVH(@NonNull View itemView) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: super(itemView);.
      super(itemView);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvName = itemView.findViewById(R.id.tvUserName);.
      tvName = itemView.findViewById(R.id.tvUserName);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvUsername = itemView.findViewById(R.id.tvUserUsername);.
      tvUsername = itemView.findViewById(R.id.tvUserUsername);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvRole = itemView.findViewById(R.id.tvUserRole);.
      tvRole = itemView.findViewById(R.id.tvUserRole);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvStatus = itemView.findViewById(R.id.tvUserStatus);.
      tvStatus = itemView.findViewById(R.id.tvUserStatus);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCreated = itemView.findViewById(R.id.tvUserCreated);.
      tvCreated = itemView.findViewById(R.id.tvUserCreated);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnBan = itemView.findViewById(R.id.btnToggleBan);.
      btnBan = itemView.findViewById(R.id.btnToggleBan);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức displayName với phạm vi truy cập tương ứng.
  private String displayName(UserEntity user) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.isEmpty(user.fullName)) {
      // Trả về kết quả user.fullName;.
      return user.fullName;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả user.username;.
    return user.username;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức roleLabel với phạm vi truy cập tương ứng.
  private String roleLabel(String role) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(role)) return "Customer";
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String lower = role.toLowerCase(Locale.getDefault());.
    String lower = role.toLowerCase(Locale.getDefault());
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (lower.equals("admin")) return "Admin";
    // Trả về kết quả Character.toUpperCase(lower.charAt(0)) + lower.substring(1);.
    return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
