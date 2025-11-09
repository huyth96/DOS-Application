package com.drinkorder.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.drinkorder.R;
import com.drinkorder.ui.admin.AdminCategoriesFragment;
import com.drinkorder.ui.admin.AdminProductsFragment;
import com.drinkorder.ui.cart.CartFragment;
import com.drinkorder.ui.home.HomeFragment;
import com.drinkorder.ui.login.ProfileActivity;
import com.drinkorder.ui.map.MapActivity;
import com.drinkorder.ui.order.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView nav = findViewById(R.id.bottomNav);
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    boolean isAdmin = "admin".equalsIgnoreCase(sp.getString("role", "customer"));

    nav.setOnItemSelectedListener(item -> {
      int id = item.getItemId();

      if (isAdmin) {
        if (id == R.id.tab_admin_products) {
          replaceFragment(new AdminProductsFragment());
          return true;
        } else if (id == R.id.tab_admin_categories) {
          replaceFragment(new AdminCategoriesFragment());
          return true;
        } else if (id == R.id.tab_profile) {
          startActivity(new Intent(this, ProfileActivity.class));
          return true;
        }
        return false;
      }

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

    if (isAdmin) {
      nav.getMenu().clear();
      nav.inflateMenu(R.menu.menu_bottom_admin);
      nav.setSelectedItemId(R.id.tab_admin_products);
      replaceFragment(new AdminProductsFragment());
    } else {
      nav.setSelectedItemId(R.id.tab_home);
      replaceFragment(new HomeFragment());
    }
  }

  private void replaceFragment(Fragment fragment) {
    getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit();
  }
}