package com.drinkorder.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.UserEntity;
import com.drinkorder.data.repo.AuthRepository;


/**
 * EditProfileActivity
 * - Màn hình cho phép người dùng chỉnh sửa thông tin cá nhân:
 *   + Họ tên, Email, Số điện thoại
 *   + Có thể thay đổi mật khẩu mới (tùy chọn)
 */
public class EditProfileActivity extends AppCompatActivity {
  private EditText edtFullName, edtEmail, edtPhone, edtNewPass;
  private Button btnSave, btnCancel;
  // Repository để thao tác với user (login, cập nhật, lưu thông tin)
  private AuthRepository auth;

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_edit_profile);

    // Ánh xạ view từ layout
    edtFullName = findViewById(R.id.edtFullName);
    edtEmail    = findViewById(R.id.edtEmail);
    edtPhone    = findViewById(R.id.edtPhone);
    edtNewPass  = findViewById(R.id.edtNewPass);
    btnSave     = findViewById(R.id.btnSave);
    btnCancel   = findViewById(R.id.btnCancel);

    // Lấy SharedPreferences để xác định người dùng hiện tại
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Khởi tạo AuthRepository dùng database và shared preferences
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Lấy username người đang đăng nhập
    String username = auth.getLoggedUserName();
    if (username == null) { finish(); return; }

    // Nạp dữ liệu hiện tại vào form
    new Thread(() -> {
      UserEntity u = AppDatabase.get(this).userDao().findByUsername(username);
      runOnUiThread(() -> {
        if (u != null) {
          edtFullName.setText(u.fullName);
          edtEmail.setText(u.email);
          edtPhone.setText(u.phone);
        }
      });
    }).start();

    btnSave.setOnClickListener(v -> {
      String full = edtFullName.getText().toString().trim();
      String email = edtEmail.getText().toString().trim();
      String phone = edtPhone.getText().toString().trim();
      String newPass = edtNewPass.getText().toString().trim();

      // Kiểm tra định dạng email hợp lệ
      if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
        return;
      }
      // Sau khi cập nhật xong, quay lại UI thông báo
      new Thread(() -> {
        var dao = AppDatabase.get(this).userDao();
        dao.updateProfile(username, full, email, phone);
        if (!newPass.isEmpty()) dao.updatePassword(username, newPass); // DEMO: chưa hash
        runOnUiThread(() -> {
          Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
          setResult(RESULT_OK, new Intent().putExtra("updated", true));
          finish();
        });
      }).start();
    });

    btnCancel.setOnClickListener(v -> finish());
  }
}

