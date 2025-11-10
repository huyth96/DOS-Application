package com.drinkorder.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminCategoriesFragment extends Fragment {

  private AdminCategoriesVM vm;
  private AdminCategoriesAdapter adapter;
  private RecyclerView recyclerView;
  private View emptyStateContainer;
  private TextView tvCategoriesTotal;
  private TextView tvCategoriesUpdated;
  private TextView tvCategoriesHealth;
  private TextView tvCategoryEmptyTitle;
  private TextView tvCategoryEmptySubtitle;
  private final List<CategoryEntity> allCategories = new ArrayList<>();
  private final SimpleDateFormat lastUpdatedFormat =
      new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
  private String currentQuery = "";
  private MaterialToolbar toolbar;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_admin_categories, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    recyclerView = view.findViewById(R.id.rvAdminCategories);
    emptyStateContainer = view.findViewById(R.id.categoryEmptyState);
    toolbar = view.findViewById(R.id.toolbarAdmin);
    tvCategoriesTotal = view.findViewById(R.id.tvCategoriesTotal);
    tvCategoriesUpdated = view.findViewById(R.id.tvCategoriesUpdated);
    tvCategoriesHealth = view.findViewById(R.id.tvCategoriesHealth);
    tvCategoryEmptyTitle = view.findViewById(R.id.tvCategoryEmptyTitle);
    tvCategoryEmptySubtitle = view.findViewById(R.id.tvCategoryEmptySubtitle);

    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    adapter = new AdminCategoriesAdapter(new AdminCategoriesAdapter.Callback() {
      @Override
      public void onEdit(CategoryEntity category) {
        showCategoryDialog(category);
      }

      @Override
      public void onDelete(CategoryEntity category) {
        confirmDelete(category);
      }
    });
    recyclerView.setAdapter(adapter);

    TextInputEditText edtSearch = view.findViewById(R.id.edtSearchCategories);
    if (edtSearch != null) {
      edtSearch.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          currentQuery = s == null ? "" : s.toString().trim();
          applyCategoryFilters();
        }
        @Override public void afterTextChanged(Editable s) {}
      });
    }

    ExtendedFloatingActionButton fab = view.findViewById(R.id.fabAddCategory);
    fab.setOnClickListener(v -> openCreateCategoryDialog());

    Button btnAddCategory = view.findViewById(R.id.btnAddCategoryFromEmpty);
    if (btnAddCategory != null) {
      btnAddCategory.setOnClickListener(v -> openCreateCategoryDialog());
    }

    vm = new ViewModelProvider(this).get(AdminCategoriesVM.class);
    vm.categories.observe(getViewLifecycleOwner(), list -> {
      allCategories.clear();
      if (list != null) allCategories.addAll(list);
      updateSummary(list);
      applyCategoryFilters();
    });
  }

  private void showCategoryDialog(@Nullable CategoryEntity editing) {
    AdminCategoryDialog dialog = AdminCategoryDialog.newInstance(editing);
    dialog.setListener((name, description) -> saveCategory(name, description, editing));
    dialog.show(getChildFragmentManager(), "category_dialog");
  }

  private void openCreateCategoryDialog() {
    showCategoryDialog(null);
  }

  private void saveCategory(String name, String description, @Nullable CategoryEntity editing) {
    CategoryEntity entity = new CategoryEntity();
    if (editing != null) {
      entity.categoryId = editing.categoryId;
      entity.createdAt = editing.createdAt;
    } else {
      entity.createdAt = System.currentTimeMillis();
    }
    entity.name = name;
    entity.description = description;

    vm.save(entity, new AdminCategoriesVM.ActionCallback() {
      @Override
      public void onSuccess() {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), editing == null ? "Category created" : "Category updated", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(Throwable throwable) {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), "Unable to save: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void confirmDelete(@Nullable CategoryEntity category) {
    if (category == null || getContext() == null) return;
    new MaterialAlertDialogBuilder(requireContext())
        .setTitle("Delete category")
        .setMessage("Are you sure you want to delete \"" + category.name + "\"?")
        .setPositiveButton("Delete", (dialog, which) -> deleteCategory(category))
        .setNegativeButton("Cancel", null)
        .show();
  }

  private void deleteCategory(CategoryEntity category) {
    vm.delete(category, new AdminCategoriesVM.ActionCallback() {
      @Override
      public void onSuccess() {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), "Category removed", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(Throwable throwable) {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), "Delete failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void applyCategoryFilters() {
    if (allCategories.isEmpty()) {
      adapter.submit(allCategories);
      toggleEmptyState(true, false);
      return;
    }
    String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());
    if (query.isEmpty()) {
      adapter.submit(allCategories);
      toggleEmptyState(false, false);
      return;
    }
    List<CategoryEntity> filtered = new ArrayList<>();
    for (CategoryEntity category : allCategories) {
      String name = category.name == null ? "" : category.name.toLowerCase(Locale.getDefault());
      String desc = category.description == null ? "" : category.description.toLowerCase(Locale.getDefault());
      if (name.contains(query) || desc.contains(query)) {
        filtered.add(category);
      }
    }
    adapter.submit(filtered);
    toggleEmptyState(filtered.isEmpty(), !query.isEmpty());
  }

  private void toggleEmptyState(boolean showEmpty, boolean fromSearch) {
    if (emptyStateContainer == null || recyclerView == null) return;
    emptyStateContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    if (!showEmpty || tvCategoryEmptyTitle == null || tvCategoryEmptySubtitle == null) return;
    if (allCategories.isEmpty()) {
      tvCategoryEmptyTitle.setText(R.string.admin_categories_empty_title);
      tvCategoryEmptySubtitle.setText(R.string.admin_categories_empty_subtitle);
    } else if (fromSearch) {
      tvCategoryEmptyTitle.setText(R.string.admin_categories_empty_search_title);
      tvCategoryEmptySubtitle.setText(R.string.admin_categories_empty_search_subtitle);
    } else {
      tvCategoryEmptyTitle.setText(R.string.admin_categories_empty_title);
      tvCategoryEmptySubtitle.setText(R.string.admin_categories_empty_subtitle);
    }
  }

  private void updateSummary(@Nullable List<CategoryEntity> list) {
    int count = list == null ? 0 : list.size();
    if (tvCategoriesTotal != null) {
      tvCategoriesTotal.setText(String.valueOf(count));
    }
    String lastUpdatedText;
    int described = 0;
    if (list == null || list.isEmpty()) {
      lastUpdatedText = getString(R.string.admin_categories_updated_placeholder);
    } else {
      long lastUpdated = 0;
      for (CategoryEntity c : list) {
        if (!TextUtils.isEmpty(c.description)) described++;
        if (c.createdAt > lastUpdated) lastUpdated = c.createdAt;
      }
      lastUpdatedText = lastUpdated <= 0
          ? getString(R.string.admin_categories_updated_placeholder)
          : lastUpdatedFormat.format(new Date(lastUpdated));
    }
    if (tvCategoriesUpdated != null) {
      tvCategoriesUpdated.setText(lastUpdatedText);
    }
    String health;
    if (count == 0) {
      health = getString(R.string.admin_categories_health_empty);
    } else {
      int threshold = Math.max(1, (int) Math.ceil(count * 0.6));
      if (described >= threshold) {
        health = getString(R.string.admin_categories_health_ready);
      } else {
        health = getString(R.string.admin_categories_health_building);
      }
    }
    if (tvCategoriesHealth != null) {
      tvCategoriesHealth.setText(health);
    }
    updateToolbarSubtitle(count, lastUpdatedText, health);
  }

  private void updateToolbarSubtitle(int count, String lastUpdatedText, String health) {
    if (toolbar == null) return;
    String subtitle = getString(R.string.admin_toolbar_categories_subtitle_format, count, lastUpdatedText, health);
    toolbar.setSubtitle(subtitle);
  }
}
