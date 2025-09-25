package com.drinkorder.ui.orders;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.vm.OrdersVM;
import java.util.ArrayList;

public class OrdersFragment extends Fragment {
  private OrdersVM vm; private ArrayAdapter<String> adapter; private ArrayList<String> items = new ArrayList<>();
  @Nullable @Override public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b){
    View v = inf.inflate(R.layout.fragment_orders, c, false);
    ListView lv = v.findViewById(R.id.lvOrders);
    adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
    lv.setAdapter(adapter);
    return v;
  }
  @Override public void onViewCreated(@NonNull View v, @Nullable Bundle b){
    super.onViewCreated(v,b);
    vm = new ViewModelProvider(requireActivity()).get(OrdersVM.class);
    vm.load(1);
    vm.orders.observe(getViewLifecycleOwner(), list -> {
      items.clear();
      if (list!=null) for (OrderEntity o: list) items.add("#"+o.orderId+" • "+o.totalAmount+" • "+o.orderStatus);
      adapter.notifyDataSetChanged();
    });
  }
}
