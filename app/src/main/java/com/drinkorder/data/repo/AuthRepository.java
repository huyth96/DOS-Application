package com.drinkorder.data.repo;

import android.content.SharedPreferences;

import com.drinkorder.data.db.dao.UserDao;
import com.drinkorder.data.db.entity.UserEntity;

/**
 * AuthRepository
 * ==============================
 * Lớp này chịu trách nhiệm xử lý mọi logic liên quan đến xác thực người dùng.
 *
 * 💡 Nhiệm vụ chính:
 *  - Xác thực thông tin đăng nhập dựa vào Room Database (UserDao).
 *  - Lưu trạng thái đăng nhập vào SharedPreferences ("auth").
 *  - Cung cấp tiện ích để lấy thông tin người dùng đang đăng nhập (username, role...).
 *
 * ⚠ Lưu ý bảo mật:
 *  - Hiện tại demo đang so sánh mật khẩu plaintext (chưa mã hóa).
 *  - Trong môi trường thực tế cần hash + salt mật khẩu trước khi lưu và kiểm tra.
 */
public class AuthRepository {

  /**
   * Enum thể hiện các trạng thái có thể xảy ra sau khi thử đăng nhập.
   */
  public enum LoginStatus {
    /** ✅ Đăng nhập thành công. */
    SUCCESS,

    /** ❌ Sai username hoặc password. */
    INVALID_CREDENTIALS,

    /** 🚫 Tài khoản bị khóa (bị admin ban). */
    BANNED
  }

  /** Truy cập bảng người dùng trong Room Database. */
  private final UserDao userDao;

  /** Lưu trữ thông tin phiên đăng nhập (userId, username, role). */
  private final SharedPreferences sp;

  /**
   * Khởi tạo AuthRepository.
   *
   * @param dao UserDao để truy vấn dữ liệu người dùng trong Room DB.
   * @param sp SharedPreferences (tên "auth") để lưu thông tin đăng nhập hiện tại.
   */
  public AuthRepository(UserDao dao, SharedPreferences sp) {
    this.userDao = dao;
    this.sp = sp;
  }

  /**
   * Thực hiện đăng nhập.
   * -----------------------------
   * - Tìm user theo username trong DB.
   * - Nếu không tồn tại → INVALID_CREDENTIALS.
   * - Nếu mật khẩu sai → INVALID_CREDENTIALS.
   * - Nếu tài khoản bị ban → BANNED.
   * - Nếu hợp lệ → Lưu thông tin user vào SharedPreferences và trả SUCCESS.
   *
   * @param username tên đăng nhập
   * @param password mật khẩu (plaintext, demo)
   * @return LoginStatus thể hiện kết quả đăng nhập
   */
  public LoginStatus login(String username, String password) {
    // Truy vấn user trong DB
    UserEntity u = userDao.findByUsername(username);

    // Nếu không có user hoặc sai password
    if (u == null || !password.equals(u.passwordHash)) {
      return LoginStatus.INVALID_CREDENTIALS;
    }

    // Nếu bị ban
    if (u.isBanned) {
      return LoginStatus.BANNED;
    }

    // Nếu hợp lệ: lưu thông tin phiên đăng nhập vào SharedPreferences
    sp.edit()
            .putInt("userId", u.userId)
            .putString("username", u.username)
            .putString("role", u.role == null ? "customer" : u.role)
            .apply();

    return LoginStatus.SUCCESS;
  }

  /**
   * Đăng xuất người dùng.
   * -----------------------------
   * Xóa toàn bộ thông tin phiên khỏi SharedPreferences.
   * Sau khi gọi hàm này → isLoggedIn() sẽ trả về false.
   */
  public void logout() {
    sp.edit().clear().apply();
  }

  /**
   * Kiểm tra người dùng có đang đăng nhập không.
   * -----------------------------
   * @return true nếu SharedPreferences có lưu "userId", ngược lại false.
   */
  public boolean isLoggedIn() {
    return sp.contains("userId");
  }

  /**
   * Lấy ID người dùng hiện tại.
   * -----------------------------
   * @return userId nếu đã đăng nhập, -1 nếu chưa.
   */
  public int userId() {
    return sp.getInt("userId", -1);
  }

  /**
   * Lấy username người dùng hiện tại.
   * -----------------------------
   * @return username hoặc null nếu chưa đăng nhập.
   */
  public String getLoggedUserName() {
    return sp.getString("username", null);
  }

  /**
   * Lấy vai trò (role) của người dùng hiện tại.
   * -----------------------------
   * @return role (admin/customer/staff/...) hoặc "customer" nếu chưa có.
   */
  public String role() {
    return sp.getString("role", "customer");
  }
}
