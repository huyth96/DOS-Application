package com.drinkorder.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.ProductEntity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminProductsAdapter extends RecyclerView.Adapter<AdminProductsAdapter.VH> {

  public interface Callback {
    void onEdit(ProductEntity product);
    void onDelete(ProductEntity product);
  }

  private final Callback callback;
  private final List<ProductEntity> items = new ArrayList<>();
  private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
  private Map<Integer, String> categoryNames = new HashMap<>();

  public AdminProductsAdapter(Callback callback) {
    this.callback = callback;
    priceFormat.setMaximumFractionDigits(0);
  }

  public void submit(List<ProductEntity> products) {
    items.clear();
    if (products != null) items.addAll(products);
    notifyDataSetChanged();
  }

  public void setCategoryNames(Map<Integer, String> names) {
    categoryNames = names == null ? Collections.emptyMap() : new HashMap<>(names);
    notifyDataSetChanged();
  }

  @NonNull @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false);
    return new VH(v);
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    ProductEntity product = items.get(position);
    holder.tvName.setText(product.name);
    holder.tvCategory.setText("Category: " + categoryNames.getOrDefault(product.categoryId, "Unknown"));
    holder.tvPrice.setText(priceFormat.format(Math.round(product.price)) + " VND");
    holder.tvMeta.setText(String.format(Locale.getDefault(), "Rating: %s  |  Stock: %d",
        formatRating(product.rating), product.stock == null ? 0 : product.stock));

    Object imageSource = (product.imageUrl == null || product.imageUrl.trim().isEmpty())
        ? R.drawable.bg_app_gradient : product.imageUrl.trim();

    Glide.with(holder.imgThumb.getContext())
        .load(imageSource)
        .apply(new RequestOptions().transform(new CenterCrop()))
        .into(holder.imgThumb);

    holder.btnEdit.setOnClickListener(v -> {
      if (callback != null) callback.onEdit(product);
    });
    holder.btnDelete.setOnClickListener(v -> {
      if (callback != null) callback.onDelete(product);
    });
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class VH extends RecyclerView.ViewHolder {
    final ShapeableImageView imgThumb;
    final TextView tvName;
    final TextView tvCategory;
    final TextView tvPrice;
    final TextView tvMeta;
    final MaterialButton btnEdit;
    final MaterialButton btnDelete;

    VH(@NonNull View itemView) {
      super(itemView);
      imgThumb = itemView.findViewById(R.id.imgThumb);
      tvName = itemView.findViewById(R.id.tvName);
      tvCategory = itemView.findViewById(R.id.tvCategory);
      tvPrice = itemView.findViewById(R.id.tvPrice);
      tvMeta = itemView.findViewById(R.id.tvMeta);
      btnEdit = itemView.findViewById(R.id.btnEdit);
      btnDelete = itemView.findViewById(R.id.btnDelete);
    }
  }

  private String formatRating(Double rating) {
    double value = (rating == null || rating <= 0) ? 4.5 : rating;
    return String.format(Locale.getDefault(), "%.1f", value);
  }
}
