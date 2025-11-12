package com.drinkorder.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.UserEntity;
import com.drinkorder.data.repo.AuthRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * ProfileActivity
 * - Màn hình hiển thị thông tin tài khoản người dùng hiện tại.
 * - Cho phép người dùng:
 *   + Xem các thông tin: username, họ tên, email, điện thoại, vai trò.
 *   + Chỉnh sửa hồ sơ cá nhân (mở EditProfileActivity).
 *   + Đăng xuất khỏi hệ thống.
 */
public class ProfileActivity extends AppCompatActivity {
  private TextView tvUsername, tvFullName, tvEmail, tvPhone, tvRole;
  private Button btnLogout, btnEdit;
  private AuthRepository auth;
  private BottomNavigationView bottomNav;

  // Mã request khi quay lại từ EditProfileActivity
  private static final int REQ_EDIT = 1001;
  public static final String EXTRA_SELECTED_TAB = "com.drinkorder.ui.login.ProfileActivity.EXTRA_SELECTED_TAB";

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
    bottomNav = findViewById(R.id.bottomNav);

    // Lấy SharedPreferences (dùng để lưu trạng thái đăng nhập)
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Khởi tạo AuthRepository với DAO và SharedPreferences
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    boolean showCustomerNav = "customer".equalsIgnoreCase(auth.role());
    if (bottomNav != null) {
      if (!showCustomerNav) {
        bottomNav.setVisibility(View.GONE);
      } else {
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.setOnItemSelectedListener(item -> {
          int id = item.getItemId();
          if (id == R.id.tab_profile) return true;
          Intent result = new Intent();
          result.putExtra(EXTRA_SELECTED_TAB, id);
          setResult(RESULT_OK, result);
          finish();
          return true;
        });
        bottomNav.setSelectedItemId(R.id.tab_profile);
      }
    }

    // Kiểm tra xem người dùng hiện tại đã đăng nhập chưa
    String username = auth.getLoggedUserName();
    if (username == null) {
      startActivity(new Intent(this, LoginActivity.class));
      finish();
      return;
    }

    // Nếu đã đăng nhập → tải thông tin người dùng hiện tại
    loadUser(username);

    // Khi nhấn nút "Logout" → đăng xuất, xóa thông tin khỏi SharedPreferences
    btnLogout.setOnClickListener(v -> {
      auth.logout();
      Intent i = new Intent(this, LoginActivity.class);
      // Xóa toàn bộ back stack để không thể quay lại bằng nút Back
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(i);
      finish();
    });

    // Khi nhấn "Edit Profile" → mở EditProfileActivity để chỉnh sửa
    btnEdit.setOnClickListener(v ->
        startActivityForResult(new Intent(this, EditProfileActivity.class), REQ_EDIT));
  }

  /**
   * Tải thông tin người dùng từ cơ sở dữ liệu và hiển thị lên giao diện.
   * @param username Tên tài khoản hiện tại
   */
  private void loadUser(String username) {
    new Thread(() -> {
      // Truy vấn thông tin người dùng từ cơ sở dữ liệu
      UserEntity user = AppDatabase.get(this).userDao().findByUsername(username);
      runOnUiThread(() -> {
        if (user == null) {
          Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
          return;
        }
        // Gán dữ liệu lên giao diện, nếu null thì hiển thị "(not set)"
        tvUsername.setText(user.username);
        tvFullName.setText(user.fullName == null ? "(not set)" : user.fullName);
        tvEmail.setText(user.email == null ? "(not set)" : user.email);
        tvPhone.setText(user.phone == null ? "(not set)" : user.phone);
        tvRole.setText(user.role == null ? "customer" : user.role);
      });
    }).start();
  }

  /**
   * Xử lý khi quay lại từ EditProfileActivity.
   * Nếu người dùng đã cập nhật hồ sơ, tải lại dữ liệu mới nhất.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // Kiểm tra request và kết quả trả về từ EditProfileActivity
    if (requestCode == REQ_EDIT && resultCode == RESULT_OK && data != null && data.getBooleanExtra("updated", false)) {
      // Lấy lại username và tải lại thông tin người dùng để cập nhật UI
      String username = auth.getLoggedUserName();
      if (username != null) loadUser(username);
    }
  }
}

