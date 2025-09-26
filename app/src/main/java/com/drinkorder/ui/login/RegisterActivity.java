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
import com.drinkorder.ui.MainActivity;

public class RegisterActivity extends AppCompatActivity {
  private EditText edtUser, edtPass, edtFullName, edtEmail, edtPhone;
  private Button btnRegister, btnBackLogin;
  private AuthRepository auth;

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_register);

    edtUser = findViewById(R.id.edtUser);
    edtPass = findViewById(R.id.edtPass);
    edtFullName = findViewById(R.id.edtFullName);
    edtEmail = findViewById(R.id.edtEmail);
    edtPhone = findViewById(R.id.edtPhone);
    btnRegister = findViewById(R.id.btnRegister);
    btnBackLogin = findViewById(R.id.btnBackLogin);

    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    btnRegister.setOnClickListener(v -> doRegister());
    btnBackLogin.setOnClickListener(v -> {
      startActivity(new Intent(this, LoginActivity.class));
      finish();
    });
  }

  private void doRegister() {
    String u = edtUser.getText().toString().trim();
    String p = edtPass.getText().toString().trim();
    String full = edtFullName.getText().toString().trim();
    String email = edtEmail.getText().toString().trim();
    String phone = edtPhone.getText().toString().trim();

    if (u.isEmpty() || p.isEmpty()) {
      Toast.makeText(this, "Vui lòng nhập username & password", Toast.LENGTH_SHORT).show();
      return;
    }

    new Thread(() -> {
      try {
        // Check tồn tại
        var dao = AppDatabase.get(this).userDao();
        if (dao.findByUsername(u) != null) {
          runOnUiThread(() ->
                  Toast.makeText(this, "Username đã tồn tại", Toast.LENGTH_SHORT).show()
          );
          return;
        }

        // Tạo user (passwordHash hiện đang lưu dạng plain trong project)
        UserEntity user = new UserEntity();
        user.username = u;
        user.passwordHash = p; // TODO: có thể thay bằng hashing sau
        user.fullName = full;
        user.email = email;
        user.phone = phone;
        user.role = "customer";
        user.createdAt = System.currentTimeMillis();
        dao.insert(user);

        // Auto login rồi vào Main
        boolean ok = auth.login(u, p);
        runOnUiThread(() -> {
          if (ok) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
          } else {
            Toast.makeText(this, "Đăng ký thành công, nhưng auto login lỗi", Toast.LENGTH_SHORT).show();
          }
        });
      } catch (Exception e) {
        runOnUiThread(() ->
                Toast.makeText(this, "Lỗi đăng ký: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
      }
    }).start();
  }
}
