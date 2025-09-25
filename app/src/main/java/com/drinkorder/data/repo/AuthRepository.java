package com.drinkorder.data.repo;
import android.content.SharedPreferences;
import com.drinkorder.data.db.dao.UserDao;
import com.drinkorder.data.db.entity.UserEntity;

public class AuthRepository {
  private final UserDao userDao; private final SharedPreferences sp;
  public AuthRepository(UserDao dao, SharedPreferences sp){ this.userDao=dao; this.sp=sp; }
  public boolean login(String username, String password){
    UserEntity u = userDao.findByUsername(username); if (u==null) return false;
    boolean ok = password.equals(u.passwordHash);
    if (ok){
      sp.edit().putInt("userId", u.userId).putString("username", u.username).putString("role", u.role==null?"customer":u.role).apply();
    }
    return ok;
  }
  public void logout(){ sp.edit().clear().apply(); }
  public boolean isLoggedIn(){ return sp.contains("userId"); }
  public int userId(){ return sp.getInt("userId", -1); }
}
