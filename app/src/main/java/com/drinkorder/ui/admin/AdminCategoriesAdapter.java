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
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
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

// Định nghĩa lớp AdminCategoriesAdapter kế thừa RecyclerView.Adapter<AdminCategoriesAdapter.VH>.
public class AdminCategoriesAdapter extends RecyclerView.Adapter<AdminCategoriesAdapter.VH> {

  // Định nghĩa interface Callback.
  public interface Callback {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onEdit(CategoryEntity category);.
    void onEdit(CategoryEntity category);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onDelete(CategoryEntity category);.
    void onDelete(CategoryEntity category);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private final Callback callback.
  private final Callback callback;
  // Khai báo thuộc tính với phạm vi truy cập: private final List<CategoryEntity> items = new ArrayList<>().
  private final List<CategoryEntity> items = new ArrayList<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

  // Định nghĩa phương thức AdminCategoriesAdapter với phạm vi truy cập tương ứng.
  public AdminCategoriesAdapter(Callback callback) {
    // Gán giá trị cho biến hoặc thuộc tính: this.callback = callback.
    this.callback = callback;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức submit với phạm vi truy cập tương ứng.
  public void submit(List<CategoryEntity> categories) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: items.clear();.
    items.clear();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (categories != null) items.addAll(categories);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
    notifyDataSetChanged();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateViewHolder với phạm vi truy cập tương ứng.
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_category, parent, false);.
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_category, parent, false);
    // Trả về kết quả new VH(v);.
    return new VH(v);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onBindViewHolder với phạm vi truy cập tương ứng.
  public void onBindViewHolder(@NonNull VH holder, int position) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: CategoryEntity category = items.get(position);.
    CategoryEntity category = items.get(position);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvName.setText(category.name);.
    holder.tvName.setText(category.name);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvDescription.setText(descriptionText(category.description));.
    holder.tvDescription.setText(descriptionText(category.description));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.tvMeta.setText("Created: " + formatDate(category.createdAt));.
    holder.tvMeta.setText("Created: " + formatDate(category.createdAt));
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    holder.btnEdit.setOnClickListener(v -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (callback != null) callback.onEdit(category);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    holder.btnDelete.setOnClickListener(v -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (callback != null) callback.onDelete(category);
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
    // Thực thi câu lệnh: final TextView tvName;.
    final TextView tvName;
    // Thực thi câu lệnh: final TextView tvDescription;.
    final TextView tvDescription;
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
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvName = itemView.findViewById(R.id.tvCategoryName);.
      tvName = itemView.findViewById(R.id.tvCategoryName);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvDescription = itemView.findViewById(R.id.tvCategoryDescription);.
      tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMeta = itemView.findViewById(R.id.tvCategoryMeta);.
      tvMeta = itemView.findViewById(R.id.tvCategoryMeta);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnEdit = itemView.findViewById(R.id.btnEdit);.
      btnEdit = itemView.findViewById(R.id.btnEdit);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: btnDelete = itemView.findViewById(R.id.btnDelete);.
      btnDelete = itemView.findViewById(R.id.btnDelete);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức formatDate với phạm vi truy cập tương ứng.
  private String formatDate(long timestamp) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (timestamp <= 0) return "Not available";
    // Trả về kết quả dateFormat.format(new Date(timestamp));.
    return dateFormat.format(new Date(timestamp));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức descriptionText với phạm vi truy cập tương ứng.
  private String descriptionText(String description) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(description)) return "No description yet";
    // Trả về kết quả description;.
    return description;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
