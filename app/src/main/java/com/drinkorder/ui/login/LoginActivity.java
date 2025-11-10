// Đặt package để file thuộc đúng không gian tên của module đăng nhập.
package com.drinkorder.ui.login;

// Import Context để truy cập tài nguyên và preferences của ứng dụng.
import android.content.Context;
// Import Intent để điều hướng sang màn hình khác sau khi đăng nhập.
import android.content.Intent;
// Import SharedPreferences để lưu và đọc trạng thái đăng nhập.
import android.content.SharedPreferences;
// Import Bundle phục vụ vòng đời Activity và nhận dữ liệu khởi tạo.
import android.os.Bundle;
// Import Button để tham chiếu tới các nút thao tác đăng nhập và chuyển trang.
import android.widget.Button;
// Import EditText để đọc dữ liệu người dùng nhập tài khoản và mật khẩu.
import android.widget.EditText;
// Import Toast để hiển thị thông báo nhanh cho người dùng.
import android.widget.Toast;

// Import AppCompatActivity để kế thừa các tiện ích tương thích ngược của Activity.
import androidx.appcompat.app.AppCompatActivity;

// Import R để truy cập layout và view đã định nghĩa trong XML.
import com.drinkorder.R;
// Import AppDatabase để lấy được DAO phục vụ xác thực.
import com.drinkorder.data.db.AppDatabase;
// Import AuthRepository để xử lý logic đăng nhập và trạng thái tài khoản.
import com.drinkorder.data.repo.AuthRepository;
// Import MainActivity để điều hướng khi đăng nhập thành công.
import com.drinkorder.ui.MainActivity;

// Khai báo LoginActivity để xử lý màn hình đăng nhập.
public class LoginActivity extends AppCompatActivity {

  // Giữ tham chiếu EditText để đọc username và password.
  private EditText edtUser, edtPass;
  // Giữ tham chiếu Button để gán sự kiện đăng nhập và chuyển sang đăng ký.
  private Button btnLogin, btnGoRegister;
  // Giữ AuthRepository để tái sử dụng trong toàn Activity.
  private AuthRepository auth;

  // Override onCreate để thiết lập UI và logic ngay khi Activity khởi tạo.
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Gọi super để đảm bảo vòng đời Activity hoạt động đúng.
    super.onCreate(savedInstanceState);
    // Gắn layout đăng nhập vào Activity.
    setContentView(R.layout.activity_login);

    // Liên kết EditText username với biến để lấy dữ liệu.
    edtUser = findViewById(R.id.edtUser);
    // Liên kết EditText password với biến để lấy dữ liệu.
    edtPass = findViewById(R.id.edtPass);
    // Liên kết Button đăng nhập để thiết lập xử lý sự kiện.
    btnLogin = findViewById(R.id.btnLogin);
    // Liên kết Button chuyển sang màn đăng ký để điều hướng.
    btnGoRegister = findViewById(R.id.btnGoRegister);

    // Lấy SharedPreferences tên "auth" để lưu trạng thái đăng nhập.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Khởi tạo AuthRepository với userDao và SharedPreferences để xử lý xác thực.
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Kiểm tra nếu đã đăng nhập thì bỏ qua màn hình đăng nhập.
    if (auth.isLoggedIn()) {

      // Điều hướng thẳng sang MainActivity khi phiên đăng nhập còn hiệu lực.
      startActivity(new Intent(this, MainActivity.class));
      // Kết thúc Activity hiện tại để tránh quay lại bằng nút Back.
      finish();
      // Thoát khỏi hàm để không gán sự kiện thừa.
      return;
    }

    // Gán hành động đăng nhập khi người dùng bấm nút Login.
    btnLogin.setOnClickListener(v -> doLogin());
    // Gán điều hướng sang màn đăng ký khi bấm nút tạo tài khoản.
    btnGoRegister.setOnClickListener(v ->
            startActivity(new Intent(this, RegisterActivity.class))
    );
  }

  // Tách logic đăng nhập để dễ tái sử dụng và đọc hiểu.
  private void doLogin() {
    // Lấy username và loại bỏ khoảng trắng dư.
    String u = edtUser.getText().toString().trim();
    // Lấy password và loại bỏ khoảng trắng dư.
    String p = edtPass.getText().toString().trim();

    // Kiểm tra nhập thiếu để phản hồi sớm cho người dùng.
    if (u.isEmpty() || p.isEmpty()) {
      // Báo lỗi bằng Toast nếu thiếu dữ liệu.
      Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
      // Dừng xử lý tiếp khi dữ liệu không hợp lệ.
      return;
    }

    // Chạy đăng nhập trong thread phụ để tránh chặn UI.
    new Thread(() -> {
      // Gọi repository để kiểm tra thông tin và cập nhật trạng thái lưu trữ.
      AuthRepository.LoginStatus status = auth.login(u, p); // thực hiện check Room + lưu SharedPreferences
      // Chuyển kết quả về UI thread để hiển thị cho người dùng.
      runOnUiThread(() -> {
        // Nếu thành công thì thông báo và điều hướng sang Main.
        if (status == AuthRepository.LoginStatus.SUCCESS) {
          // Xác nhận đăng nhập thành công cho người dùng.
          Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show();
          // Điều hướng tới màn hình chính sau khi xác thực.
          startActivity(new Intent(this, MainActivity.class));
          // Kết thúc Activity đăng nhập để không quay lại.
          finish();
        } else if (status == AuthRepository.LoginStatus.BANNED) {
          // Thông báo tài khoản bị khóa nếu repository trả về trạng thái BANNED.
          Toast.makeText(this, "Your account has been banned. Contact support.", Toast.LENGTH_LONG).show();
        } else {
          // Hiển thị lỗi mặc định khi thông tin sai.
          Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
      });
    }).start();
  }
}
