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
 * Màn hình Đăng nhập (LoginActivity)
 * -------------------------------
 * Chức năng:
 * - Nhập username và password để đăng nhập.
 * - Gọi AuthRepository để kiểm tra thông tin đăng nhập.
 * - Nếu đã đăng nhập từ trước thì bỏ qua màn hình này.
 */
public class LoginActivity extends AppCompatActivity {

  /** Ô nhập username và password */
  private EditText edtUser, edtPass;

  /** Hai nút: đăng nhập và chuyển sang màn hình đăng ký */
  private Button btnLogin, btnGoRegister;

  /** Repository xử lý logic xác thực người dùng */
  private AuthRepository auth;

  /**
   * @Override
   * Hàm này được gọi khi Activity được tạo lần đầu.
   * → Dùng để khởi tạo layout, ánh xạ view, gán sự kiện và logic ban đầu.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login); // Gắn layout giao diện

    // Gắn view trong layout vào biến
    edtUser = findViewById(R.id.edtUser);
    edtPass = findViewById(R.id.edtPass);
    btnLogin = findViewById(R.id.btnLogin);
    btnGoRegister = findViewById(R.id.btnGoRegister);

    // Tạo đối tượng AuthRepository để dùng cho đăng nhập.
    // Dùng SharedPreferences “auth” để lưu trạng thái người dùng.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Nếu người dùng đã đăng nhập từ trước → chuyển thẳng sang MainActivity.
    if (auth.isLoggedIn()) {
      startActivity(new Intent(this, MainActivity.class));
      finish(); // Đóng màn hình Login để không quay lại được nữa.
      return;
    }

    // Sự kiện bấm nút “Đăng nhập”
    btnLogin.setOnClickListener(v -> doLogin());

    // Sự kiện bấm “Đăng ký” → chuyển sang màn hình RegisterActivity
    btnGoRegister.setOnClickListener(v ->
            startActivity(new Intent(this, RegisterActivity.class))
    );
  }

  /**
   * Thực hiện quá trình đăng nhập:
   * 1. Lấy username và password từ ô nhập.
   * 2. Kiểm tra xem có để trống không.
   * 3. Gọi AuthRepository để kiểm tra tài khoản (chạy trong thread phụ).
   * 4. Dựa vào kết quả trả về → cập nhật giao diện (UI thread).
   */
  private void doLogin() {
    String u = edtUser.getText().toString().trim();
    String p = edtPass.getText().toString().trim();

    // Nếu trống → thông báo và dừng lại.
    if (u.isEmpty() || p.isEmpty()) {
      Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
      return;
    }

    // Chạy đăng nhập trong thread phụ (tránh đứng UI).
    new Thread(() -> {
      AuthRepository.LoginStatus status = auth.login(u, p);

      // Kết quả xong → quay lại luồng chính để hiển thị thông báo.
      runOnUiThread(() -> {
        if (status == AuthRepository.LoginStatus.SUCCESS) {
          // Đăng nhập thành công
          Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(this, MainActivity.class));
          finish(); // Đóng màn hình login để không quay lại được nữa
        } else if (status == AuthRepository.LoginStatus.BANNED) {
          // Tài khoản bị ban
          Toast.makeText(this, "Your account has been banned. Contact support.", Toast.LENGTH_LONG).show();
        } else {
          // Sai username hoặc password
          Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
      });
    }).start();
  }
}
