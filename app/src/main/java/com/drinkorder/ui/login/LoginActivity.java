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
import com.drinkorder.data.repo.AuthRepository;
import com.drinkorder.ui.MainActivity;

/**
 * LoginActivity
 * - Màn hình đăng nhập: nhận username/password, gọi AuthRepository để xác thực.
 * - Nếu đã có phiên đăng nhập, bỏ qua màn hình này và chuyển thẳng sang MainActivity.
 */
public class LoginActivity extends AppCompatActivity {

  /** Ô nhập username/password. */
  private EditText edtUser, edtPass;
  /** Nút đăng nhập và điều hướng sang đăng ký. */
  private Button btnLogin, btnGoRegister;
  /** Repository xác thực dùng chung trong Activity. */
  private AuthRepository auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // Bind view
    edtUser = findViewById(R.id.edtUser);
    edtPass = findViewById(R.id.edtPass);
    btnLogin = findViewById(R.id.btnLogin);
    btnGoRegister = findViewById(R.id.btnGoRegister);

    // Khởi tạo AuthRepository với SharedPreferences "auth"
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Nếu đã đăng nhập -> chuyển sang MainActivity
    if (auth.isLoggedIn()) {
      startActivity(new Intent(this, MainActivity.class));
      finish();
      return;
    }

    // Sự kiện nút
    btnLogin.setOnClickListener(v -> doLogin());
    btnGoRegister.setOnClickListener(v ->
        startActivity(new Intent(this, RegisterActivity.class))
    );
  }

  /**
   * Thực hiện đăng nhập:
   * - Lấy input, kiểm tra rỗng.
   * - Gọi auth.login() trong background thread (tránh chặn UI).
   * - Chuyển kết quả về UI để điều hướng/hiển thị thông báo.
   */
  private void doLogin() {
    String u = edtUser.getText().toString().trim();
    String p = edtPass.getText().toString().trim();

    if (u.isEmpty() || p.isEmpty()) {
      Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
      return;
    }

    new Thread(() -> {
      AuthRepository.LoginStatus status = auth.login(u, p);
      runOnUiThread(() -> {
        if (status == AuthRepository.LoginStatus.SUCCESS) {
          Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(this, MainActivity.class));
          finish();
        } else if (status == AuthRepository.LoginStatus.BANNED) {
          Toast.makeText(this, "Your account has been banned. Contact support.", Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
      });
    }).start();
  }
}

