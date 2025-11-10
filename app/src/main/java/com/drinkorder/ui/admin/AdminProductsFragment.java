package com.drinkorder.ui.admin;

import android.os.Bundle;
import android.text.Editable;
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
import com.drinkorder.data.db.entity.ProductEntity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

public class AdminProductsFragment extends Fragment {

  private AdminProductsVM vm;
  private AdminProductsAdapter adapter;
  private TextView tvProductCount;
  private TextView tvCategoryCount;
  private View emptyStateContainer;
  private TextView tvEmptyTitle;
  private TextView tvEmptySubtitle;
  private RecyclerView recyclerView;
  private final List<ProductEntity> allProducts = new ArrayList<>();
  private Map<Integer, String> categoryNames = new HashMap<>();
  private String currentQuery = "";
  private TextInputEditText edtSearch;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_admin_products, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    tvProductCount = view.findViewById(R.id.tvProductCount);
    tvCategoryCount = view.findViewById(R.id.tvCategoryCount);
    emptyStateContainer = view.findViewById(R.id.emptyStateContainer);
    tvEmptyTitle = view.findViewById(R.id.tvEmptyState);
    tvEmptySubtitle = view.findViewById(R.id.tvEmptySubtitle);
    recyclerView = view.findViewById(R.id.rvAdminProducts);
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    adapter = new AdminProductsAdapter(new AdminProductsAdapter.Callback() {
      @Override public void onEdit(ProductEntity product) {
        if (getContext() == null) return;
        AdminProductFormActivity.start(getContext(), product.productId);
      }

      @Override public void onDelete(ProductEntity product) {
        confirmDelete(product);
      }
    });
    recyclerView.setAdapter(adapter);

    edtSearch = view.findViewById(R.id.edtSearchProducts);
    if (edtSearch != null) {
      edtSearch.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          currentQuery = s == null ? "" : s.toString().trim();
          applyFilters();
        }
        @Override public void afterTextChanged(Editable s) {}
      });
    }

    ExtendedFloatingActionButton fab = view.findViewById(R.id.fabAddProduct);
    fab.setOnClickListener(v -> openProductForm());

    Button btnAddFromEmpty = view.findViewById(R.id.btnAddProductFromEmpty);
    if (btnAddFromEmpty != null) {
      btnAddFromEmpty.setOnClickListener(v -> openProductForm());
    }

    vm = new ViewModelProvider(this).get(AdminProductsVM.class);
    vm.products.observe(getViewLifecycleOwner(), list -> {
      allProducts.clear();
      if (list != null) allProducts.addAll(list);
      updateProductCount(allProducts.size());
      applyFilters();
    });
    vm.categories.observe(getViewLifecycleOwner(), list -> {
      categoryNames = toCategoryMap(list);
      adapter.setCategoryNames(categoryNames);
      int count = list == null ? 0 : list.size();
      tvCategoryCount.setText(String.format(Locale.getDefault(), "%d %s", count,
          count == 1 ? "category" : "categories"));
      applyFilters();
    });
  }

  private void confirmDelete(ProductEntity product) {
    if (product == null || getContext() == null) return;
    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
        .setTitle("Remove product")
        .setMessage("Are you sure you want to delete \"" + product.name + "\"?")
        .setPositiveButton("Delete", (dialog, which) ->
            vm.deleteProduct(product, new AdminProductsVM.ActionCallback() {
              @Override public void onSuccess() {
                Toast.makeText(getContext(), "Product removed", Toast.LENGTH_SHORT).show();
              }

              @Override public void onError(Throwable throwable) {
                Toast.makeText(getContext(), "Delete failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }))
        .setNegativeButton("Cancel", null)
        .show();
  }

  private Map<Integer, String> toCategoryMap(List<CategoryEntity> list) {
    Map<Integer, String> map = new HashMap<>();
    if (list != null) {
      for (CategoryEntity c : list) {
        map.put(c.categoryId, c.name);
      }
    }
    return map;
  }

  private void applyFilters() {
    List<ProductEntity> filtered = new ArrayList<>();
    String query = currentQuery == null ? "" : currentQuery.toLowerCase(Locale.getDefault());
    for (ProductEntity p : allProducts) {
      if (query.isEmpty()) {
        filtered.add(p);
      } else {
        String name = p.name == null ? "" : p.name.toLowerCase(Locale.getDefault());
        String category = categoryNames.getOrDefault(p.categoryId, "")
            .toLowerCase(Locale.getDefault());
        if (name.contains(query) || category.contains(query)) {
          filtered.add(p);
        }
      }
    }
    adapter.submit(filtered);
    updateEmptyState(filtered.isEmpty(), !query.isEmpty());
  }

  private void updateProductCount(int total) {
    if (tvProductCount == null) return;
    tvProductCount.setText(String.format(Locale.getDefault(), "%d %s", total,
        total == 1 ? "product" : "products"));
  }

  private void updateEmptyState(boolean showEmpty, boolean isFiltering) {
    if (recyclerView == null || emptyStateContainer == null) return;
    recyclerView.setVisibility(showEmpty ? View.GONE : View.VISIBLE);
    emptyStateContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    if (!showEmpty) return;
    if (tvEmptyTitle == null || tvEmptySubtitle == null) return;
    if (allProducts.isEmpty()) {
      tvEmptyTitle.setText("No products yet");
      tvEmptySubtitle.setText("Tap the button below to add your first drink.");
    } else if (isFiltering) {
      tvEmptyTitle.setText("No products match your search");
      tvEmptySubtitle.setText("Try another keyword or clear the filter.");
    } else {
      tvEmptyTitle.setText("No products found");
      tvEmptySubtitle.setText("Add a new product to get started.");
    }
  }

  private void openProductForm() {
    if (getContext() != null) {
      AdminProductFormActivity.start(getContext(), -1);
    }
  }
}

