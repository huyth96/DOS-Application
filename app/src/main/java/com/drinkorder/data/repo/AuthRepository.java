package com.drinkorder.data.repo;

import android.content.SharedPreferences;

import com.drinkorder.data.db.dao.UserDao;
import com.drinkorder.data.db.entity.UserEntity;

/**
 * AuthRepository
 * - Chịu trách nhiệm xác thực người dùng dựa trên Room (UserDao) và lưu trạng thái phiên vào
 *   SharedPreferences (namespace "auth").
 * - Cung cấp các tiện ích: đăng nhập, đăng xuất, kiểm tra đã đăng nhập, lấy username/role hiện tại.
 * - Lưu ý: Ở bản demo, mật khẩu đang được so sánh trực tiếp (chưa hash). Khi triển khai thật cần hash/salt.
 */
public class AuthRepository {

  /** Trạng thái trả về khi thử đăng nhập. */
  public enum LoginStatus {
    /** Đăng nhập thành công. */
    SUCCESS,
    /** Sai tên đăng nhập hoặc mật khẩu. */
    INVALID_CREDENTIALS,
    /** Tài khoản đang bị khóa (banned). */
    BANNED
  }

  private final UserDao userDao;
  private final SharedPreferences sp;

  /**
   * Khởi tạo repository.
   * @param dao UserDao để truy vấn thông tin người dùng từ Room
   * @param sp SharedPreferences để lưu phiên (userId/username/role)
   */
  public AuthRepository(UserDao dao, SharedPreferences sp) {
    this.userDao = dao;
    this.sp = sp;
  }

  /**
   * Thực hiện đăng nhập:
   * - Tìm user theo username; nếu không tồn tại hoặc mật khẩu không khớp -> INVALID_CREDENTIALS
   * - Nếu user bị ban -> BANNED
   * - Ngược lại: lưu userId/username/role vào SharedPreferences và trả về SUCCESS
   */
  public LoginStatus login(String username, String password) {
    UserEntity u = userDao.findByUsername(username);
    if (u == null || !password.equals(u.passwordHash)) {
      return LoginStatus.INVALID_CREDENTIALS;
    }
    if (u.isBanned) {
      return LoginStatus.BANNED;
    }
    sp.edit()
        .putInt("userId", u.userId)
        .putString("username", u.username)
        .putString("role", u.role == null ? "customer" : u.role)
        .apply();
    return LoginStatus.SUCCESS;
  }

  /** Xóa toàn bộ thông tin phiên đăng nhập khỏi SharedPreferences. */
  public void logout() {
    sp.edit().clear().apply();
  }

  /** Kiểm tra đã đăng nhập hay chưa (dựa trên khóa userId trong SharedPreferences). */
  public boolean isLoggedIn() {
    return sp.contains("userId");
  }

  /** Lấy userId hiện tại; trả về -1 nếu chưa đăng nhập. */
  public int userId() {
    return sp.getInt("userId", -1);
  }

  /** Lấy username hiện tại; null nếu chưa đăng nhập. */
  public String getLoggedUserName() {
    return sp.getString("username", null);
  }

  /** Lấy role hiện tại; mặc định "customer". */
  public String role() {
    return sp.getString("role", "customer");
  }
}
