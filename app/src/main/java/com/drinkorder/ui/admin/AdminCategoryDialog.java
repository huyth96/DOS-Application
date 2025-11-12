package com.drinkorder.ui.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class AdminCategoryDialog extends DialogFragment { // Định nghĩa lớp AdminCategoryDialog kế thừa từ DialogFragment, dùng để hiển thị dialog thêm/sửa danh mục.

  private static final String ARG_IS_EDIT = "arg_is_edit"; // Hằng số tĩnh lưu key cho argument xác định có phải chỉnh sửa không.
  private static final String ARG_NAME = "arg_name"; // Hằng số tĩnh lưu key cho argument tên danh mục.
  private static final String ARG_DESC = "arg_desc"; // Hằng số tĩnh lưu key cho argument mô tả danh mục.

  private TextInputEditText edtName; // Biến lưu trường input tên danh mục.
  private TextInputEditText edtDescription; // Biến lưu trường input mô tả danh mục.
  private Listener listener; // Biến lưu listener để callback khi submit.

  public interface Listener { // Interface định nghĩa listener cho dialog.
    void onSubmit(String name, String description); // Phương thức gọi khi submit, nhận tên và mô tả.
  }

  public static AdminCategoryDialog newInstance(@Nullable CategoryEntity category) { // Phương thức tĩnh tạo instance mới của dialog, nhận entity danh mục có thể null.
    AdminCategoryDialog dialog = new AdminCategoryDialog(); // Tạo instance mới.
    Bundle args = new Bundle(); // Tạo Bundle để lưu arguments.
    if (category != null) { // Nếu category không null (chỉnh sửa).
      args.putBoolean(ARG_IS_EDIT, true); // Đặt argument là chỉnh sửa.
      args.putString(ARG_NAME, category.name); // Đặt tên từ entity.
      args.putString(ARG_DESC, category.description); // Đặt mô tả từ entity.
    }
    dialog.setArguments(args); // Gán arguments cho dialog.
    return dialog; // Trả về dialog.
  }

  public void setListener(Listener listener) { // Phương thức setter cho listener.
    this.listener = listener; // Gán listener.
  }

  @NonNull // Annotation chỉ ra phương thức trả về không null.
  @Override // Ghi đè phương thức onCreateDialog từ DialogFragment.
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) { // Phương thức tạo dialog, nhận savedInstanceState có thể null.
    View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_admin_category, null, false); // Inflate layout cho dialog từ resource.
    edtName = view.findViewById(R.id.edtCategoryName); // Tìm và lưu trường input tên.
    edtDescription = view.findViewById(R.id.edtCategoryDescription); // Tìm và lưu trường input mô tả.

    Bundle args = getArguments(); // Lấy arguments của fragment.
    boolean isEdit = args != null && args.getBoolean(ARG_IS_EDIT, false); // Xác định có phải chỉnh sửa không, mặc định false.
    if (args != null) { // Nếu arguments không null.
      edtName.setText(args.getString(ARG_NAME, "")); // Đặt text tên từ argument, mặc định rỗng.
      edtDescription.setText(args.getString(ARG_DESC, "")); // Đặt text mô tả từ argument, mặc định rỗng.
    }

    AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext()) // Tạo builder cho AlertDialog sử dụng Material theme.
            .setTitle(isEdit ? "Edit category" : "Add category") // Đặt tiêu đề dựa trên việc chỉnh sửa hay thêm mới.
            .setView(view) // Đặt view inflate làm nội dung dialog.
            .setNegativeButton("Cancel", (d, which) -> dismiss()) // Đặt nút negative là Cancel, gọi dismiss khi click.
            .setPositiveButton("Save", null) // Đặt nút positive là Save, nhưng listener null tạm thời.
            .create(); // Tạo dialog từ builder.

    dialog.setOnShowListener(d -> { // Thiết lập listener khi dialog hiển thị.
      dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> { // Thiết lập listener cho nút positive (Save).
        String name = textOf(edtName); // Lấy tên từ input.
        if (TextUtils.isEmpty(name)) { // Kiểm tra tên rỗng.
          edtName.setError("Name cannot be empty"); // Hiển thị lỗi.
          return; // Thoát.
        }
        String description = textOf(edtDescription); // Lấy mô tả từ input.
        if (listener != null) listener.onSubmit(name.trim(), description.trim()); // Nếu có listener, gọi onSubmit với tên và mô tả đã trim.
        dismiss(); // Dismiss dialog.
      });
    });

    return dialog; // Trả về dialog đã tạo.
  }

  private String textOf(TextInputEditText edt) { // Phương thức private lấy text từ TextInputEditText, xử lý null và trim.
    return edt.getText() == null ? "" : edt.getText().toString().trim(); // Trả về rỗng nếu null, иначе trim text.
  }
}