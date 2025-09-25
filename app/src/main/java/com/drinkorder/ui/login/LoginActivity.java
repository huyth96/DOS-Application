package com.drinkorder.ui.login;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.repo.AuthRepository;
import com.drinkorder.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {
  EditText edtUser, edtPass; Button btnLogin; AuthRepository auth;
  @Override protected void onCreate(Bundle b){
    super.onCreate(b);
    setContentView(R.layout.activity_login);
    edtUser = findViewById(R.id.edtUser);
    edtPass = findViewById(R.id.edtPass);
    btnLogin = findViewById(R.id.btnLogin);
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);
    if (auth.isLoggedIn()) goMain();
    btnLogin.setOnClickListener(v -> {
      String u = edtUser.getText().toString().trim();
      String p = edtPass.getText().toString().trim();
      new Thread(() -> {
        boolean ok = auth.login(u,p);
        runOnUiThread(() -> { if (ok) goMain(); else Toast.makeText(this,"Sai tài khoản hoặc mật khẩu",Toast.LENGTH_SHORT).show(); });
      }).start();
    });
  }
  private void goMain(){ startActivity(new Intent(this, MainActivity.class)); finish(); }
}
