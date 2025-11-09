package com.drinkorder.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.ProductEntity;
import com.google.android.material.imageview.ShapeableImageView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.VH> {

    public interface OnAdd { void onAdd(ProductEntity p); }
    public interface OnClick { void onClick(ProductEntity p); }

    private final OnAdd onAdd;
    private final OnClick onClick;
    private final List<ProductEntity> data = new ArrayList<>();
    private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    private static final String[] RATINGS = {"4.8", "4.6", "4.9", "4.7"};
    private static final String[] ETA = {"15 phút", "12 phút", "20 phút", "18 phút"};
    private static final String[] DELIVERY = {"Miễn phí", "12K", "Miễn phí", "9K"};

    public ProductsAdapter(OnAdd onAdd, OnClick onClick){
        this.onAdd = onAdd;
        this.onClick = onClick;
        priceFormat.setMaximumFractionDigits(0);
    }

    public void submit(List<ProductEntity> list){
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position){
        ProductEntity e = data.get(position);
        h.title.setText(e.name);
        h.subtitle.setText(e.description == null || e.description.isEmpty()
                ? "Thức uống được ưa chuộng" : e.description);
        h.rating.setText(RATINGS[position % RATINGS.length]);
        h.delivery.setText(DELIVERY[position % DELIVERY.length]);
        h.eta.setText(ETA[position % ETA.length]);
        h.price.setText(formatPrice(e.price));

        Glide.with(h.img.getContext())
                .load(e.imageUrl == null ? "" : e.imageUrl)
                .apply(new RequestOptions()
                        .transform(new CenterCrop())
                        .placeholder(R.drawable.bg_app_gradient)
                        .error(R.drawable.bg_app_gradient))
                .into(h.img);

        h.btn.setOnClickListener(v -> { if (onAdd != null) onAdd.onAdd(e); });
        h.itemView.setOnClickListener(v -> { if (onClick != null) onClick.onClick(e); });
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder{
        final ShapeableImageView img;
        final TextView title;
        final TextView subtitle;
        final TextView rating;
        final TextView delivery;
        final TextView eta;
        final TextView price;
        final Button btn;

        VH(View v){
            super(v);
            img   = v.findViewById(R.id.img);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            rating = v.findViewById(R.id.tvRating);
            delivery = v.findViewById(R.id.tvDelivery);
            eta = v.findViewById(R.id.tvEta);
            price = v.findViewById(R.id.price);
            btn   = v.findViewById(R.id.btnAdd);
        }
    }

    private String formatPrice(double price){
        return priceFormat.format(Math.round(price)) + " đ";
    }
}
