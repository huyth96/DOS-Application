package com.drinkorder.ui.order;

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

import java.lang.reflect.Field;
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
        adapter = new OrderLineAdapter(); // đã dùng double trong Adapter này
        rvLines.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(OrderDetailVM.class);

        int orderId = getArguments() != null ? getArguments().getInt(ARG_ORDER_ID, -1) : -1;
        if (orderId == -1) {
            tvStatus.setText("No order id");
            return;
        }
        vm.setOrderId(orderId);

        vm.order.observe(getViewLifecycleOwner(), this::bindOrder);
    }

    private void bindOrder(OrderWithItems data) {
        if (data == null || data.order == null) return;

        // orderId (field này chắc chắn có)
        tvOrderId.setText("#" + getIntField(data.order, new String[]{"orderId", "id"}));

        // status: thử nhiều tên
        String status = getStringField(data.order, new String[]{"status","orderStatus","state","orderState"});
        tvStatus.setText(safe(status));

        // createdAt: thử nhiều tên; nhận long/Long
        long created = getLongField(data.order, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});
        tvDate.setText(created > 0 ? formatDate(created) : "");

        // total: nếu entity có, dùng; nếu không, tự tính từ items (double)
        double total = getDoubleField(data.order, new String[]{"totalAmount","total","grandTotal","amount"});
        if (total <= 0d) total = computeTotal(data);
        tvTotal.setText(String.format(Locale.getDefault(), "%.0f", total));

        adapter.submitList(data.items);
    }

    /** Tính tổng bằng double để tránh lossy-conversion */
    private double computeTotal(OrderWithItems data) {
        if (data.items == null) return 0d;
        double s = 0d;
        for (OrderItemWithProduct row : data.items) {
            int q = (int) getNumberField(row.item, new String[]{"quantity","qty","count"}, 0);
            double p = getDoubleField(row.item, new String[]{"unitPrice","price","unit_cost","unitCost"});
            s += q * p;
        }
        return s;
    }

    private String formatDate(long epochMillis) {
        Date d = new Date(epochMillis);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(d);
    }

    private String safe(String s) { return s == null ? "" : s; }

    // ===================== Reflection helpers =====================
    private static String getStringField(Object obj, String[] names) {
        Object v = getFieldValue(obj, names);
        return v == null ? null : String.valueOf(v);
    }

    private static int getIntField(Object obj, String[] names) {
        Object v = getFieldValue(obj, names);
        if (v instanceof Number) return ((Number) v).intValue();
        try { return v == null ? 0 : Integer.parseInt(v.toString()); } catch (Exception e) { return 0; }
    }

    private static long getLongField(Object obj, String[] names) {
        Object v = getFieldValue(obj, names);
        if (v instanceof Number) return ((Number) v).longValue();
        try { return v == null ? 0L : Long.parseLong(v.toString()); } catch (Exception e) { return 0L; }
    }

    private static double getDoubleField(Object obj, String[] names) {
        Object v = getFieldValue(obj, names);
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return v == null ? 0d : Double.parseDouble(v.toString()); } catch (Exception e) { return 0d; }
    }

    private static double getNumberField(Object obj, String[] names, double def) {
        Object v = getFieldValue(obj, names);
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return v == null ? def : Double.parseDouble(v.toString()); } catch (Exception e) { return def; }
    }

    private static Object getFieldValue(Object obj, String[] names) {
        if (obj == null) return null;
        Class<?> c = obj.getClass();
        for (String name : names) {
            try {
                Field f = c.getField(name); // public field
                f.setAccessible(true);
                return f.get(obj);
            } catch (Exception ignore) {
                try {
                    Field f = c.getDeclaredField(name); // private/protected
                    f.setAccessible(true);
                    return f.get(obj);
                } catch (Exception ignore2) {}
            }
        }
        return null;
    }
}
