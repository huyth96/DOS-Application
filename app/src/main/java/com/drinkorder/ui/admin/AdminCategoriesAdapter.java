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

/**
 * Adapter cho RecyclerView hiển thị danh sách các danh mục (CategoryEntity) trong giao diện admin.
 * Kế thừa từ RecyclerView.Adapter, quản lý dữ liệu danh mục và xử lý sự kiện chỉnh sửa/xóa qua callback.
 * Sử dụng ViewHolder để tối ưu hóa hiệu suất hiển thị.
 */
public class AdminCategoriesAdapter extends RecyclerView.Adapter<AdminCategoriesAdapter.VH> {

  /**
   * Interface Callback để xử lý sự kiện chỉnh sửa và xóa danh mục.
   * Được sử dụng để thông báo cho lớp gọi (ví dụ: Fragment hoặc Activity) khi người dùng tương tác.
   */
  public interface Callback {
    /**
     * Gọi khi người dùng nhấn nút chỉnh sửa danh mục.
     *
     * @param category Danh mục cần chỉnh sửa.
     */
    void onEdit(CategoryEntity category);

    /**
     * Gọi khi người dùng nhấn nút xóa danh mục.
     *
     * @param category Danh mục cần xóa.
     */
    void onDelete(CategoryEntity category);
  }

  /**
   * Callback để xử lý sự kiện từ adapter.
   */
  private final Callback callback;

  /**
   * Danh sách các danh mục để hiển thị, sử dụng ArrayList để dễ quản lý.
   */
  private final List<CategoryEntity> items = new ArrayList<>();

  /**
   * Định dạng ngày tháng để hiển thị thời gian tạo danh mục.
   */
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

  /**
   * Constructor để khởi tạo adapter với callback.
   *
   * @param callback Callback để xử lý sự kiện chỉnh sửa/xóa.
   */
  public AdminCategoriesAdapter(Callback callback) {
    this.callback = callback;
  }

  /**
   * Cập nhật dữ liệu danh sách danh mục và thông báo adapter refresh giao diện.
   * Xóa dữ liệu cũ, thêm dữ liệu mới nếu có, và gọi notifyDataSetChanged để cập nhật RecyclerView.
   *
   * @param categories Danh sách danh mục mới để hiển thị.
   */
  public void submit(List<CategoryEntity> categories) {
    items.clear();
    if (categories != null) items.addAll(categories);
    notifyDataSetChanged();
  }

  /**
   * Tạo ViewHolder mới cho item trong RecyclerView.
   * Inflate layout từ file XML R.layout.item_admin_category.
   *
   * @param parent ViewGroup cha chứa item.
   * @param viewType Loại view (không sử dụng ở đây).
   * @return ViewHolder mới.
   */
  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_category, parent, false);
    return new VH(v);
  }

  /**
   * Bind dữ liệu vào ViewHolder tại vị trí position.
   * Thiết lập text cho tên, mô tả, meta (thời gian tạo), và lắng nghe sự kiện click cho nút edit/delete.
   *
   * @param holder ViewHolder cần bind dữ liệu.
   * @param position Vị trí item trong danh sách.
   */
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

  /**
   * Trả về số lượng item trong danh sách.
   *
   * @return Số lượng danh mục.
   */
  @Override
  public int getItemCount() {
    return items.size();
  }

  /**
   * Lớp ViewHolder static để giữ các view con của item, tối ưu hóa hiệu suất bằng cách reuse view.
   */
  static class VH extends RecyclerView.ViewHolder {
    /**
     * TextView hiển thị tên danh mục.
     */
    final TextView tvName;

    /**
     * TextView hiển thị mô tả danh mục.
     */
    final TextView tvDescription;

    /**
     * TextView hiển thị thông tin meta (thời gian tạo).
     */
    final TextView tvMeta;

    /**
     * Nút chỉnh sửa danh mục.
     */
    final MaterialButton btnEdit;

    /**
     * Nút xóa danh mục.
     */
    final MaterialButton btnDelete;

    /**
     * Constructor ViewHolder, find và gán các view từ itemView.
     *
     * @param itemView View của item.
     */
    VH(@NonNull View itemView) {
      super(itemView);
      tvName = itemView.findViewById(R.id.tvCategoryName);
      tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
      tvMeta = itemView.findViewById(R.id.tvCategoryMeta);
      btnEdit = itemView.findViewById(R.id.btnEdit);
      btnDelete = itemView.findViewById(R.id.btnDelete);
    }
  }

  /**
   * Định dạng timestamp thành chuỗi ngày tháng dễ đọc.
   * Nếu timestamp <= 0, trả về "Not available".
   *
   * @param timestamp Thời gian tạo (long).
   * @return Chuỗi ngày tháng định dạng.
   */
  private String formatDate(long timestamp) {
    if (timestamp <= 0) return "Not available";
    return dateFormat.format(new Date(timestamp));
  }

  /**
   * Xử lý text mô tả: nếu rỗng thì trả về "No description yet", ngược lại trả về mô tả gốc.
   *
   * @param description Mô tả danh mục.
   * @return Text hiển thị cho mô tả.
   */
  private String descriptionText(String description) {
    if (TextUtils.isEmpty(description)) return "No description yet";
    return description;
  }
}
