// Khai báo package com.drinkorder cho toàn bộ lớp.
package com.drinkorder;
// Import android.app.Application để sử dụng các lớp hoặc hàm tương ứng.
import android.app.Application;
// Import com.drinkorder.data.SeedInitializer để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.SeedInitializer;
// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;

// Định nghĩa lớp App kế thừa Application.
public class App extends Application {
  // Áp dụng annotation @Override và ghi đè phương thức onCreate.
  @Override public void onCreate(){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate();.
    super.onCreate();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: SeedInitializer.runIfFirstLaunch(this, AppDatabase.get(this));.
    SeedInitializer.runIfFirstLaunch(this, AppDatabase.get(this));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
