#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
ui_order_detail_files.py
Generate Order Detail UI layer (Fragment + ViewModel + Adapter + XML layouts).

Run from Android project root (same folder as settings.gradle).
Safe to re-run: existing files are skipped; no destructive edits.

Creates:
- app/src/main/java/com/drinkorder/ui/order/OrderDetailFragment.java
- app/src/main/java/com/drinkorder/ui/order/OrderDetailVM.java
- app/src/main/java/com/drinkorder/ui/order/OrderLineAdapter.java
- app/src/main/res/layout/fragment_order_detail.xml
- app/src/main/res/layout/item_order_line.xml

Assumptions:
- You already have Room with:
  - OrderDao#getOrderWithItems(int) returning LiveData<OrderWithItems> (from step 1).
  - AppDatabase singleton exposes orderDao().
- Entities/POJOs:
  - OrderWithItems { OrderEntity order; List<OrderItemWithProduct> items; }
  - OrderItemWithProduct { OrderItemEntity item; ProductEntity product; }

Fragment arg:
- Pass orderId via arguments key "order_id" (int).
"""
import os
from pathlib import Path

PKG = "com.drinkorder"
JAVA_BASE = Path("app/src/main/java") / PKG.replace('.', '/')
UI_DIR = JAVA_BASE / "ui/order"
RES_LAYOUT = Path("app/src/main/res/layout")

files = {}

files[UI_DIR / "OrderDetailVM.java"] = """package com.drinkorder.ui.order;

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
    private final OrderDao orderDao;
    private final MutableLiveData<Integer> orderId = new MutableLiveData<>();
    public final LiveData<OrderWithItems> order;

    public OrderDetailVM(@NonNull Application app) {
        super(app);
        AppDatabase db = AppDatabase.getInstance(app);
        orderDao = db.orderDao();

        order = Transformations.switchMap(orderId, id -> {
            if (id == null) return new MediatorLiveData<>();
            return orderDao.getOrderWithItems(id);
        });
    }

    public void setOrderId(int id) {
        Integer cur = orderId.getValue();
        if (cur == null || cur != id) orderId.setValue(id);
    }
}
"""

files[UI_DIR / "OrderLineAdapter.java"] = """package com.drinkorder.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.pojo.OrderItemWithProduct;

public class OrderLineAdapter extends ListAdapter<OrderItemWithProduct, OrderLineAdapter.VH> {

    public OrderLineAdapter() {
        super(DIFF);
    }

    static final DiffUtil.ItemCallback<OrderItemWithProduct> DIFF =
            new DiffUtil.ItemCallback<OrderItemWithProduct>() {
                @Override
                public boolean areItemsTheSame(@NonNull OrderItemWithProduct a, @NonNull OrderItemWithProduct b) {
                    if (a.item == null || b.item == null) return false;
                    return a.item.orderItemId == b.item.orderItemId;
                }

                @Override
                public boolean areContentsTheSame(@NonNull OrderItemWithProduct a, @NonNull OrderItemWithProduct b) {
                    // naive compare
                    String an = a.product != null ? a.product.name : "";
                    String bn = b.product != null ? b.product.name : "";
                    return an.equals(bn)
                            && a.item.quantity == b.item.quantity
                            && Float.compare(a.item.unitPrice, b.item.unitPrice) == 0;
                }
            };

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_line, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        OrderItemWithProduct row = getItem(position);
        String name = row.product != null ? row.product.name : "(unknown)";
        int qty = row.item != null ? row.item.quantity : 0;
        float price = row.item != null ? row.item.unitPrice : 0f;
        float total = qty * price;

