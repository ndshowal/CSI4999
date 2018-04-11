package com.newtest.test.test;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Timestamp;

public class GenerateTransaction extends AppCompatActivity implements LocationListener {

    private User user;
    private String transactionType;
    private String locationPermission;
    private EditText usernameInput, amountInput, memoInput;
    private TextView headerText;

    private SharedPreferences sp;
    SharedPreferences.Editor ed;

    private UserChecker uc;

    LocationManager lm;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_transaction);

        user = getIntent().getParcelableExtra("UserKey");
        transactionType = getIntent().getExtras().getString("TypeKey");

        location = null;

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ed = sp.edit();
        locationPermission = sp.getString("locationPermission", "");

        if (ActivityCompat.checkSelfPermission(GenerateTransaction.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(GenerateTransaction.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        usernameInput = findViewById(R.id.username_textbox);
        amountInput = findViewById(R.id.amount_textbox);
        memoInput = findViewById(R.id.memo_textbox);

        //Create Send/Request button and set header text
        headerText = findViewById(R.id.header);
        Button sendBtn = (Button) findViewById(R.id.send_button);
        if (transactionType.equals("Send")) {
            headerText.setText("Send Funds");
            sendBtn.setText("Send");
        } else if (transactionType.equals("Request")) {
            headerText.setText("Request Funds");
            sendBtn.setText("Request");
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (valid()) {
                            User target = null;
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GenerateTransaction.this, "Uploading your transaction...", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                target = new GetUser(usernameInput.getText().toString()).getUser();
                                System.out.println(target.getUserHash());

                                Transaction tx = null;

                                //If user is sending funds
                                if (transactionType.equals("Send")) {
                                    tx = new Transaction(user, target, user,
                                            Float.parseFloat(amountInput.getText().toString()), memoInput.getText().toString(),
                                            new Timestamp(System.currentTimeMillis()), null, true, false);
                                    if (location != null) {
                                        tx.setInitialLatitude(location.getLatitude());
                                        tx.setInitialLongitude(location.getLongitude());
                                    } else {
                                        tx.setInitialLatitude(0.0);
                                        tx.setInitialLongitude(0.0);
                                    }
                                    //If user is requesting funds
                                } else if (transactionType.equals("Request")) {
                                    tx = new Transaction(target, user, user,
                                            Float.parseFloat(amountInput.getText().toString()), memoInput.getText().toString(),
                                            new Timestamp(System.currentTimeMillis()), null, true, false);
                                    if (location != null) {
                                        tx.setInitialLatitude(location.getLatitude());
                                        tx.setInitialLongitude(location.getLongitude());
                                    } else {
                                        tx.setInitialLatitude(0.0);
                                        tx.setInitialLongitude(0.0);
                                    }
                                }

                                if (tx != null && new TransactionUploader().upload(tx)) {
                                    Intent intent = new Intent(GenerateTransaction.this, Confirmation.class);
                                    intent.putExtra("UserKey", user);
                                    intent.putExtra("TxKey", tx);
                                    switch (transactionType) {
                                        case "Send":
                                            intent.putExtra("TxMessage", "sent funds");
                                            break;
                                        case "Request":
                                            intent.putExtra("TxMessage", "requested funds");
                                            break;
                                    }

                                    startActivity(intent);
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(GenerateTransaction.this, "Transaction failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        //to create a cancel button
        Button cancelBtn = (Button) findViewById(R.id.cancel_button);
        cancelBtn.setTextSize(24);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
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
                                ActivityCompat.requestPermissions(GenerateTransaction.this,
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

    //Check if the input dollar value is valid (not too many decimals)
    public Boolean valid() {
        boolean valid = true;

        try {
            //Check if username exists in user table. Prompts user if not.
            if (!new UserChecker().usernameExists(usernameInput.getText().toString())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        usernameInput.setError("Username doesn't exist/is incorrect.");
                    }
                });
                valid = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //If username field is left blank, prompt user to enter something
        if(usernameInput == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    usernameInput.setError("Please input a username");
                }
            });
            valid = false;
        } else if(usernameInput.equals(user.getUsername())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    usernameInput.setError("You can't begin a transaction with yourself!");
                }
            });
            valid = false;
        }

        //Checks if the user enters a valid dollar ammount into the input field (<x...x>.xx)
        // If not, prompts the user to enter a valid amount
        boolean start = false;
        int count = 0;
        for (Character c : amountInput.getText().toString().toCharArray()) {
            if (start) {
                count++;
            }
            if (c.equals('.')) {
                start = true;
            }
        }

        if (count > 2 || count < 2 || amountInput == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    amountInput.setError("Please input a valid dollar amount (with decimals.");
                }
            });
            valid = false;
        }

        if((new UserChecker().getBalance(user) < Float.valueOf(amountInput.getText().toString()))
                && transactionType.equals("Send")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    amountInput.setError("Transaction amount will overdraw your account!");
                }
            });
            valid = false;
        }

        return valid;
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
