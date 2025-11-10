// Khai báo package com.drinkorder.ui cho toàn bộ lớp.
package com.drinkorder.ui;

// Import android.content.Context để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Context;
// Import android.content.Intent để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Intent;
// Import android.content.SharedPreferences để sử dụng các lớp hoặc hàm tương ứng.
import android.content.SharedPreferences;
// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;

// Import androidx.annotation.IdRes để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.IdRes;
// Import androidx.appcompat.app.AppCompatActivity để sử dụng các lớp hoặc hàm tương ứng.
import androidx.appcompat.app.AppCompatActivity;
// Import androidx.core.view.GravityCompat để sử dụng các lớp hoặc hàm tương ứng.
import androidx.core.view.GravityCompat;
// Import androidx.drawerlayout.widget.DrawerLayout để sử dụng các lớp hoặc hàm tương ứng.
import androidx.drawerlayout.widget.DrawerLayout;
// Import androidx.fragment.app.Fragment để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.Fragment;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.ui.admin.AdminCategoriesFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.admin.AdminCategoriesFragment;
// Import com.drinkorder.ui.admin.AdminProductsFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.admin.AdminProductsFragment;
// Import com.drinkorder.ui.admin.AdminUsersFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.admin.AdminUsersFragment;
// Import com.drinkorder.ui.admin.AdminChatFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.admin.AdminChatFragment;
// Import com.drinkorder.ui.cart.CartFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.cart.CartFragment;
// Import com.drinkorder.ui.chat.ChatFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.chat.ChatFragment;
// Import com.drinkorder.ui.home.HomeFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.home.HomeFragment;
// Import com.drinkorder.ui.login.ProfileActivity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.login.ProfileActivity;
// Import com.drinkorder.ui.map.MapActivity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.map.MapActivity;
// Import com.drinkorder.ui.order.OrdersFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.order.OrdersFragment;
// Import com.google.android.material.appbar.MaterialToolbar để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.appbar.MaterialToolbar;
// Import com.google.android.material.bottomnavigation.BottomNavigationView để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.bottomnavigation.BottomNavigationView;
// Import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
// Import com.google.android.material.navigation.NavigationView để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.navigation.NavigationView;

// Định nghĩa lớp MainActivity kế thừa AppCompatActivity.
public class MainActivity extends AppCompatActivity {

