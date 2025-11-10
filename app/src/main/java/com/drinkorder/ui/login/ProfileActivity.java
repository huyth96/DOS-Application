// Đặt package để hoạt động trong module quản lý hồ sơ.
package com.drinkorder.ui.login;

// Import Context để truy cập SharedPreferences và tài nguyên ứng dụng.
import android.content.Context;
// Import Intent để điều hướng tới các Activity khác như Login và EditProfile.
import android.content.Intent;
// Import SharedPreferences để lấy thông tin người dùng đăng nhập hiện tại.
import android.content.SharedPreferences;
// Import Bundle phục vụ vòng đời Activity.
import android.os.Bundle;
// Import Button để xử lý nút đăng xuất và chỉnh sửa hồ sơ.
import android.widget.Button;
// Import TextView để hiển thị thông tin hồ sơ.
import android.widget.TextView;
// Import Toast để thông báo trạng thái tải dữ liệu.
import android.widget.Toast;

// Import Nullable để chú thích tham số có thể vắng mặt trong callback.
import androidx.annotation.Nullable;
// Import AppCompatActivity để kế thừa các tiện ích hỗ trợ giao diện.
import androidx.appcompat.app.AppCompatActivity;

// Import R để truy cập layout hiển thị hồ sơ.
import com.drinkorder.R;
// Import AppDatabase để truy vấn dữ liệu người dùng từ Room.
import com.drinkorder.data.db.AppDatabase;
// Import UserEntity để làm việc với dữ liệu trả về.
import com.drinkorder.data.db.entity.UserEntity;
// Import AuthRepository để xử lý phiên đăng nhập và đăng xuất.
import com.drinkorder.data.repo.AuthRepository;

// Activity hiển thị và cho phép cập nhật thông tin hồ sơ người dùng.
public class ProfileActivity extends AppCompatActivity {
  // Lưu tham chiếu các TextView để cập nhật dữ liệu lên giao diện.
  private TextView tvUsername, tvFullName, tvEmail, tvPhone, tvRole;
  // Lưu tham chiếu hai nút thao tác hồ sơ.
  private Button btnLogout, btnEdit;
  // Lưu repository xác thực để quản lý phiên và lấy username.
  private AuthRepository auth;

  // Định nghĩa request code để nhận kết quả chỉnh sửa hồ sơ.
  private static final int REQ_EDIT = 1001;

  // Override onCreate để thiết lập giao diện và dữ liệu khi mở hồ sơ.
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    // Gọi super để đảm bảo vòng đời chuẩn của Activity.
    super.onCreate(savedInstanceState);
    // Thiết lập layout hiển thị hồ sơ người dùng.
    setContentView(R.layout.activity_profile);

    // Ánh xạ TextView username để hiển thị tên tài khoản.
    tvUsername = findViewById(R.id.tvUsername);
    // Ánh xạ TextView full name để hiển thị họ tên.
    tvFullName = findViewById(R.id.tvFullName);
    // Ánh xạ TextView email để hiển thị email.
    tvEmail = findViewById(R.id.tvEmail);
    // Ánh xạ TextView phone để hiển thị số điện thoại.
    tvPhone = findViewById(R.id.tvPhone);
    // Ánh xạ TextView role để hiển thị vai trò.
    tvRole = findViewById(R.id.tvRole);
    // Ánh xạ nút đăng xuất để gán hành động logout.
    btnLogout = findViewById(R.id.btnLogout);
    // Ánh xạ nút chỉnh sửa để mở màn chỉnh sửa hồ sơ.
    btnEdit = findViewById(R.id.btnEdit);

    // Khởi tạo SharedPreferences để truy xuất phiên đăng nhập.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Tạo AuthRepository dùng chung xuyên suốt Activity.
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Lấy username hiện tại nhằm kiểm tra trạng thái đăng nhập.
    String username = auth.getLoggedUserName();
    // Nếu không có username thì điều hướng về màn đăng nhập.
    if (username == null) {
      // Mở LoginActivity khi người dùng chưa đăng nhập.
      startActivity(new Intent(this, LoginActivity.class));
      // Đóng ProfileActivity để tránh trạng thái không hợp lệ.
      finish();
      // Thoát sớm để không tải dữ liệu.
      return;
    }

    // Tải dữ liệu hồ sơ dựa trên username hiện tại.
    loadUser(username);

    // Gán sự kiện đăng xuất khi người dùng bấm nút Logout.
    btnLogout.setOnClickListener(v -> {
      // Xóa trạng thái đăng nhập khỏi SharedPreferences.
      auth.logout();
      // Chuẩn bị Intent quay lại màn đăng nhập và dọn ngăn xếp.
      Intent i = new Intent(this, LoginActivity.class);
      // Thêm cờ để xóa lịch sử Activity cũ và tạo task mới.
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      // Điều hướng về LoginActivity sau khi logout.
      startActivity(i);
      // Đóng ProfileActivity để tránh quay lại.
      finish();
    });

    // Gán sự kiện mở màn chỉnh sửa khi bấm nút Edit.
    btnEdit.setOnClickListener(v ->
        startActivityForResult(new Intent(this, EditProfileActivity.class), REQ_EDIT));
  }

  // Tải thông tin hồ sơ của người dùng từ cơ sở dữ liệu.
  private void loadUser(String username) {
    // Dùng thread phụ để không chặn giao diện khi truy vấn dữ liệu.
    new Thread(() -> {
      // Truy vấn thông tin người dùng dựa trên username.
      UserEntity user = AppDatabase.get(this).userDao().findByUsername(username);
      // Trả kết quả về UI thread để cập nhật giao diện.
      runOnUiThread(() -> {
        // Nếu không tìm thấy tài khoản thì thông báo lỗi.
        if (user == null) {
          Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
          return;
        }
        // Cập nhật tên đăng nhập lên màn hình.
        tvUsername.setText(user.username);
        // Cập nhật họ tên, hiển thị placeholder nếu thiếu.
        tvFullName.setText(user.fullName == null ? "(not set)" : user.fullName);
        // Cập nhật email, hiển thị placeholder nếu rỗng.
        tvEmail.setText(user.email == null ? "(not set)" : user.email);
        // Cập nhật số điện thoại, hiển thị placeholder nếu rỗng.
        tvPhone.setText(user.phone == null ? "(not set)" : user.phone);
        // Cập nhật vai trò, mặc định là customer nếu chưa có.
        tvRole.setText(user.role == null ? "customer" : user.role);
      });
    }).start();
  }

  // Nhận kết quả sau khi chỉnh sửa hồ sơ và làm mới dữ liệu.
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    // Gọi super để duy trì hành vi mặc định của Activity.
    super.onActivityResult(requestCode, resultCode, data);
    // Kiểm tra đúng request code, kết quả thành công và dữ liệu xác nhận cập nhật.
    if (requestCode == REQ_EDIT && resultCode == RESULT_OK && data != null && data.getBooleanExtra("updated", false)) {
      // Lấy lại username hiện tại để nạp dữ liệu mới.
      String username = auth.getLoggedUserName();
      // Nếu vẫn còn đăng nhập thì tải lại hồ sơ.
      if (username != null) loadUser(username);
    }
  }
}
