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
// Import com.drinkorder.data.repo.AuthRepository để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.repo.AuthRepository;
// Import com.drinkorder.ui.MainActivity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.MainActivity;

// Định nghĩa lớp LoginActivity kế thừa AppCompatActivity.
public class LoginActivity extends AppCompatActivity {

  // Khai báo thuộc tính với phạm vi truy cập: private EditText edtUser, edtPass.
  private EditText edtUser, edtPass;
  // Khai báo thuộc tính với phạm vi truy cập: private Button btnLogin, btnGoRegister.
  private Button btnLogin, btnGoRegister;
  // Khai báo thuộc tính với phạm vi truy cập: private AuthRepository auth.
  private AuthRepository auth;

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreate với phạm vi truy cập tương ứng.
  protected void onCreate(Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate(savedInstanceState);.
    super.onCreate(savedInstanceState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: setContentView(R.layout.activity_login);.
    setContentView(R.layout.activity_login);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtUser = findViewById(R.id.edtUser);.
    edtUser = findViewById(R.id.edtUser);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtPass = findViewById(R.id.edtPass);.
    edtPass = findViewById(R.id.edtPass);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnLogin = findViewById(R.id.btnLogin);.
    btnLogin = findViewById(R.id.btnLogin);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnGoRegister = findViewById(R.id.btnGoRegister);.
    btnGoRegister = findViewById(R.id.btnGoRegister);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);.
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Nếu đã đăng nhập -> vào thẳng Main
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (auth.isLoggedIn()) {

      // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(new Intent(this, MainActivity.class));.
      startActivity(new Intent(this, MainActivity.class));
      // Thực hiện lời gọi phương thức hoặc khởi tạo: finish();.
      finish();
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnLogin.setOnClickListener(v -> doLogin());.
    btnLogin.setOnClickListener(v -> doLogin());
    // Thực thi câu lệnh: btnGoRegister.setOnClickListener(v ->.
    btnGoRegister.setOnClickListener(v ->
            // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(new Intent(this, RegisterActivity.class)).
            startActivity(new Intent(this, RegisterActivity.class))
    // Thực hiện lời gọi phương thức hoặc khởi tạo: );.
    );
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức doLogin với phạm vi truy cập tương ứng.
  private void doLogin() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String u = edtUser.getText().toString().trim();.
    String u = edtUser.getText().toString().trim();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String p = edtPass.getText().toString().trim();.
    String p = edtPass.getText().toString().trim();

    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (u.isEmpty() || p.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();.
      Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Khởi tạo đối tượng mới với biểu thức new Thread(() -> {.
    new Thread(() -> {
      // Thực thi câu lệnh: AuthRepository.LoginStatus status = auth.login(u, p); // thực hiện check Room + lưu SharedPreferences.
      AuthRepository.LoginStatus status = auth.login(u, p); // thực hiện check Room + lưu SharedPreferences
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      runOnUiThread(() -> {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (status == AuthRepository.LoginStatus.SUCCESS) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show();.
          Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show();
          // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(new Intent(this, MainActivity.class));.
          startActivity(new Intent(this, MainActivity.class));
          // Thực hiện lời gọi phương thức hoặc khởi tạo: finish();.
          finish();
        // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
        } else if (status == AuthRepository.LoginStatus.BANNED) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Your account has been banned. Contact support.", Toast.LENGTH_LONG).show();.
          Toast.makeText(this, "Your account has been banned. Contact support.", Toast.LENGTH_LONG).show();
        // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
        } else {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();.
          Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }).start();.
    }).start();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
