package com.drinkorder.ui.cart;

import android.os.Bundle;
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
import com.drinkorder.data.db.entity.CartItemEntity;
import com.drinkorder.data.db.pojo.CartItemWithProduct;
import com.drinkorder.vm.CartVM;
import com.drinkorder.vm.OrdersVM;

import java.text.NumberFormat;
import java.util.Locale;

public class CartFragment extends Fragment {
  private CartVM vm;
  private OrdersVM ordersVM;
  private CartAdapter adapter;
  private TextView tvTotal;
  private TextView tvGrandTotal;
  private NumberFormat priceFormat;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    priceFormat.setMaximumFractionDigits(0);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b){
    View v = inf.inflate(R.layout.fragment_cart, c, false);
    RecyclerView rv = v.findViewById(R.id.rvCart);
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new CartAdapter(new CartAdapter.Callback(){
      @Override public void onPlus(CartItemEntity e){ vm.setQty(e.productId, e.quantity+1); }
      @Override public void onMinus(CartItemEntity e){ vm.setQty(e.productId, Math.max(1, e.quantity-1)); }
      @Override public void onRemove(CartItemEntity e){ vm.remove(e.cartItemId); }
    });
    rv.setAdapter(adapter);
    tvTotal = v.findViewById(R.id.tvTotal);
    tvGrandTotal = v.findViewById(R.id.tvGrandTotal);
    Button btnCheckout = v.findViewById(R.id.btnCheckout);
    btnCheckout.setOnClickListener(vv -> ordersVM.checkout(1, "Cash", new com.drinkorder.data.repo.OrderRepository.Callback(){
      @Override public void onSuccess(long id){ Toast.makeText(getContext(),"Order "+id+" created",Toast.LENGTH_SHORT).show(); }
      @Override public void onError(Throwable t){ Toast.makeText(getContext(),"Checkout failed: "+t.getMessage(),Toast.LENGTH_SHORT).show(); }
    }));
    return v;
  }

  @Override
  public void onViewCreated(@NonNull View v, @Nullable Bundle b){
    super.onViewCreated(v,b);
    vm = new ViewModelProvider(requireActivity()).get(CartVM.class);
    ordersVM = new ViewModelProvider(requireActivity()).get(OrdersVM.class);
    vm.cart.observe(getViewLifecycleOwner(), list -> {
      adapter.submit(list);
      long totalItems = 0;
      double totalPrice = 0d;
      if (list != null) {
        for (CartItemWithProduct row : list) {
          if (row == null || row.item == null) continue;
          int qty = row.item.quantity;
          totalItems += qty;
          double price = row.product != null ? row.product.price : 0d;
          totalPrice += price * qty;
        }
      }
      tvTotal.setText("Items: " + totalItems);
      if (tvGrandTotal != null) {
        tvGrandTotal.setText(formatPrice(totalPrice));
      }
    });
  }

  private String formatPrice(double amount){
    if (priceFormat == null) return "0 VND";
    return priceFormat.format(Math.round(amount)) + " VND";
  }
}
