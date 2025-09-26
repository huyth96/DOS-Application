package com.drinkorder.ui.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drinkorder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gmap;

    // Toạ độ demo: trung tâm Q.1, TP.HCM
    private static final LatLng SHOP = new LatLng(10.776, 106.700);
    private static final float DEFAULT_ZOOM = 15.5f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        Button btn = findViewById(R.id.btnOpenGMaps);
        btn.setOnClickListener(v -> openExternalGoogleMaps(SHOP));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.gmap = map;

        // Thêm marker vị trí cửa hàng (đổi title/subtitle theo ý bạn)
        gmap.addMarker(new MarkerOptions()
                .position(SHOP)
                .title("DrinkOrder Shop")
                .snippet("Mở 8:00–22:00"));
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SHOP, DEFAULT_ZOOM));

        // Tắt cử chỉ/tuỳ chọn nếu muốn map tối giản:
        // gmap.getUiSettings().setMapToolbarEnabled(false);
        // gmap.getUiSettings().setZoomControlsEnabled(true);

        // Bật my-location cần quyền runtime -> để đơn giản demo này tạm tắt:
        // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        //     gmap.setMyLocationEnabled(true);
    }

    private void openExternalGoogleMaps(LatLng latLng) {
        String uri = "geo:" + latLng.latitude + "," + latLng.longitude +
                "?q=" + latLng.latitude + "," + latLng.longitude + "(DrinkOrder+Shop)";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