        h.tvName.setText(name);
        h.tvQty.setText(String.valueOf(qty));
        h.tvUnit.setText(String.format("%.0f", price));
        h.tvLineTotal.setText(String.format("%.0f", total));
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvUnit, tvLineTotal;
        VH(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvQty = v.findViewById(R.id.tvQty);
            tvUnit = v.findViewById(R.id.tvUnit);
            tvLineTotal = v.findViewById(R.id.tvLineTotal);
        }
    }
}
"""

files[UI_DIR / "OrderDetailFragment.java"] = """package com.drinkorder.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.pojo.OrderItemWithProduct;
import com.drinkorder.data.db.pojo.OrderWithItems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDetailFragment extends Fragment {

    public static final String ARG_ORDER_ID = "order_id";

    private OrderDetailVM vm;
    private OrderLineAdapter adapter;

    private TextView tvOrderId, tvStatus, tvDate, tvTotal;
    private RecyclerView rvLines;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        tvOrderId = v.findViewById(R.id.tvOrderId);
        tvStatus  = v.findViewById(R.id.tvStatus);
        tvDate    = v.findViewById(R.id.tvDate);
        tvTotal   = v.findViewById(R.id.tvTotal);
        rvLines   = v.findViewById(R.id.rvLines);

        rvLines.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new OrderLineAdapter();
        rvLines.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(OrderDetailVM.class);

        int orderId = getArguments() != null ? getArguments().getInt(ARG_ORDER_ID, -1) : -1;
        if (orderId == -1) {
            tvStatus.setText("No order id");
            return;
        }
        vm.setOrderId(orderId);

        vm.order.observe(getViewLifecycleOwner(), data -> bindOrder(data));
    }

    private void bindOrder(OrderWithItems data) {
        if (data == null || data.order == null) return;

        tvOrderId.setText("#" + data.order.orderId);
        tvStatus.setText(safe(data.order.status));
        tvDate.setText(formatDate(data.order.createdAt));
        float total = data.order.totalAmount; // assumes you have this field; otherwise compute
        if (total <= 0f) total = computeTotal(data);
        tvTotal.setText(String.format("%.0f", total));

        adapter.submitList(data.items);
    }

    private float computeTotal(OrderWithItems data) {
        if (data.items == null) return 0f;
        float s = 0f;
        for (OrderItemWithProduct row : data.items) {
            int q = row.item != null ? row.item.quantity : 0;
            float p = row.item != null ? row.item.unitPrice : 0f;
            s += q * p;
        }
        return s;
    }

    private String formatDate(Long epochMillis) {
        if (epochMillis == null) return "";
        Date d = new Date(epochMillis);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(d);
    }

    private String safe(String s) { return s == null ? "" : s; }
}
"""

files[RES_LAYOUT / "fragment_order_detail.xml"] = """<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/tvOrderId"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textStyle="bold"
        android:textSize="18sp"
        android:text="#1234" />

    <TextView
        android:id="@+id/tvStatus"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Pending" />

    <TextView
        android:id="@+id/tvDate"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="01/01/2025 12:00" />

    <TextView
        android:id="@+id/tvTotal"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textStyle="bold"
        android:textSize="16sp"
        android:text="0"
        android:layout_marginTop="4dp"/>

    <View
        android:layout_width="match_parent"
        android:layout_height="8dp" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvLines"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

</LinearLayout>
"""

files[RES_LAYOUT / "item_order_line.xml"] = """<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="12dp">

    <TextView
        android:id="@+id/tvName"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="Product name"/>

    <TextView
        android:id="@+id/tvQty"
        android:layout_width="40dp"
        android:layout_height="wrap_content"
        android:gravity="end"
        android:text="1"/>

    <TextView
        android:id="@+id/tvUnit"
        android:layout_width="64dp"
        android:layout_height="wrap_content"
        android:gravity="end"
        android:text="10000"/>

    <TextView
        android:id="@+id/tvLineTotal"
        android:layout_width="72dp"
        android:layout_height="wrap_content"
        android:gravity="end"
        android:textStyle="bold"
        android:text="10000"/>
</LinearLayout>
"""

def write_once(path: Path, content: str):
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists():
        print(f"= SKIP (exists): {path}")
        return
    path.write_text(content, encoding="utf-8")
    print(f"+ Created: {path}")

def main():
    # quick sanity check
    root_ok = Path("settings.gradle").exists() or Path("settings.gradle.kts").exists()
    if not root_ok:
        print("! Warning: Not sure you're at the Android project root.")
    for path, content in files.items():
        write_once(path, content)
    print("\nDone. Next: navigate to OrderDetailFragment with Bundle.putInt(\"order_id\", id).")


if __name__ == "__main__":
    main()
