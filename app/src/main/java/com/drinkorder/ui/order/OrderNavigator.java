// Khai báo package com.drinkorder.ui.order cho toàn bộ lớp.
package com.drinkorder.ui.order;

// Import androidx.fragment.app.FragmentActivity để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.FragmentActivity;
// Import androidx.fragment.app.FragmentManager để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.FragmentManager;
// Import androidx.fragment.app.FragmentTransaction để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.FragmentTransaction;

// Định nghĩa lớp OrderNavigator.
public class OrderNavigator {
    // Định nghĩa phương thức open với phạm vi truy cập tương ứng.
    public static void open(FragmentActivity activity, int orderId) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: OrderDetailFragment f = new OrderDetailFragment();.
        OrderDetailFragment f = new OrderDetailFragment();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: android.os.Bundle b = new android.os.Bundle();.
        android.os.Bundle b = new android.os.Bundle();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: b.putInt(OrderDetailFragment.ARG_ORDER_ID, orderId);.
        b.putInt(OrderDetailFragment.ARG_ORDER_ID, orderId);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: f.setArguments(b);.
        f.setArguments(b);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: FragmentManager fm = activity.getSupportFragmentManager();.
        FragmentManager fm = activity.getSupportFragmentManager();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: FragmentTransaction tx = fm.beginTransaction();.
        FragmentTransaction tx = fm.beginTransaction();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tx.replace(com.drinkorder.R.id.container, f);.
        tx.replace(com.drinkorder.R.id.container, f);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tx.addToBackStack("order_detail");.
        tx.addToBackStack("order_detail");
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tx.commit();.
        tx.commit();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