  // Khai báo thuộc tính với phạm vi truy cập: private DrawerLayout drawerLayout.
  private DrawerLayout drawerLayout;
  // Khai báo thuộc tính với phạm vi truy cập: private NavigationView adminNavView.
  private NavigationView adminNavView;
  // Khai báo thuộc tính với phạm vi truy cập: private BottomNavigationView bottomNav.
  private BottomNavigationView bottomNav;
  // Khai báo thuộc tính với phạm vi truy cập: private MaterialToolbar toolbar.
  private MaterialToolbar toolbar;
  // Khai báo thuộc tính với phạm vi truy cập: private ExtendedFloatingActionButton mapFab.
  private ExtendedFloatingActionButton mapFab;
  // Khai báo thuộc tính với phạm vi truy cập: private boolean isAdmin.
  private boolean isAdmin;

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreate với phạm vi truy cập tương ứng.
  protected void onCreate(Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate(savedInstanceState);.
    super.onCreate(savedInstanceState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: setContentView(R.layout.activity_main);.
    setContentView(R.layout.activity_main);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: drawerLayout = findViewById(R.id.drawerLayout);.
    drawerLayout = findViewById(R.id.drawerLayout);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: adminNavView = findViewById(R.id.navAdmin);.
    adminNavView = findViewById(R.id.navAdmin);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: bottomNav = findViewById(R.id.bottomNav);.
    bottomNav = findViewById(R.id.bottomNav);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: toolbar = findViewById(R.id.toolbarMain);.
    toolbar = findViewById(R.id.toolbarMain);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: mapFab = findViewById(R.id.fabMap);.
    mapFab = findViewById(R.id.fabMap);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);.
    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: isAdmin = "admin".equalsIgnoreCase(sp.getString("role", "customer"));.
    isAdmin = "admin".equalsIgnoreCase(sp.getString("role", "customer"));

    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (isAdmin) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: setupAdminNavigation();.
      setupAdminNavigation();
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: setupCustomerNavigation();.
      setupCustomerNavigation();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setupAdminNavigation với phạm vi truy cập tương ứng.
  private void setupAdminNavigation() {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (bottomNav != null) bottomNav.setVisibility(View.GONE);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (mapFab != null) mapFab.setVisibility(View.GONE);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (toolbar != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: toolbar.setVisibility(View.VISIBLE);.
      toolbar.setVisibility(View.VISIBLE);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: toolbar.setNavigationIcon(R.drawable.ic_menu_24);.
      toolbar.setNavigationIcon(R.drawable.ic_menu_24);
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      toolbar.setNavigationOnClickListener(v -> {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (drawerLayout != null) drawerLayout.openDrawer(GravityCompat.START);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (drawerLayout != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);.
      drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (adminNavView != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adminNavView.setVisibility(View.VISIBLE);.
      adminNavView.setVisibility(View.VISIBLE);
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      adminNavView.setNavigationItemSelectedListener(item -> {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: boolean handled = handleAdminDestination(item.getItemId());.
        boolean handled = handleAdminDestination(item.getItemId());
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (handled && drawerLayout != null) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: drawerLayout.closeDrawer(GravityCompat.START);.
          drawerLayout.closeDrawer(GravityCompat.START);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Trả về kết quả handled;.
        return handled;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adminNavView.setCheckedItem(R.id.nav_admin_products);.
      adminNavView.setCheckedItem(R.id.nav_admin_products);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: handleAdminDestination(R.id.nav_admin_products);.
    handleAdminDestination(R.id.nav_admin_products);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setupCustomerNavigation với phạm vi truy cập tương ứng.
  private void setupCustomerNavigation() {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (drawerLayout != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);.
      drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (adminNavView != null) adminNavView.setVisibility(View.GONE);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (toolbar != null) toolbar.setVisibility(View.GONE);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (bottomNav == null) return;

    // Thực hiện lời gọi phương thức hoặc khởi tạo: bottomNav.setVisibility(View.VISIBLE);.
    bottomNav.setVisibility(View.VISIBLE);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    bottomNav.setOnItemSelectedListener(item -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: int id = item.getItemId();.
      int id = item.getItemId();
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (id == R.id.tab_cart) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new CartFragment());.
        replaceFragment(new CartFragment());
        // Trả về kết quả true;.
        return true;
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else if (id == R.id.tab_orders) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new OrdersFragment());.
        replaceFragment(new OrdersFragment());
        // Trả về kết quả true;.
        return true;
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else if (id == R.id.tab_chat) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new ChatFragment());.
        replaceFragment(new ChatFragment());
        // Trả về kết quả true;.
        return true;
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else if (id == R.id.tab_profile) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(new Intent(this, ProfileActivity.class));.
        startActivity(new Intent(this, ProfileActivity.class));
        // Trả về kết quả true;.
        return true;
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new HomeFragment());.
        replaceFragment(new HomeFragment());
        // Trả về kết quả true;.
        return true;
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: bottomNav.setSelectedItemId(R.id.tab_home);.
    bottomNav.setSelectedItemId(R.id.tab_home);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new HomeFragment());.
    replaceFragment(new HomeFragment());

    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (mapFab != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: mapFab.setVisibility(View.VISIBLE);.
      mapFab.setVisibility(View.VISIBLE);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: mapFab.setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));.
      mapFab.setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức handleAdminDestination với phạm vi truy cập tương ứng.
  private boolean handleAdminDestination(@IdRes int menuId) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (menuId == R.id.nav_admin_products) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new AdminProductsFragment());.
      replaceFragment(new AdminProductsFragment());
      // Trả về kết quả true;.
      return true;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (menuId == R.id.nav_admin_categories) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new AdminCategoriesFragment());.
      replaceFragment(new AdminCategoriesFragment());
      // Trả về kết quả true;.
      return true;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (menuId == R.id.nav_admin_users) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new AdminUsersFragment());.
      replaceFragment(new AdminUsersFragment());
      // Trả về kết quả true;.
      return true;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (menuId == R.id.nav_admin_chat) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: replaceFragment(new AdminChatFragment());.
      replaceFragment(new AdminChatFragment());
      // Trả về kết quả true;.
      return true;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (menuId == R.id.nav_admin_profile) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(new Intent(this, ProfileActivity.class));.
      startActivity(new Intent(this, ProfileActivity.class));
      // Trả về kết quả true;.
      return true;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả false;.
    return false;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức replaceFragment với phạm vi truy cập tương ứng.
  private void replaceFragment(Fragment fragment) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: getSupportFragmentManager().
    getSupportFragmentManager()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .beginTransaction().
        .beginTransaction()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .replace(R.id.container, fragment).
        .replace(R.id.container, fragment)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .commit();.
        .commit();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onBackPressed với phạm vi truy cập tương ứng.
  public void onBackPressed() {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (isAdmin && drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: drawerLayout.closeDrawer(GravityCompat.START);.
      drawerLayout.closeDrawer(GravityCompat.START);
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onBackPressed();.
    super.onBackPressed();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
