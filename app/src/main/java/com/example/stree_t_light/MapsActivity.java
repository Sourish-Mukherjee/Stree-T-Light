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
    TextView currlocationtV;
    int PROXIMITY_RADIUS = 10000;

    public String getUUURRLL(String endlat,String endlong) {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+12.9675+","+77.7141);
        googleDirectionsUrl.append("&destination="+endlat+","+endlong);
        googleDirectionsUrl.append("&key="+"AIzaSyBMoWc6AjidjeA7BzdFcOQR_vc2tgXrajA");
        return googleDirectionsUrl.toString();

    }

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
        String police = "police";
        String url = getUrl(currentLocation.getLatitude(), currentLocation.getLongitude(), police);
        Object dt[] = new Object[3];
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(getApplicationContext(), "NearByPolice Stations", Toast.LENGTH_SHORT).show();
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
                currlocationtV.setText("Waiting for Location");
            } else {
                if (addresses.size() > 0) {
                    currlocationtV.setText("Current Location : " + addresses.get(0).getFeatureName() + "," + addresses.get(0).getSubLocality() + ", " +
                            addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + "," + addresses.get(0).getPostalCode());
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    private String getUrl(double latitutde, double longitutde, String nearbyPlace) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitutde + "," + longitutde);
        googlePlaceUrl.append("&radius=" + 3000);
        googlePlaceUrl.append("&types=" + "police");
        googlePlaceUrl.append("&key=" + "AIzaSyBMoWc6AjidjeA7BzdFcOQR_vc2tgXrajA");
        return googlePlaceUrl.toString();

    }

}
