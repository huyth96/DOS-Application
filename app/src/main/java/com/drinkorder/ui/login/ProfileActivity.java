package com.drinkorder.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.UserEntity;
import com.drinkorder.data.repo.AuthRepository;

public class ProfileActivity extends AppCompatActivity {
  private TextView tvUsername, tvFullName, tvEmail, tvPhone, tvRole;
  private Button btnLogout, btnEdit;
  private AuthRepository auth;

  private static final int REQ_EDIT = 1001;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState); // ✅ fix typo
    setContentView(R.layout.activity_profile);

    tvUsername = findViewById(R.id.tvUsername);
    tvFullName = findViewById(R.id.tvFullName);
    tvEmail    = findViewById(R.id.tvEmail);
    tvPhone    = findViewById(R.id.tvPhone);
    tvRole     = findViewById(R.id.tvRole);
    btnLogout  = findViewById(R.id.btnLogout);
    btnEdit    = findViewById(R.id.btnEdit); // ✅ nhớ có trong layout

    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    String username = auth.getLoggedUserName();
    if (username == null) {
      startActivity(new Intent(this, LoginActivity.class));
      finish();
      return;
    }

    loadUser(username);

    btnLogout.setOnClickListener(v -> {
      auth.logout();
      Intent i = new Intent(this, LoginActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(i);
      finish();
    });

    btnEdit.setOnClickListener(v -> {
      startActivityForResult(new Intent(this, EditProfileActivity.class), REQ_EDIT);
    });
  }

  private void loadUser(String username) {
    new Thread(() -> {
      UserEntity u = AppDatabase.get(this).userDao().findByUsername(username);
      runOnUiThread(() -> {
        if (u != null) {
          tvUsername.setText(u.username);
          tvFullName.setText(u.fullName == null ? "(chưa cập nhật)" : u.fullName);
          tvEmail.setText(u.email == null ? "(chưa cập nhật)" : u.email);
          tvPhone.setText(u.phone == null ? "(chưa cập nhật)" : u.phone);
          tvRole.setText(u.role);
        } else {
          Toast.makeText(this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
        }
      });
    }).start();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQ_EDIT && resultCode == RESULT_OK && data != null && data.getBooleanExtra("updated", false)) {
      String username = auth.getLoggedUserName();
      if (username != null) loadUser(username);
    }
  }
}
