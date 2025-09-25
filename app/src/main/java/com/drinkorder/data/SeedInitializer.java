package com.drinkorder.data;
import android.content.Context;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.*;
import org.json.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;

public class SeedInitializer {
  public static void runIfFirstLaunch(Context ctx, AppDatabase db){
    Executors.newSingleThreadExecutor().execute(() -> {
      try {
        if (db.userDao().count() > 0) return;
        try (InputStream is = ctx.getAssets().open("seed.json")){
          String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
          JSONObject root = new JSONObject(json);

          JSONArray users = root.optJSONArray("users");
          if (users != null){
            for (int i=0;i<users.length();i++){
              JSONObject o = users.getJSONObject(i);
              UserEntity u = new UserEntity();
              u.username = o.getString("username");
              u.passwordHash = o.getString("passwordHash");
              u.fullName = o.optString("fullName", null);
              u.role = o.optString("role","customer");
              u.createdAt = System.currentTimeMillis();
              db.userDao().insert(u);
            }
          }
          JSONArray cats = root.optJSONArray("categories");
          if (cats != null){
            java.util.List<CategoryEntity> list = new ArrayList<>();
            for (int i=0;i<cats.length();i++){
              JSONObject o = cats.getJSONObject(i);
              CategoryEntity c = new CategoryEntity();
              c.name = o.getString("name");
              c.description = o.optString("description", null);
              c.createdAt = System.currentTimeMillis();
              list.add(c);
            }
            db.categoryDao().upsertAll(list);
          }
          JSONArray prods = root.optJSONArray("products");
          if (prods != null){
            java.util.List<ProductEntity> list = new ArrayList<>();
            for (int i=0;i<prods.length();i++){
              JSONObject o = prods.getJSONObject(i);
              ProductEntity p = new ProductEntity();
              p.name = o.getString("name");
              p.description = o.optString("description", null);
              p.price = o.getDouble("price");
              p.stock = o.optInt("stock", 0);
              p.categoryId = o.getInt("categoryId");
              p.imageUrl = o.optString("imageUrl", "");
              list.add(p);
            }
            db.productDao().upsertAll(list);
          }
        }
      } catch (Exception ignored){}
    });
  }
}
