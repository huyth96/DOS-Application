// Đặt package để fragment thuộc nhóm chức năng chi tiết đơn hàng.
package com.drinkorder.ui.order;

// Import Bundle phục vụ vòng đời fragment.
import android.os.Bundle;
// Import LayoutInflater để tạo view từ XML.
import android.view.LayoutInflater;
// Import View để thao tác với giao diện.
import android.view.View;
// Import ViewGroup làm container cho fragment.
import android.view.ViewGroup;
// Import TextView để hiển thị thông tin tổng quan đơn hàng.
import android.widget.TextView;

// Import NonNull để chú thích tham số bắt buộc có giá trị.
import androidx.annotation.NonNull;
// Import Nullable để chú thích tham số có thể rỗng.
import androidx.annotation.Nullable;
// Import Fragment làm lớp cơ sở cho màn chi tiết đơn hàng.
import androidx.fragment.app.Fragment;
// Import ViewModelProvider để lấy ViewModel quản lý dữ liệu.
import androidx.lifecycle.ViewModelProvider;
// Import LinearLayoutManager để sắp xếp danh sách line item theo chiều dọc.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import RecyclerView để hiển thị danh sách sản phẩm trong đơn.
import androidx.recyclerview.widget.RecyclerView;

// Import R để truy cập layout hiển thị chi tiết đơn hàng.
import com.drinkorder.R;
// Import OrderItemWithProduct để có thông tin sản phẩm kèm dòng đơn.
import com.drinkorder.data.db.pojo.OrderItemWithProduct;
// Import OrderWithItems để nhận toàn bộ dữ liệu đơn hàng kèm chi tiết.
import com.drinkorder.data.db.pojo.OrderWithItems;

// Import Field để truy cập phản xạ các thuộc tính linh hoạt.
import java.lang.reflect.Field;
// Import SimpleDateFormat để định dạng thời gian tạo đơn.
import java.text.SimpleDateFormat;
// Import Date để chuyển đổi timestamp sang dạng đọc được.
import java.util.Date;
// Import Locale để định dạng chuỗi phù hợp ngôn ngữ thiết bị.
import java.util.Locale;

// Fragment hiển thị chi tiết một đơn hàng cụ thể.
public class OrderDetailFragment extends Fragment {

    // Khóa Bundle để truyền id đơn hàng cần hiển thị.
    public static final String ARG_ORDER_ID = "order_id";

    // ViewModel cung cấp dữ liệu đơn hàng từ Room.
    private OrderDetailVM vm;
    // Adapter hiển thị từng dòng sản phẩm trong đơn.
    private OrderLineAdapter adapter;

    // TextView hiển thị mã đơn.
    private TextView tvOrderId, tvStatus, tvDate, tvTotal;
    // RecyclerView hiển thị các dòng sản phẩm.
    private RecyclerView rvLines;

    // Inflate layout chi tiết đơn hàng khi tạo fragment.
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Trả về layout hiển thị thông tin đơn hàng và danh sách sản phẩm.
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    // Thiết lập view và bắt đầu tải dữ liệu sau khi layout sẵn sàng.
    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        // Gọi super để duy trì vòng đời chuẩn của fragment.
        super.onViewCreated(v, savedInstanceState);

        // Ánh xạ TextView hiển thị mã đơn hàng.
        tvOrderId = v.findViewById(R.id.tvOrderId);
        // Ánh xạ TextView hiển thị trạng thái đơn hàng.
        tvStatus  = v.findViewById(R.id.tvStatus);
        // Ánh xạ TextView hiển thị thời gian tạo đơn.
        tvDate    = v.findViewById(R.id.tvDate);
        // Ánh xạ TextView hiển thị tổng tiền đơn hàng.
        tvTotal   = v.findViewById(R.id.tvTotal);
        // Ánh xạ RecyclerView hiển thị danh sách dòng sản phẩm.
        rvLines   = v.findViewById(R.id.rvLines);

        // Dùng LinearLayoutManager dọc để hiển thị các dòng sản phẩm.
        rvLines.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Khởi tạo adapter hiển thị chi tiết từng dòng.
        adapter = new OrderLineAdapter(); // đã dùng double trong Adapter này
        // Gắn adapter vào RecyclerView để hiển thị danh sách.
        rvLines.setAdapter(adapter);

        // Lấy ViewModel để truy vấn dữ liệu chi tiết đơn hàng.
        vm = new ViewModelProvider(this).get(OrderDetailVM.class);

