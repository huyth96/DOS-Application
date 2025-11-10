// Khai báo package com.drinkorder.data.repo cho toàn bộ lớp.
package com.drinkorder.data.repo;

// Import android.content.SharedPreferences để sử dụng các lớp hoặc hàm tương ứng.
import android.content.SharedPreferences;

// Import com.drinkorder.data.db.dao.UserDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.UserDao;
// Import com.drinkorder.data.db.entity.UserEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.UserEntity;

// Định nghĩa lớp AuthRepository.
public class AuthRepository {

  // Định nghĩa enum LoginStatus.
  public enum LoginStatus {
    // Thực thi câu lệnh: SUCCESS,.
    SUCCESS,
    // Thực thi câu lệnh: INVALID_CREDENTIALS,.
    INVALID_CREDENTIALS,
    // Thực thi câu lệnh: BANNED.
    BANNED
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private final UserDao userDao.
  private final UserDao userDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final SharedPreferences sp.
  private final SharedPreferences sp;

  // Định nghĩa phương thức AuthRepository với phạm vi truy cập tương ứng.
  public AuthRepository(UserDao dao, SharedPreferences sp) {
    // Gán giá trị cho biến hoặc thuộc tính: this.userDao = dao.
    this.userDao = dao;
    // Gán giá trị cho biến hoặc thuộc tính: this.sp = sp.
    this.sp = sp;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức login với phạm vi truy cập tương ứng.
  public LoginStatus login(String username, String password) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: UserEntity u = userDao.findByUsername(username);.
    UserEntity u = userDao.findByUsername(username);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (u == null || !password.equals(u.passwordHash)) {
      // Trả về kết quả LoginStatus.INVALID_CREDENTIALS;.
      return LoginStatus.INVALID_CREDENTIALS;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (u.isBanned) {
      // Trả về kết quả LoginStatus.BANNED;.
      return LoginStatus.BANNED;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: sp.edit().
    sp.edit()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .putInt("userId", u.userId).
        .putInt("userId", u.userId)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .putString("username", u.username).
        .putString("username", u.username)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .putString("role", u.role == null ? "customer" : u.role).
        .putString("role", u.role == null ? "customer" : u.role)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .apply();.
        .apply();
    // Trả về kết quả LoginStatus.SUCCESS;.
    return LoginStatus.SUCCESS;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức logout với phạm vi truy cập tương ứng.
  public void logout() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: sp.edit().clear().apply();.
    sp.edit().clear().apply();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức isLoggedIn với phạm vi truy cập tương ứng.
  public boolean isLoggedIn() {
    // Trả về kết quả sp.contains("userId");.
    return sp.contains("userId");
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức userId với phạm vi truy cập tương ứng.
  public int userId() {
    // Trả về kết quả sp.getInt("userId", -1);.
    return sp.getInt("userId", -1);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getLoggedUserName với phạm vi truy cập tương ứng.
  public String getLoggedUserName() {
    // Trả về kết quả sp.getString("username", null);.
    return sp.getString("username", null);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức role với phạm vi truy cập tương ứng.
  public String role() {
    // Trả về kết quả sp.getString("role", "customer");.
    return sp.getString("role", "customer");
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
