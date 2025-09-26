package com.drinkorder.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.UserEntity;
import com.drinkorder.data.repo.AuthRepository;

public class EditProfileActivity extends AppCompatActivity {
  private EditText edtFullName, edtEmail, edtPhone, edtNewPass;
  private Button btnSave, btnCancel;
  private AuthRepository auth;

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_edit_profile);

    edtFullName = findViewById(R.id.edtFullName);
    edtEmail    = findViewById(R.id.edtEmail);
    edtPhone    = findViewById(R.id.edtPhone);
    edtNewPass  = findViewById(R.id.edtNewPass);
    btnSave     = findViewById(R.id.btnSave);
    btnCancel   = findViewById(R.id.btnCancel);

    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    String username = auth.getLoggedUserName();
    if (username == null) { finish(); return; }

    // Prefill
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

      if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
        return;
      }

      new Thread(() -> {
        var dao = AppDatabase.get(this).userDao();
        dao.updateProfile(username, full, email, phone);
        if (!newPass.isEmpty()) dao.updatePassword(username, newPass); // TODO: hash sau
        runOnUiThread(() -> {
          Toast.makeText(this, "Đã lưu thay đổi", Toast.LENGTH_SHORT).show();
          setResult(RESULT_OK, new Intent().putExtra("updated", true));
          finish();
        });
      }).start();
    });

    btnCancel.setOnClickListener(v -> finish());
  }
}
