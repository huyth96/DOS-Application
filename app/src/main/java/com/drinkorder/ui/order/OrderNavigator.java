package com.drinkorder.ui.order;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class OrderNavigator {
    /**
     * Mở màn hình chi tiết đơn hàng.
     * @param activity Activity hiện tại chứa container để show fragment
     * @param orderId id của đơn hàng cần hiển thị
     */
    public static void open(FragmentActivity activity, int orderId) {
        // 1️. Tạo instance fragment chi tiết đơn hàng
        OrderDetailFragment f = new OrderDetailFragment();
        // 2️. Tạo Bundle để truyền orderId vào fragment, Gửi dữ liệu cho fragment
        android.os.Bundle b = new android.os.Bundle();
        b.putInt(OrderDetailFragment.ARG_ORDER_ID, orderId);
        f.setArguments(b);
        // 3️. Lấy FragmentManager từ Activity
        FragmentManager fm = activity.getSupportFragmentManager();
        // 4️. Bắt đầu một giao dịch fragment
        FragmentTransaction tx = fm.beginTransaction();
        // 5️. Thay thế container bằng fragment mới
        tx.replace(com.drinkorder.R.id.container, f);
        // 6️. Thêm vào back stack để khi nhấn back có thể quay lại fragment trước
        tx.addToBackStack("order_detail");
        // 7. Commit giao dịch để thực hiện hiển thị fragment
        tx.commit();
    }
}
