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
// Import android.widget.* để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.*;
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

// Định nghĩa lớp EditProfileActivity kế thừa AppCompatActivity.
public class EditProfileActivity extends AppCompatActivity {
  // Khai báo thuộc tính với phạm vi truy cập: private EditText edtFullName, edtEmail, edtPhone, edtNewPass.
  private EditText edtFullName, edtEmail, edtPhone, edtNewPass;
  // Khai báo thuộc tính với phạm vi truy cập: private Button btnSave, btnCancel.
  private Button btnSave, btnCancel;
  // Khai báo thuộc tính với phạm vi truy cập: private AuthRepository auth.
  private AuthRepository auth;

  // Áp dụng annotation @Override và ghi đè phương thức onCreate.
  @Override protected void onCreate(Bundle b) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate(b);.
    super.onCreate(b);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: setContentView(R.layout.activity_edit_profile);.
    setContentView(R.layout.activity_edit_profile);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtFullName = findViewById(R.id.edtFullName);.
    edtFullName = findViewById(R.id.edtFullName);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtEmail    = findViewById(R.id.edtEmail);.
    edtEmail    = findViewById(R.id.edtEmail);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtPhone    = findViewById(R.id.edtPhone);.
    edtPhone    = findViewById(R.id.edtPhone);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtNewPass  = findViewById(R.id.edtNewPass);.
    edtNewPass  = findViewById(R.id.edtNewPass);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnSave     = findViewById(R.id.btnSave);.
    btnSave     = findViewById(R.id.btnSave);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnCancel   = findViewById(R.id.btnCancel);.
    btnCancel   = findViewById(R.id.btnCancel);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);.
    auth = new AuthRepository(AppDatabase.get(this).userDao(), sp);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: String username = auth.getLoggedUserName();.
    String username = auth.getLoggedUserName();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (username == null) { finish(); return; }

    // Prefill
    // Khởi tạo đối tượng mới với biểu thức new Thread(() -> {.
    new Thread(() -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: UserEntity u = AppDatabase.get(this).userDao().findByUsername(username);.
      UserEntity u = AppDatabase.get(this).userDao().findByUsername(username);
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      runOnUiThread(() -> {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (u != null) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: edtFullName.setText(u.fullName);.
          edtFullName.setText(u.fullName);
          // Thực hiện lời gọi phương thức hoặc khởi tạo: edtEmail.setText(u.email);.
          edtEmail.setText(u.email);
          // Thực hiện lời gọi phương thức hoặc khởi tạo: edtPhone.setText(u.phone);.
          edtPhone.setText(u.phone);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: }).start();.
    }).start();

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    btnSave.setOnClickListener(v -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String full = edtFullName.getText().toString().trim();.
      String full = edtFullName.getText().toString().trim();
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String email = edtEmail.getText().toString().trim();.
      String email = edtEmail.getText().toString().trim();
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String phone = edtPhone.getText().toString().trim();.
      String phone = edtPhone.getText().toString().trim();
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String newPass = edtNewPass.getText().toString().trim();.
      String newPass = edtNewPass.getText().toString().trim();

      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();.
        Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
        // Trả về kết quả ;.
        return;
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }

      // Khởi tạo đối tượng mới với biểu thức new Thread(() -> {.
      new Thread(() -> {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: var dao = AppDatabase.get(this).userDao();.
        var dao = AppDatabase.get(this).userDao();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: dao.updateProfile(username, full, email, phone);.
        dao.updateProfile(username, full, email, phone);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (!newPass.isEmpty()) dao.updatePassword(username, newPass); // TODO: hash sau
        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        runOnUiThread(() -> {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();.
          Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
          // Thực hiện lời gọi phương thức hoặc khởi tạo: setResult(RESULT_OK, new Intent().putExtra("updated", true));.
          setResult(RESULT_OK, new Intent().putExtra("updated", true));
          // Thực hiện lời gọi phương thức hoặc khởi tạo: finish();.
          finish();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
        });
      // Thực hiện lời gọi phương thức hoặc khởi tạo: }).start();.
      }).start();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });

    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnCancel.setOnClickListener(v -> finish());.
    btnCancel.setOnClickListener(v -> finish());
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
