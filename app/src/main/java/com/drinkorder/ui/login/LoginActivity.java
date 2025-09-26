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

public class LoginActivity extends AppCompatActivity {

  private EditText edtUser, edtPass;
  private Button btnLogin, btnGoRegister;
  private AuthRepository auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    edtUser = findViewById(R.id.edtUser);
    edtPass = findViewById(R.id.edtPass);
    btnLogin = findViewById(R.id.btnLogin);
    btnGoRegister = findViewById(R.id.btnGoRegister);

    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Nếu đã đăng nhập -> vào thẳng Main
    if (auth.isLoggedIn()) {

      startActivity(new Intent(this, MainActivity.class));
      finish();
      return;
    }

    btnLogin.setOnClickListener(v -> doLogin());
    btnGoRegister.setOnClickListener(v ->
            startActivity(new Intent(this, RegisterActivity.class))
    );
  }

  private void doLogin() {
    String u = edtUser.getText().toString().trim();
    String p = edtPass.getText().toString().trim();

    if (u.isEmpty() || p.isEmpty()) {
      Toast.makeText(this, "Vui lòng nhập username và password", Toast.LENGTH_SHORT).show();
      return;
    }

    new Thread(() -> {
      boolean ok = auth.login(u, p); // thực hiện check Room + lưu SharedPreferences
      runOnUiThread(() -> {
        if (ok) {
          Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(this, MainActivity.class));
          finish();
        } else {
          Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
      });
    }).start();
  }
}
