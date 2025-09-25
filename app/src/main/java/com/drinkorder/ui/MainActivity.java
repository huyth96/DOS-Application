package com.drinkorder.ui;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.drinkorder.R;
import com.drinkorder.ui.cart.CartFragment;
import com.drinkorder.ui.home.HomeFragment;
import com.drinkorder.ui.orders.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle b){
    super.onCreate(b);
    setContentView(R.layout.activity_main);
    BottomNavigationView nav = findViewById(R.id.bottomNav);
    nav.setOnItemSelectedListener(item -> {
      Fragment f;
      int id=item.getItemId();
      if (id==R.id.tab_cart) f=new CartFragment();
      else if (id==R.id.tab_orders) f=new OrdersFragment();
      else f=new HomeFragment();
      getSupportFragmentManager().beginTransaction().replace(R.id.container,f).commit();
      return true;
    });
    nav.setSelectedItemId(R.id.tab_home);
  }
}
