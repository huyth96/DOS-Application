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

public class AdminProductsAdapter extends RecyclerView.Adapter<AdminProductsAdapter.VH> { // Định nghĩa lớp AdminProductsAdapter kế thừa từ RecyclerView.Adapter, với ViewHolder là VH, dùng để hiển thị danh sách sản phẩm trong RecyclerView cho admin.

  public interface Callback { // Interface định nghĩa callback cho adapter, dùng để xử lý sự kiện edit và delete sản phẩm.
    void onEdit(ProductEntity product); // Phương thức gọi khi edit sản phẩm, nhận entity sản phẩm.
    void onDelete(ProductEntity product); // Phương thức gọi khi delete sản phẩm, nhận entity sản phẩm.
  }

  private final Callback callback; // Biến final lưu callback được truyền vào constructor.
  private final List<ProductEntity> items = new ArrayList<>(); // Danh sách lưu các sản phẩm, khởi tạo là ArrayList rỗng.
  private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN")); // Biến lưu NumberFormat để định dạng giá theo locale Việt Nam.
  private Map<Integer, String> categoryNames = new HashMap<>(); // Map lưu tên danh mục theo ID, khởi tạo là HashMap rỗng.

  public AdminProductsAdapter(Callback callback) { // Constructor của adapter, nhận callback.
    this.callback = callback; // Gán callback vào biến instance.
    priceFormat.setMaximumFractionDigits(0); // Thiết lập NumberFormat không hiển thị thập phân (giá nguyên).
  }

  public void submit(List<ProductEntity> products) { // Phương thức submit dữ liệu sản phẩm mới vào adapter.
    items.clear(); // Xóa danh sách hiện tại.
    if (products != null) items.addAll(products); // Nếu products không null, thêm tất cả vào items.
    notifyDataSetChanged(); // Thông báo adapter thay đổi dữ liệu để refresh RecyclerView.
  }

  public void setCategoryNames(Map<Integer, String> names) { // Phương thức thiết lập map tên danh mục.
    categoryNames = names == null ? Collections.emptyMap() : new HashMap<>(names); // Nếu names null, dùng emptyMap, ngược lại copy HashMap mới.
    notifyDataSetChanged(); // Thông báo adapter thay đổi dữ liệu để refresh (vì tên danh mục thay đổi).
  }

  @NonNull @Override // Ghi đè phương thức onCreateViewHolder từ Adapter.
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Phương thức tạo ViewHolder mới.
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false); // Inflate layout item từ resource.
    return new VH(v); // Trả về ViewHolder mới với view đã inflate.
  }

  @Override // Ghi đè phương thức onBindViewHolder từ Adapter.
  public void onBindViewHolder(@NonNull VH holder, int position) { // Phương thức bind dữ liệu vào ViewHolder tại vị trí position.
    ProductEntity product = items.get(position); // Lấy sản phẩm tại vị trí.
    holder.tvName.setText(product.name); // Đặt tên sản phẩm vào TextView.
    holder.tvCategory.setText("Category: " + categoryNames.getOrDefault(product.categoryId, "Unknown")); // Đặt tên danh mục, dùng "Unknown" nếu không tìm thấy.
    holder.tvPrice.setText(priceFormat.format(Math.round(product.price)) + " VND"); // Đặt giá đã định dạng và thêm " VND".
    holder.tvMeta.setText(String.format(Locale.getDefault(), "Rating: %s  |  Stock: %d", // Đặt meta với rating và stock.
            formatRating(product.rating), product.stock == null ? 0 : product.stock)); // Sử dụng formatRating cho rating, stock mặc định 0 nếu null.

    Object imageSource = (product.imageUrl == null || product.imageUrl.trim().isEmpty()) // Xác định nguồn hình ảnh.
            ? R.drawable.bg_app_gradient : product.imageUrl.trim(); // Nếu URL rỗng, dùng drawable mặc định, ngược lại dùng URL đã trim.

    Glide.with(holder.imgThumb.getContext()) // Sử dụng Glide để load hình ảnh.
            .load(imageSource) // Load từ nguồn đã xác định.
            .apply(new RequestOptions().transform(new CenterCrop())) // Áp dụng transform crop center.
            .into(holder.imgThumb); // Load vào ImageView thumb.

    holder.btnEdit.setOnClickListener(v -> { // Thiết lập listener cho nút edit.
      if (callback != null) callback.onEdit(product); // Nếu có callback, gọi onEdit với sản phẩm.
    });
    holder.btnDelete.setOnClickListener(v -> { // Thiết lập listener cho nút delete.
      if (callback != null) callback.onDelete(product); // Nếu có callback, gọi onDelete với sản phẩm.
    });
  }

  @Override // Ghi đè phương thức getItemCount từ Adapter.
  public int getItemCount() { // Phương thức trả về số lượng item.
    return items.size(); // Trả về kích thước danh sách items.
  }

  static class VH extends RecyclerView.ViewHolder { // Lớp static ViewHolder kế thừa từ RecyclerView.ViewHolder.
    final ShapeableImageView imgThumb; // Biến final lưu ImageView thumb.
    final TextView tvName; // Biến final lưu TextView tên.
    final TextView tvCategory; // Biến final lưu TextView danh mục.
    final TextView tvPrice; // Biến final lưu TextView giá.
    final TextView tvMeta; // Biến final lưu TextView meta.
    final MaterialButton btnEdit; // Biến final lưu nút edit.
    final MaterialButton btnDelete; // Biến final lưu nút delete.

    VH(@NonNull View itemView) { // Constructor của ViewHolder, nhận itemView.
      super(itemView); // Gọi constructor cha.
      imgThumb = itemView.findViewById(R.id.imgThumb); // Tìm và lưu ImageView thumb.
      tvName = itemView.findViewById(R.id.tvName); // Tìm và lưu TextView tên.
      tvCategory = itemView.findViewById(R.id.tvCategory); // Tìm và lưu TextView danh mục.
      tvPrice = itemView.findViewById(R.id.tvPrice); // Tìm và lưu TextView giá.
      tvMeta = itemView.findViewById(R.id.tvMeta); // Tìm và lưu TextView meta.
      btnEdit = itemView.findViewById(R.id.btnEdit); // Tìm và lưu nút edit.
      btnDelete = itemView.findViewById(R.id.btnDelete); // Tìm và lưu nút delete.
    }
  }

  private String formatRating(Double rating) { // Phương thức private định dạng rating.
    double value = (rating == null || rating <= 0) ? 4.5 : rating; // Nếu rating null hoặc <=0, dùng 4.5, ngược lại dùng giá trị.
    return String.format(Locale.getDefault(), "%.1f", value); // Trả về string định dạng 1 thập phân.
  }
}
