package ru.tishin.findaddress;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String address_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final EditText edit_text = findViewById(R.id.edittext_address);
        edit_text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    address_str = edit_text.getText().toString();
                    return true;
                }
                return false;
            }
        });
    }

    public void OnClick(View view) {
        if (!address_str.equals("")) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();

        try {
            Geocoder gc = new Geocoder(this);
            List<Address> addresses = gc.getFromLocationName(address_str, 5);
            Address address = addresses.get(0);

            LatLng lng = new LatLng(address.getLatitude(), address.getLongitude());

            googleMap.addMarker(new MarkerOptions().position(lng).title(address_str));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}