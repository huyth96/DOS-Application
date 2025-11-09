package com.drinkorder.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.CartItemEntity;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.data.db.pojo.CartItemWithProduct;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
  public interface Callback { void onPlus(CartItemEntity e); void onMinus(CartItemEntity e); void onRemove(CartItemEntity e); }

  private final Callback cb;
  private final List<CartItemWithProduct> data = new ArrayList<>();
  private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

  public CartAdapter(Callback cb){
    this.cb = cb;
    priceFormat.setMaximumFractionDigits(0);
  }

  public void submit(List<CartItemWithProduct> list){
    data.clear();
    if (list != null) data.addAll(list);
    notifyDataSetChanged();
  }

  @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vt){
    return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_cart, p, false));
  }

  @Override public void onBindViewHolder(@NonNull VH h, int position){
    CartItemWithProduct row = data.get(position);
    CartItemEntity item = row.item;
    ProductEntity product = row.product;

    String title = (product != null && product.name != null && !product.name.isEmpty())
        ? product.name
        : "Product #" + (item != null ? item.productId : "");
    double unitPrice = product != null ? product.price : 0d;
    int qty = item != null ? item.quantity : 0;

    h.title.setText(title);
    h.price.setText(formatPrice(unitPrice));
    h.qty.setText(String.valueOf(qty));

    h.btnPlus.setOnClickListener(v -> {
      if (item != null) cb.onPlus(item);
    });
    h.btnMinus.setOnClickListener(v -> {
      if (item != null) cb.onMinus(item);
    });
    h.btnRemove.setOnClickListener(v -> {
      if (item != null) cb.onRemove(item);
    });
  }

  @Override public int getItemCount(){ return data.size(); }

  private String formatPrice(double price){
    return priceFormat.format(Math.round(price)) + " VND";
  }

  static class VH extends RecyclerView.ViewHolder{
    final TextView title;
    final TextView qty;
    final TextView price;
    final Button btnPlus;
    final Button btnMinus;
    final Button btnRemove;
    VH(View v){
      super(v);
      title = v.findViewById(R.id.title);
      qty = v.findViewById(R.id.qty);
      price = v.findViewById(R.id.tvPrice);
      btnPlus = v.findViewById(R.id.btnPlus);
      btnMinus = v.findViewById(R.id.btnMinus);
      btnRemove = v.findViewById(R.id.btnRemove);
    }
  }
}
