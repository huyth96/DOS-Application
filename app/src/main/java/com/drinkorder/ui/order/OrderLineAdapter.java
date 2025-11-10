// Khai báo package com.drinkorder.ui.order cho toàn bộ lớp.
package com.drinkorder.ui.order;

// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;
// Import android.widget.TextView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.TextView;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.recyclerview.widget.DiffUtil để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.DiffUtil;
// Import androidx.recyclerview.widget.ListAdapter để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.ListAdapter;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.pojo.OrderItemWithProduct để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.OrderItemWithProduct;

// Định nghĩa lớp OrderLineAdapter kế thừa ListAdapter<OrderItemWithProduct,.
public class OrderLineAdapter extends ListAdapter<OrderItemWithProduct, OrderLineAdapter.VH> {

    // Định nghĩa phương thức OrderLineAdapter với phạm vi truy cập tương ứng.
    public OrderLineAdapter() {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: super(DIFF);.
        super(DIFF);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    /** So sánh từng dòng dựa vào id, số lượng và đơn giá (double) */
    // Thực thi câu lệnh: static final DiffUtil.ItemCallback<OrderItemWithProduct> DIFF =.
    static final DiffUtil.ItemCallback<OrderItemWithProduct> DIFF =
            // Khởi tạo đối tượng mới với biểu thức new DiffUtil.ItemCallback<OrderItemWithProduct>() {.
            new DiffUtil.ItemCallback<OrderItemWithProduct>() {
                // Áp dụng annotation @Override cho phần tử bên dưới.
                @Override
                // Định nghĩa phương thức areItemsTheSame với phạm vi truy cập tương ứng.
                public boolean areItemsTheSame(@NonNull OrderItemWithProduct a, @NonNull OrderItemWithProduct b) {
                    // Kiểm tra điều kiện if để quyết định luồng xử lý.
                    if (a.item == null || b.item == null) return false;
                    // Trả về kết quả a.item.orderItemId == b.item.orderItemId;.
                    return a.item.orderItemId == b.item.orderItemId;
                // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
                }

                // Áp dụng annotation @Override cho phần tử bên dưới.
                @Override
                // Định nghĩa phương thức areContentsTheSame với phạm vi truy cập tương ứng.
                public boolean areContentsTheSame(@NonNull OrderItemWithProduct a, @NonNull OrderItemWithProduct b) {
                    // Gán giá trị cho biến hoặc thuộc tính: String an = (a.product != null && a.product.name != null) ? a.product.name : "".
                    String an = (a.product != null && a.product.name != null) ? a.product.name : "";
                    // Gán giá trị cho biến hoặc thuộc tính: String bn = (b.product != null && b.product.name != null) ? b.product.name : "".
                    String bn = (b.product != null && b.product.name != null) ? b.product.name : "";

                    // Gán giá trị cho biến hoặc thuộc tính: int aq = (a.item != null) ? a.item.quantity : 0.
                    int aq = (a.item != null) ? a.item.quantity : 0;
                    // Gán giá trị cho biến hoặc thuộc tính: int bq = (b.item != null) ? b.item.quantity : 0.
                    int bq = (b.item != null) ? b.item.quantity : 0;

                    // Gán giá trị cho biến hoặc thuộc tính: double ap = (a.item != null) ? a.item.unitPrice : 0d.
                    double ap = (a.item != null) ? a.item.unitPrice : 0d;
                    // Gán giá trị cho biến hoặc thuộc tính: double bp = (b.item != null) ? b.item.unitPrice : 0d.
                    double bp = (b.item != null) ? b.item.unitPrice : 0d;

                    // Trả về kết quả an.equals(bn).
                    return an.equals(bn)
                            // Thực thi câu lệnh: && aq == bq.
                            && aq == bq
                            // Thực thi câu lệnh: && Double.compare(ap, bp) == 0;   // dùng Double cho double.
                            && Double.compare(ap, bp) == 0;   // dùng Double cho double
                // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
                }
            // Thực thi câu lệnh: };.
            };

    // Áp dụng annotation @NonNull và @Override cho phần tử bên dưới.
    @NonNull @Override
    // Định nghĩa phương thức onCreateViewHolder với phạm vi truy cập tương ứng.
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: View v = LayoutInflater.from(parent.getContext()).
        View v = LayoutInflater.from(parent.getContext())
                // Thực hiện lời gọi phương thức hoặc khởi tạo: .inflate(R.layout.item_order_line, parent, false);.
                .inflate(R.layout.item_order_line, parent, false);
        // Trả về kết quả new VH(v);.
        return new VH(v);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onBindViewHolder với phạm vi truy cập tương ứng.
    public void onBindViewHolder(@NonNull VH h, int position) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: OrderItemWithProduct row = getItem(position);.
        OrderItemWithProduct row = getItem(position);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: String name = (row.product != null && row.product.name != null).
        String name = (row.product != null && row.product.name != null)
                // Thực thi câu lệnh: ? row.product.name : "(unknown)";.
                ? row.product.name : "(unknown)";

        // Gán giá trị cho biến hoặc thuộc tính: int qty = (row.item != null) ? row.item.quantity : 0.
        int qty = (row.item != null) ? row.item.quantity : 0;
        // Gán giá trị cho biến hoặc thuộc tính: double price = (row.item != null) ? row.item.unitPrice : 0d.
        double price = (row.item != null) ? row.item.unitPrice : 0d;
        // Gán giá trị cho biến hoặc thuộc tính: double total = qty * price.
        double total = qty * price;

        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.tvName.setText(name);.
        h.tvName.setText(name);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.tvQty.setText(String.valueOf(qty));.
        h.tvQty.setText(String.valueOf(qty));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.tvUnit.setText(String.format("%.0f", price));.
        h.tvUnit.setText(String.format("%.0f", price));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.tvLineTotal.setText(String.format("%.0f", total));.
        h.tvLineTotal.setText(String.format("%.0f", total));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa lớp VH kế thừa RecyclerView.ViewHolder.
    static class VH extends RecyclerView.ViewHolder {
        // Thực thi câu lệnh: TextView tvName, tvQty, tvUnit, tvLineTotal;.
        TextView tvName, tvQty, tvUnit, tvLineTotal;
        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        VH(@NonNull View v) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: super(v);.
            super(v);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: tvName = v.findViewById(R.id.tvName);.
            tvName = v.findViewById(R.id.tvName);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: tvQty = v.findViewById(R.id.tvQty);.
            tvQty = v.findViewById(R.id.tvQty);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: tvUnit = v.findViewById(R.id.tvUnit);.
            tvUnit = v.findViewById(R.id.tvUnit);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: tvLineTotal = v.findViewById(R.id.tvLineTotal);.
            tvLineTotal = v.findViewById(R.id.tvLineTotal);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
