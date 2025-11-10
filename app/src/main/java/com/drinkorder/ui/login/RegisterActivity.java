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
// Import android.widget.EditText để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.EditText;
// Import android.widget.Toast để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Toast;
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
// Import com.drinkorder.ui.MainActivity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.MainActivity;

// Định nghĩa lớp RegisterActivity kế thừa AppCompatActivity.
public class RegisterActivity extends AppCompatActivity {
  // Khai báo thuộc tính với phạm vi truy cập: private EditText edtUser, edtPass, edtFullName, edtEmail, edtPhone.
  private EditText edtUser, edtPass, edtFullName, edtEmail, edtPhone;
  // Khai báo thuộc tính với phạm vi truy cập: private Button btnRegister, btnBackLogin.
  private Button btnRegister, btnBackLogin;
  // Khai báo thuộc tính với phạm vi truy cập: private AuthRepository auth.
  private AuthRepository auth;

  // Áp dụng annotation @Override và ghi đè phương thức onCreate.
  @Override protected void onCreate(Bundle b) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate(b);.
    super.onCreate(b);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: setContentView(R.layout.activity_register);.
    setContentView(R.layout.activity_register);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtUser = findViewById(R.id.edtUser);.
    edtUser = findViewById(R.id.edtUser);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtPass = findViewById(R.id.edtPass);.
    edtPass = findViewById(R.id.edtPass);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtFullName = findViewById(R.id.edtFullName);.
    edtFullName = findViewById(R.id.edtFullName);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtEmail = findViewById(R.id.edtEmail);.
    edtEmail = findViewById(R.id.edtEmail);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtPhone = findViewById(R.id.edtPhone);.
    edtPhone = findViewById(R.id.edtPhone);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnRegister = findViewById(R.id.btnRegister);.
    btnRegister = findViewById(R.id.btnRegister);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnBackLogin = findViewById(R.id.btnBackLogin);.
    btnBackLogin = findViewById(R.id.btnBackLogin);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);.
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnRegister.setOnClickListener(v -> doRegister());.
    btnRegister.setOnClickListener(v -> doRegister());
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    btnBackLogin.setOnClickListener(v -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(new Intent(this, LoginActivity.class));.
      startActivity(new Intent(this, LoginActivity.class));
      // Thực hiện lời gọi phương thức hoặc khởi tạo: finish();.
      finish();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức doRegister với phạm vi truy cập tương ứng.
  private void doRegister() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String u = edtUser.getText().toString().trim();.
    String u = edtUser.getText().toString().trim();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String p = edtPass.getText().toString().trim();.
    String p = edtPass.getText().toString().trim();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String full = edtFullName.getText().toString().trim();.
    String full = edtFullName.getText().toString().trim();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String email = edtEmail.getText().toString().trim();.
    String email = edtEmail.getText().toString().trim();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String phone = edtPhone.getText().toString().trim();.
    String phone = edtPhone.getText().toString().trim();

    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (u.isEmpty() || p.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Please enter username & password", Toast.LENGTH_SHORT).show();.
      Toast.makeText(this, "Please enter username & password", Toast.LENGTH_SHORT).show();
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Khởi tạo đối tượng mới với biểu thức new Thread(() -> {.
    new Thread(() -> {
      // Bắt đầu khối try để bắt lỗi có thể phát sinh.
      try {
        // Check tồn tại
        // Thực hiện lời gọi phương thức hoặc khởi tạo: var dao = AppDatabase.get(this).userDao();.
        var dao = AppDatabase.get(this).userDao();
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (dao.findByUsername(u) != null) {
          // Thực thi câu lệnh: runOnUiThread(() ->.
          runOnUiThread(() ->
                  // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show().
                  Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
          // Thực hiện lời gọi phương thức hoặc khởi tạo: );.
          );
          // Trả về kết quả ;.
          return;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }

        // Tạo user (passwordHash hiện đang lưu dạng plain trong project)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: UserEntity user = new UserEntity();.
        UserEntity user = new UserEntity();
        // Gán giá trị cho biến hoặc thuộc tính: user.username = u.
        user.username = u;
        // Thực thi câu lệnh: user.passwordHash = p; // TODO: có thể thay bằng hashing sau.
        user.passwordHash = p; // TODO: có thể thay bằng hashing sau
        // Gán giá trị cho biến hoặc thuộc tính: user.fullName = full.
        user.fullName = full;
        // Gán giá trị cho biến hoặc thuộc tính: user.email = email.
        user.email = email;
        // Gán giá trị cho biến hoặc thuộc tính: user.phone = phone.
        user.phone = phone;
        // Gán giá trị cho biến hoặc thuộc tính: user.role = "customer".
        user.role = "customer";
        // Thực hiện lời gọi phương thức hoặc khởi tạo: user.createdAt = System.currentTimeMillis();.
        user.createdAt = System.currentTimeMillis();
        // Gán giá trị cho biến hoặc thuộc tính: user.isBanned = false.
        user.isBanned = false;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: dao.insert(user);.
        dao.insert(user);

        // Auto login rồi vào Main
        // Thực hiện lời gọi phương thức hoặc khởi tạo: AuthRepository.LoginStatus status = auth.login(u, p);.
        AuthRepository.LoginStatus status = auth.login(u, p);
        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        runOnUiThread(() -> {
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (status == AuthRepository.LoginStatus.SUCCESS) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(new Intent(this, MainActivity.class));.
            startActivity(new Intent(this, MainActivity.class));
            // Thực hiện lời gọi phương thức hoặc khởi tạo: finish();.
            finish();
          // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
          } else if (status == AuthRepository.LoginStatus.BANNED) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Account is temporarily disabled", Toast.LENGTH_SHORT).show();.
            Toast.makeText(this, "Account is temporarily disabled", Toast.LENGTH_SHORT).show();
          // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
          } else {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Registered successfully, but automatic login failed", Toast.LENGTH_SHORT).show();.
            Toast.makeText(this, "Registered successfully, but automatic login failed", Toast.LENGTH_SHORT).show();
          // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
          }
        // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
        });
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      } catch (Exception e) {
        // Thực thi câu lệnh: runOnUiThread(() ->.
        runOnUiThread(() ->
                // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show().
                Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: );.
        );
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }).start();.
    }).start();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
