// Khai báo package com.drinkorder.ui.home cho toàn bộ lớp.
package com.drinkorder.ui.home;

// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;
// Import android.widget.Button để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Button;
// Import android.widget.TextView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.TextView;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.bumptech.glide.Glide để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.Glide;
// Import com.bumptech.glide.load.resource.bitmap.CenterCrop để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
// Import com.bumptech.glide.request.RequestOptions để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.request.RequestOptions;
// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.google.android.material.imageview.ShapeableImageView để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.imageview.ShapeableImageView;

// Import java.text.NumberFormat để sử dụng các lớp hoặc hàm tương ứng.
import java.text.NumberFormat;
// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp ProductsAdapter kế thừa RecyclerView.Adapter<ProductsAdapter.VH>.
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.VH> {

    // Định nghĩa interface OnAdd.
    public interface OnAdd { void onAdd(ProductEntity p); }
    // Định nghĩa interface OnClick.
    public interface OnClick { void onClick(ProductEntity p); }

    // Khai báo thuộc tính với phạm vi truy cập: private final OnAdd onAdd.
    private final OnAdd onAdd;
    // Khai báo thuộc tính với phạm vi truy cập: private final OnClick onClick.
    private final OnClick onClick;
    // Khai báo thuộc tính với phạm vi truy cập: private final List<ProductEntity> data = new ArrayList<>().
    private final List<ProductEntity> data = new ArrayList<>();
    // Khai báo thuộc tính với phạm vi truy cập: private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN")).
    private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    // Khai báo thuộc tính với phạm vi truy cập: private static final String[] ETA = {"15 minutes", "12 minutes", "20 minutes", "18 minutes"}.
    private static final String[] ETA = {"15 minutes", "12 minutes", "20 minutes", "18 minutes"};
    // Khai báo thuộc tính với phạm vi truy cập: private static final String[] DELIVERY = {"Free", "12K", "Free", "9K"}.
    private static final String[] DELIVERY = {"Free", "12K", "Free", "9K"};

    // Định nghĩa phương thức ProductsAdapter với phạm vi truy cập tương ứng.
    public ProductsAdapter(OnAdd onAdd, OnClick onClick){
        // Gán giá trị cho biến hoặc thuộc tính: this.onAdd = onAdd.
        this.onAdd = onAdd;
        // Gán giá trị cho biến hoặc thuộc tính: this.onClick = onClick.
        this.onClick = onClick;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: priceFormat.setMaximumFractionDigits(0);.
        priceFormat.setMaximumFractionDigits(0);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức submit với phạm vi truy cập tương ứng.
    public void submit(List<ProductEntity> list){
        // Thực hiện lời gọi phương thức hoặc khởi tạo: data.clear();.
        data.clear();
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (list != null) data.addAll(list);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
        notifyDataSetChanged();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @NonNull và @Override cho phần tử bên dưới.
    @NonNull @Override
    // Định nghĩa phương thức onCreateViewHolder với phạm vi truy cập tương ứng.
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // Thực hiện lời gọi phương thức hoặc khởi tạo: View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        // Trả về kết quả new VH(v);.
        return new VH(v);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onBindViewHolder với phạm vi truy cập tương ứng.
    public void onBindViewHolder(@NonNull VH h, int position){
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ProductEntity e = data.get(position);.
        ProductEntity e = data.get(position);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.title.setText(e.name);.
        h.title.setText(e.name);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.subtitle.setText(resolveDescription(e.description));.
        h.subtitle.setText(resolveDescription(e.description));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.rating.setText(formatRating(e.rating));.
        h.rating.setText(formatRating(e.rating));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.delivery.setText(DELIVERY[position % DELIVERY.length]);.
        h.delivery.setText(DELIVERY[position % DELIVERY.length]);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.eta.setText(ETA[position % ETA.length]);.
        h.eta.setText(ETA[position % ETA.length]);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.price.setText(formatPrice(e.price));.
        h.price.setText(formatPrice(e.price));

        // Thực hiện lời gọi phương thức hoặc khởi tạo: Glide.with(h.img.getContext()).
        Glide.with(h.img.getContext())
                // Thực hiện lời gọi phương thức hoặc khởi tạo: .load(resolveImageSource(e.imageUrl)).
                .load(resolveImageSource(e.imageUrl))
                // Thực hiện lời gọi phương thức hoặc khởi tạo: .apply(new RequestOptions().
                .apply(new RequestOptions()
                        // Thực hiện lời gọi phương thức hoặc khởi tạo: .transform(new CenterCrop()).
                        .transform(new CenterCrop())
                        // Thực hiện lời gọi phương thức hoặc khởi tạo: .placeholder(R.drawable.bg_app_gradient).
                        .placeholder(R.drawable.bg_app_gradient)
                        // Thực hiện lời gọi phương thức hoặc khởi tạo: .error(R.drawable.bg_app_gradient)).
                        .error(R.drawable.bg_app_gradient))
                // Thực hiện lời gọi phương thức hoặc khởi tạo: .into(h.img);.
                .into(h.img);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.btn.setOnClickListener(v -> { if (onAdd != null) onAdd.onAdd(e); });.
        h.btn.setOnClickListener(v -> { if (onAdd != null) onAdd.onAdd(e); });
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.itemView.setOnClickListener(v -> { if (onClick != null) onClick.onClick(e); });.
        h.itemView.setOnClickListener(v -> { if (onClick != null) onClick.onClick(e); });
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override và ghi đè phương thức getItemCount.
    @Override public int getItemCount(){ return data.size(); }

    // Định nghĩa lớp VH kế thừa RecyclerView.ViewHolder.
    static class VH extends RecyclerView.ViewHolder{
        // Thực thi câu lệnh: final ShapeableImageView img;.
        final ShapeableImageView img;
        // Thực thi câu lệnh: final TextView title;.
        final TextView title;
        // Thực thi câu lệnh: final TextView subtitle;.
        final TextView subtitle;
        // Thực thi câu lệnh: final TextView rating;.
        final TextView rating;
        // Thực thi câu lệnh: final TextView delivery;.
        final TextView delivery;
        // Thực thi câu lệnh: final TextView eta;.
        final TextView eta;
        // Thực thi câu lệnh: final TextView price;.
        final TextView price;
        // Thực thi câu lệnh: final Button btn;.
        final Button btn;

        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        VH(View v){
            // Thực hiện lời gọi phương thức hoặc khởi tạo: super(v);.
            super(v);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: img   = v.findViewById(R.id.img);.
            img   = v.findViewById(R.id.img);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: title = v.findViewById(R.id.title);.
            title = v.findViewById(R.id.title);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: subtitle = v.findViewById(R.id.subtitle);.
            subtitle = v.findViewById(R.id.subtitle);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: rating = v.findViewById(R.id.tvRating);.
            rating = v.findViewById(R.id.tvRating);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: delivery = v.findViewById(R.id.tvDelivery);.
            delivery = v.findViewById(R.id.tvDelivery);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: eta = v.findViewById(R.id.tvEta);.
            eta = v.findViewById(R.id.tvEta);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: price = v.findViewById(R.id.price);.
            price = v.findViewById(R.id.price);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: btn   = v.findViewById(R.id.btnAdd);.
            btn   = v.findViewById(R.id.btnAdd);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức formatPrice với phạm vi truy cập tương ứng.
    private String formatPrice(double price){
        // Trả về kết quả priceFormat.format(Math.round(price)) + " VND";.
        return priceFormat.format(Math.round(price)) + " VND";
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức formatRating với phạm vi truy cập tương ứng.
    private String formatRating(Double rating){
        // Gán giá trị cho biến hoặc thuộc tính: double value = (rating == null || rating <= 0) ? 4.5 : rating.
        double value = (rating == null || rating <= 0) ? 4.5 : rating;
        // Trả về kết quả String.format(Locale.getDefault(), "%.1f", value);.
        return String.format(Locale.getDefault(), "%.1f", value);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức resolveImageSource với phạm vi truy cập tương ứng.
    private Object resolveImageSource(String imageUrl){
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (imageUrl == null || imageUrl.trim().isEmpty()) return R.drawable.bg_app_gradient;
        // Trả về kết quả imageUrl.trim();.
        return imageUrl.trim();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức resolveDescription với phạm vi truy cập tương ứng.
    private String resolveDescription(String description){
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (description == null || description.trim().isEmpty()) {
            // Trả về kết quả "This drink is trending right now";.
            return "This drink is trending right now";
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Trả về kết quả description;.
        return description;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
