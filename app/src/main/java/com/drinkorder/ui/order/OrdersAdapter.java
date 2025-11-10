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
// Import com.drinkorder.data.db.entity.OrderEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.OrderEntity;

// Import java.lang.reflect.Field để sử dụng các lớp hoặc hàm tương ứng.
import java.lang.reflect.Field;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp OrdersAdapter kế thừa ListAdapter<OrderEntity,.
public class OrdersAdapter extends ListAdapter<OrderEntity, OrdersAdapter.VH> {

    // Định nghĩa interface OnOrderClick.
    public interface OnOrderClick { void onClick(OrderEntity order); }
    // Khai báo thuộc tính với phạm vi truy cập: private final OnOrderClick onClick.
    private final OnOrderClick onClick;

    // Định nghĩa phương thức OrdersAdapter với phạm vi truy cập tương ứng.
    public OrdersAdapter(OnOrderClick onClick) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: super(DIFF);.
        super(DIFF);
        // Gán giá trị cho biến hoặc thuộc tính: this.onClick = onClick.
        this.onClick = onClick;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    /** So sánh linh hoạt theo nhiều tên field, tránh .equals cho kiểu nguyên thủy */
    // Định nghĩa phương thức tĩnh ItemCallback<OrderEntity>.
    static final DiffUtil.ItemCallback<OrderEntity> DIFF = new DiffUtil.ItemCallback<OrderEntity>() {
        // Áp dụng annotation @Override cho phần tử bên dưới.
        @Override
        // Định nghĩa phương thức areItemsTheSame với phạm vi truy cập tương ứng.
        public boolean areItemsTheSame(@NonNull OrderEntity a, @NonNull OrderEntity b) {
            // ưu tiên field orderId, nếu thiếu thì thử "id"
            // Thực hiện lời gọi phương thức hoặc khởi tạo: int aid = getIntField(a, new String[]{"orderId", "id"});.
            int aid = getIntField(a, new String[]{"orderId", "id"});
            // Thực hiện lời gọi phương thức hoặc khởi tạo: int bid = getIntField(b, new String[]{"orderId", "id"});.
            int bid = getIntField(b, new String[]{"orderId", "id"});
            // Trả về kết quả aid == bid;.
            return aid == bid;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }

        // Áp dụng annotation @Override cho phần tử bên dưới.
        @Override
        // Định nghĩa phương thức areContentsTheSame với phạm vi truy cập tương ứng.
        public boolean areContentsTheSame(@NonNull OrderEntity a, @NonNull OrderEntity b) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: String as = getStringField(a, new String[]{"status","orderStatus","state","orderState"});.
            String as = getStringField(a, new String[]{"status","orderStatus","state","orderState"});
            // Thực hiện lời gọi phương thức hoặc khởi tạo: String bs = getStringField(b, new String[]{"status","orderStatus","state","orderState"});.
            String bs = getStringField(b, new String[]{"status","orderStatus","state","orderState"});

            // Thực hiện lời gọi phương thức hoặc khởi tạo: double at = getDoubleField(a, new String[]{"totalAmount","total","grandTotal","amount"});.
            double at = getDoubleField(a, new String[]{"totalAmount","total","grandTotal","amount"});
            // Thực hiện lời gọi phương thức hoặc khởi tạo: double bt = getDoubleField(b, new String[]{"totalAmount","total","grandTotal","amount"});.
            double bt = getDoubleField(b, new String[]{"totalAmount","total","grandTotal","amount"});

            // Thực hiện lời gọi phương thức hoặc khởi tạo: long ac = getLongField(a, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});.
            long ac = getLongField(a, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});
            // Thực hiện lời gọi phương thức hoặc khởi tạo: long bc = getLongField(b, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});.
            long bc = getLongField(b, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});

            // Trả về kết quả safe(as).equals(safe(bs)).
            return safe(as).equals(safe(bs))
                    // Thực thi câu lệnh: && Double.compare(at, bt) == 0.
                    && Double.compare(at, bt) == 0
                    // Gán giá trị cho biến hoặc thuộc tính: && ac == bc.
                    && ac == bc;
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
                // Thực hiện lời gọi phương thức hoặc khởi tạo: .inflate(R.layout.item_order, parent, false);.
                .inflate(R.layout.item_order, parent, false);
        // Trả về kết quả new VH(v);.
        return new VH(v);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onBindViewHolder với phạm vi truy cập tương ứng.
    public void onBindViewHolder(@NonNull VH h, int position) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: OrderEntity o = getItem(position);.
        OrderEntity o = getItem(position);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: int id = getIntField(o, new String[]{"orderId","id"});.
        int id = getIntField(o, new String[]{"orderId","id"});
        // Thực hiện lời gọi phương thức hoặc khởi tạo: String status = getStringField(o, new String[]{"status","orderStatus","state","orderState"});.
        String status = getStringField(o, new String[]{"status","orderStatus","state","orderState"});
        // Thực hiện lời gọi phương thức hoặc khởi tạo: double total = getDoubleField(o, new String[]{"totalAmount","total","grandTotal","amount"});.
        double total = getDoubleField(o, new String[]{"totalAmount","total","grandTotal","amount"});

        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.tvId.setText("#" + id);.
        h.tvId.setText("#" + id);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.tvStatus.setText(safe(status));.
        h.tvStatus.setText(safe(status));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: h.tvTotal.setText(String.format(Locale.getDefault(), "%.0f", total));.
        h.tvTotal.setText(String.format(Locale.getDefault(), "%.0f", total));

        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        h.itemView.setOnClickListener(v -> {
            // Kiểm tra điều kiện if để quyết định luồng xử lý.
            if (onClick != null) onClick.onClick(o);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
        });
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa lớp VH kế thừa RecyclerView.ViewHolder.
    static class VH extends RecyclerView.ViewHolder {
        // Thực thi câu lệnh: TextView tvId, tvStatus, tvTotal;.
        TextView tvId, tvStatus, tvTotal;
        // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
        VH(@NonNull View v) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: super(v);.
            super(v);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: tvId = v.findViewById(R.id.tvId);.
            tvId = v.findViewById(R.id.tvId);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: tvStatus = v.findViewById(R.id.tvStatus);.
            tvStatus = v.findViewById(R.id.tvStatus);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTotal = v.findViewById(R.id.tvTotal);.
            tvTotal = v.findViewById(R.id.tvTotal);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // --------------------- Helpers (reflection an toàn) ---------------------
    // Định nghĩa phương thức safe với phạm vi truy cập tương ứng.
    private static String safe(String s) { return s == null ? "" : s; }

    // Định nghĩa phương thức getFieldValue với phạm vi truy cập tương ứng.
    private static Object getFieldValue(Object obj, String[] names) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (obj == null) return null;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Class<?> c = obj.getClass();.
        Class<?> c = obj.getClass();
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (String name : names) {
            // Bắt đầu khối try để bắt lỗi có thể phát sinh.
            try {
                // Thực thi câu lệnh: Field f = c.getField(name); // public.
                Field f = c.getField(name); // public
                // Thực hiện lời gọi phương thức hoặc khởi tạo: f.setAccessible(true);.
                f.setAccessible(true);
                // Trả về kết quả f.get(obj);.
                return f.get(obj);
            // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
            } catch (Exception ignore) {
                // Bắt đầu khối try để bắt lỗi có thể phát sinh.
                try {
                    // Thực thi câu lệnh: Field f = c.getDeclaredField(name); // private/protected.
                    Field f = c.getDeclaredField(name); // private/protected
                    // Thực hiện lời gọi phương thức hoặc khởi tạo: f.setAccessible(true);.
                    f.setAccessible(true);
                    // Trả về kết quả f.get(obj);.
                    return f.get(obj);
                // Kết thúc khối lệnh vừa mở phía trên.
                } catch (Exception ignore2) {}
            // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
            }
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Trả về kết quả null;.
        return null;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức getStringField với phạm vi truy cập tương ứng.
    private static String getStringField(Object obj, String[] names) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Object v = getFieldValue(obj, names);.
        Object v = getFieldValue(obj, names);
        // Trả về kết quả v == null ? null : String.valueOf(v);.
        return v == null ? null : String.valueOf(v);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức getIntField với phạm vi truy cập tương ứng.
    private static int getIntField(Object obj, String[] names) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Object v = getFieldValue(obj, names);.
        Object v = getFieldValue(obj, names);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (v instanceof Number) return ((Number) v).intValue();
        // Bắt đầu khối try để bắt lỗi có thể phát sinh.
        try { return v == null ? 0 : Integer.parseInt(v.toString()); } catch (Exception e) { return 0; }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức getLongField với phạm vi truy cập tương ứng.
    private static long getLongField(Object obj, String[] names) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Object v = getFieldValue(obj, names);.
        Object v = getFieldValue(obj, names);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (v instanceof Number) return ((Number) v).longValue();
        // Bắt đầu khối try để bắt lỗi có thể phát sinh.
        try { return v == null ? 0L : Long.parseLong(v.toString()); } catch (Exception e) { return 0L; }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức getDoubleField với phạm vi truy cập tương ứng.
    private static double getDoubleField(Object obj, String[] names) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Object v = getFieldValue(obj, names);.
        Object v = getFieldValue(obj, names);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (v instanceof Number) return ((Number) v).doubleValue();
        // Bắt đầu khối try để bắt lỗi có thể phát sinh.
        try { return v == null ? 0d : Double.parseDouble(v.toString()); } catch (Exception e) { return 0d; }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
