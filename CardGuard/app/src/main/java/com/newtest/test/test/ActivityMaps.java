package com.newtest.test.test;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private User user;
    private ArrayList<Transaction> txlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //user = getIntent().getParcelableExtra("UserKey");
        //txlist = user.getTransactions();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // for(Transaction tx : txlist){
       //     LatLng loc = new LatLng }
       // }

        //Add a marker in Rochester Hills and move the camera
        LatLng rochesterHills = new LatLng(42.677587, -83.219336);
        mMap.addMarker(new MarkerOptions().position(rochesterHills).title("Marker in Rochester Hills"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rochesterHills));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}