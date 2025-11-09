package com.drinkorder.ui.admin;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;  // Thêm import này cho EditText
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;  // Thêm import này cho Toast
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.CategoryEntity;

public class AdminCategoriesAdapter extends ListAdapter<CategoryEntity, AdminCategoriesAdapter.ViewHolder> {
    private AdminCategoryVM viewModel; // Để gọi update/delete

    public AdminCategoriesAdapter(AdminCategoryVM viewModel) {
        super(DIFF_CALLBACK);
        this.viewModel = viewModel;
    }

    private static final DiffUtil.ItemCallback<CategoryEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<CategoryEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CategoryEntity oldItem, @NonNull CategoryEntity newItem) {
            return oldItem.categoryId == newItem.categoryId; // So sánh bằng ID
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryEntity oldItem, @NonNull CategoryEntity newItem) {
            return oldItem.name.equals(newItem.name) && oldItem.description.equals(newItem.description); // So sánh nội dung
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryEntity category = getItem(position); // Dùng getItem thay vì categories.get
        if (category == null) return;  // Kiểm tra null để an toàn

        holder.tvName.setText(category.name);
        holder.tvDescription.setText(category.description);

        // Nút sửa (edit)
        holder.btnEdit.setOnClickListener(v -> {
            // Mở AlertDialog để edit
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Sửa danh mục");

            // Inflate layout cho dialog (tạo layout đơn giản hoặc dùng code)
            LinearLayout dialogLayout = new LinearLayout(holder.itemView.getContext());
            dialogLayout.setOrientation(LinearLayout.VERTICAL);
            dialogLayout.setPadding(16, 16, 16, 16);

            EditText etName = new EditText(holder.itemView.getContext());
            etName.setText(category.name);  // Set text hiện tại
            etName.setHint("Tên danh mục");
            dialogLayout.addView(etName);

            EditText etDesc = new EditText(holder.itemView.getContext());
            etDesc.setText(category.description);  // Set text hiện tại
            etDesc.setHint("Mô tả");
            dialogLayout.addView(etDesc);

            builder.setView(dialogLayout);

            builder.setPositiveButton("Lưu", (dialog, which) -> {
                String newName = etName.getText().toString().trim();
                String newDesc = etDesc.getText().toString().trim();
                if (!newName.isEmpty()) {
                    category.name = newName;
                    category.description = newDesc;
                    viewModel.update(category);  // Gọi update từ ViewModel
                    // LiveData sẽ tự refresh list, hoặc thủ công: submitList(getCurrentList());
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Tên không được để trống", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Hủy", null);
            builder.show();
        });

        // Nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            viewModel.delete(category);
            // LiveData sẽ tự cập nhật list sau khi xóa
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}