        // Lấy id đơn hàng được truyền qua arguments.
        int orderId = getArguments() != null ? getArguments().getInt(ARG_ORDER_ID, -1) : -1;
        // Nếu không có id hợp lệ thì thông báo và ngừng xử lý.
        if (orderId == -1) {
            tvStatus.setText("No order id");
            return;
        }
        // Thiết lập id cho ViewModel để tải đúng đơn hàng.
        vm.setOrderId(orderId);

        // Quan sát LiveData và cập nhật giao diện khi dữ liệu thay đổi.
        vm.order.observe(getViewLifecycleOwner(), this::bindOrder);
    }

    // Gán dữ liệu đơn hàng lên giao diện khi nhận được kết quả.
    private void bindOrder(OrderWithItems data) {
        // Nếu không có dữ liệu hợp lệ thì bỏ qua.
        if (data == null || data.order == null) return;

        // orderId: hiển thị mã đơn kèm dấu #.
        tvOrderId.setText("#" + getIntField(data.order, new String[]{"orderId", "id"}));

        // status: thử nhiều tên thuộc tính để tương thích các entity khác nhau.
        String status = getStringField(data.order, new String[]{"status","orderStatus","state","orderState"});
        tvStatus.setText(safe(status));

        // createdAt: hỗ trợ nhiều tên trường và kiểu dữ liệu số.
        long created = getLongField(data.order, new String[]{"createdAt","createdTime","createdOn","timestamp","createdAtMillis"});
        tvDate.setText(created > 0 ? formatDate(created) : "");

        // total: ưu tiên dùng trường có sẵn, nếu không sẽ tự tính từ danh sách items.
        double total = getDoubleField(data.order, new String[]{"totalAmount","total","grandTotal","amount"});
        if (total <= 0d) total = computeTotal(data);
        tvTotal.setText(String.format(Locale.getDefault(), "%.0f", total));

        // Đưa danh sách dòng sản phẩm vào adapter để hiển thị.
        adapter.submitList(data.items);
    }

    /** Tính tổng bằng double để tránh lossy-conversion */
    private double computeTotal(OrderWithItems data) {
        // Nếu không có dòng sản phẩm thì tổng bằng 0.
        if (data.items == null) return 0d;
        // Sử dụng biến cộng dồn kiểu double để giữ độ chính xác.
        double s = 0d;
        // Duyệt từng dòng sản phẩm để tính số lượng * đơn giá.
        for (OrderItemWithProduct row : data.items) {
            int q = (int) getNumberField(row.item, new String[]{"quantity","qty","count"}, 0);
            double p = getDoubleField(row.item, new String[]{"unitPrice","price","unit_cost","unitCost"});
            s += q * p;
        }
        // Trả về tổng cộng.
        return s;
    }

    // Định dạng thời gian từ mili giây sang chuỗi đọc dễ dàng.
    private String formatDate(long epochMillis) {
        Date d = new Date(epochMillis);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(d);
    }

    // Trả về chuỗi rỗng khi giá trị null để tránh hiển thị "null".
    private String safe(String s) { return s == null ? "" : s; }

    // ===================== Reflection helpers =====================
    // Lấy giá trị chuỗi theo nhiều tên thuộc tính khác nhau.
    private static String getStringField(Object obj, String[] names) {
        Object v = getFieldValue(obj, names);
        return v == null ? null : String.valueOf(v);
    }

    // Lấy giá trị số nguyên theo nhiều tên trường.
    private static int getIntField(Object obj, String[] names) {
        Object v = getFieldValue(obj, names);
        if (v instanceof Number) return ((Number) v).intValue();
        try { return v == null ? 0 : Integer.parseInt(v.toString()); } catch (Exception e) { return 0; }
    }

    // Lấy giá trị số dài theo nhiều tên trường.
    private static long getLongField(Object obj, String[] names) {
        Object v = getFieldValue(obj, names);
        if (v instanceof Number) return ((Number) v).longValue();
        try { return v == null ? 0L : Long.parseLong(v.toString()); } catch (Exception e) { return 0L; }
    }

    // Lấy giá trị số thực theo nhiều tên trường.
    private static double getDoubleField(Object obj, String[] names) {
        Object v = getFieldValue(obj, names);
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return v == null ? 0d : Double.parseDouble(v.toString()); } catch (Exception e) { return 0d; }
    }

    // Lấy giá trị số bất kỳ với giá trị mặc định khi không đọc được.
    private static double getNumberField(Object obj, String[] names, double def) {
        Object v = getFieldValue(obj, names);
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return v == null ? def : Double.parseDouble(v.toString()); } catch (Exception e) { return def; }
    }

    // Thực hiện truy xuất phản xạ với danh sách tên trường dự phòng.
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
