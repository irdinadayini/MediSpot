package com.example.mapnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar mapToolbar;

    private LatLng centerlocation;
    private Vector<MarkerOptions> markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Set up the toolbar
        mapToolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(mapToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Maps");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize center location and marker options
        centerlocation = new LatLng(6.442055282470381, 100.26587420221344);

        markerOptions = new Vector<>();
        markerOptions.add(new MarkerOptions().title("Unit Kesihatan Klinik UiTM ")
                .position(new LatLng(6.44708468548699, 100.28006094017391))
                .snippet("9AM - 5PM"));
        markerOptions.add(new MarkerOptions().title("Kampung Gial Health Clinic ")
                .position(new LatLng(6.465416385595192, 100.27403387437762))
                .snippet("8AM - 5PM"));
        markerOptions.add(new MarkerOptions().title("Arau Health Clinic  ")
                .position(new LatLng(6.434067712236922, 100.26951854655158))
                .snippet("8AM - 5PM"));
        markerOptions.add(new MarkerOptions().title("Klinik Kamil Arif ")
                .position(new LatLng(6.428171540269451, 100.27350664348492))
                .snippet("9AM - 9.30PM"));
        markerOptions.add(new MarkerOptions().title("Naurah Clinic ")
                .position(new LatLng(6.435324264633094, 100.297435225085))
                .snippet("9AM - 6PM"));
        markerOptions.add(new MarkerOptions().title("Klinik Haji Adnan")
                .position(new LatLng(6.447159967948237, 100.23781005326747))
                .snippet("8.30AM - 5PM"));
        markerOptions.add(new MarkerOptions().title("Poliklinik Dr.Azhar dan Rakan-rakan Pauh")
                .position(new LatLng(6.437510562131281, 100.30461652200484))
                .snippet("9AM - 10PM"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add all markers to the map
        for (MarkerOptions mark : markerOptions) {
            mMap.addMarker(mark);
        }

        enableMyLocation();

        // Move camera to the center location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerlocation, 10));
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    200
            );
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location features
                if (mMap != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "Permission denied to access your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
