package com.drinkorder.ui.cart;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.CartItemEntity;
import java.util.*;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
  public interface Callback { void onPlus(CartItemEntity e); void onMinus(CartItemEntity e); void onRemove(CartItemEntity e); }
  private final Callback cb; private final List<CartItemEntity> data = new ArrayList<>();
  public CartAdapter(Callback cb){ this.cb=cb; }
  public void submit(List<CartItemEntity> list){ data.clear(); if (list!=null) data.addAll(list); notifyDataSetChanged(); }
  @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vt){ return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_cart, p, false)); }
  @Override public void onBindViewHolder(@NonNull VH h, int i){
    CartItemEntity e = data.get(i);
    h.title.setText("Product #"+e.productId);
    h.qty.setText(String.valueOf(e.quantity));
    h.btnPlus.setOnClickListener(v -> cb.onPlus(e));
    h.btnMinus.setOnClickListener(v -> cb.onMinus(e));
    h.btnRemove.setOnClickListener(v -> cb.onRemove(e));
  }
  @Override public int getItemCount(){ return data.size(); }
  static class VH extends RecyclerView.ViewHolder{
    TextView title, qty; Button btnPlus, btnMinus, btnRemove;
    VH(View v){ super(v); title=v.findViewById(R.id.title); qty=v.findViewById(R.id.qty); btnPlus=v.findViewById(R.id.btnPlus); btnMinus=v.findViewById(R.id.btnMinus); btnRemove=v.findViewById(R.id.btnRemove); }
  }
}
