package com.newtest.test.test;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        user = getIntent().getParcelableExtra("UserKey");
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

        //Add a marker in Rochester Hills and move the camera
        LatLng rochesterHills = new LatLng(42.677587, -83.219336);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rochesterHills));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        String markerMessage = "";



        //Adds a map marker for the transaction coordinates
        for(Transaction tx : user.getTransactions()) {
            if (user.getUsername().equals(tx.getInitiator().getUsername())) {
                //If user is the sender
                if (user.getUsername().equals(tx.getRecipient().getUsername())) {
                    markerMessage = "You requested from " + tx.getSender().getUsername() + " for " + tx.getFormattedAmount();
                    //If user is the recipient
                } else if (user.getUsername().equals(tx.getSender().getUsername())) {
                    markerMessage = "You sent " + tx.getFormattedAmount() + " to " + tx.getRecipient().getUsername();
                }
                //If user did not initiate the transaction...
            } else {
                //If user is the recipient of the transaction
                if (user.getUsername().equals((tx.getRecipient().getUsername()))) {
                    markerMessage = "You received " + tx.getFormattedAmount() + " from " + tx.getSender().getUsername();
                    //If user is the sender
                } else if(user.getUsername().equals(tx.getSender().getUsername())) {
                    markerMessage = tx.getSender().getUsername() + " requested " + tx.getFormattedAmount() + " from you";
                }
            }

            LatLng txInitialLocation = new LatLng(tx.getInitialLatitude(), tx.getInitialLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(txInitialLocation)
                    .title(markerMessage)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            if(!tx.inProgress()) {
                LatLng txCompletedLocation = new LatLng(tx.getCompletionLatitude(), tx.getCompletionLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(txCompletedLocation)
                        .title(markerMessage)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }
}
