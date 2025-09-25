package com.drinkorder.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.vm.CartVM;
import com.drinkorder.vm.ProductDetailVM;

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
  private ImageView img;
  private TextView tvName, tvPrice, tvDesc;
  private Button btnAdd;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_product_detail, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(v, savedInstanceState);
    img = v.findViewById(R.id.imgCover);
    tvName = v.findViewById(R.id.tvName);
    tvPrice = v.findViewById(R.id.tvPrice);
    tvDesc = v.findViewById(R.id.tvDesc);
    btnAdd = v.findViewById(R.id.btnAddToCart);

    vm = new ViewModelProvider(this).get(ProductDetailVM.class);
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);

    int productId = getArguments() != null ? getArguments().getInt(ARG_PRODUCT_ID, -1) : -1;
    if (productId <= 0) {
      Toast.makeText(getContext(), "Product invalid", Toast.LENGTH_SHORT).show();
      requireActivity().onBackPressed();
      return;
    }

    vm.productLive(productId).observe(getViewLifecycleOwner(), this::bindProduct);
  }

  private void bindProduct(ProductEntity p) {
    if (p == null) return;
    tvName.setText(p.name);
    tvPrice.setText(String.valueOf((long)p.price));
    tvDesc.setText(p.description == null ? "" : p.description);
    Glide.with(this)
            .load(p.imageUrl == null || p.imageUrl.isEmpty() ? R.drawable.ic_launcher_foreground : p.imageUrl)
            .into(img);

    btnAdd.setOnClickListener(v -> {
      cartVM.add(p);
      Toast.makeText(getContext(), "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
    });
  }
}
