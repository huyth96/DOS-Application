package com.drinkorder.data.repo;

import android.content.SharedPreferences;

import com.drinkorder.data.db.dao.UserDao;
import com.drinkorder.data.db.entity.UserEntity;

public class AuthRepository {

  public enum LoginStatus {
    SUCCESS,
    INVALID_CREDENTIALS,
    BANNED
  }

  private final UserDao userDao;
  private final SharedPreferences sp;

  public AuthRepository(UserDao dao, SharedPreferences sp) {
    this.userDao = dao;
    this.sp = sp;
  }

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

  public void logout() {
    sp.edit().clear().apply();
  }

  public boolean isLoggedIn() {
    return sp.contains("userId");
  }

  public int userId() {
    return sp.getInt("userId", -1);
  }

  public String getLoggedUserName() {
    return sp.getString("username", null);
  }

  public String role() {
    return sp.getString("role", "customer");
  }
}
