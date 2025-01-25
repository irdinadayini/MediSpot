package com.example.mapnew;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 1001;
    private Toolbar myToolbar;
    private TextView greetTv;
    private FirebaseAuth auth;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Authentication and FusedLocationProviderClient
        auth = FirebaseAuth.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize Views
        myToolbar = findViewById(R.id.my_toolbar);
        greetTv = findViewById(R.id.greetTv);

        // Set Toolbar
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MediSpot");
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // Show User Info
        showUserInfo();

        // Get User Location
        getLocation();
    }

    private void showUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            if (name != null && !name.isEmpty()) {
                greetTv.setText("Welcome, " + name + "!\nFetching your location...");
            } else {
                greetTv.setText("Welcome, " + email + "!\nFetching your location...");
            }
        } else {
            greetTv.setText("Welcome, Guest!\nFetching your location...");
        }
    }

    private void getLocation() {
        // Check if location services are enabled
        if (!isLocationEnabled()) {
            greetTv.setText("Please enable location services.");
            return;
        }

        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
            return;
        }

        // Use location updates for accurate results
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // 10-second interval
                .setFastestInterval(5000); // 5 seconds minimum

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    greetTv.setText("Unable to fetch location.");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        updateGreetingWithLocation(location.getLatitude(), location.getLongitude());
                    }
                }
            }
        };

        // Check permissions before requesting updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private void updateGreetingWithLocation(double latitude, double longitude) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            String greeting;
            if (name != null && !name.isEmpty()) {
                greeting = "Welcome, " + name + "!\nYour location:\nLatitude: " + latitude + "\nLongitude: " + longitude;
            } else {
                greeting = "Welcome, " + email + "!\nYour location:\nLatitude: " + latitude + "\nLongitude: " + longitude;
            }
            greetTv.setText(greeting);
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                getLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission is required to fetch your location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Item_About) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        } else if (item.getItemId() == R.id.Item_Map) {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            startActivity(mapIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String s) {
    }
}
