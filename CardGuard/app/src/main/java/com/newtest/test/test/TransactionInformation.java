package com.newtest.test.test;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionInformation extends AppCompatActivity implements LocationListener {
    TextView notification, initiatedText, completedText, memoText;

    User user;
    Transaction tx;
    Button confirmBtn;
    Button denyBtn;

    LocationManager lm;
    Location location;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    String locationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasaction_information);

        user = getIntent().getParcelableExtra("UserKey");
        tx = getIntent().getParcelableExtra("TxKey");
        initiatedText = findViewById(R.id.initiated_actual_label);
        completedText = findViewById(R.id.accepted_actual_label);
        notification = findViewById(R.id.notification_message);
        memoText = findViewById(R.id.memo_actual_label);

        final String username = user.getUsername();

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ed = sp.edit();
        locationPermission = sp.getString("locationPermission", "");

        if (ActivityCompat.checkSelfPermission(TransactionInformation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(TransactionInformation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
        } else {
            ed.putString("locationPermission", "granted");
            ed.apply();
        }



        switch(locationPermission) {
            case "granted":
                lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (lm != null) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else {
                    location.setLatitude(0.0);
                    location.setLongitude(0.0);
                }
                break;
            case "denied":
                location.setLatitude(0.0);
                location.setLongitude(0.0);
                break;
        }

        //Set message text
        //If user is the initiator of the transaction...
        if (username.equals(tx.getInitiator().getUsername())) {
            //If user is the sender
            if (username.equals(tx.getRecipient().getUsername())) {
                notification.setText("Your request from " + tx.getSender().getUsername() + " for " + tx.getFormattedAmount());
                //If user is the recipient
            } else if (username.equals(tx.getSender().getUsername())) {
                notification.setText("You sent " + tx.getFormattedAmount() + " to " + tx.getRecipient().getUsername());
            }
            //If user did not initiate the transaction...
        } else {
            //If user is the recipient of the transaction
            if (username.equals((tx.getRecipient().getUsername()))) {
                notification.setText("You received " + tx.getFormattedAmount() + " from " + tx.getSender().getUsername());
                //If user is the sender
            } else if (username.equals(tx.getSender().getUsername())) {
                notification.setText(tx.getRecipient().getUsername() + " requested " + tx.getFormattedAmount() + " from you");
            }
        }

        initiatedText.setText(tx.getTransactionStartDateString());
        completedText.setText(tx.getTransactionCompleteDateString());
        memoText.setText(tx.getMemo());

        if (tx.getTransactionCompleteDate() != null) {
            completedText.setText(tx.getTransactionCompleteDateString());
        } else {
            completedText.setText(R.string.waiting_for_transaction);
        }

        //Confirm Button
        confirmBtn = findViewById(R.id.confirm_button);
        //Deny Button
        denyBtn = findViewById(R.id.deny_button);

        // Hide the confirm/deny buttons if user is the initiator of the transaction
        if (username.equals(tx.getInitiator().getUsername()) || tx.isCompleted()) {
            confirmBtn.setVisibility(View.INVISIBLE);
            denyBtn.setVisibility(View.INVISIBLE);
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Date utilDate = tx.getTransactionStartDate();
                java.sql.Timestamp tempTimestamp = new java.sql.Timestamp(utilDate.getTime());

                final Transaction newTx = new Transaction(tx.getTransactionID(), tx.getSender(), tx.getRecipient(), tx.getInitiator(),
                        tx.getTransactionAmount(), tx.getMemo(), tempTimestamp, new Timestamp(System.currentTimeMillis()), false, true);
                if (location != null) {
                    newTx.setCompletionLongitude(0.0);
                    newTx.setCompletionLongitude(0.0);
                } else {
                    newTx.setInitialLatitude(0.0);
                    newTx.setInitialLongitude(0.0);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TransactionUploader tu = null;
                        try {
                            tu = new TransactionUploader();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (tu.upload(newTx)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TransactionInformation.this, "Updating transaction...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(TransactionInformation.this, Confirmation.class);
                        intent.putExtra("UserKey", user);
                        intent.putExtra("TxKey", newTx);

                        //If sending party = user, recipient balance ^, user balance v
                        if(newTx.getSender().getUsername().equals(user.getUsername())) {
                            tu.updateBalances(tx.getRecipient(), user, tx.getTransactionAmount());
                        //If recipient party = user, user balance ^, sending party v
                        } else if(newTx.getRecipient().getUsername().equals(user.getUsername())) {
                            tu.updateBalances(user, tx.getSender(), tx.getTransactionAmount());
                        }

                        if(!tx.getInitiator().getUsername().equals(user.getUsername()) && tx.getSender().getUsername().equals(user.getUsername())) {
                            intent.putExtra("TxMessage", "send funds approved");
                        } else if(!tx.getInitiator().getUsername().equals(user.getUsername()) && tx.getRecipient().getUsername().equals(user.getUsername())) {
                            intent.putExtra("TxMessage", "receive funds approved");
                        }

                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });

        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Date utilDate = tx.getTransactionStartDate();
                java.sql.Timestamp tempTimestamp = new java.sql.Timestamp(utilDate.getTime());

                final Transaction newTx = new Transaction(tx.getTransactionID(), tx.getSender(), tx.getRecipient(), tx.getInitiator(),
                        tx.getTransactionAmount(), tx.getMemo(), tempTimestamp, new Timestamp(System.currentTimeMillis()), false, false);
                if (location != null) {
                    newTx.setCompletionLongitude(0.0);
                    newTx.setCompletionLongitude(0.0);
                } else {
                    newTx.setInitialLatitude(0.0);
                    newTx.setInitialLongitude(0.0);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(new TransactionUploader().upload(newTx)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TransactionInformation.this, "Updating transaction...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(TransactionInformation.this, Confirmation.class);
                        intent.putExtra("UserKey", user);
                        intent.putExtra("TxKey", newTx);

                        if(newTx.getSender().getUsername().equals(user.getUsername())) {
                            intent.putExtra("TxMessage", "send funds denied");
                        } else if(newTx.getRecipient().getUsername().equals(user.getUsername())) {
                            intent.putExtra("TxMessage", "receive funds denied");
                        }

                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });

        //Back Button
        Button backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(TransactionInformation.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(TransactionInformation.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }).create().show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ed = sp.edit();
                    ed.putString("locationPermission", "granted");
                    ed.apply();

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        //Request location updates:
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
                    }
                } else {
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("locationPermission", "denied");
                    ed.apply();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            lm.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
