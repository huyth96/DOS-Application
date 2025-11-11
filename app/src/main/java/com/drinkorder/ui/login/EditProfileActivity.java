// Đặt package để hoạt động chung với nhóm chức năng tài khoản.
package com.drinkorder.ui.login;

// Import Context để truy cập SharedPreferences.
import android.content.Context;
// Import Intent để trả kết quả cập nhật hồ sơ.
import android.content.Intent;
// Import SharedPreferences để xác định người dùng hiện tại.
import android.content.SharedPreferences;
// Import Bundle phục vụ vòng đời Activity.
import android.os.Bundle;
// Import toàn bộ widget Android để dùng EditText, Button và Toast.
import android.widget.*;
// Kế thừa AppCompatActivity nhằm sử dụng các API hỗ trợ giao diện.
import androidx.appcompat.app.AppCompatActivity;
// Import R để truy cập layout chỉnh sửa hồ sơ.
import com.drinkorder.R;
// Import AppDatabase để thao tác với dữ liệu người dùng.
import com.drinkorder.data.db.AppDatabase;
// Import UserEntity để nhận dữ liệu từ cơ sở dữ liệu.
import com.drinkorder.data.db.entity.UserEntity;
// Import AuthRepository để kiểm tra và cập nhật phiên đăng nhập.
import com.drinkorder.data.repo.AuthRepository;

// Activity cho phép người dùng cập nhật thông tin hồ sơ.
public class EditProfileActivity extends AppCompatActivity {
  // Giữ tham chiếu các trường nhập liệu để đọc và gán dữ liệu.
  private EditText edtFullName, edtEmail, edtPhone, edtNewPass;
  // Giữ tham chiếu các nút để xử lý lưu và hủy.
  private Button btnSave, btnCancel;
  // Sử dụng AuthRepository để truy cập thông tin người dùng đăng nhập.
  private AuthRepository auth;

  // Override onCreate để thiết lập giao diện chỉnh sửa hồ sơ.
  @Override protected void onCreate(Bundle b) {
    // Gọi super để đảm bảo chu trình Activity chuẩn.
    super.onCreate(b);
    // Thiết lập layout chỉnh sửa cho màn hình.
    setContentView(R.layout.activity_edit_profile);

    // Ánh xạ ô nhập họ tên để gán dữ liệu hiện tại.
    edtFullName = findViewById(R.id.edtFullName);
    // Ánh xạ ô nhập email để hiển thị và cập nhật email.
    edtEmail    = findViewById(R.id.edtEmail);
    // Ánh xạ ô nhập số điện thoại để cập nhật thông tin liên hệ.
    edtPhone    = findViewById(R.id.edtPhone);
    // Ánh xạ ô nhập mật khẩu mới để cho phép thay đổi thông tin đăng nhập.
    edtNewPass  = findViewById(R.id.edtNewPass);
    // Ánh xạ nút lưu để gán hành động cập nhật dữ liệu.
    btnSave     = findViewById(R.id.btnSave);
    // Ánh xạ nút hủy để thoát khỏi màn hình chỉnh sửa.
    btnCancel   = findViewById(R.id.btnCancel);

    // Lấy SharedPreferences để xác định người dùng hiện tại.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Khởi tạo AuthRepository nhằm tái sử dụng trong toàn Activity.
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Lấy username đăng nhập hiện tại để biết ai đang chỉnh sửa.
    String username = auth.getLoggedUserName();
    // Nếu không có username thì đóng Activity để tránh chỉnh sửa trái phép.
    if (username == null) { finish(); return; }

    // Đổ dữ liệu hiện có vào form để người dùng chỉnh sửa.
    new Thread(() -> {
      // Truy vấn hồ sơ hiện tại của người dùng.
      UserEntity u = AppDatabase.get(this).userDao().findByUsername(username);
      // Cập nhật giao diện trên UI thread theo quy tắc Android.
      runOnUiThread(() -> {
        // Chỉ gán dữ liệu khi tìm thấy người dùng.
        if (u != null) {
          // Hiển thị họ tên hiện tại.
          edtFullName.setText(u.fullName);
          // Hiển thị email hiện tại.
          edtEmail.setText(u.email);
          // Hiển thị số điện thoại hiện tại.
          edtPhone.setText(u.phone);
        }
      });
    }).start();

    // Gán sự kiện lưu để cập nhật hồ sơ khi người dùng bấm nút.
    btnSave.setOnClickListener(v -> {
      // Đọc họ tên người dùng nhập vào.
      String full = edtFullName.getText().toString().trim();
      // Đọc email người dùng nhập vào.
      String email = edtEmail.getText().toString().trim();
      // Đọc số điện thoại người dùng nhập vào.
      String phone = edtPhone.getText().toString().trim();
      // Đọc mật khẩu mới nếu có.
      String newPass = edtNewPass.getText().toString().trim();

      // Kiểm tra định dạng email để đảm bảo dữ liệu hợp lệ.
      if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        // Thông báo lỗi nếu email không đúng định dạng.
        Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
        // Ngừng xử lý khi dữ liệu sai.
        return;
      }

      // Chạy cập nhật trong thread phụ để giữ UI mượt mà.
      new Thread(() -> {
        // Lấy DAO người dùng để thao tác cập nhật.
        var dao = AppDatabase.get(this).userDao();
        // Cập nhật thông tin hồ sơ với dữ liệu mới.
        dao.updateProfile(username, full, email, phone);
        // Nếu người dùng nhập mật khẩu mới thì cập nhật mật khẩu.
        if (!newPass.isEmpty()) dao.updatePassword(username, newPass); // TODO: hash sau
        // Quay lại UI thread để thông báo và trả kết quả.
        runOnUiThread(() -> {
          // Thông báo lưu thành công cho người dùng.
          Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
          // Trả kết quả về Activity trước với cờ cập nhật thành công.
          setResult(RESULT_OK, new Intent().putExtra("updated", true));
          // Đóng màn hình chỉnh sửa sau khi lưu xong.
          finish();
        });
      }).start();
    });

    // Cho phép người dùng hủy và quay lại mà không lưu thay đổi.
    btnCancel.setOnClickListener(v -> finish());
  }
}
