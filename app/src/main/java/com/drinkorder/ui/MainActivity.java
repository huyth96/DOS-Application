package com.drinkorder.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.drinkorder.R;
import com.drinkorder.ui.admin.AdminCategoriesFragment;
import com.drinkorder.ui.admin.AdminProductsFragment;
import com.drinkorder.ui.admin.AdminUsersFragment;
import com.drinkorder.ui.cart.CartFragment;
import com.drinkorder.ui.home.HomeFragment;
import com.drinkorder.ui.login.ProfileActivity;
import com.drinkorder.ui.map.MapActivity;
import com.drinkorder.ui.order.OrdersFragment;
import com.drinkorder.ui.chat.admin.AdminChatFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

  private DrawerLayout drawerLayout;
  private NavigationView adminNavView;
  private BottomNavigationView bottomNav;
  private MaterialToolbar toolbar;
  private boolean isAdmin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    drawerLayout = findViewById(R.id.drawerLayout);
    adminNavView = findViewById(R.id.navAdmin);
    bottomNav = findViewById(R.id.bottomNav);
    toolbar = findViewById(R.id.toolbarMain);

    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    isAdmin = "admin".equalsIgnoreCase(sp.getString("role", "customer"));

    if (isAdmin) {
      setupAdminNavigation();
    } else {
      setupCustomerNavigation();
    }
  }

  private void setupAdminNavigation() {
    if (bottomNav != null) bottomNav.setVisibility(View.GONE);
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

  private void setupCustomerNavigation() {
    if (drawerLayout != null) {
      drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    if (adminNavView != null) adminNavView.setVisibility(View.GONE);
    if (toolbar != null) toolbar.setVisibility(View.GONE);
    if (bottomNav == null) return;

    bottomNav.setVisibility(View.VISIBLE);
    bottomNav.setOnItemSelectedListener(item -> {
      int id = item.getItemId();
      if (id == R.id.tab_cart) {
        replaceFragment(new CartFragment());
        return true;
      } else if (id == R.id.tab_orders) {
        replaceFragment(new OrdersFragment());
        return true;
      } else if (id == R.id.tab_map) {
        startActivity(new Intent(this, MapActivity.class));
        return false;
      } else if (id == R.id.tab_profile) {
        startActivity(new Intent(this, ProfileActivity.class));
        return true;
      } else {
        replaceFragment(new HomeFragment());
        return true;
      }
    });
    bottomNav.setSelectedItemId(R.id.tab_home);
    replaceFragment(new HomeFragment());
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
  public void onBackPressed() {
    if (isAdmin && drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
      return;
    }
    super.onBackPressed();
  }
}
