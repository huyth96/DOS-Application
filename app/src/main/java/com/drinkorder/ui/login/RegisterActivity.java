// Đặt package để Activity thuộc đúng module đăng ký.
package com.drinkorder.ui.login;

// Import Context để truy cập SharedPreferences và tài nguyên.
import android.content.Context;
// Import Intent để điều hướng trở lại màn hình đăng nhập hoặc sang Main.
import android.content.Intent;
// Import SharedPreferences để tự động đăng nhập sau khi tạo tài khoản.
import android.content.SharedPreferences;
// Import Bundle cho vòng đời Activity.
import android.os.Bundle;
// Import Button để xử lý các nút đăng ký và quay lại.
import android.widget.Button;
// Import EditText để lấy thông tin nhập từ người dùng.
import android.widget.EditText;
// Import Toast để hiển thị phản hồi ngắn.
import android.widget.Toast;
// Kế thừa AppCompatActivity nhằm dùng được các API tương thích.
import androidx.appcompat.app.AppCompatActivity;

// Import R để truy cập layout và id view.
import com.drinkorder.R;
// Import AppDatabase để thao tác với bảng người dùng.
import com.drinkorder.data.db.AppDatabase;
// Import UserEntity để tạo bản ghi người dùng mới.
import com.drinkorder.data.db.entity.UserEntity;
// Import AuthRepository để dùng chung luồng đăng nhập.
import com.drinkorder.data.repo.AuthRepository;
// Import MainActivity để điều hướng sau khi đăng ký thành công.
import com.drinkorder.ui.MainActivity;

// Khai báo Activity chịu trách nhiệm đăng ký người dùng mới.
public class RegisterActivity extends AppCompatActivity {
  // Khai báo các ô nhập liệu để lưu tham chiếu và đọc giá trị.
  private EditText edtUser, edtPass, edtFullName, edtEmail, edtPhone;
  // Khai báo các nút để gán sự kiện đăng ký và quay lại đăng nhập.
  private Button btnRegister, btnBackLogin;
  // Khai báo AuthRepository để xử lý đăng nhập tự động sau đăng ký.
  private AuthRepository auth;

  // Override onCreate để cấu hình giao diện khi Activity khởi chạy.
  @Override protected void onCreate(Bundle b) {
    // Gọi super để đảm bảo vòng đời mặc định.
    super.onCreate(b);
    // Gắn layout đăng ký cho Activity.
    setContentView(R.layout.activity_register);

    // Liên kết trường username với biến để đọc dữ liệu.
    edtUser = findViewById(R.id.edtUser);
    // Liên kết trường password để lấy mật khẩu nhập vào.
    edtPass = findViewById(R.id.edtPass);
    // Liên kết trường fullname để lưu họ tên người dùng.
    edtFullName = findViewById(R.id.edtFullName);
    // Liên kết trường email để ghi nhận thông tin liên hệ.
    edtEmail = findViewById(R.id.edtEmail);
    // Liên kết trường phone nhằm lưu số điện thoại.
    edtPhone = findViewById(R.id.edtPhone);
    // Liên kết nút đăng ký để gán hành động tạo tài khoản.
    btnRegister = findViewById(R.id.btnRegister);
    // Liên kết nút quay lại đăng nhập để điều hướng.
    btnBackLogin = findViewById(R.id.btnBackLogin);

    // Lấy SharedPreferences để sử dụng cho phiên đăng nhập sau khi tạo tài khoản.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Khởi tạo AuthRepository với DAO nhằm tái sử dụng logic xác thực.
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Đăng ký xử lý khi người dùng bấm nút tạo tài khoản.
    btnRegister.setOnClickListener(v -> doRegister());
    // Cấu hình nút quay lại để trở về màn đăng nhập.
    btnBackLogin.setOnClickListener(v -> {
      // Điều hướng sang LoginActivity khi người dùng muốn quay lại.
      startActivity(new Intent(this, LoginActivity.class));
      // Đóng Activity hiện tại để tránh chồng màn hình.
      finish();
    });
  }

