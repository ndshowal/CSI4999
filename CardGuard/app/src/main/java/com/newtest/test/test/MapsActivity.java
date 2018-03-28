package com.newtest.test.test;

/**
 * Created by John on 3/26/2018.
 */
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private SupportMapFragment mapFragment;


    @Override

    public void onMapReady(GoogleMap googleMap) {

        // Add a marker and move the camera (i.e. the center of the map).

        LatLng offerLocation = new LatLng(33.7490, -84.3880);

        googleMap.addMarker(new MarkerOptions().position(offerLocation).title("Location"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(offerLocation));

    }

}