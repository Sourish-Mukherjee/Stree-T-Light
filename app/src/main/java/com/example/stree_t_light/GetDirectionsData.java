package com.example.stree_t_light;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;


public class GetDirectionsData extends AsyncTask<Object,String,String> {

    private GoogleMap mMap;
    private String name;
    private String url;
    private String googleDirectionsData;
    private String duration,distance;
    LatLng latLng;

    public GetDirectionsData(String name) {
        this.name = name;
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        latLng = (LatLng)objects[2];
        DownloadURL downloadURL = new DownloadURL();
        try {
            googleDirectionsData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        HashMap<String,String> directonList = null;
        DataParser dataParser = new DataParser();
        directonList = dataParser.parseDirections(s);
        duration=directonList.get("duration");
        distance=directonList.get("distance");
        GetNearbyPlacesData.dist="";
        for (int i = 0; i < distance.length(); i++) {
            if (Character.isDigit(distance.charAt(i)) || distance.charAt(i) == '.')
                GetNearbyPlacesData.dist=GetNearbyPlacesData.dist.concat(""+distance.charAt(i));
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        markerOptions.position(latLng);
        markerOptions.draggable(false);
        markerOptions.title(name);
        markerOptions.snippet("Duration = "+duration);
        mMap.addMarker(markerOptions);
    }

}
