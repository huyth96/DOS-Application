package com.drinkorder.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.drinkorder.R;
import com.drinkorder.ui.cart.CartFragment;
import com.drinkorder.ui.home.HomeFragment;
import com.drinkorder.ui.orders.OrdersFragment;
import com.drinkorder.ui.map.MapActivity;   // 👈 import MapActivity
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle b){
    super.onCreate(b);
    setContentView(R.layout.activity_main);

    BottomNavigationView nav = findViewById(R.id.bottomNav);
    nav.setOnItemSelectedListener(item -> {
      int id = item.getItemId();

      if (id == R.id.tab_cart) {
        replaceFragment(new CartFragment());
        return true;

      } else if (id == R.id.tab_orders) {
        replaceFragment(new OrdersFragment());
        return true;

      } else if (id == R.id.tab_map) {
        // 👉 Trường hợp Map: mở Activity mới
        startActivity(new Intent(this, MapActivity.class));
        return false; // hoặc true: tuỳ bạn có muốn giữ trạng thái chọn Map hay không

      } else {
        replaceFragment(new HomeFragment());
        return true;
      }
    });

    nav.setSelectedItemId(R.id.tab_home);
  }

  private void replaceFragment(Fragment f) {
    getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, f)
            .commit();
  }
}
