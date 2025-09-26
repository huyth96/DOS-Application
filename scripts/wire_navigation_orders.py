#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
wire_navigation_orders.py
Wire click on order list -> open OrderDetailFragment(order_id).

Run from Android project root. Safe to re-run.
What this does:
1) Create a tiny navigator helper:
   - app/src/main/java/com/drinkorder/ui/order/OrderNavigator.java
     OrderNavigator.open(FragmentActivity, int orderId)
     -> replaces R.id.container with OrderDetailFragment and addToBackStack.
2) Provide a ready OrdersAdapter with click support (if you don't have one):
   - app/src/main/java/com/drinkorder/ui/order/OrdersAdapter.java
3) Provide a minimal OrdersFragment if you don't have one yet (list of orders from Room):
   - app/src/main/java/com/drinkorder/ui/order/OrdersFragment.java
   This fragment observes LiveData<List<OrderEntity>> and sets click to navigate.
4) Provide a simple row layout for orders:
   - app/src/main/res/layout/item_order.xml

If you already have OrdersFragment/adapter, this script WILL NOT overwrite them.
It prints short integration hints if it detects existing files.
"""
import re, sys
from pathlib import Path

PKG = "com.drinkorder"
JAVA_BASE = Path("app/src/main/java") / PKG.replace('.', '/')
UI_DIR = JAVA_BASE / "ui/order"
RES_LAYOUT = Path("app/src/main/res/layout")
DAO_DIR = JAVA_BASE / "data/db/dao"

def write_once(path: Path, content: str):
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists():
        print(f"= SKIP (exists): {path}")
        return False
    path.write_text(content, encoding="utf-8")
    print(f"+ Created: {path}")
    return True

def create_files():
    created_any = False

    navigator = """package com.drinkorder.ui.order;

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
"""
    created_any |= write_once(UI_DIR / "OrderNavigator.java", navigator)

    orders_adapter = """package com.drinkorder.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.OrderEntity;

public class OrdersAdapter extends ListAdapter<OrderEntity, OrdersAdapter.VH> {

    public interface OnOrderClick { void onClick(OrderEntity order); }
    private final OnOrderClick onClick;

    public OrdersAdapter(OnOrderClick onClick) {
        super(DIFF);
        this.onClick = onClick;
    }

    static final DiffUtil.ItemCallback<OrderEntity> DIFF = new DiffUtil.ItemCallback<OrderEntity>() {
        @Override public boolean areItemsTheSame(@NonNull OrderEntity a, @NonNull OrderEntity b) {
            return a.orderId == b.orderId;
        }
        @Override public boolean areContentsTheSame(@NonNull OrderEntity a, @NonNull OrderEntity b) {
            return a.status.equals(b.status) && Float.compare(a.totalAmount, b.totalAmount) == 0 && a.createdAt.equals(b.createdAt);
        }
    };

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        OrderEntity o = getItem(position);
        h.tvId.setText("#" + o.orderId);
        h.tvStatus.setText(o.status != null ? o.status : "");
        h.tvTotal.setText(String.format("%.0f", o.totalAmount));
        h.itemView.setOnClickListener(v -> {
            if (onClick != null) onClick.onClick(o);
        });
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvId, tvStatus, tvTotal;
        VH(@NonNull View v) {
            super(v);
            tvId = v.findViewById(R.id.tvId);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvTotal = v.findViewById(R.id.tvTotal);
        }
    }
}
"""
    created_any |= write_once(UI_DIR / "OrdersAdapter.java", orders_adapter)

    item_order_xml = """<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="12dp">

    <TextView
        android:id="@+id/tvId"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="#1001"
        android:textStyle="bold"/>

    <TextView
        android:id="@+id/tvStatus"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="12dp"
        android:text="Paid"/>

    <TextView
        android:id="@+id/tvTotal"
        android:layout_width="72dp"
        android:layout_height="wrap_content"
        android:gravity="end"
        android:text="120000"/>
</LinearLayout>
"""
    created_any |= write_once(RES_LAYOUT / "item_order.xml", item_order_xml)

    orders_fragment = """package com.drinkorder.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.dao.OrderDao;
import com.drinkorder.data.db.entity.OrderEntity;

public class OrdersFragment extends Fragment {

    private OrdersAdapter adapter;
    private OrderListVM vm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        RecyclerView rv = v.findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new OrdersAdapter(order -> {
            if (getActivity() != null) OrderNavigator.open(getActivity(), order.orderId);
        });
        rv.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(OrderListVM.class);
        vm.orders.observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
    }

    public static class OrderListVM extends androidx.lifecycle.AndroidViewModel {
        public final androidx.lifecycle.LiveData<java.util.List<OrderEntity>> orders;
        public OrderListVM(@NonNull android.app.Application app) {
            super(app);
            OrderDao dao = AppDatabase.getInstance(app).orderDao();
            orders = dao.getAllOrders(); // assumes you have this; if not, add @Query in OrderDao
        }
    }
}
"""
    # Only write OrdersFragment if missing
    if not (UI_DIR / "OrdersFragment.java").exists():
        created_any |= write_once(UI_DIR / "OrdersFragment.java", orders_fragment)
        # also provide a super simple recycler layout container if missing
        simple_recycler = """<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</FrameLayout>
"""
        write_once(RES_LAYOUT / "simple_recycler.xml", simple_recycler)
    else:
        print("= Detected existing OrdersFragment.java")
        print("  -> Integrate click like:")
        print("     adapter = new OrdersAdapter(order -> OrderNavigator.open(getActivity(), order.orderId));")
        print("     rv.setAdapter(adapter);")

    return created_any

def main():
    root_ok = Path("settings.gradle").exists() or Path("settings.gradle.kts").exists()
    if not root_ok:
        print("! Warning: Not sure you're at project root (no settings.gradle). Proceeding anyway...")
    created = create_files()
    print("\nDone. Now ensure you navigate to OrdersFragment somewhere with a container id R.id.container.")
    print("If OrderDao lacks getAllOrders(), add to your OrderDao.java:")
    print("""
    @androidx.room.Query("SELECT * FROM orders ORDER BY createdAt DESC")
    androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.entity.OrderEntity>> getAllOrders();
    """)

if __name__ == "__main__":
    main()
