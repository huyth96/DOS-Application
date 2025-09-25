package com.drinkorder.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.ui.detail.ProductDetailFragment;
import com.drinkorder.vm.CartVM;
import com.drinkorder.vm.HomeVM;

public class HomeFragment extends Fragment {

  private HomeVM vm;
  private CartVM cartVM;
  private ProductsAdapter adapter;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b){
    View v = inf.inflate(R.layout.fragment_home, c, false);
    RecyclerView rv = v.findViewById(R.id.rvProducts); // id đúng với layout của bạn
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new ProductsAdapter(
            p -> { // Add to cart
              cartVM.add(p);
              Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            },
            this::openDetail // click item -> mở chi tiết
    );
    rv.setAdapter(adapter);
    return v;
  }

  @Override public void onViewCreated(@NonNull View v, @Nullable Bundle b){
    super.onViewCreated(v,b);
    vm = new ViewModelProvider(this).get(HomeVM.class);
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);
    vm.products.observe(getViewLifecycleOwner(), list -> adapter.submit(list));
  }

  private void openDetail(ProductEntity item){
    Fragment f = ProductDetailFragment.newInstance(item.productId);
    // activity_main.xml có FrameLayout id @id/container (đã xem file của bạn) → dùng id này
    requireActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, f) // <-- id container từ activity_main.xml
            .addToBackStack("product_detail")
            .commit();
  }
}
