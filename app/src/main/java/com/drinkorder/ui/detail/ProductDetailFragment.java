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

public class ProductDetailFragment extends Fragment {

  private static final String ARG_PRODUCT_ID = "product_id";

  public static ProductDetailFragment newInstance(int productId) {
    ProductDetailFragment f = new ProductDetailFragment();
    Bundle b = new Bundle();
    b.putInt(ARG_PRODUCT_ID, productId);
    f.setArguments(b);
    return f;
  }

  private ProductDetailVM vm;
  private CartVM cartVM;

  private ShapeableImageView img;
  private TextView tvBrand;
  private TextView tvName;
  private TextView tvPrice;
  private TextView tvDesc;
  private TextView tvMetaRating;
  private TextView tvMetaDelivery;
  private TextView tvMetaTime;
  private MaterialButton btnAdd;
  private final NumberFormat priceFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
  private final NumberFormat ratingFormat = NumberFormat.getInstance(Locale.getDefault());

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_product_detail, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(v, savedInstanceState);

    img = v.findViewById(R.id.imgCover);
    tvBrand = v.findViewById(R.id.tvBrand);
    tvName = v.findViewById(R.id.tvName);
    tvPrice = v.findViewById(R.id.tvPrice);
    tvDesc = v.findViewById(R.id.tvDesc);
    tvMetaRating = v.findViewById(R.id.tvMetaRating);
    tvMetaDelivery = v.findViewById(R.id.tvMetaDelivery);
    tvMetaTime = v.findViewById(R.id.tvMetaTime);
    btnAdd = v.findViewById(R.id.btnAddToCart);

    ImageButton btnBack = v.findViewById(R.id.btnBack);
    ImageButton btnFavorite = v.findViewById(R.id.btnFavorite);

    priceFormat.setMaximumFractionDigits(0);
    ratingFormat.setMaximumFractionDigits(1);
    ratingFormat.setMinimumFractionDigits(1);

    btnBack.setOnClickListener(view ->
        requireActivity().getOnBackPressedDispatcher().onBackPressed());

    btnFavorite.setOnClickListener(view ->
        Toast.makeText(getContext(), "Favorites coming soon", Toast.LENGTH_SHORT).show());

    vm = new ViewModelProvider(this).get(ProductDetailVM.class);
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);

    int productId = getArguments() != null ? getArguments().getInt(ARG_PRODUCT_ID, -1) : -1;
    if (productId <= 0) {
      Toast.makeText(getContext(), "Product not found", Toast.LENGTH_SHORT).show();
      requireActivity().getOnBackPressedDispatcher().onBackPressed();
      return;
    }

    vm.productLive(productId).observe(getViewLifecycleOwner(), this::bindProduct);
  }

  private void bindProduct(ProductEntity p) {
    if (p == null) return;

    tvBrand.setText(resolveCategoryName(p.categoryId));
    tvName.setText(p.name);
    tvDesc.setText(p.description == null
        ? "This drink is trending with our customers."
        : p.description);
    tvPrice.setText(formatPrice(p.price));

    tvMetaRating.setText(formatRating(p.rating));
    tvMetaDelivery.setText("Free delivery");
    tvMetaTime.setText("15 minutes");

    Object imageSource = (p.imageUrl == null || p.imageUrl.trim().isEmpty())
        ? R.drawable.bg_app_gradient
        : p.imageUrl.trim();

    Glide.with(this)
        .load(imageSource)
        .apply(new RequestOptions().transform(new CenterCrop()))
        .into(img);

    btnAdd.setOnClickListener(view -> {
      cartVM.add(p);
      Toast.makeText(getContext(), "Added to your cart", Toast.LENGTH_SHORT).show();
    });
  }

  private String resolveCategoryName(int categoryId) {
    return switch (categoryId) {
      case 1 -> "Milk tea";
      case 2 -> "Coffee";
      default -> "Beverage";
    };
  }

  private String formatPrice(double price) {
    return priceFormat.format(Math.round(price)) + " VND";
  }

  private String formatRating(Double rating) {
    double value = (rating == null || rating <= 0) ? 4.5 : rating;
    return ratingFormat != null
        ? ratingFormat.format(value)
        : String.format(Locale.getDefault(), "%.1f", value);
  }
}
