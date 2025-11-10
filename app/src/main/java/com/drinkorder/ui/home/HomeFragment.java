package com.drinkorder.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.drinkorder.data.db.pojo.CartItemWithProduct;
import com.drinkorder.ui.detail.ProductDetailFragment;
import com.drinkorder.ui.map.MapActivity;
import com.drinkorder.vm.CartVM;
import com.drinkorder.vm.HomeVM;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

  private HomeVM vm;
  private CartVM cartVM;
  private ProductsAdapter adapter;

  private ChipGroup chipCategories;
  private TextView tvCartBadge;
  private TextView tvGreetingLine;
  private TextView tvGreetingHighlight;
  private TextInputEditText edtSearch;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    View v = inflater.inflate(R.layout.fragment_home, container, false);

    RecyclerView rv = v.findViewById(R.id.rvProducts);
    rv.setLayoutManager(new LinearLayoutManager(requireContext()));
    adapter = new ProductsAdapter(
        product -> {
          cartVM.add(product);
        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
        },
        this::openDetail
    );
    rv.setAdapter(adapter);

    chipCategories = v.findViewById(R.id.chipCategories);
    tvCartBadge = v.findViewById(R.id.tvCartBadge);
    tvGreetingLine = v.findViewById(R.id.tvGreetingLine);
    tvGreetingHighlight = v.findViewById(R.id.tvGreetingHighlight);
    edtSearch = v.findViewById(R.id.edtSearch);

    View btnMenu = v.findViewById(R.id.btnOpenMenu);
    View btnCartTop = v.findViewById(R.id.btnCartTop);
    MaterialCardView btnShortcutFast = v.findViewById(R.id.btnShortcutFast);
    MaterialCardView btnShortcutLocations = v.findViewById(R.id.btnShortcutLocations);
    MaterialCardView btnShortcutOrders = v.findViewById(R.id.btnShortcutOrders);

    btnCartTop.setOnClickListener(view -> switchTab(R.id.tab_cart));
    btnShortcutFast.setOnClickListener(view -> switchTab(R.id.tab_cart));
    btnShortcutOrders.setOnClickListener(view -> switchTab(R.id.tab_orders));
    btnShortcutLocations.setOnClickListener(view -> startActivity(new Intent(requireContext(), MapActivity.class)));
    btnMenu.setOnClickListener(view ->
        Toast.makeText(getContext(), "Quick menu is coming soon", Toast.LENGTH_SHORT).show());

    v.findViewById(R.id.btnSeeAllProducts).setOnClickListener(view ->
        Toast.makeText(getContext(), "All products will be visible soon", Toast.LENGTH_SHORT).show());

    v.findViewById(R.id.btnSeeAllCategories).setOnClickListener(view ->
        Toast.makeText(getContext(), "Category details will be added soon", Toast.LENGTH_SHORT).show());

    edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
      if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
        performSearch(textView.getText());
        return true;
      }
      return false;
    });

    setupGreeting();

    return v;
  }

  @Override
  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState){
    super.onViewCreated(v, savedInstanceState);
    vm = new ViewModelProvider(this).get(HomeVM.class);
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);

    vm.products.observe(getViewLifecycleOwner(), adapter::submit);
    vm.categories.observe(getViewLifecycleOwner(), this::renderCategories);
    vm.selectedCategory.observe(getViewLifecycleOwner(), this::highlightSelectedCategory);

    cartVM.cart.observe(getViewLifecycleOwner(), list -> {
      int total = 0;
      if (list != null) {
        for (CartItemWithProduct row : list) {
          if (row == null || row.item == null) continue;
          total += row.item.quantity;
        }
      }
      if (total > 0) {
        tvCartBadge.setVisibility(View.VISIBLE);
        tvCartBadge.setText(String.valueOf(total));
      } else {
        tvCartBadge.setVisibility(View.GONE);
      }
    });
  }

  private void renderCategories(List<CategoryEntity> categories) {
    if (chipCategories == null) return;
    chipCategories.removeAllViews();
    LayoutInflater inflater = LayoutInflater.from(requireContext());
    Integer selected = vm.selectedCategory.getValue();

    if (categories != null && !categories.isEmpty() && selected == null) {
      vm.selectedCategory.setValue(categories.get(0).categoryId);
      selected = categories.get(0).categoryId;
    }

    if (categories != null) {
      for (CategoryEntity cat : categories) {
        Chip chip = (Chip) inflater.inflate(R.layout.item_category_chip, chipCategories, false);
        chip.setText(cat.name);
        chip.setTag(cat.categoryId);
        chip.setChecked(selected != null && selected == cat.categoryId);
        chip.setOnClickListener(view -> vm.selectedCategory.setValue(cat.categoryId));
        chipCategories.addView(chip);
      }
    }
  }

  private void highlightSelectedCategory(Integer selectedId) {
    if (chipCategories == null) return;
    for (int i = 0; i < chipCategories.getChildCount(); i++) {
      View child = chipCategories.getChildAt(i);
      if (child instanceof Chip chip) {
        Object tag = chip.getTag();
        boolean isSelected = tag instanceof Integer && ((Integer) tag) == selectedId;
        chip.setChecked(isSelected);
      }
    }
  }

  private void setupGreeting() {
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    String period;
    if (hour < 11) period = "morning";
    else if (hour < 17) period = "afternoon";
    else period = "evening";

    tvGreetingLine.setText(String.format(Locale.getDefault(), "Good %s,", period));

    SharedPreferences sp = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
    String username = sp.getString("username", null);
    if (TextUtils.isEmpty(username)) {
      tvGreetingHighlight.setText("What drink are you craving today?");
    } else {
      tvGreetingHighlight.setText(String.format(Locale.getDefault(), "%s, what sounds good today?", username));
    }
  }

  private void performSearch(CharSequence query) {
    if (TextUtils.isEmpty(query)) {
      Toast.makeText(getContext(), "Enter a drink name to search", Toast.LENGTH_SHORT).show();
      return;
    }
    Toast.makeText(getContext(), "Search is coming soon!", Toast.LENGTH_SHORT).show();
  }

  private void openDetail(ProductEntity item){
    Fragment f = ProductDetailFragment.newInstance(item.productId);
    requireActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, f)
        .addToBackStack("product_detail")
        .commit();
  }

  private void switchTab(int tabId) {
    BottomNavigationView nav = requireActivity().findViewById(R.id.bottomNav);
    if (nav != null) {
      nav.setSelectedItemId(tabId);
    }
  }
}
