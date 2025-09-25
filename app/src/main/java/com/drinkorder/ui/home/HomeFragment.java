package com.drinkorder.ui.home;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.ProductEntity;
import com.drinkorder.vm.HomeVM;
import com.drinkorder.vm.CartVM;

public class HomeFragment extends Fragment {
  private HomeVM vm; private CartVM cartVM; private ProductsAdapter adapter;
  @Nullable @Override public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b){
    View v = inf.inflate(R.layout.fragment_home, c, false);
    RecyclerView rv = v.findViewById(R.id.rvProducts);
    rv.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new ProductsAdapter(p -> { cartVM.add(p); Toast.makeText(getContext(),"Added to cart",Toast.LENGTH_SHORT).show(); });
    rv.setAdapter(adapter);
    return v;
  }
  @Override public void onViewCreated(@NonNull View v, @Nullable Bundle b){
    super.onViewCreated(v,b);
    vm = new ViewModelProvider(this).get(HomeVM.class);
    cartVM = new ViewModelProvider(requireActivity()).get(CartVM.class);
    vm.products.observe(getViewLifecycleOwner(), list -> adapter.submit(list));
  }
}
