// Khai báo package com.drinkorder.ui.order cho toàn bộ lớp.
package com.drinkorder.ui.order;

// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
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
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.fragment.app.Fragment để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.Fragment;
// Import androidx.lifecycle.ViewModelProvider để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.ViewModelProvider;
// Import androidx.recyclerview.widget.LinearLayoutManager để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.pojo.OrderItemWithProduct để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.OrderItemWithProduct;
// Import com.drinkorder.data.db.pojo.OrderWithItems để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.pojo.OrderWithItems;

// Import java.lang.reflect.Field để sử dụng các lớp hoặc hàm tương ứng.
import java.lang.reflect.Field;
// Import java.text.SimpleDateFormat để sử dụng các lớp hoặc hàm tương ứng.
import java.text.SimpleDateFormat;
// Import java.util.Date để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Date;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp OrderDetailFragment kế thừa Fragment.
public class OrderDetailFragment extends Fragment {

    // Khai báo thuộc tính với phạm vi truy cập: public static final String ARG_ORDER_ID = "order_id".
    public static final String ARG_ORDER_ID = "order_id";

    // Khai báo thuộc tính với phạm vi truy cập: private OrderDetailVM vm.
    private OrderDetailVM vm;
    // Khai báo thuộc tính với phạm vi truy cập: private OrderLineAdapter adapter.
    private OrderLineAdapter adapter;

    // Khai báo thuộc tính với phạm vi truy cập: private TextView tvOrderId, tvStatus, tvDate, tvTotal.
    private TextView tvOrderId, tvStatus, tvDate, tvTotal;
    // Khai báo thuộc tính với phạm vi truy cập: private RecyclerView rvLines.
    private RecyclerView rvLines;

    // Áp dụng annotation @Nullable và @Override cho phần tử bên dưới.
    @Nullable @Override
    // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Trả về kết quả inflater.inflate(R.layout.fragment_order_detail, container, false);.
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(v, savedInstanceState);.
        super.onViewCreated(v, savedInstanceState);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvOrderId = v.findViewById(R.id.tvOrderId);.
        tvOrderId = v.findViewById(R.id.tvOrderId);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvStatus  = v.findViewById(R.id.tvStatus);.
        tvStatus  = v.findViewById(R.id.tvStatus);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvDate    = v.findViewById(R.id.tvDate);.
        tvDate    = v.findViewById(R.id.tvDate);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTotal   = v.findViewById(R.id.tvTotal);.
        tvTotal   = v.findViewById(R.id.tvTotal);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: rvLines   = v.findViewById(R.id.rvLines);.
        rvLines   = v.findViewById(R.id.rvLines);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: rvLines.setLayoutManager(new LinearLayoutManager(requireContext()));.
        rvLines.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Thực thi câu lệnh: adapter = new OrderLineAdapter(); // đã dùng double trong Adapter này.
        adapter = new OrderLineAdapter(); // đã dùng double trong Adapter này
        // Thực hiện lời gọi phương thức hoặc khởi tạo: rvLines.setAdapter(adapter);.
        rvLines.setAdapter(adapter);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(this).get(OrderDetailVM.class);.
        vm = new ViewModelProvider(this).get(OrderDetailVM.class);

        // Gán giá trị cho biến hoặc thuộc tính: int orderId = getArguments() != null ? getArguments().getInt(ARG_ORDER_ID, -1) : -1.
        int orderId = getArguments() != null ? getArguments().getInt(ARG_ORDER_ID, -1) : -1;
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (orderId == -1) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: tvStatus.setText("No order id");.
            tvStatus.setText("No order id");
            // Trả về kết quả ;.
            return;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.setOrderId(orderId);.
        vm.setOrderId(orderId);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.order.observe(getViewLifecycleOwner(), this::bindOrder);.
        vm.order.observe(getViewLifecycleOwner(), this::bindOrder);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức bindOrder với phạm vi truy cập tương ứng.
    private void bindOrder(OrderWithItems data) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (data == null || data.order == null) return;

        // orderId (field này chắc chắn có)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvOrderId.setText("#" + getIntField(data.order, new String[]{"orderId", "id"}));.
        tvOrderId.setText("#" + getIntField(data.order, new String[]{"orderId", "id"}));

        // status: thử nhiều tên
        // Thực hiện lời gọi phương thức hoặc khởi tạo: String status = getStringField(data.order, new String[]{"status","orderStatus","state","orderState"});.
        String status = getStringField(data.order, new String[]{"status","orderStatus","state","orderState"});
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvStatus.setText(safe(status));.
        tvStatus.setText(safe(status));

        // createdAt: thử nhiều tên; nhận long/Long
        // Thực hiện lời gọi phương thức hoặc khởi tạo: long created = getLongField(data.order, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});.
        long created = getLongField(data.order, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvDate.setText(created > 0 ? formatDate(created) : "");.
        tvDate.setText(created > 0 ? formatDate(created) : "");

        // total: nếu entity có, dùng; nếu không, tự tính từ items (double)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: double total = getDoubleField(data.order, new String[]{"totalAmount","total","grandTotal","amount"});.
        double total = getDoubleField(data.order, new String[]{"totalAmount","total","grandTotal","amount"});
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (total <= 0d) total = computeTotal(data);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTotal.setText(String.format(Locale.getDefault(), "%.0f", total));.
        tvTotal.setText(String.format(Locale.getDefault(), "%.0f", total));

        // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submitList(data.items);.
        adapter.submitList(data.items);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    /** Tính tổng bằng double để tránh lossy-conversion */
    // Định nghĩa phương thức computeTotal với phạm vi truy cập tương ứng.
    private double computeTotal(OrderWithItems data) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (data.items == null) return 0d;
        // Gán giá trị cho biến hoặc thuộc tính: double s = 0d.
        double s = 0d;
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (OrderItemWithProduct row : data.items) {
            // Thực hiện lời gọi phương thức hoặc khởi tạo: int q = (int) getNumberField(row.item, new String[]{"quantity","qty","count"}, 0);.
            int q = (int) getNumberField(row.item, new String[]{"quantity","qty","count"}, 0);
            // Thực hiện lời gọi phương thức hoặc khởi tạo: double p = getDoubleField(row.item, new String[]{"unitPrice","price","unit_cost","unitCost"});.
            double p = getDoubleField(row.item, new String[]{"unitPrice","price","unit_cost","unitCost"});
            // Gán giá trị cho biến hoặc thuộc tính: s += q * p.
            s += q * p;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
        // Trả về kết quả s;.
        return s;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức formatDate với phạm vi truy cập tương ứng.
    private String formatDate(long epochMillis) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Date d = new Date(epochMillis);.
        Date d = new Date(epochMillis);
        // Trả về kết quả new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(d);.
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(d);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức safe với phạm vi truy cập tương ứng.
    private String safe(String s) { return s == null ? "" : s; }

    // ===================== Reflection helpers =====================
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

    // Định nghĩa phương thức getNumberField với phạm vi truy cập tương ứng.
    private static double getNumberField(Object obj, String[] names, double def) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Object v = getFieldValue(obj, names);.
        Object v = getFieldValue(obj, names);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (v instanceof Number) return ((Number) v).doubleValue();
        // Bắt đầu khối try để bắt lỗi có thể phát sinh.
        try { return v == null ? def : Double.parseDouble(v.toString()); } catch (Exception e) { return def; }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

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
                // Thực thi câu lệnh: Field f = c.getField(name); // public field.
                Field f = c.getField(name); // public field
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
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
