// Khai báo package com.drinkorder.ui.order cho toàn bộ lớp.
package com.drinkorder.ui.order;

// Import android.app.Application để sử dụng các lớp hoặc hàm tương ứng.
import android.app.Application;
// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.lifecycle.AndroidViewModel để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.AndroidViewModel;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.lifecycle.MediatorLiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.MediatorLiveData;
// Import androidx.lifecycle.MutableLiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.MutableLiveData;
// Import androidx.lifecycle.Transformations để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.Transformations;

// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.dao.OrderDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.OrderDao;
// Import com.drinkorder.data.db.pojo.OrderWithItems để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.OrderWithItems;

// Định nghĩa lớp OrderDetailVM kế thừa AndroidViewModel.
public class OrderDetailVM extends AndroidViewModel {
    // Khai báo thuộc tính với phạm vi truy cập: private final OrderDao orderDao.
    private final OrderDao orderDao;
    // Khai báo thuộc tính với phạm vi truy cập: private final MutableLiveData<Integer> orderId = new MutableLiveData<>().
    private final MutableLiveData<Integer> orderId = new MutableLiveData<>();
    // Khai báo thuộc tính với phạm vi truy cập: public final LiveData<OrderWithItems> order.
    public final LiveData<OrderWithItems> order;

    // Định nghĩa phương thức OrderDetailVM với phạm vi truy cập tương ứng.
    public OrderDetailVM(@NonNull Application app) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: super(app);.
        super(app);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: AppDatabase db = AppDatabase.get(app);.
        AppDatabase db = AppDatabase.get(app);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: orderDao = db.orderDao();.
        orderDao = db.orderDao();

        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        order = Transformations.switchMap(orderId, id -> {
            // Kiểm tra điều kiện if để quyết định luồng xử lý.
            if (id == null) return new MediatorLiveData<>();
            // Trả về kết quả orderDao.getOrderWithItems(id);.
            return orderDao.getOrderWithItems(id);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
        });
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức setOrderId với phạm vi truy cập tương ứng.
    public void setOrderId(int id) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Integer cur = orderId.getValue();.
        Integer cur = orderId.getValue();
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (cur == null || cur != id) orderId.setValue(id);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
