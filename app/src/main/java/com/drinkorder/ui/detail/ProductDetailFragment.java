package com.drinkorder.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.vm.CartVM;
import com.drinkorder.vm.ProductDetailVM;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailFragment extends Fragment { // Định nghĩa lớp ProductDetailFragment kế thừa từ Fragment, dùng để hiển thị chi tiết sản phẩm.

  private static final String ARG_PRODUCT_ID = "product_id"; // Hằng số tĩnh lưu key cho argument ID sản phẩm.

  public static ProductDetailFragment newInstance(int productId) { // Phương thức tĩnh tạo instance mới của fragment, nhận ID sản phẩm.
    ProductDetailFragment f = new ProductDetailFragment(); // Tạo instance fragment mới.
    Bundle b = new Bundle(); // Tạo Bundle để lưu arguments.
    b.putInt(ARG_PRODUCT_ID, productId); // Đặt ID sản phẩm vào Bundle.
    f.setArguments(b); // Gán Bundle vào fragment.
    return f; // Trả về fragment.
  }

  private ProductDetailVM vm; // Biến lưu ViewModel cho chi tiết sản phẩm.
  private CartVM cartVM; // Biến lưu ViewModel cho giỏ hàng.

  private ShapeableImageView img; // Biến lưu ImageView cho hình ảnh sản phẩm.
  private TextView tvBrand; // Biến lưu TextView cho thương hiệu (danh mục).
  private TextView tvName; // Biến lưu TextView cho tên sản phẩm.
  private TextView tvPrice; // Biến lưu TextView cho giá sản phẩm.
  private TextView tvDesc; // Biến lưu TextView cho mô tả sản phẩm.
  private TextView tvMetaRating; // Biến lưu TextView cho đánh giá meta.
  private TextView tvMetaDelivery; // Biến lưu TextView cho giao hàng meta.
  private TextView tvMetaTime; // Biến lưu TextView cho thời gian meta.
  private MaterialButton btnAdd; // Biến lưu nút thêm vào giỏ hàng.
  private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN")); // Biến final lưu NumberFormat cho giá theo locale Việt Nam.
  private final NumberFormat ratingFormat = NumberFormat.getInstance(Locale.getDefault()); // Biến final lưu NumberFormat cho đánh giá theo locale mặc định.

  @Nullable @Override // Ghi đè phương thức onCreateView từ Fragment, có thể trả về null.
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { // Phương thức tạo view cho fragment.
    return inflater.inflate(R.layout.fragment_product_detail, container, false); // Inflate layout từ resource và trả về view.
  }

  @Override // Ghi đè phương thức onViewCreated từ Fragment.
  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) { // Phương thức gọi sau khi view được tạo, nhận view và savedInstanceState.
    super.onViewCreated(v, savedInstanceState); // Gọi onViewCreated của lớp cha.

    img = v.findViewById(R.id.imgCover); // Tìm và lưu ImageView hình ảnh cover.
    tvBrand = v.findViewById(R.id.tvBrand); // Tìm và lưu TextView thương hiệu.
    tvName = v.findViewById(R.id.tvName); // Tìm và lưu TextView tên.
    tvPrice = v.findViewById(R.id.tvPrice); // Tìm và lưu TextView giá.
    tvDesc = v.findViewById(R.id.tvDesc); // Tìm và lưu TextView mô tả.
    tvMetaRating = v.findViewById(R.id.tvMetaRating); // Tìm và lưu TextView đánh giá meta.
    tvMetaDelivery = v.findViewById(R.id.tvMetaDelivery); // Tìm và lưu TextView giao hàng meta.
    tvMetaTime = v.findViewById(R.id.tvMetaTime); // Tìm và lưu TextView thời gian meta.
    btnAdd = v.findViewById(R.id.btnAddToCart); // Tìm và lưu nút thêm vào giỏ.

    ImageButton btnBack = v.findViewById(R.id.btnBack); // Tìm và lưu nút back.
    ImageButton btnFavorite = v.findViewById(R.id.btnFavorite); // Tìm và lưu nút favorite.

    priceFormat.setMaximumFractionDigits(0); // Thiết lập NumberFormat giá không hiển thị thập phân.
    ratingFormat.setMaximumFractionDigits(1); // Thiết lập NumberFormat đánh giá tối đa 1 thập phân.
    ratingFormat.setMinimumFractionDigits(1); // Thiết lập NumberFormat đánh giá tối thiểu 1 thập phân.

    btnBack.setOnClickListener(view -> // Thiết lập listener cho nút back.
            requireActivity().getOnBackPressedDispatcher().onBackPressed()); // Gọi back pressed của activity.

    btnFavorite.setOnClickListener(view -> // Thiết lập listener cho nút favorite.
            Toast.makeText(getContext(), "Favorites coming soon", Toast.LENGTH_SHORT).show()); // Hiển thị toast thông báo tính năng sắp có.

    vm = new ViewModelProvider(this).get(ProductDetailVM.class); // Khởi tạo ViewModel cho chi tiết sản phẩm.
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class); // Khởi tạo ViewModel cho giỏ hàng từ activity.

    int productId = getArguments() != null ? getArguments().getInt(ARG_PRODUCT_ID, -1) : -1; // Lấy ID sản phẩm từ arguments, mặc định -1 nếu không có.
    if (productId <= 0) { // Kiểm tra ID không hợp lệ.
      Toast.makeText(getContext(), "Product not found", Toast.LENGTH_SHORT).show(); // Hiển thị toast sản phẩm không tìm thấy.
      requireActivity().getOnBackPressedDispatcher().onBackPressed(); // Gọi back pressed để thoát.
      return; // Thoát phương thức.
    }

    vm.productLive(productId).observe(getViewLifecycleOwner(), this::bindProduct); // Quan sát LiveData sản phẩm và gọi bindProduct khi thay đổi.
  }

  private void bindProduct(ProductEntity p) { // Phương thức bind dữ liệu sản phẩm vào view.
    if (p == null) return; // Thoát nếu sản phẩm null.

    tvBrand.setText(resolveCategoryName(p.categoryId)); // Đặt tên danh mục (thương hiệu).
    tvName.setText(p.name); // Đặt tên sản phẩm.
    tvDesc.setText(p.description == null // Đặt mô tả, dùng mặc định nếu null.
            ? "This drink is trending with our customers."
            : p.description);
    tvPrice.setText(formatPrice(p.price)); // Đặt giá đã định dạng.

    tvMetaRating.setText(formatRating(p.rating)); // Đặt đánh giá meta.
    tvMetaDelivery.setText("Free delivery"); // Đặt giao hàng miễn phí.
    tvMetaTime.setText("15 minutes"); // Đặt thời gian 15 phút.

    Object imageSource = (p.imageUrl == null || p.imageUrl.trim().isEmpty()) // Xác định nguồn hình ảnh.
            ? R.drawable.bg_app_gradient // Nếu URL rỗng, dùng drawable mặc định.
            : p.imageUrl.trim(); // Ngược lại dùng URL đã trim.

    Glide.with(this) // Sử dụng Glide để load hình ảnh.
            .load(imageSource) // Load từ nguồn.
            .apply(new RequestOptions().transform(new CenterCrop())) // Áp dụng crop center.
            .into(img); // Load vào ImageView.

    btnAdd.setOnClickListener(view -> { // Thiết lập listener cho nút thêm.
      cartVM.add(p); // Thêm sản phẩm vào giỏ hàng qua ViewModel.
      Toast.makeText(getContext(), "Added to your cart", Toast.LENGTH_SHORT).show(); // Hiển thị toast thêm thành công.
    });
  }

  private String resolveCategoryName(int categoryId) { // Phương thức resolve tên danh mục dựa trên ID.
    return switch (categoryId) { // Sử dụng switch expression.
      case 1 -> "Milk tea"; // ID 1: Milk tea.
      case 2 -> "Coffee"; // ID 2: Coffee.
      default -> "Beverage"; // Mặc định: Beverage.
    };
  }

  private String formatPrice(double price) { // Phương thức định dạng giá.
    return priceFormat.format(Math.round(price)) + " VND"; // Định dạng giá nguyên và thêm " VND".
  }

  private String formatRating(Double rating) { // Phương thức định dạng đánh giá.
    double value = (rating == null || rating <= 0) ? 4.5 : rating; // Nếu rating null hoặc <=0, dùng 4.5.
    return ratingFormat != null // Kiểm tra ratingFormat không null.
            ? ratingFormat.format(value) // Dùng format nếu có.
            : String.format(Locale.getDefault(), "%.1f", value); // Ngược lại dùng String.format.
  }
}