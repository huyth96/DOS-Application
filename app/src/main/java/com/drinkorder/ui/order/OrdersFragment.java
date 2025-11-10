// Đặt package để fragment nằm trong nhóm chức năng đặt hàng.
package com.drinkorder.ui.order;

// Import Bundle phục vụ vòng đời fragment.
import android.os.Bundle;
// Import LayoutInflater để tạo view từ XML.
import android.view.LayoutInflater;
// Import View để thao tác với giao diện.
import android.view.View;
// Import ViewGroup làm container cho fragment.
import android.view.ViewGroup;

// Import NonNull để chú thích tham số bắt buộc có giá trị.
import androidx.annotation.NonNull;
// Import Nullable để chú thích tham số có thể rỗng.
import androidx.annotation.Nullable;
// Import Fragment làm lớp cơ sở cho màn danh sách đơn hàng.
import androidx.fragment.app.Fragment;
// Import ViewModelProvider để lấy ViewModel lưu trữ dữ liệu.
import androidx.lifecycle.ViewModelProvider;
// Import LinearLayoutManager để sắp xếp danh sách đơn hàng theo chiều dọc.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import RecyclerView để hiển thị danh sách đơn hàng.
import androidx.recyclerview.widget.RecyclerView;

// Import R để truy cập layout hiển thị RecyclerView.
import com.drinkorder.R;
// Import AppDatabase để lấy DAO truy vấn đơn hàng.
import com.drinkorder.data.db.AppDatabase;
// Import OrderDao để thao tác bảng đơn hàng trong Room.
import com.drinkorder.data.db.dao.OrderDao;
// Import OrderEntity để biểu diễn dữ liệu từng đơn.
import com.drinkorder.data.db.entity.OrderEntity;

// Fragment hiển thị danh sách đơn hàng cho quản trị viên.
public class OrdersFragment extends Fragment {

    // Adapter quản lý hiển thị từng dòng đơn hàng.
    private OrdersAdapter adapter;
    // ViewModel cung cấp dữ liệu đơn hàng từ Room.
    private OrderListVM vm;

    // Inflate layout chứa RecyclerView khi tạo fragment.
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng layout chung có RecyclerView để tái sử dụng giao diện.
        return inflater.inflate(R.layout.simple_recycler, container, false);
    }

    // Thiết lập RecyclerView và quan sát dữ liệu sau khi view sẵn sàng.
    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        // Gọi super để duy trì vòng đời chuẩn của fragment.
        super.onViewCreated(v, savedInstanceState);
        // Tìm RecyclerView trong layout để chuẩn bị hiển thị dữ liệu.
        RecyclerView rv = v.findViewById(R.id.recycler);
        // Dùng LinearLayoutManager dọc để danh sách dễ đọc.
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Khởi tạo adapter với callback mở chi tiết đơn hàng.
        adapter = new OrdersAdapter(order -> {
            // Chỉ mở chi tiết khi activity cha tồn tại.
            if (getActivity() != null) OrderNavigator.open(getActivity(), order.orderId);
        });
        // Gắn adapter vào RecyclerView để hiển thị dữ liệu.
        rv.setAdapter(adapter);

        // Lấy ViewModel để quan sát dữ liệu đơn hàng từ Room.
        vm = new ViewModelProvider(this).get(OrderListVM.class);
        // Đăng ký observer để cập nhật danh sách mỗi khi dữ liệu thay đổi.
        vm.orders.observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
    }

    // ViewModel lấy danh sách đơn hàng thông qua Room database.
    public static class OrderListVM extends androidx.lifecycle.AndroidViewModel {
        // LiveData chứa danh sách đơn hàng theo thời gian thực.
        public final androidx.lifecycle.LiveData<java.util.List<OrderEntity>> orders;
        // Khởi tạo ViewModel với Application để truy cập database.
        public OrderListVM(@NonNull android.app.Application app) {
            // Gọi super để lưu lại context ứng dụng.
            super(app);
            // Lấy DAO đơn hàng từ cơ sở dữ liệu Room.
            OrderDao dao = AppDatabase.get(app).orderDao();
            // Lấy toàn bộ đơn hàng dưới dạng LiveData để tự động cập nhật UI.
            orders = dao.getAllOrders(); // assumes you have this; if not, add @Query in OrderDao
        }
    }
}
