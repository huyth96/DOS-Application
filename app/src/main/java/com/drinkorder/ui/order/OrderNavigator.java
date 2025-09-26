package com.drinkorder.ui.order;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class OrderNavigator {
    public static void open(FragmentActivity activity, int orderId) {
        OrderDetailFragment f = new OrderDetailFragment();
        android.os.Bundle b = new android.os.Bundle();
        b.putInt(OrderDetailFragment.ARG_ORDER_ID, orderId);
        f.setArguments(b);

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(com.drinkorder.R.id.container, f);
        tx.addToBackStack("order_detail");
        tx.commit();
    }
}
