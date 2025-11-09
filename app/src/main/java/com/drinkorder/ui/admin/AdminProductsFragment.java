package com.drinkorder.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.db.entity.ProductEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminProductsFragment extends Fragment {

  private AdminProductsVM vm;
  private AdminProductsAdapter adapter;
  private TextView tvEmptyState;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_admin_products, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Toolbar toolbar = view.findViewById(R.id.toolbarAdmin);
    if (toolbar != null) {
      toolbar.setTitle("Quan ly san pham");
      toolbar.setNavigationIcon(null);
    }

    tvEmptyState = view.findViewById(R.id.tvEmptyState);
    RecyclerView rv = view.findViewById(R.id.rvAdminProducts);
    rv.setLayoutManager(new LinearLayoutManager(requireContext()));
    adapter = new AdminProductsAdapter(new AdminProductsAdapter.Callback() {
      @Override public void onEdit(ProductEntity product) {
        if (getContext() == null) return;
        AdminProductFormActivity.start(getContext(), product.productId);
      }

      @Override public void onDelete(ProductEntity product) {
        confirmDelete(product);
      }
    });
    rv.setAdapter(adapter);

    FloatingActionButton fab = view.findViewById(R.id.fabAddProduct);
    fab.setOnClickListener(v -> {
      if (getContext() != null) {
        AdminProductFormActivity.start(getContext(), -1);
      }
    });

    vm = new ViewModelProvider(this).get(AdminProductsVM.class);
    vm.products.observe(getViewLifecycleOwner(), list -> {
      adapter.submit(list);
      tvEmptyState.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
    });
    vm.categories.observe(getViewLifecycleOwner(), list -> adapter.setCategoryNames(toCategoryMap(list)));
  }

  private void confirmDelete(ProductEntity product) {
    if (product == null || getContext() == null) return;
    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
        .setTitle("Xoa san pham")
        .setMessage("Ban co chac muon xoa \"" + product.name + "\"?")
        .setPositiveButton("Xoa", (dialog, which) ->
            vm.deleteProduct(product, new AdminProductsVM.ActionCallback() {
              @Override public void onSuccess() {
                Toast.makeText(getContext(), "Da xoa san pham", Toast.LENGTH_SHORT).show();
              }

              @Override public void onError(Throwable throwable) {
                Toast.makeText(getContext(), "Xoa that bai: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }))
        .setNegativeButton("Huy", null)
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
}
