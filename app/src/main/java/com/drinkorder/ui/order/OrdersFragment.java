// Khai báo package com.drinkorder.ui.order cho toàn bộ lớp.
package com.drinkorder.ui.order;

// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.fragment.app.Fragment để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.Fragment;
// Import androidx.lifecycle.ViewModelProvider để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.ViewModelProvider;
// Import androidx.recyclerview.widget.LinearLayoutManager để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.dao.OrderDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.OrderDao;
// Import com.drinkorder.data.db.entity.OrderEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderEntity;

// Định nghĩa lớp OrdersFragment kế thừa Fragment.
public class OrdersFragment extends Fragment {

    // Khai báo thuộc tính với phạm vi truy cập: private OrdersAdapter adapter.
    private OrdersAdapter adapter;
    // Khai báo thuộc tính với phạm vi truy cập: private OrderListVM vm.
    private OrderListVM vm;

    // Áp dụng annotation @Nullable và @Override cho phần tử bên dưới.
    @Nullable @Override
    // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Trả về kết quả inflater.inflate(R.layout.simple_recycler, container, false);.
        return inflater.inflate(R.layout.simple_recycler, container, false);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(v, savedInstanceState);.
        super.onViewCreated(v, savedInstanceState);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: RecyclerView rv = v.findViewById(R.id.recycler);.
        RecyclerView rv = v.findViewById(R.id.recycler);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: rv.setLayoutManager(new LinearLayoutManager(requireContext()));.
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        adapter = new OrdersAdapter(order -> {
            // Kiểm tra điều kiện if để quyết định luồng xử lý.
            if (getActivity() != null) OrderNavigator.open(getActivity(), order.orderId);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
        });
        // Thực hiện lời gọi phương thức hoặc khởi tạo: rv.setAdapter(adapter);.
        rv.setAdapter(adapter);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(this).get(OrderListVM.class);.
        vm = new ViewModelProvider(this).get(OrderListVM.class);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.orders.observe(getViewLifecycleOwner(), list -> adapter.submitList(list));.
        vm.orders.observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa lớp OrderListVM kế thừa androidx.lifecycle.AndroidViewModel.
    public static class OrderListVM extends androidx.lifecycle.AndroidViewModel {
        // Khai báo thuộc tính với phạm vi truy cập: public final androidx.lifecycle.LiveData<java.util.List<OrderEntity>> orders.
        public final androidx.lifecycle.LiveData<java.util.List<OrderEntity>> orders;
        // Định nghĩa phương thức OrderListVM với phạm vi truy cập tương ứng.
        public OrderListVM(@NonNull android.app.Application app) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: super(app);.
            super(app);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: OrderDao dao = AppDatabase.get(app).orderDao();.
            OrderDao dao = AppDatabase.get(app).orderDao();
            // Thực thi câu lệnh: orders = dao.getAllOrders(); // assumes you have this; if not, add @Query in OrderDao.
            orders = dao.getAllOrders(); // assumes you have this; if not, add @Query in OrderDao
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
