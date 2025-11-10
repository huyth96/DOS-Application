package com.drinkorder.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    tvUsername = findViewById(R.id.tvUsername);
    tvFullName = findViewById(R.id.tvFullName);
    tvEmail = findViewById(R.id.tvEmail);
    tvPhone = findViewById(R.id.tvPhone);
    tvRole = findViewById(R.id.tvRole);
    btnLogout = findViewById(R.id.btnLogout);
    btnEdit = findViewById(R.id.btnEdit);

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

    btnEdit.setOnClickListener(v ->
        startActivityForResult(new Intent(this, EditProfileActivity.class), REQ_EDIT));
  }

  private void loadUser(String username) {
    new Thread(() -> {
      UserEntity user = AppDatabase.get(this).userDao().findByUsername(username);
      runOnUiThread(() -> {
        if (user == null) {
          Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
          return;
        }
        tvUsername.setText(user.username);
        tvFullName.setText(user.fullName == null ? "(not set)" : user.fullName);
        tvEmail.setText(user.email == null ? "(not set)" : user.email);
        tvPhone.setText(user.phone == null ? "(not set)" : user.phone);
        tvRole.setText(user.role == null ? "customer" : user.role);
      });
    }).start();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQ_EDIT && resultCode == RESULT_OK && data != null && data.getBooleanExtra("updated", false)) {
      String username = auth.getLoggedUserName();
      if (username != null) loadUser(username);
    }
  }
}
