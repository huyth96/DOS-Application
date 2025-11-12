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
 * Màn hình Đăng ký (RegisterActivity)
 * -----------------------------------
 * - Cho phép người dùng tạo tài khoản mới (mặc định role = "customer").
 * - Kiểm tra trùng username, lưu thông tin vào cơ sở dữ liệu Room.
 * - Sau khi đăng ký thành công → tự động đăng nhập và chuyển sang MainActivity.
 * - Lưu ý: bản demo lưu password ở dạng plaintext. Khi triển khai thực tế cần hash/salt.
 */
public class RegisterActivity extends AppCompatActivity {

  // Các ô nhập thông tin
  private EditText edtUser, edtPass, edtFullName, edtEmail, edtPhone;

  // Hai nút: Đăng ký và quay lại màn hình đăng nhập
  private Button btnRegister, btnBackLogin;

  // Repository xử lý xác thực (dùng chung với LoginActivity)
  private AuthRepository auth;

  /**
   * @Override
   * Hàm này được gọi khi Activity được khởi tạo.
   * Dùng để gắn layout, ánh xạ view, và thiết lập sự kiện cho các nút.
   */
  @Override
  protected void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_register); // Gắn layout giao diện

    // Gán các view từ XML vào biến
    edtUser = findViewById(R.id.edtUser);
    edtPass = findViewById(R.id.edtPass);
    edtFullName = findViewById(R.id.edtFullName);
    edtEmail = findViewById(R.id.edtEmail);
    edtPhone = findViewById(R.id.edtPhone);
    btnRegister = findViewById(R.id.btnRegister);
    btnBackLogin = findViewById(R.id.btnBackLogin);

    // Chuẩn bị AuthRepository để xử lý tạo tài khoản và đăng nhập
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Khi người dùng bấm “Register” → chạy hàm doRegister()
    btnRegister.setOnClickListener(v -> doRegister());

    // Khi bấm “Back to Login” → quay lại LoginActivity
    btnBackLogin.setOnClickListener(v -> {
      startActivity(new Intent(this, LoginActivity.class));
      finish(); // Đóng màn hình hiện tại
    });
  }

  /**
   * Thực hiện đăng ký tài khoản mới:
   * 1️⃣ Lấy dữ liệu người dùng nhập.
   * 2️⃣ Kiểm tra dữ liệu có bị trống không.
   * 3️⃣ Kiểm tra trùng username trong database.
   * 4️⃣ Nếu hợp lệ → tạo UserEntity mới, lưu vào Room database.
   * 5️⃣ Đăng nhập tự động và chuyển sang MainActivity.
   */
  private void doRegister() {
    String u = edtUser.getText().toString().trim();
    String p = edtPass.getText().toString().trim();
    String full = edtFullName.getText().toString().trim();
    String email = edtEmail.getText().toString().trim();
    String phone = edtPhone.getText().toString().trim();

    // Bước 1: kiểm tra username & password có rỗng không
    if (u.isEmpty() || p.isEmpty()) {
      Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
      return;
    }

    // Bước 2: chạy các thao tác DB trong thread phụ để không làm đơ giao diện
    new Thread(() -> {
      try {
        // Lấy DAO để truy cập bảng user trong Room database
        var dao = AppDatabase.get(this).userDao();

        // Kiểm tra username có tồn tại chưa
        if (dao.findByUsername(u) != null) {
          runOnUiThread(() ->
                  Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
          );
          return;
        }

        // Bước 3: tạo đối tượng UserEntity mới để insert vào DB
        UserEntity user = new UserEntity();
        user.username = u;
        user.passwordHash = p; // ⚠ DEMO: chưa mã hóa password
        user.fullName = full;
        user.email = email;
        user.phone = phone;
        user.role = "customer"; // Tài khoản thường
        user.createdAt = System.currentTimeMillis();
        user.isBanned = false;

        dao.insert(user); // Lưu vào database

        // Bước 4: đăng nhập tự động ngay sau khi đăng ký xong
        AuthRepository.LoginStatus status = auth.login(u, p);

        // Quay về luồng chính để cập nhật giao diện (hiển thị Toast, chuyển màn)
        runOnUiThread(() -> {
          if (status == AuthRepository.LoginStatus.SUCCESS) {
            // Đăng ký & đăng nhập thành công → chuyển sang màn chính
            startActivity(new Intent(this, MainActivity.class));
            finish();
          } else if (status == AuthRepository.LoginStatus.BANNED) {
            // Tài khoản bị ban (hiếm khi xảy ra)
            Toast.makeText(this, "Account is temporarily disabled", Toast.LENGTH_SHORT).show();
          } else {
            // Đăng ký ok nhưng auto login bị lỗi
            Toast.makeText(this,
                    "Registered successfully, but automatic login failed",
                    Toast.LENGTH_SHORT).show();
          }
        });
      } catch (Exception e) {
        // Nếu có lỗi (ví dụ: lỗi database), hiển thị thông báo cho người dùng
        runOnUiThread(() ->
                Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
      }
    }).start();
  }
}
