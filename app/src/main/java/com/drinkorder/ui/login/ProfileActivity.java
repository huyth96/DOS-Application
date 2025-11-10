// Khai báo package com.drinkorder.ui.login cho toàn bộ lớp.
package com.drinkorder.ui.login;

// Import android.content.Context để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Context;
// Import android.content.Intent để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Intent;
// Import android.content.SharedPreferences để sử dụng các lớp hoặc hàm tương ứng.
import android.content.SharedPreferences;
// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.widget.Button để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Button;
// Import android.widget.TextView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.TextView;
// Import android.widget.Toast để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Toast;

// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.appcompat.app.AppCompatActivity để sử dụng các lớp hoặc hàm tương ứng.
import androidx.appcompat.app.AppCompatActivity;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.entity.UserEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.UserEntity;
// Import com.drinkorder.data.repo.AuthRepository để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.repo.AuthRepository;

// Định nghĩa lớp ProfileActivity kế thừa AppCompatActivity.
public class ProfileActivity extends AppCompatActivity {
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvUsername, tvFullName, tvEmail, tvPhone, tvRole.
  private TextView tvUsername, tvFullName, tvEmail, tvPhone, tvRole;
  // Khai báo thuộc tính với phạm vi truy cập: private Button btnLogout, btnEdit.
  private Button btnLogout, btnEdit;
  // Khai báo thuộc tính với phạm vi truy cập: private AuthRepository auth.
  private AuthRepository auth;

  // Khai báo thuộc tính với phạm vi truy cập: private static final int REQ_EDIT = 1001.
  private static final int REQ_EDIT = 1001;

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreate với phạm vi truy cập tương ứng.
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate(savedInstanceState);.
    super.onCreate(savedInstanceState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: setContentView(R.layout.activity_profile);.
    setContentView(R.layout.activity_profile);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvUsername = findViewById(R.id.tvUsername);.
    tvUsername = findViewById(R.id.tvUsername);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvFullName = findViewById(R.id.tvFullName);.
    tvFullName = findViewById(R.id.tvFullName);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmail = findViewById(R.id.tvEmail);.
    tvEmail = findViewById(R.id.tvEmail);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvPhone = findViewById(R.id.tvPhone);.
    tvPhone = findViewById(R.id.tvPhone);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvRole = findViewById(R.id.tvRole);.
    tvRole = findViewById(R.id.tvRole);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnLogout = findViewById(R.id.btnLogout);.
    btnLogout = findViewById(R.id.btnLogout);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnEdit = findViewById(R.id.btnEdit);.
    btnEdit = findViewById(R.id.btnEdit);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);.
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: String username = auth.getLoggedUserName();.
    String username = auth.getLoggedUserName();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (username == null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(new Intent(this, LoginActivity.class));.
      startActivity(new Intent(this, LoginActivity.class));
      // Thực hiện lời gọi phương thức hoặc khởi tạo: finish();.
      finish();
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: loadUser(username);.
    loadUser(username);

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    btnLogout.setOnClickListener(v -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: auth.logout();.
      auth.logout();
      // Thực hiện lời gọi phương thức hoặc khởi tạo: Intent i = new Intent(this, LoginActivity.class);.
      Intent i = new Intent(this, LoginActivity.class);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);.
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(i);.
      startActivity(i);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: finish();.
      finish();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });

    // Thực thi câu lệnh: btnEdit.setOnClickListener(v ->.
    btnEdit.setOnClickListener(v ->
        // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivityForResult(new Intent(this, EditProfileActivity.class), REQ_EDIT));.
        startActivityForResult(new Intent(this, EditProfileActivity.class), REQ_EDIT));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức loadUser với phạm vi truy cập tương ứng.
  private void loadUser(String username) {
    // Khởi tạo đối tượng mới với biểu thức new Thread(() -> {.
    new Thread(() -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: UserEntity user = AppDatabase.get(this).userDao().findByUsername(username);.
      UserEntity user = AppDatabase.get(this).userDao().findByUsername(username);
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      runOnUiThread(() -> {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (user == null) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();.
          Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
          // Trả về kết quả ;.
          return;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvUsername.setText(user.username);.
        tvUsername.setText(user.username);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvFullName.setText(user.fullName == null ? "(not set)" : user.fullName);.
        tvFullName.setText(user.fullName == null ? "(not set)" : user.fullName);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmail.setText(user.email == null ? "(not set)" : user.email);.
        tvEmail.setText(user.email == null ? "(not set)" : user.email);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvPhone.setText(user.phone == null ? "(not set)" : user.phone);.
        tvPhone.setText(user.phone == null ? "(not set)" : user.phone);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvRole.setText(user.role == null ? "customer" : user.role);.
        tvRole.setText(user.role == null ? "customer" : user.role);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }).start();.
    }).start();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onActivityResult với phạm vi truy cập tương ứng.
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onActivityResult(requestCode, resultCode, data);.
    super.onActivityResult(requestCode, resultCode, data);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (requestCode == REQ_EDIT && resultCode == RESULT_OK && data != null && data.getBooleanExtra("updated", false)) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String username = auth.getLoggedUserName();.
      String username = auth.getLoggedUserName();
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (username != null) loadUser(username);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
