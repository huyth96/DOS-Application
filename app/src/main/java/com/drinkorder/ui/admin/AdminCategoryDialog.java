// Khai báo package com.drinkorder.ui.admin cho toàn bộ lớp.
package com.drinkorder.ui.admin;

// Import android.app.Dialog để sử dụng các lớp hoặc hàm tương ứng.
import android.app.Dialog;
// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;
// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.appcompat.app.AlertDialog để sử dụng các lớp hoặc hàm tương ứng.
import androidx.appcompat.app.AlertDialog;
// Import androidx.fragment.app.DialogFragment để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.DialogFragment;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import com.google.android.material.dialog.MaterialAlertDialogBuilder để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
// Import com.google.android.material.textfield.TextInputEditText để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.textfield.TextInputEditText;

// Định nghĩa lớp AdminCategoryDialog kế thừa DialogFragment.
public class AdminCategoryDialog extends DialogFragment {

  // Khai báo thuộc tính với phạm vi truy cập: private static final String ARG_IS_EDIT = "arg_is_edit".
  private static final String ARG_IS_EDIT = "arg_is_edit";
  // Khai báo thuộc tính với phạm vi truy cập: private static final String ARG_NAME = "arg_name".
  private static final String ARG_NAME = "arg_name";
  // Khai báo thuộc tính với phạm vi truy cập: private static final String ARG_DESC = "arg_desc".
  private static final String ARG_DESC = "arg_desc";

  // Khai báo thuộc tính với phạm vi truy cập: private TextInputEditText edtName.
  private TextInputEditText edtName;
  // Khai báo thuộc tính với phạm vi truy cập: private TextInputEditText edtDescription.
  private TextInputEditText edtDescription;
  // Khai báo thuộc tính với phạm vi truy cập: private Listener listener.
  private Listener listener;

  // Định nghĩa interface Listener.
  public interface Listener {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onSubmit(String name, String description);.
    void onSubmit(String name, String description);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức newInstance với phạm vi truy cập tương ứng.
  public static AdminCategoryDialog newInstance(@Nullable CategoryEntity category) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: AdminCategoryDialog dialog = new AdminCategoryDialog();.
    AdminCategoryDialog dialog = new AdminCategoryDialog();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Bundle args = new Bundle();.
    Bundle args = new Bundle();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (category != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: args.putBoolean(ARG_IS_EDIT, true);.
      args.putBoolean(ARG_IS_EDIT, true);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: args.putString(ARG_NAME, category.name);.
      args.putString(ARG_NAME, category.name);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: args.putString(ARG_DESC, category.description);.
      args.putString(ARG_DESC, category.description);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: dialog.setArguments(args);.
    dialog.setArguments(args);
    // Trả về kết quả dialog;.
    return dialog;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setListener với phạm vi truy cập tương ứng.
  public void setListener(Listener listener) {
    // Gán giá trị cho biến hoặc thuộc tính: this.listener = listener.
    this.listener = listener;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateDialog với phạm vi truy cập tương ứng.
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_admin_category, null, false);.
    View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_admin_category, null, false);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtName = view.findViewById(R.id.edtCategoryName);.
    edtName = view.findViewById(R.id.edtCategoryName);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtDescription = view.findViewById(R.id.edtCategoryDescription);.
    edtDescription = view.findViewById(R.id.edtCategoryDescription);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: Bundle args = getArguments();.
    Bundle args = getArguments();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: boolean isEdit = args != null && args.getBoolean(ARG_IS_EDIT, false);.
    boolean isEdit = args != null && args.getBoolean(ARG_IS_EDIT, false);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (args != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: edtName.setText(args.getString(ARG_NAME, ""));.
      edtName.setText(args.getString(ARG_NAME, ""));
      // Thực hiện lời gọi phương thức hoặc khởi tạo: edtDescription.setText(args.getString(ARG_DESC, ""));.
      edtDescription.setText(args.getString(ARG_DESC, ""));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext()).
    AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setTitle(isEdit ? "Edit category" : "Add category").
        .setTitle(isEdit ? "Edit category" : "Add category")
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setView(view).
        .setView(view)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setNegativeButton("Cancel", (d, which) -> dismiss()).
        .setNegativeButton("Cancel", (d, which) -> dismiss())
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .setPositiveButton("Save", null).
        .setPositiveButton("Save", null)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .create();.
        .create();

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    dialog.setOnShowListener(d -> {
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: String name = textOf(edtName);.
        String name = textOf(edtName);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (TextUtils.isEmpty(name)) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: edtName.setError("Name cannot be empty");.
          edtName.setError("Name cannot be empty");
          // Trả về kết quả ;.
          return;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Thực hiện lời gọi phương thức hoặc khởi tạo: String description = textOf(edtDescription);.
        String description = textOf(edtDescription);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (listener != null) listener.onSubmit(name.trim(), description.trim());
        // Thực hiện lời gọi phương thức hoặc khởi tạo: dismiss();.
        dismiss();
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });

    // Trả về kết quả dialog;.
    return dialog;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức textOf với phạm vi truy cập tương ứng.
  private String textOf(TextInputEditText edt) {
    // Trả về kết quả edt.getText() == null ? "" : edt.getText().toString().trim();.
    return edt.getText() == null ? "" : edt.getText().toString().trim();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
