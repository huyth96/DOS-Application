// Khai báo package com.drinkorder.ui.admin cho toàn bộ lớp.
package com.drinkorder.ui.admin;

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
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.bumptech.glide.Glide để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.Glide;
// Import com.bumptech.glide.load.resource.bitmap.CenterCrop để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
// Import com.bumptech.glide.request.RequestOptions để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.request.RequestOptions;
// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.google.android.material.button.MaterialButton để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.button.MaterialButton;
// Import com.google.android.material.imageview.ShapeableImageView để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.imageview.ShapeableImageView;

// Import java.text.NumberFormat để sử dụng các lớp hoặc hàm tương ứng.
import java.text.NumberFormat;
// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.Collections để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Collections;
// Import java.util.HashMap để sử dụng các lớp hoặc hàm tương ứng.
import java.util.HashMap;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;
// Import java.util.Map để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Map;

// Định nghĩa lớp AdminProductsAdapter kế thừa RecyclerView.Adapter<AdminProductsAdapter.VH>.
public class AdminProductsAdapter extends RecyclerView.Adapter<AdminProductsAdapter.VH> {

  // Định nghĩa interface Callback.
  public interface Callback {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onEdit(ProductEntity product);.
    void onEdit(ProductEntity product);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onDelete(ProductEntity product);.
    void onDelete(ProductEntity product);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private final Callback callback.
  private final Callback callback;
  // Khai báo thuộc tính với phạm vi truy cập: private final List<ProductEntity> items = new ArrayList<>().
  private final List<ProductEntity> items = new ArrayList<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN")).
  private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
  // Khai báo thuộc tính với phạm vi truy cập: private Map<Integer, String> categoryNames = new HashMap<>().
  private Map<Integer, String> categoryNames = new HashMap<>();

  // Định nghĩa phương thức AdminProductsAdapter với phạm vi truy cập tương ứng.
  public AdminProductsAdapter(Callback callback) {
    // Gán giá trị cho biến hoặc thuộc tính: this.callback = callback.
    this.callback = callback;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: priceFormat.setMaximumFractionDigits(0);.
    priceFormat.setMaximumFractionDigits(0);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức submit với phạm vi truy cập tương ứng.
  public void submit(List<ProductEntity> products) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: items.clear();.
    items.clear();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (products != null) items.addAll(products);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
    notifyDataSetChanged();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setCategoryNames với phạm vi truy cập tương ứng.
  public void setCategoryNames(Map<Integer, String> names) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryNames = names == null ? Collections.emptyMap() : new HashMap<>(names);.
    categoryNames = names == null ? Collections.emptyMap() : new HashMap<>(names);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
    notifyDataSetChanged();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @NonNull và @Override cho phần tử bên dưới.
  @NonNull @Override
  // Định nghĩa phương thức onCreateViewHolder với phạm vi truy cập tương ứng.
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false);.
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false);
    // Trả về kết quả new VH(v);.
    return new VH(v);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onBindViewHolder với phạm vi truy cập tương ứng.
  public void onBindViewHolder(@NonNull VH holder, int position) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ProductEntity product = items.get(position);.
    ProductEntity product = items.get(position);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvName.setText(product.name);.
    holder.tvName.setText(product.name);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvCategory.setText("Category: " + categoryNames.getOrDefault(product.categoryId, "Unknown"));.
    holder.tvCategory.setText("Category: " + categoryNames.getOrDefault(product.categoryId, "Unknown"));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvPrice.setText(priceFormat.format(Math.round(product.price)) + " VND");.
    holder.tvPrice.setText(priceFormat.format(Math.round(product.price)) + " VND");
    // Thực thi câu lệnh: holder.tvMeta.setText(String.format(Locale.getDefault(), "Rating: %s  |  Stock: %d",.
    holder.tvMeta.setText(String.format(Locale.getDefault(), "Rating: %s  |  Stock: %d",
        // Thực hiện lời gọi phương thức hoặc khởi tạo: formatRating(product.rating), product.stock == null ? 0 : product.stock));.
        formatRating(product.rating), product.stock == null ? 0 : product.stock));

    // Thực hiện lời gọi phương thức hoặc khởi tạo: Object imageSource = (product.imageUrl == null || product.imageUrl.trim().isEmpty()).
    Object imageSource = (product.imageUrl == null || product.imageUrl.trim().isEmpty())
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? R.drawable.bg_app_gradient : product.imageUrl.trim();.
        ? R.drawable.bg_app_gradient : product.imageUrl.trim();

    // Thực hiện lời gọi phương thức hoặc khởi tạo: Glide.with(holder.imgThumb.getContext()).
    Glide.with(holder.imgThumb.getContext())
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .load(imageSource).
        .load(imageSource)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .apply(new RequestOptions().transform(new CenterCrop())).
        .apply(new RequestOptions().transform(new CenterCrop()))
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .into(holder.imgThumb);.
        .into(holder.imgThumb);

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    holder.btnEdit.setOnClickListener(v -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (callback != null) callback.onEdit(product);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    holder.btnDelete.setOnClickListener(v -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (callback != null) callback.onDelete(product);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
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

  // Định nghĩa lớp VH kế thừa RecyclerView.ViewHolder.
  static class VH extends RecyclerView.ViewHolder {
    // Thực thi câu lệnh: final ShapeableImageView imgThumb;.
    final ShapeableImageView imgThumb;
    // Thực thi câu lệnh: final TextView tvName;.
    final TextView tvName;
    // Thực thi câu lệnh: final TextView tvCategory;.
    final TextView tvCategory;
    // Thực thi câu lệnh: final TextView tvPrice;.
    final TextView tvPrice;
    // Thực thi câu lệnh: final TextView tvMeta;.
    final TextView tvMeta;
    // Thực thi câu lệnh: final MaterialButton btnEdit;.
    final MaterialButton btnEdit;
    // Thực thi câu lệnh: final MaterialButton btnDelete;.
    final MaterialButton btnDelete;

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    VH(@NonNull View itemView) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: super(itemView);.
      super(itemView);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: imgThumb = itemView.findViewById(R.id.imgThumb);.
      imgThumb = itemView.findViewById(R.id.imgThumb);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvName = itemView.findViewById(R.id.tvName);.
      tvName = itemView.findViewById(R.id.tvName);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvCategory = itemView.findViewById(R.id.tvCategory);.
      tvCategory = itemView.findViewById(R.id.tvCategory);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvPrice = itemView.findViewById(R.id.tvPrice);.
      tvPrice = itemView.findViewById(R.id.tvPrice);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMeta = itemView.findViewById(R.id.tvMeta);.
      tvMeta = itemView.findViewById(R.id.tvMeta);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnEdit = itemView.findViewById(R.id.btnEdit);.
      btnEdit = itemView.findViewById(R.id.btnEdit);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnDelete = itemView.findViewById(R.id.btnDelete);.
      btnDelete = itemView.findViewById(R.id.btnDelete);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức formatRating với phạm vi truy cập tương ứng.
  private String formatRating(Double rating) {
    // Gán giá trị cho biến hoặc thuộc tính: double value = (rating == null || rating <= 0) ? 4.5 : rating.
    double value = (rating == null || rating <= 0) ? 4.5 : rating;
    // Trả về kết quả String.format(Locale.getDefault(), "%.1f", value);.
    return String.format(Locale.getDefault(), "%.1f", value);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
