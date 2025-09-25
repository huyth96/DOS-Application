package com.drinkorder.ui.home;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.ProductEntity;
import java.util.*;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.VH> {
  public interface OnAdd { void onAdd(ProductEntity p); }
  private final OnAdd cb; private final List<ProductEntity> data = new ArrayList<>();
  public ProductsAdapter(OnAdd cb){ this.cb=cb; }
  public void submit(List<ProductEntity> list){ data.clear(); if (list!=null) data.addAll(list); notifyDataSetChanged(); }
  @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vt){ return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_product, p, false)); }
  @Override public void onBindViewHolder(@NonNull VH h, int i){
    ProductEntity e = data.get(i);
    h.title.setText(e.name);
    h.price.setText(String.valueOf((long)e.price));
    Glide.with(h.img.getContext()).load(e.imageUrl==null? "": e.imageUrl).into(h.img);
    h.btn.setOnClickListener(v -> cb.onAdd(e));
  }
  @Override public int getItemCount(){ return data.size(); }
  static class VH extends RecyclerView.ViewHolder{
    ImageView img; TextView title, price; Button btn;
    VH(View v){ super(v); img=v.findViewById(R.id.img); title=v.findViewById(R.id.title); price=v.findViewById(R.id.price); btn=v.findViewById(R.id.btnAdd); }
  }
}
