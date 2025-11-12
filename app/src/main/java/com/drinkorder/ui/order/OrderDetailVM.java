package com.drinkorder.ui.order;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.dao.OrderDao;
import com.drinkorder.data.db.pojo.OrderWithItems;

public class OrderDetailVM extends AndroidViewModel {
    // DAO để truy vấn dữ liệu đơn hàng từ Room
    private final OrderDao orderDao;
    // LiveData lưu trữ id đơn hàng hiện tại; fragment sẽ set id này
    private final MutableLiveData<Integer> orderId = new MutableLiveData<>();
    // LiveData chứa toàn bộ dữ liệu Order + OrderItems, quan sát từ fragment
    public final LiveData<OrderWithItems> order;

    public OrderDetailVM(@NonNull Application app) {
        super(app);
        AppDatabase db = AppDatabase.get(app);
        orderDao = db.orderDao();

        // Khi orderId thay đổi, tự động gọi getOrderWithItems(id)
        order = Transformations.switchMap(orderId, id -> {
            if (id == null) return new MediatorLiveData<>();// nếu id null trả LiveData rỗng
            return orderDao.getOrderWithItems(id); // LiveData chứa OrderWithItems
        });
    }
    // Cập nhật id đơn hàng cần hiển thị
    public void setOrderId(int id) {
        Integer cur = orderId.getValue();
        // Chỉ set khi id mới khác id hiện tại, tránh query trùng
        if (cur == null || cur != id) orderId.setValue(id);
    }
}
