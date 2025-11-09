package com.drinkorder.ui.admin;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.repo.CatalogRepository;
import java.util.List;

public class AdminCategoryVM extends AndroidViewModel {
    private CatalogRepository repository;
    private LiveData<List<CategoryEntity>> allCategories;

    public AdminCategoryVM(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.get(application); // Lấy instance DB
        repository = new CatalogRepository(db.categoryDao(), db.productDao()); // Khởi tạo repo
        allCategories = repository.categories(); // Lấy LiveData từ repo
    }

    public void insert(CategoryEntity category) {
        // Chạy async để insert (Room không cho phép trên main thread)
        new Thread(() -> repository.c.insert(category)).start();
    }

    public void update(CategoryEntity category) {
        new Thread(() -> repository.c.update(category)).start(); // Giả định bạn thêm update vào CategoryDao
    }

    public void delete(CategoryEntity category) {
        new Thread(() -> repository.c.delete(category)).start(); // Giả định bạn thêm delete vào CategoryDao
    }

    public LiveData<List<CategoryEntity>> getAllCategories() {
        return allCategories;
    }
}