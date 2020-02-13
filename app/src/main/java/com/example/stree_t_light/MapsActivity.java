package com.example.stree_t_light;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView currlocationtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        currlocationtV = findViewById(R.id.currlocation_tv);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

    }

    private void fetchLastLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                   // Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_LONG).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(true);
        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            if (addresses.isEmpty()) {
                currlocationtV.setText( "Waiting for Location");
            } else {
                if (addresses.size() > 0) {
                    currlocationtV.setText("Current Location : "+addresses.get(0).getFeatureName()+","+addresses.get(0).getSubLocality() + ", " +
                            addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea()+","+addresses.get(0).getPostalCode());
            }
            }
        } catch (Exception e) {
            return;
        }
    }
}
