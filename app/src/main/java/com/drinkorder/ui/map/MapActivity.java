// Đặt package để Activity thuộc nhóm chức năng bản đồ.
package com.drinkorder.ui.map;

// Import Intent để mở ứng dụng Google Maps bên ngoài.
import android.content.Intent;
// Import Uri để tạo đường dẫn geo mở trên Google Maps.
import android.net.Uri;
// Import Bundle phục vụ vòng đời Activity.
import android.os.Bundle;
// Import Button để xử lý nút mở Google Maps.
import android.widget.Button;

// Import NonNull chú thích tham số bắt buộc.
import androidx.annotation.NonNull;
// Import Nullable chú thích tham số có thể rỗng.
import androidx.annotation.Nullable;
// Import AppCompatActivity làm lớp cơ sở cho Activity hiển thị bản đồ.
import androidx.appcompat.app.AppCompatActivity;

// Import R để truy cập layout và view của màn hình bản đồ.
import com.drinkorder.R;
// Import CameraUpdateFactory để điều khiển camera của GoogleMap.
import com.google.android.gms.maps.CameraUpdateFactory;
// Import GoogleMap để thao tác với bản đồ.
import com.google.android.gms.maps.GoogleMap;
// Import OnMapReadyCallback để nhận callback khi bản đồ sẵn sàng.
import com.google.android.gms.maps.OnMapReadyCallback;
// Import SupportMapFragment để nhúng bản đồ vào Activity.
import com.google.android.gms.maps.SupportMapFragment;
// Import LatLng biểu diễn toạ độ địa lý.
import com.google.android.gms.maps.model.LatLng;
// Import MarkerOptions để cấu hình marker hiển thị.
import com.google.android.gms.maps.model.MarkerOptions;

// Activity hiển thị bản đồ vị trí cửa hàng và hỗ trợ mở Google Maps ngoài.
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Giữ tham chiếu GoogleMap để điều chỉnh camera và thêm marker.
    private GoogleMap gmap;

    // Toạ độ demo: trung tâm Q.1, TP.HCM.
    private static final LatLng SHOP = new LatLng(10.776, 106.700);
    // Mức zoom mặc định để thấy rõ khu vực quanh cửa hàng.
    private static final float DEFAULT_ZOOM = 15.5f;

    // Thiết lập layout và khởi tạo bản đồ khi Activity được tạo.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Gọi super để đảm bảo vòng đời chuẩn của Activity.
        super.onCreate(savedInstanceState);
        // Áp dụng layout activity_map chứa fragment bản đồ và nút hành động.
        setContentView(R.layout.activity_map);

        // Lấy SupportMapFragment từ layout để đăng ký callback khi bản đồ sẵn sàng.
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        // Nếu fragment tồn tại thì yêu cầu Google Maps chuẩn bị dữ liệu bất đồng bộ.
        if (mapFragment != null) mapFragment.getMapAsync(this);

        // Ánh xạ nút mở Google Maps bên ngoài.
        Button btn = findViewById(R.id.btnOpenGMaps);
        // Khi nhấn nút sẽ mở ứng dụng Google Maps tới vị trí cửa hàng.
        btn.setOnClickListener(v -> openExternalGoogleMaps(SHOP));
    }

    // Callback được gọi khi GoogleMap đã sẵn sàng để sử dụng.
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        // Lưu lại tham chiếu GoogleMap để dùng ở các thao tác tiếp theo.
        this.gmap = map;

        // Thêm marker đánh dấu vị trí cửa hàng kèm tiêu đề và mô tả.
        gmap.addMarker(new MarkerOptions()
                .position(SHOP)
                .title("DrinkOrder Shop")
                .snippet("Open 8:00-22:00"));
        // Di chuyển camera tới vị trí cửa hàng với mức zoom mặc định.
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SHOP, DEFAULT_ZOOM));

        // Các tuỳ chọn khác như tắt toolbar hay bật zoom controls có thể bật nếu cần.
        // gmap.getUiSettings().setMapToolbarEnabled(false);
        // gmap.getUiSettings().setZoomControlsEnabled(true);

        // Bật hiển thị vị trí người dùng cần xin quyền runtime nên demo tạm thời bỏ qua.
        // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        //     gmap.setMyLocationEnabled(true);
    }

    // Mở ứng dụng Google Maps bên ngoài với toạ độ chỉ định.
    private void openExternalGoogleMaps(LatLng latLng) {
        // Xây dựng URI geo với toạ độ và tên cửa hàng để Google Maps hiển thị.
        String uri = "geo:" + latLng.latitude + "," + latLng.longitude +
                "?q=" + latLng.latitude + "," + latLng.longitude + "(DrinkOrder+Shop)";
        // Tạo Intent ACTION_VIEW để hệ thống mở ứng dụng bản đồ thích hợp.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        // Giới hạn Intent cho ứng dụng Google Maps nhằm mang lại trải nghiệm nhất quán.
        intent.setPackage("com.google.android.apps.maps");
        // Thực thi Intent để chuyển sang Google Maps.
        startActivity(intent);
    }
}
