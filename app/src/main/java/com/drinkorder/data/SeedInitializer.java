// Khai báo package com.drinkorder.data cho toàn bộ lớp.
package com.drinkorder.data;
// Import android.content.Context để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Context;
// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.entity.* để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.*;
// Import org.json.* để sử dụng các lớp hoặc hàm tương ứng.
import org.json.*;
// Import java.io.InputStream để sử dụng các lớp hoặc hàm tương ứng.
import java.io.InputStream;
// Import java.nio.charset.StandardCharsets để sử dụng các lớp hoặc hàm tương ứng.
import java.nio.charset.StandardCharsets;
// Import java.util.* để sử dụng các lớp hoặc hàm tương ứng.
import java.util.*;
// Import java.util.concurrent.Executors để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executors;

// Định nghĩa lớp SeedInitializer.
public class SeedInitializer {
  // Định nghĩa phương thức runIfFirstLaunch với phạm vi truy cập tương ứng.
  public static void runIfFirstLaunch(Context ctx, AppDatabase db){
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    Executors.newSingleThreadExecutor().execute(() -> {
      // Bắt đầu khối try để bắt lỗi có thể phát sinh.
      try {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (db.userDao().count() > 0) return;
        // Bắt đầu khối try để bắt lỗi có thể phát sinh.
        try (InputStream is = ctx.getAssets().open("seed.json")){
          // Thực hiện lời gọi phương thức hoặc khởi tạo: String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);.
          String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
          // Thực hiện lời gọi phương thức hoặc khởi tạo: JSONObject root = new JSONObject(json);.
          JSONObject root = new JSONObject(json);

          // Thực hiện lời gọi phương thức hoặc khởi tạo: JSONArray users = root.optJSONArray("users");.
          JSONArray users = root.optJSONArray("users");
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (users != null){
            // Bắt đầu vòng lặp for để duyệt dữ liệu.
            for (int i=0;i<users.length();i++){
              // Thực hiện lời gọi phương thức hoặc khởi tạo: JSONObject o = users.getJSONObject(i);.
              JSONObject o = users.getJSONObject(i);
              // Thực hiện lời gọi phương thức hoặc khởi tạo: UserEntity u = new UserEntity();.
              UserEntity u = new UserEntity();
              // Thực hiện lời gọi phương thức hoặc khởi tạo: u.username = o.getString("username");.
              u.username = o.getString("username");
              // Thực hiện lời gọi phương thức hoặc khởi tạo: u.passwordHash = o.getString("passwordHash");.
              u.passwordHash = o.getString("passwordHash");
              // Thực hiện lời gọi phương thức hoặc khởi tạo: u.fullName = o.optString("fullName", null);.
              u.fullName = o.optString("fullName", null);
              // Thực hiện lời gọi phương thức hoặc khởi tạo: u.role = o.optString("role","customer");.
              u.role = o.optString("role","customer");
              // Thực hiện lời gọi phương thức hoặc khởi tạo: u.createdAt = System.currentTimeMillis();.
              u.createdAt = System.currentTimeMillis();
              // Thực hiện lời gọi phương thức hoặc khởi tạo: u.isBanned = o.optBoolean("isBanned", false);.
              u.isBanned = o.optBoolean("isBanned", false);
              // Thực hiện lời gọi phương thức hoặc khởi tạo: db.userDao().insert(u);.
              db.userDao().insert(u);
            // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
            }
          // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
          }
          // Thực hiện lời gọi phương thức hoặc khởi tạo: JSONArray cats = root.optJSONArray("categories");.
          JSONArray cats = root.optJSONArray("categories");
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (cats != null){
            // Thực hiện lời gọi phương thức hoặc khởi tạo: java.util.List<CategoryEntity> list = new ArrayList<>();.
            java.util.List<CategoryEntity> list = new ArrayList<>();
            // Bắt đầu vòng lặp for để duyệt dữ liệu.
            for (int i=0;i<cats.length();i++){
              // Thực hiện lời gọi phương thức hoặc khởi tạo: JSONObject o = cats.getJSONObject(i);.
              JSONObject o = cats.getJSONObject(i);
              // Thực hiện lời gọi phương thức hoặc khởi tạo: CategoryEntity c = new CategoryEntity();.
              CategoryEntity c = new CategoryEntity();
              // Thực hiện lời gọi phương thức hoặc khởi tạo: c.name = o.getString("name");.
              c.name = o.getString("name");
              // Thực hiện lời gọi phương thức hoặc khởi tạo: c.description = o.optString("description", null);.
              c.description = o.optString("description", null);
              // Thực hiện lời gọi phương thức hoặc khởi tạo: c.createdAt = System.currentTimeMillis();.
              c.createdAt = System.currentTimeMillis();
              // Thực hiện lời gọi phương thức hoặc khởi tạo: list.add(c);.
              list.add(c);
            // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
            }
            // Thực hiện lời gọi phương thức hoặc khởi tạo: db.categoryDao().upsertAll(list);.
            db.categoryDao().upsertAll(list);
          // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
          }
          // Thực hiện lời gọi phương thức hoặc khởi tạo: JSONArray prods = root.optJSONArray("products");.
          JSONArray prods = root.optJSONArray("products");
          // Kiểm tra điều kiện if để quyết định luồng xử lý.
          if (prods != null){
            // Thực hiện lời gọi phương thức hoặc khởi tạo: java.util.List<ProductEntity> list = new ArrayList<>();.
            java.util.List<ProductEntity> list = new ArrayList<>();
            // Bắt đầu vòng lặp for để duyệt dữ liệu.
            for (int i=0;i<prods.length();i++){
              // Thực hiện lời gọi phương thức hoặc khởi tạo: JSONObject o = prods.getJSONObject(i);.
              JSONObject o = prods.getJSONObject(i);
              // Thực hiện lời gọi phương thức hoặc khởi tạo: ProductEntity p = new ProductEntity();.
              ProductEntity p = new ProductEntity();
              // Thực hiện lời gọi phương thức hoặc khởi tạo: p.name = o.getString("name");.
              p.name = o.getString("name");
              // Thực hiện lời gọi phương thức hoặc khởi tạo: p.description = o.optString("description", null);.
              p.description = o.optString("description", null);
              // Thực hiện lời gọi phương thức hoặc khởi tạo: p.price = o.getDouble("price");.
              p.price = o.getDouble("price");
              // Thực hiện lời gọi phương thức hoặc khởi tạo: p.stock = o.optInt("stock", 0);.
              p.stock = o.optInt("stock", 0);
              // Thực hiện lời gọi phương thức hoặc khởi tạo: p.categoryId = o.getInt("categoryId");.
              p.categoryId = o.getInt("categoryId");
              // Thực hiện lời gọi phương thức hoặc khởi tạo: p.imageUrl = o.optString("imageUrl", "");.
              p.imageUrl = o.optString("imageUrl", "");
              // Kiểm tra điều kiện if để quyết định luồng xử lý.
              if (o.has("rating")) p.rating = o.optDouble("rating");
              // Gán giá trị cho biến hoặc thuộc tính: else p.rating = 4.5.
              else p.rating = 4.5;
              // Thực hiện lời gọi phương thức hoặc khởi tạo: list.add(p);.
              list.add(p);
            // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
            }
            // Thực hiện lời gọi phương thức hoặc khởi tạo: db.productDao().upsertAll(list);.
            db.productDao().upsertAll(list);
          // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
          }
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Kết thúc khối lệnh vừa mở phía trên.
      } catch (Exception ignored){}
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
