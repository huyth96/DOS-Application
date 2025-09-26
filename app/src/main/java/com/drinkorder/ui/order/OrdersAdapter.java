package com.drinkorder.ui.order;

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

import java.lang.reflect.Field;
import java.util.Locale;

public class OrdersAdapter extends ListAdapter<OrderEntity, OrdersAdapter.VH> {

    public interface OnOrderClick { void onClick(OrderEntity order); }
    private final OnOrderClick onClick;

    public OrdersAdapter(OnOrderClick onClick) {
        super(DIFF);
        this.onClick = onClick;
    }

    /** So sánh linh hoạt theo nhiều tên field, tránh .equals cho kiểu nguyên thủy */
    static final DiffUtil.ItemCallback<OrderEntity> DIFF = new DiffUtil.ItemCallback<OrderEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull OrderEntity a, @NonNull OrderEntity b) {
            // ưu tiên field orderId, nếu thiếu thì thử "id"
            int aid = getIntField(a, new String[]{"orderId", "id"});
            int bid = getIntField(b, new String[]{"orderId", "id"});
            return aid == bid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull OrderEntity a, @NonNull OrderEntity b) {
            String as = getStringField(a, new String[]{"status","orderStatus","state","orderState"});
            String bs = getStringField(b, new String[]{"status","orderStatus","state","orderState"});

            double at = getDoubleField(a, new String[]{"totalAmount","total","grandTotal","amount"});
            double bt = getDoubleField(b, new String[]{"totalAmount","total","grandTotal","amount"});

            long ac = getLongField(a, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});
            long bc = getLongField(b, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});

            return safe(as).equals(safe(bs))
                    && Double.compare(at, bt) == 0
                    && ac == bc;
        }
    };

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        OrderEntity o = getItem(position);

        int id = getIntField(o, new String[]{"orderId","id"});
        String status = getStringField(o, new String[]{"status","orderStatus","state","orderState"});
        double total = getDoubleField(o, new String[]{"totalAmount","total","grandTotal","amount"});

        h.tvId.setText("#" + id);
        h.tvStatus.setText(safe(status));
        h.tvTotal.setText(String.format(Locale.getDefault(), "%.0f", total));

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

    // --------------------- Helpers (reflection an toàn) ---------------------
    private static String safe(String s) { return s == null ? "" : s; }

    private static Object getFieldValue(Object obj, String[] names) {
        if (obj == null) return null;
        Class<?> c = obj.getClass();
        for (String name : names) {
            try {
                Field f = c.getField(name); // public
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
}
