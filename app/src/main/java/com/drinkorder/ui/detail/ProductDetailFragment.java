// Khai báo package com.drinkorder.ui.detail cho toàn bộ lớp.
package com.drinkorder.ui.detail;

// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;
// Import android.widget.ImageButton để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.ImageButton;
// Import android.widget.TextView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.TextView;
// Import android.widget.Toast để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Toast;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.fragment.app.Fragment để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.Fragment;
// Import androidx.lifecycle.ViewModelProvider để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.ViewModelProvider;

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
// Import com.drinkorder.vm.CartVM để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.vm.CartVM;
// Import com.drinkorder.vm.ProductDetailVM để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.vm.ProductDetailVM;
// Import com.google.android.material.button.MaterialButton để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.button.MaterialButton;
// Import com.google.android.material.imageview.ShapeableImageView để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.imageview.ShapeableImageView;

// Import java.text.NumberFormat để sử dụng các lớp hoặc hàm tương ứng.
import java.text.NumberFormat;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp ProductDetailFragment kế thừa Fragment.
public class ProductDetailFragment extends Fragment {

  // Khai báo thuộc tính với phạm vi truy cập: private static final String ARG_PRODUCT_ID = "product_id".
  private static final String ARG_PRODUCT_ID = "product_id";

  // Định nghĩa phương thức newInstance với phạm vi truy cập tương ứng.
  public static ProductDetailFragment newInstance(int productId) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ProductDetailFragment f = new ProductDetailFragment();.
    ProductDetailFragment f = new ProductDetailFragment();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Bundle b = new Bundle();.
    Bundle b = new Bundle();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: b.putInt(ARG_PRODUCT_ID, productId);.
    b.putInt(ARG_PRODUCT_ID, productId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: f.setArguments(b);.
    f.setArguments(b);
    // Trả về kết quả f;.
    return f;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private ProductDetailVM vm.
  private ProductDetailVM vm;
  // Khai báo thuộc tính với phạm vi truy cập: private CartVM cartVM.
  private CartVM cartVM;

  // Khai báo thuộc tính với phạm vi truy cập: private ShapeableImageView img.
  private ShapeableImageView img;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvBrand.
  private TextView tvBrand;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvName.
  private TextView tvName;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvPrice.
  private TextView tvPrice;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvDesc.
  private TextView tvDesc;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvMetaRating.
  private TextView tvMetaRating;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvMetaDelivery.
  private TextView tvMetaDelivery;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvMetaTime.
  private TextView tvMetaTime;
  // Khai báo thuộc tính với phạm vi truy cập: private MaterialButton btnAdd.
  private MaterialButton btnAdd;
  // Khai báo thuộc tính với phạm vi truy cập: private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN")).
  private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
  // Khai báo thuộc tính với phạm vi truy cập: private final NumberFormat ratingFormat = NumberFormat.getInstance(Locale.getDefault()).
  private final NumberFormat ratingFormat = NumberFormat.getInstance(Locale.getDefault());

  // Áp dụng annotation @Nullable và @Override cho phần tử bên dưới.
  @Nullable @Override
  // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Trả về kết quả inflater.inflate(R.layout.fragment_product_detail, container, false);.
    return inflater.inflate(R.layout.fragment_product_detail, container, false);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(v, savedInstanceState);.
    super.onViewCreated(v, savedInstanceState);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: img = v.findViewById(R.id.imgCover);.
    img = v.findViewById(R.id.imgCover);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvBrand = v.findViewById(R.id.tvBrand);.
    tvBrand = v.findViewById(R.id.tvBrand);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvName = v.findViewById(R.id.tvName);.
    tvName = v.findViewById(R.id.tvName);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvPrice = v.findViewById(R.id.tvPrice);.
    tvPrice = v.findViewById(R.id.tvPrice);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvDesc = v.findViewById(R.id.tvDesc);.
    tvDesc = v.findViewById(R.id.tvDesc);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMetaRating = v.findViewById(R.id.tvMetaRating);.
    tvMetaRating = v.findViewById(R.id.tvMetaRating);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMetaDelivery = v.findViewById(R.id.tvMetaDelivery);.
    tvMetaDelivery = v.findViewById(R.id.tvMetaDelivery);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMetaTime = v.findViewById(R.id.tvMetaTime);.
    tvMetaTime = v.findViewById(R.id.tvMetaTime);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnAdd = v.findViewById(R.id.btnAddToCart);.
    btnAdd = v.findViewById(R.id.btnAddToCart);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: ImageButton btnBack = v.findViewById(R.id.btnBack);.
    ImageButton btnBack = v.findViewById(R.id.btnBack);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ImageButton btnFavorite = v.findViewById(R.id.btnFavorite);.
    ImageButton btnFavorite = v.findViewById(R.id.btnFavorite);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: priceFormat.setMaximumFractionDigits(0);.
    priceFormat.setMaximumFractionDigits(0);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ratingFormat.setMaximumFractionDigits(1);.
    ratingFormat.setMaximumFractionDigits(1);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ratingFormat.setMinimumFractionDigits(1);.
    ratingFormat.setMinimumFractionDigits(1);

    // Thực thi câu lệnh: btnBack.setOnClickListener(view ->.
    btnBack.setOnClickListener(view ->
        // Thực hiện lời gọi phương thức hoặc khởi tạo: requireActivity().getOnBackPressedDispatcher().onBackPressed());.
        requireActivity().getOnBackPressedDispatcher().onBackPressed());

    // Thực thi câu lệnh: btnFavorite.setOnClickListener(view ->.
    btnFavorite.setOnClickListener(view ->
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Favorites coming soon", Toast.LENGTH_SHORT).show());.
        Toast.makeText(getContext(), "Favorites coming soon", Toast.LENGTH_SHORT).show());

    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(this).get(ProductDetailVM.class);.
    vm = new ViewModelProvider(this).get(ProductDetailVM.class);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);.
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);

    // Gán giá trị cho biến hoặc thuộc tính: int productId = getArguments() != null ? getArguments().getInt(ARG_PRODUCT_ID, -1) : -1.
    int productId = getArguments() != null ? getArguments().getInt(ARG_PRODUCT_ID, -1) : -1;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (productId <= 0) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Product not found", Toast.LENGTH_SHORT).show();.
      Toast.makeText(getContext(), "Product not found", Toast.LENGTH_SHORT).show();
      // Thực hiện lời gọi phương thức hoặc khởi tạo: requireActivity().getOnBackPressedDispatcher().onBackPressed();.
      requireActivity().getOnBackPressedDispatcher().onBackPressed();
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.productLive(productId).observe(getViewLifecycleOwner(), this::bindProduct);.
    vm.productLive(productId).observe(getViewLifecycleOwner(), this::bindProduct);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức bindProduct với phạm vi truy cập tương ứng.
  private void bindProduct(ProductEntity p) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (p == null) return;

    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvBrand.setText(resolveCategoryName(p.categoryId));.
    tvBrand.setText(resolveCategoryName(p.categoryId));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvName.setText(p.name);.
    tvName.setText(p.name);
    // Thực thi câu lệnh: tvDesc.setText(p.description == null.
    tvDesc.setText(p.description == null
        // Thực thi câu lệnh: ? "This drink is trending with our customers.".
        ? "This drink is trending with our customers."
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : p.description);.
        : p.description);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvPrice.setText(formatPrice(p.price));.
    tvPrice.setText(formatPrice(p.price));

    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMetaRating.setText(formatRating(p.rating));.
    tvMetaRating.setText(formatRating(p.rating));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMetaDelivery.setText("Free delivery");.
    tvMetaDelivery.setText("Free delivery");
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMetaTime.setText("15 minutes");.
    tvMetaTime.setText("15 minutes");

    // Thực hiện lời gọi phương thức hoặc khởi tạo: Object imageSource = (p.imageUrl == null || p.imageUrl.trim().isEmpty()).
    Object imageSource = (p.imageUrl == null || p.imageUrl.trim().isEmpty())
        // Thực thi câu lệnh: ? R.drawable.bg_app_gradient.
        ? R.drawable.bg_app_gradient
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : p.imageUrl.trim();.
        : p.imageUrl.trim();

    // Thực hiện lời gọi phương thức hoặc khởi tạo: Glide.with(this).
    Glide.with(this)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .load(imageSource).
        .load(imageSource)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .apply(new RequestOptions().transform(new CenterCrop())).
        .apply(new RequestOptions().transform(new CenterCrop()))
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .into(img);.
        .into(img);

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    btnAdd.setOnClickListener(view -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: cartVM.add(p);.
      cartVM.add(p);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(getContext(), "Added to your cart", Toast.LENGTH_SHORT).show();.
      Toast.makeText(getContext(), "Added to your cart", Toast.LENGTH_SHORT).show();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức resolveCategoryName với phạm vi truy cập tương ứng.
  private String resolveCategoryName(int categoryId) {
    // Trả về kết quả switch (categoryId) {.
    return switch (categoryId) {
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case 1 -> "Milk tea";
      // Định nghĩa một nhánh case trong cấu trúc switch.
      case 2 -> "Coffee";
      // Thực thi câu lệnh: default -> "Beverage";.
      default -> "Beverage";
    // Thực thi câu lệnh: };.
    };
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức formatPrice với phạm vi truy cập tương ứng.
  private String formatPrice(double price) {
    // Trả về kết quả priceFormat.format(Math.round(price)) + " VND";.
    return priceFormat.format(Math.round(price)) + " VND";
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức formatRating với phạm vi truy cập tương ứng.
  private String formatRating(Double rating) {
    // Gán giá trị cho biến hoặc thuộc tính: double value = (rating == null || rating <= 0) ? 4.5 : rating.
    double value = (rating == null || rating <= 0) ? 4.5 : rating;
    // Trả về kết quả ratingFormat != null.
    return ratingFormat != null
        // Thực hiện lời gọi phương thức hoặc khởi tạo: ? ratingFormat.format(value).
        ? ratingFormat.format(value)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: : String.format(Locale.getDefault(), "%.1f", value);.
        : String.format(Locale.getDefault(), "%.1f", value);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
