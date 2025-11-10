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

public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.UserVH> {

  public interface Callback {
    void onToggleBan(UserEntity user);
  }

  private final Callback callback;
  private final List<UserEntity> items = new ArrayList<>();
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

  public AdminUsersAdapter(Callback callback) {
    this.callback = callback;
  }

  public void submit(List<UserEntity> data) {
    items.clear();
    if (data != null) items.addAll(data);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_admin_user, parent, false);
    return new UserVH(view);
  }

  @Override
  public void onBindViewHolder(@NonNull UserVH holder, int position) {
    UserEntity user = items.get(position);
    holder.tvName.setText(displayName(user));
    holder.tvUsername.setText("@" + user.username);
    holder.tvRole.setText(roleLabel(user.role));
    holder.tvCreated.setText(holder.itemView.getContext()
        .getString(R.string.admin_user_created_at, dateFormat.format(new Date(user.createdAt))));

    boolean isBanned = user.isBanned;
    holder.tvStatus.setText(isBanned ? R.string.admin_user_status_banned : R.string.admin_user_status_active);
    int statusColor = ContextCompat.getColor(holder.itemView.getContext(),
        isBanned ? R.color.brand_error : R.color.brand_success);
    holder.tvStatus.setTextColor(statusColor);

    boolean isAdminRole = "admin".equalsIgnoreCase(user.role);
    if (isAdminRole) {
      holder.btnBan.setEnabled(false);
      holder.btnBan.setText(R.string.admin_user_action_protected);
      holder.btnBan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.brand_muted_text));
      holder.btnBan.setOnClickListener(null);
    } else {
      holder.btnBan.setEnabled(true);
      holder.btnBan.setText(isBanned ? R.string.admin_user_action_unban : R.string.admin_user_action_ban);
      holder.btnBan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
          isBanned ? R.color.brand_success : R.color.brand_error));
      holder.btnBan.setOnClickListener(v -> {
        if (callback != null) callback.onToggleBan(user);
      });
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class UserVH extends RecyclerView.ViewHolder {
    final TextView tvName;
    final TextView tvUsername;
    final TextView tvRole;
    final TextView tvStatus;
    final TextView tvCreated;
    final MaterialButton btnBan;

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

  private String displayName(UserEntity user) {
    if (!TextUtils.isEmpty(user.fullName)) {
      return user.fullName;
    }
    return user.username;
  }

  private String roleLabel(String role) {
    if (TextUtils.isEmpty(role)) return "Customer";
    String lower = role.toLowerCase(Locale.getDefault());
    if (lower.equals("admin")) return "Admin";
    return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
  }
}
