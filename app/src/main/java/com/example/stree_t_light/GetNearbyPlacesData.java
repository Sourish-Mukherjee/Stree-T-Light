package com.example.stree_t_light;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    public static String name = "";
    static int minDist = Integer.MAX_VALUE;
    public static String dist = "5";
    private GoogleMap mMap;
    private String url;
    private MapsActivity mapsActivity = new MapsActivity();

    @Override
    protected String doInBackground(Object... objects) {

        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList = null;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {
        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
            String placeName = googlePlace.get("place_name");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            GetDirectionsData getDirectionsData = new GetDirectionsData(placeName);
            Object dt[] = new Object[3];
            dt[0]=mMap;
            dt[1]=mapsActivity.getUUURRLL(String.valueOf(lat),String.valueOf(lng));
            dt[2]=new LatLng(lat,lng);
            getDirectionsData.execute(dt);
            if(Integer.valueOf(dist)<minDist)
                minDist=Integer.valueOf(dist);
        }

    }
}
