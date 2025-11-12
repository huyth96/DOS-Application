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

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.VH> { // Định nghĩa lớp ProductsAdapter kế thừa từ RecyclerView.Adapter, với ViewHolder là VH, dùng để hiển thị danh sách sản phẩm trong RecyclerView.

    public interface OnAdd { void onAdd(ProductEntity p); } // Interface định nghĩa callback cho hành động thêm sản phẩm, nhận entity sản phẩm.
    public interface OnClick { void onClick(ProductEntity p); } // Interface định nghĩa callback cho hành động click sản phẩm, nhận entity sản phẩm.

    private final OnAdd onAdd; // Biến final lưu callback OnAdd được truyền vào constructor.
    private final OnClick onClick; // Biến final lưu callback OnClick được truyền vào constructor.
    private final List<ProductEntity> data = new ArrayList<>(); // Danh sách lưu dữ liệu sản phẩm, khởi tạo là ArrayList rỗng.
    private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN")); // Biến final lưu NumberFormat để định dạng giá theo locale Việt Nam.

    private static final String[] ETA = {"15 minutes", "12 minutes", "20 minutes", "18 minutes"}; // Mảng tĩnh lưu các giá trị thời gian ước tính (ETA) giả định.
    private static final String[] DELIVERY = {"Free", "12K", "Free", "9K"}; // Mảng tĩnh lưu các giá trị phí giao hàng giả định.

    public ProductsAdapter(OnAdd onAdd, OnClick onClick){ // Constructor của adapter, nhận hai callback OnAdd và OnClick.
        this.onAdd = onAdd; // Gán callback OnAdd vào biến instance.
        this.onClick = onClick; // Gán callback OnClick vào biến instance.
        priceFormat.setMaximumFractionDigits(0); // Thiết lập NumberFormat không hiển thị thập phân cho giá.
    }

    public void submit(List<ProductEntity> list){ // Phương thức submit dữ liệu sản phẩm mới vào adapter.
        data.clear(); // Xóa danh sách dữ liệu hiện tại.
        if (list != null) data.addAll(list); // Nếu list không null, thêm tất cả vào data.
        notifyDataSetChanged(); // Thông báo adapter thay đổi dữ liệu để refresh RecyclerView.
    }

    @NonNull @Override // Ghi đè phương thức onCreateViewHolder từ Adapter, đảm bảo trả về không null.
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){ // Phương thức tạo ViewHolder mới.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false); // Inflate layout item từ resource.
        return new VH(v); // Trả về ViewHolder mới với view đã inflate.
    }

    @Override // Ghi đè phương thức onBindViewHolder từ Adapter.
    public void onBindViewHolder(@NonNull VH h, int position){ // Phương thức bind dữ liệu vào ViewHolder tại vị trí position.
        ProductEntity e = data.get(position); // Lấy sản phẩm tại vị trí từ danh sách data.
        h.title.setText(e.name); // Đặt tên sản phẩm vào TextView title.
        h.subtitle.setText(resolveDescription(e.description)); // Đặt mô tả đã resolve vào TextView subtitle.
        h.rating.setText(formatRating(e.rating)); // Đặt đánh giá đã định dạng vào TextView rating.
        h.delivery.setText(DELIVERY[position % DELIVERY.length]); // Đặt phí giao hàng từ mảng theo vị trí modulo.
        h.eta.setText(ETA[position % ETA.length]); // Đặt ETA từ mảng theo vị trí modulo.
        h.price.setText(formatPrice(e.price)); // Đặt giá đã định dạng vào TextView price.

        Glide.with(h.img.getContext()) // Sử dụng Glide để load hình ảnh.
                .load(resolveImageSource(e.imageUrl)) // Load từ nguồn hình ảnh đã resolve.
                .apply(new RequestOptions() // Áp dụng các options cho Glide.
                        .transform(new CenterCrop()) // Transform crop center.
                        .placeholder(R.drawable.bg_app_gradient) // Placeholder nếu đang load.
                        .error(R.drawable.bg_app_gradient)) // Error nếu load thất bại.
                .into(h.img); // Load vào ImageView img.

        h.btn.setOnClickListener(v -> { if (onAdd != null) onAdd.onAdd(e); }); // Thiết lập listener cho nút thêm, gọi callback OnAdd nếu có.
        h.itemView.setOnClickListener(v -> { if (onClick != null) onClick.onClick(e); }); // Thiết lập listener cho toàn itemView, gọi callback OnClick nếu có.
    }

    @Override public int getItemCount(){ return data.size(); } // Ghi đè phương thức getItemCount, trả về kích thước danh sách data.

    static class VH extends RecyclerView.ViewHolder{ // Lớp static ViewHolder kế thừa từ RecyclerView.ViewHolder.
        final ShapeableImageView img; // Biến final lưu ImageView hình ảnh.
        final TextView title; // Biến final lưu TextView title.
        final TextView subtitle; // Biến final lưu TextView subtitle.
        final TextView rating; // Biến final lưu TextView rating.
        final TextView delivery; // Biến final lưu TextView delivery.
        final TextView eta; // Biến final lưu TextView eta.
        final TextView price; // Biến final lưu TextView price.
        final Button btn; // Biến final lưu Button thêm.

        VH(View v){ // Constructor của ViewHolder, nhận view.
            super(v); // Gọi constructor cha.
            img   = v.findViewById(R.id.img); // Tìm và lưu ImageView img.
            title = v.findViewById(R.id.title); // Tìm và lưu TextView title.
            subtitle = v.findViewById(R.id.subtitle); // Tìm và lưu TextView subtitle.
            rating = v.findViewById(R.id.tvRating); // Tìm và lưu TextView rating.
            delivery = v.findViewById(R.id.tvDelivery); // Tìm và lưu TextView delivery.
            eta = v.findViewById(R.id.tvEta); // Tìm và lưu TextView eta.
            price = v.findViewById(R.id.price); // Tìm và lưu TextView price.
            btn   = v.findViewById(R.id.btnAdd); // Tìm và lưu Button btn.
        }
    }

    private String formatPrice(double price){ // Phương thức private định dạng giá.
        return priceFormat.format(Math.round(price)) + " VND"; // Định dạng giá nguyên và thêm " VND".
    }

    private String formatRating(Double rating){ // Phương thức private định dạng đánh giá.
        double value = (rating == null || rating <= 0) ? 4.5 : rating; // Nếu rating null hoặc <=0, dùng 4.5, ngược lại dùng giá trị.
        return String.format(Locale.getDefault(), "%.1f", value); // Trả về string định dạng 1 thập phân.
    }

    private Object resolveImageSource(String imageUrl){ // Phương thức private resolve nguồn hình ảnh.
        if (imageUrl == null || imageUrl.trim().isEmpty()) return R.drawable.bg_app_gradient; // Nếu URL null hoặc rỗng sau trim, trả về drawable mặc định.
        return imageUrl.trim(); // Ngược lại trả về URL đã trim.
    }

    private String resolveDescription(String description){ // Phương thức private resolve mô tả.
        if (description == null || description.trim().isEmpty()) { // Nếu mô tả null hoặc rỗng sau trim.
            return "This drink is trending right now"; // Trả về mô tả mặc định.
        }
        return description; // Ngược lại trả về mô tả gốc.
    }
}
