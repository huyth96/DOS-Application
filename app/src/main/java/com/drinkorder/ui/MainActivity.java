package com.drinkorder.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.drinkorder.R;
import com.drinkorder.ui.admin.AdminCategoriesFragment;
import com.drinkorder.ui.admin.AdminProductsFragment;
import com.drinkorder.ui.admin.AdminUsersFragment;
import com.drinkorder.ui.admin.AdminChatFragment;
import com.drinkorder.ui.cart.CartFragment;
import com.drinkorder.ui.chat.ChatFragment;
import com.drinkorder.ui.home.HomeFragment;
import com.drinkorder.ui.login.ProfileActivity;
import com.drinkorder.ui.map.MapActivity;
import com.drinkorder.ui.order.OrdersFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

  private DrawerLayout drawerLayout;
  private NavigationView adminNavView;
  private BottomNavigationView bottomNav;
  private MaterialToolbar toolbar;
  private ExtendedFloatingActionButton mapFab;
  private boolean isAdmin;
  private static final int REQ_PROFILE = 2001;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    drawerLayout = findViewById(R.id.drawerLayout);
    adminNavView = findViewById(R.id.navAdmin);
    bottomNav = findViewById(R.id.bottomNav);
    toolbar = findViewById(R.id.toolbarMain);
//    mapFab = findViewById(R.id.fabMap);

    // Đọc role hiện tại từ phiên đăng nhập để điều hướng giao diện phù hợp
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    isAdmin = "admin".equalsIgnoreCase(sp.getString("role", "customer"));

    if (isAdmin) {
      setupAdminNavigation();
    } else {
      setupCustomerNavigation();
    }
  }

  /** Thiết lập điều hướng cho vai trò Admin (ngăn kéo trái). */
  private void setupAdminNavigation() {
    if (bottomNav != null) bottomNav.setVisibility(View.GONE);
    if (mapFab != null) mapFab.setVisibility(View.GONE);
    if (toolbar != null) {
      toolbar.setVisibility(View.VISIBLE);
      toolbar.setNavigationIcon(R.drawable.ic_menu_24);
      toolbar.setNavigationOnClickListener(v -> {
        if (drawerLayout != null) drawerLayout.openDrawer(GravityCompat.START);
      });
    }
    if (drawerLayout != null) {
      drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
    if (adminNavView != null) {
      adminNavView.setVisibility(View.VISIBLE);
      adminNavView.setNavigationItemSelectedListener(item -> {
        boolean handled = handleAdminDestination(item.getItemId());
        if (handled && drawerLayout != null) {
          drawerLayout.closeDrawer(GravityCompat.START);
        }
        return handled;
      });
      adminNavView.setCheckedItem(R.id.nav_admin_products);
    }
    handleAdminDestination(R.id.nav_admin_products);
  }

  /** Thiết lập điều hướng cho Khách hàng (bottom navigation). */
  private void setupCustomerNavigation() {
    if (drawerLayout != null) {
      drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    if (adminNavView != null) adminNavView.setVisibility(View.GONE);
    if (toolbar != null) toolbar.setVisibility(View.GONE);
    if (bottomNav == null) return;

    bottomNav.setVisibility(View.VISIBLE);
    bottomNav.setOnItemSelectedListener(item -> handleCustomerDestination(item.getItemId()));
    bottomNav.setSelectedItemId(R.id.tab_home);

    if (mapFab != null) {
      mapFab.setVisibility(View.VISIBLE);
      mapFab.setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));
    }
  }

  private boolean handleCustomerDestination(@IdRes int menuId) {
    if (menuId == R.id.tab_cart) {
      replaceFragment(new CartFragment());
      return true;
    } else if (menuId == R.id.tab_orders) {
      replaceFragment(new OrdersFragment());
      return true;
    } else if (menuId == R.id.tab_chat) {
      replaceFragment(new ChatFragment());
      return true;
    } else if (menuId == R.id.tab_profile) {
      startActivityForResult(new Intent(this, ProfileActivity.class), REQ_PROFILE);
      return true;
    } else {
      replaceFragment(new HomeFragment());
      return true;
    }
  }

  private boolean handleAdminDestination(@IdRes int menuId) {
    if (menuId == R.id.nav_admin_products) {
      replaceFragment(new AdminProductsFragment());
      return true;
    } else if (menuId == R.id.nav_admin_categories) {
      replaceFragment(new AdminCategoriesFragment());
      return true;
    } else if (menuId == R.id.nav_admin_users) {
      replaceFragment(new AdminUsersFragment());
      return true;
    } else if (menuId == R.id.nav_admin_chat) {
      replaceFragment(new AdminChatFragment());
      return true;
    } else if (menuId == R.id.nav_admin_profile) {
      startActivity(new Intent(this, ProfileActivity.class));
      return true;
    }
    return false;
  }

  private void replaceFragment(Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, fragment)
        .commit();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQ_PROFILE && resultCode == RESULT_OK && data != null && bottomNav != null) {
      int selectedTab = data.getIntExtra(ProfileActivity.EXTRA_SELECTED_TAB, R.id.tab_home);
      bottomNav.setSelectedItemId(selectedTab);
    }
  }

  @Override
  public void onBackPressed() {
    if (isAdmin && drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
      return;
    }
    super.onBackPressed();
  }
}
