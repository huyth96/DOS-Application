// Khai báo package com.drinkorder.ui.map cho toàn bộ lớp.
package com.drinkorder.ui.map;

// Import android.content.Intent để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Intent;
// Import android.net.Uri để sử dụng các lớp hoặc hàm tương ứng.
import android.net.Uri;
// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.widget.Button để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Button;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.appcompat.app.AppCompatActivity để sử dụng các lớp hoặc hàm tương ứng.
import androidx.appcompat.app.AppCompatActivity;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.google.android.gms.maps.CameraUpdateFactory để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.gms.maps.CameraUpdateFactory;
// Import com.google.android.gms.maps.GoogleMap để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.gms.maps.GoogleMap;
// Import com.google.android.gms.maps.OnMapReadyCallback để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.gms.maps.OnMapReadyCallback;
// Import com.google.android.gms.maps.SupportMapFragment để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.gms.maps.SupportMapFragment;
// Import com.google.android.gms.maps.model.LatLng để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.gms.maps.model.LatLng;
// Import com.google.android.gms.maps.model.MarkerOptions để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.gms.maps.model.MarkerOptions;

// Định nghĩa lớp MapActivity kế thừa AppCompatActivity và triển khai OnMapReadyCallback.
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Khai báo thuộc tính với phạm vi truy cập: private GoogleMap gmap.
    private GoogleMap gmap;

    // Toạ độ demo: trung tâm Q.1, TP.HCM
    // Khai báo thuộc tính với phạm vi truy cập: private static final LatLng SHOP = new LatLng(10.776, 106.700).
    private static final LatLng SHOP = new LatLng(10.776, 106.700);
    // Khai báo thuộc tính với phạm vi truy cập: private static final float DEFAULT_ZOOM = 15.5f.
    private static final float DEFAULT_ZOOM = 15.5f;

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onCreate với phạm vi truy cập tương ứng.
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate(savedInstanceState);.
        super.onCreate(savedInstanceState);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: setContentView(R.layout.activity_map);.
        setContentView(R.layout.activity_map);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: SupportMapFragment mapFragment = (SupportMapFragment).
        SupportMapFragment mapFragment = (SupportMapFragment)
                // Thực hiện lời gọi phương thức hoặc khởi tạo: getSupportFragmentManager().findFragmentById(R.id.mapFragment);.
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (mapFragment != null) mapFragment.getMapAsync(this);

        // Thực hiện lời gọi phương thức hoặc khởi tạo: Button btn = findViewById(R.id.btnOpenGMaps);.
        Button btn = findViewById(R.id.btnOpenGMaps);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: btn.setOnClickListener(v -> openExternalGoogleMaps(SHOP));.
        btn.setOnClickListener(v -> openExternalGoogleMaps(SHOP));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onMapReady với phạm vi truy cập tương ứng.
    public void onMapReady(@NonNull GoogleMap map) {
        // Gán giá trị cho biến hoặc thuộc tính: this.gmap = map.
        this.gmap = map;

        // Add marker vị trí cửa hàng (đổi title/subtitle theo ý bạn)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: gmap.addMarker(new MarkerOptions().
        gmap.addMarker(new MarkerOptions()
                // Thực hiện lời gọi phương thức hoặc khởi tạo: .position(SHOP).
                .position(SHOP)
                // Thực hiện lời gọi phương thức hoặc khởi tạo: .title("DrinkOrder Shop").
                .title("DrinkOrder Shop")
                // Thực hiện lời gọi phương thức hoặc khởi tạo: .snippet("Open 8:00-22:00"));.
                .snippet("Open 8:00-22:00"));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SHOP, DEFAULT_ZOOM));.
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SHOP, DEFAULT_ZOOM));

        // Tắt cử chỉ/tuỳ chọn nếu muốn map tối giản:
        // gmap.getUiSettings().setMapToolbarEnabled(false);
        // gmap.getUiSettings().setZoomControlsEnabled(true);

        // Bật my-location cần quyền runtime -> để đơn giản demo này tạm tắt:
        // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        //     gmap.setMyLocationEnabled(true);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức openExternalGoogleMaps với phạm vi truy cập tương ứng.
    private void openExternalGoogleMaps(LatLng latLng) {
        // Thực thi câu lệnh: String uri = "geo:" + latLng.latitude + "," + latLng.longitude +.
        String uri = "geo:" + latLng.latitude + "," + latLng.longitude +
                // Gán giá trị cho biến hoặc thuộc tính: "?q=" + latLng.latitude + "," + latLng.longitude + "(DrinkOrder+Shop)".
                "?q=" + latLng.latitude + "," + latLng.longitude + "(DrinkOrder+Shop)";
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        // Thực hiện lời gọi phương thức hoặc khởi tạo: intent.setPackage("com.google.android.apps.maps");.
        intent.setPackage("com.google.android.apps.maps");
        // Thực hiện lời gọi phương thức hoặc khởi tạo: startActivity(intent);.
        startActivity(intent);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
