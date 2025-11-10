package com.drinkorder.ui.admin;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminCategoriesAdapter extends RecyclerView.Adapter<AdminCategoriesAdapter.VH> {

  public interface Callback {
    void onEdit(CategoryEntity category);
    void onDelete(CategoryEntity category);
  }

  private final Callback callback;
  private final List<CategoryEntity> items = new ArrayList<>();
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

  public AdminCategoriesAdapter(Callback callback) {
    this.callback = callback;
  }

  public void submit(List<CategoryEntity> categories) {
    items.clear();
    if (categories != null) items.addAll(categories);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_category, parent, false);
    return new VH(v);
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    CategoryEntity category = items.get(position);
    holder.tvName.setText(category.name);
    holder.tvDescription.setText(descriptionText(category.description));
    holder.tvMeta.setText("Created: " + formatDate(category.createdAt));
    holder.btnEdit.setOnClickListener(v -> {
      if (callback != null) callback.onEdit(category);
    });
    holder.btnDelete.setOnClickListener(v -> {
      if (callback != null) callback.onDelete(category);
    });
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class VH extends RecyclerView.ViewHolder {
    final TextView tvName;
    final TextView tvDescription;
    final TextView tvMeta;
    final MaterialButton btnEdit;
    final MaterialButton btnDelete;

    VH(@NonNull View itemView) {
      super(itemView);
      tvName = itemView.findViewById(R.id.tvCategoryName);
      tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
      tvMeta = itemView.findViewById(R.id.tvCategoryMeta);
      btnEdit = itemView.findViewById(R.id.btnEdit);
      btnDelete = itemView.findViewById(R.id.btnDelete);
    }
  }

  private String formatDate(long timestamp) {
    if (timestamp <= 0) return "Not available";
    return dateFormat.format(new Date(timestamp));
  }

  private String descriptionText(String description) {
    if (TextUtils.isEmpty(description)) return "No description yet";
    return description;
  }
}
