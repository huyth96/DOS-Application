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
import com.drinkorder.data.db.pojo.OrderItemWithProduct;

public class OrderLineAdapter extends ListAdapter<OrderItemWithProduct, OrderLineAdapter.VH> {

    // Constructor: truyền DIFF để ListAdapter so sánh và update danh sách hiệu quả
    public OrderLineAdapter() {
        super(DIFF);
    }

    /**
     * DiffUtil giúp so sánh item cũ và mới:
     * - areItemsTheSame: cùng id → là cùng 1 item
     * - areContentsTheSame: nội dung (tên, số lượng, giá) có thay đổi không
     */
    static final DiffUtil.ItemCallback<OrderItemWithProduct> DIFF =
            new DiffUtil.ItemCallback<OrderItemWithProduct>() {
                @Override
                public boolean areItemsTheSame(@NonNull OrderItemWithProduct a, @NonNull OrderItemWithProduct b) {
                    if (a.item == null || b.item == null) return false;
                    return a.item.orderItemId == b.item.orderItemId;
                }

                @Override
                public boolean areContentsTheSame(@NonNull OrderItemWithProduct a, @NonNull OrderItemWithProduct b) {
                    // So sánh tên sản phẩm
                    String an = (a.product != null && a.product.name != null) ? a.product.name : "";
                    String bn = (b.product != null && b.product.name != null) ? b.product.name : "";

                    // So sánh số lượng
                    int aq = (a.item != null) ? a.item.quantity : 0;
                    int bq = (b.item != null) ? b.item.quantity : 0;

                    // So sánh đơn giá
                    double ap = (a.item != null) ? a.item.unitPrice : 0d;
                    double bp = (b.item != null) ? b.item.unitPrice : 0d;

                    return an.equals(bn) // tên giống
                            && aq == bq// số lượng giống
                            && Double.compare(ap, bp) == 0; // giá giống, dùng Double cho double
                }
            };

    // Tạo ViewHolder: inflate layout item_order_line.xml
    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_line, parent, false);
        return new VH(v);
    }

    // Gán dữ liệu của từng OrderItemWithProduct vào các view trong ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        OrderItemWithProduct row = getItem(position);

        String name = (row.product != null && row.product.name != null)
                ? row.product.name : "(unknown)";

        int qty = (row.item != null) ? row.item.quantity : 0;
        double price = (row.item != null) ? row.item.unitPrice : 0d;
        double total = qty * price;

        h.tvName.setText(name);
        h.tvQty.setText(String.valueOf(qty));
        h.tvUnit.setText(String.format("%.0f", price));
        h.tvLineTotal.setText(String.format("%.0f", total));
    }
    // Giữ tham chiếu tới các view (TextView) của một dòng item.
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