  // Hàm tách riêng để thực hiện toàn bộ quy trình đăng ký.
  private void doRegister() {
    // Lấy username và loại bỏ khoảng trắng dư.
    String u = edtUser.getText().toString().trim();
    // Lấy password đã nhập và loại bỏ khoảng trắng dư.
    String p = edtPass.getText().toString().trim();
    // Lấy họ tên để lưu vào hồ sơ.
    String full = edtFullName.getText().toString().trim();
    // Lấy email nhằm phục vụ liên hệ.
    String email = edtEmail.getText().toString().trim();
    // Lấy số điện thoại phục vụ hỗ trợ khách hàng.
    String phone = edtPhone.getText().toString().trim();

    // Kiểm tra dữ liệu bắt buộc để tránh tạo tài khoản thiếu thông tin.
    if (u.isEmpty() || p.isEmpty()) {
      // Thông báo cần điền đủ tài khoản và mật khẩu.
      Toast.makeText(this, "Please enter username & password", Toast.LENGTH_SHORT).show();
      // Dừng xử lý khi dữ liệu không hợp lệ.
      return;
    }

    // Thực thi thao tác nặng trong thread phụ để tránh khóa UI.
    new Thread(() -> {
      try {
        // Lấy DAO người dùng để thao tác với cơ sở dữ liệu.
        var dao = AppDatabase.get(this).userDao();
        // Kiểm tra trùng username trước khi tạo tài khoản mới.
        if (dao.findByUsername(u) != null) {
          // Chuyển về UI thread để báo lỗi trùng tài khoản.
          runOnUiThread(() ->
                  Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
          );
          // Dừng xử lý nếu đã có tài khoản tồn tại.
          return;
        }

        // Khởi tạo thực thể UserEntity để lưu thông tin vào Room.
        UserEntity user = new UserEntity();
        // Gán username cho bản ghi mới.
        user.username = u;
        // Lưu password tạm thời dưới dạng plain text theo thiết kế hiện tại.
        user.passwordHash = p; // TODO: có thể thay bằng hashing sau
        // Lưu họ tên cho mục đích hiển thị hồ sơ.
        user.fullName = full;
        // Lưu email để phục vụ liên lạc.
        user.email = email;
        // Lưu số điện thoại cho thông tin hồ sơ.
        user.phone = phone;
        // Thiết lập role mặc định cho khách hàng mới.
        user.role = "customer";
        // Ghi lại thời điểm tạo tài khoản phục vụ audit.
        user.createdAt = System.currentTimeMillis();
        // Mặc định tài khoản không bị khóa khi mới tạo.
        user.isBanned = false;
        // Chèn bản ghi mới vào cơ sở dữ liệu.
        dao.insert(user);

        // Tiến hành đăng nhập tự động để giảm bước cho người dùng.
        AuthRepository.LoginStatus status = auth.login(u, p);
        // Trả kết quả về UI thread để cập nhật giao diện.
        runOnUiThread(() -> {
          // Nếu đăng nhập thành công thì mở MainActivity.
          if (status == AuthRepository.LoginStatus.SUCCESS) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
          } else if (status == AuthRepository.LoginStatus.BANNED) {
            // Báo tài khoản bị khóa nếu server trả về trạng thái BANNED.
            Toast.makeText(this, "Account is temporarily disabled", Toast.LENGTH_SHORT).show();
          } else {
            // Thông báo lỗi chung nếu đăng nhập tự động thất bại.
            Toast.makeText(this, "Registered successfully, but automatic login failed", Toast.LENGTH_SHORT).show();
          }
        });
      } catch (Exception e) {
        // Báo lỗi đăng ký nếu xảy ra ngoại lệ trong quá trình xử lý.
        runOnUiThread(() ->
                Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
      }
    }).start();
  }
}
