package com.drinkorder.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.UserEntity;
import com.drinkorder.data.repo.AuthRepository;
import com.drinkorder.ui.MainActivity;

/**
 * RegisterActivity
 * - Màn hình tạo tài khoản mới (customer) và tự động đăng nhập sau khi tạo thành công.
 * - Lưu ý bảo mật: demo lưu plaintext password; khi triển khai thực tế cần hash/salt.
 */
public class RegisterActivity extends AppCompatActivity {

  private EditText edtUser, edtPass, edtFullName, edtEmail, edtPhone;
  private Button btnRegister, btnBackLogin;
  private AuthRepository auth;

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_register);

    // Bind view
    edtUser = findViewById(R.id.edtUser);
    edtPass = findViewById(R.id.edtPass);
    edtFullName = findViewById(R.id.edtFullName);
    edtEmail = findViewById(R.id.edtEmail);
    edtPhone = findViewById(R.id.edtPhone);
    btnRegister = findViewById(R.id.btnRegister);
    btnBackLogin = findViewById(R.id.btnBackLogin);

    // Chuẩn bị repository xác thực
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Sự kiện nút
    btnRegister.setOnClickListener(v -> doRegister());
    btnBackLogin.setOnClickListener(v -> {
      startActivity(new Intent(this, LoginActivity.class));
      finish();
    });
  }

  /**
   * Tạo tài khoản mới:
   * - Validate input cơ bản.
   * - Kiểm tra trùng username trong Room.
   * - Insert bản ghi UserEntity (role=customer, isBanned=false, createdAt=now).
   * - Auto login và điều hướng sang MainActivity khi thành công.
   */
  private void doRegister() {
    String u = edtUser.getText().toString().trim();
    String p = edtPass.getText().toString().trim();
    String full = edtFullName.getText().toString().trim();
    String email = edtEmail.getText().toString().trim();
    String phone = edtPhone.getText().toString().trim();

    if (u.isEmpty() || p.isEmpty()) {
      Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
      return;
    }
    // Không bắt buộc email hợp lệ trong bản demo; cho phép để trống hoặc điền sau ở Edit Profile

    new Thread(() -> {
      try {
        var dao = AppDatabase.get(this).userDao();
        // Tránh trùng username
        if (dao.findByUsername(u) != null) {
          runOnUiThread(() -> Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show());
          return;
        }
        // Tạo user mới
        UserEntity user = new UserEntity();
        user.username = u;
        user.passwordHash = p; // DEMO: chưa hash
        user.fullName = full;
        user.email = email;
        user.phone = phone;
        user.role = "customer";
        user.createdAt = System.currentTimeMillis();
        user.isBanned = false;
        dao.insert(user);

        // Auto-login sau khi đăng ký thành công
        AuthRepository.LoginStatus status = auth.login(u, p);
        runOnUiThread(() -> {
          if (status == AuthRepository.LoginStatus.SUCCESS) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
          } else if (status == AuthRepository.LoginStatus.BANNED) {
            Toast.makeText(this, "Account is temporarily disabled", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(this, "Registered successfully, but automatic login failed", Toast.LENGTH_SHORT).show();
          }
        });
      } catch (Exception e) {
        runOnUiThread(() ->
            Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
      }
    }).start();
  }
}
