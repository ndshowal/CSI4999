package com.newtest.test.test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class FullTransactionHistory extends AppCompatActivity implements LocationListener {

    private User user;
    private String searchKey;
    private EditText searchText;
    int flag;

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private String locationPermission;

    LocationManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_transaction_history);

        //Hide the keyboard when activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        user = getIntent().getParcelableExtra("UserKey");
        searchKey = getIntent().getExtras().getString("SearchKey");

        //Variable for updating UI
        flag = 0;

        //Thread for updating user's transaction list
        new Thread(new Runnable() {
            @Override
            public void run() {
                AsyncTask tp = new TransactionPuller(user, new TransactionPuller.AsyncResponse() {
                    @Override
                    public void processFinished(String output) {}}).execute();
            }
        }).start();

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ed = sp.edit();
        locationPermission = sp.getString("locationPermission", "");

        if (ActivityCompat.checkSelfPermission(FullTransactionHistory.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(FullTransactionHistory.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
        } else {
            ed.putString("locationPermission", "granted");
            ed.apply();
        }

        updateUI();
    }

    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Only runs once (when activity is started)
                if (flag == 0) {
                    final Button searchBtn = findViewById(R.id.search_button);
                    searchBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Hides the keyboard on click (for aesthetics)
                            InputMethodManager inputMethodManager = (InputMethodManager) FullTransactionHistory.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            try {
                                inputMethodManager.hideSoftInputFromWindow(FullTransactionHistory.this.getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception ex) {}
                            searchText = findViewById(R.id.search_textbox);
                            sortByUser(searchText.getText().toString());
                        }
                    });

                    sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    locationPermission = sp.getString("locationPermission", "");

                    System.out.println(locationPermission.toUpperCase());

                    //Transaction Map Button
                    Button mapBtn = findViewById(R.id.transaction_map_button);

                    switch(locationPermission) {
                        case "granted":
                            mapBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(FullTransactionHistory.this, MapsActivity.class);
                                    intent.putExtra("UserKey", user);
                                    startActivity(intent);

                                }
                            });
                            break;

                        case "denied":
                            System.out.println("Location permission denied");
                            mapBtn.setVisibility(View.GONE);
                            break;

                        default:
                            System.out.println("Location permission not set");
                            mapBtn.setVisibility(View.GONE);
                    }

                    System.out.println("Transactions in user list: " + user.getTransactions().size());

                    //Create a back button
                    Button backBtn = findViewById(R.id.back_button);
                    backBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProgressDialog pd = new ProgressDialog(FullTransactionHistory.this);
                            pd.setMessage("Retrieving account information...");
                            pd.show();
                            Intent intent = new Intent(FullTransactionHistory.this, Account.class);
                            intent.putExtra("UserKey", user);
                            startActivity(intent);
                            finish();
                        }
                    });

                    //Adds a button entry for each transaction to the scroll view on the Account page, or informs user they don't have any yet
                    if (user.getTransactions().size() == 0) {
                        LinearLayout ll = findViewById(R.id.button_layout);
                        ll.removeAllViews();

                        TextView noTransactionsMessage = new TextView(FullTransactionHistory.this);
                        noTransactionsMessage.setText("You have no transactions yet! Tap 'New Transaction' to get started, or wait for someone to begin one with you!");

                        //Define the layout and add the message to it
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, 10, 0, 10);
                        ll.addView(noTransactionsMessage, lp);
                    } else {
                        //For each transaction, create a button and add it to the defined layout
                        LinearLayout ll = findViewById(R.id.button_layout);
                        ll.removeAllViews();

                        int count = 0;

                        for (final Transaction tx : user.getTransactions()) {
                            count++;

                            Button transactionInfoBtn = new Button(FullTransactionHistory.this);
                            transactionInfoBtn.setText(tx.getSimpleDescription());

                            if(count % 2 == 0) {
                                transactionInfoBtn.setBackgroundResource(R.drawable.button_alt);
                            }

                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            lp.setMargins(0, 10, 0, 10);
                            ll.addView(transactionInfoBtn, lp);

                            transactionInfoBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(FullTransactionHistory.this, TransactionInformation.class);
                                    intent.putExtra("UserKey", user);
                                    intent.putExtra("TxKey", tx);
                                    intent.putExtra("SourceKey", "Full Transaction History");
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }

                    //Called if user searches for a username
                } else if (flag == 1) {
                    LinearLayout ll = findViewById(R.id.button_layout);
                    ll.removeAllViews();

                    int count = 0;

                    for (final Transaction tx : user.getTransactions(searchKey)) {
                        count++;

                        Button transactionInfoBtn = new Button(FullTransactionHistory.this);
                        transactionInfoBtn.setText(tx.getSimpleDescription());

                        if(count % 2 == 0) {
                            transactionInfoBtn.setBackgroundResource(R.drawable.button_alt);
                        }

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, 10, 0, 10);
                        ll.addView(transactionInfoBtn, lp);

                        transactionInfoBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FullTransactionHistory.this, TransactionInformation.class);
                                intent.putExtra("UserKey", user);
                                intent.putExtra("TxKey", tx);
                                startActivity(intent);
                            }
                        });
                    }
                    flag = 0;
                }
            }
        });
    }

    //Search for a username, if username exists, reload the page and pass in that username (used to sort the table)
    // If username doesn't exist, let the user know via error dialog in the search field
    public void sortByUser(final String in) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (searchText.getText().toString().equals("")) {
                        searchKey = in;
                        flag = 1;
                        updateUI();
                    } else if (new UserChecker().usernameExists(in)) {
                        searchKey = in;
                        flag = 1;
                        updateUI();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchText.setError("Username doesn't exist/is incorrect");
                                updateUI();
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                                ActivityCompat.requestPermissions(FullTransactionHistory.this,
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
    public void onBackPressed() {
        ProgressDialog pd = new ProgressDialog(FullTransactionHistory.this);
        pd.setMessage("Retrieving account information...");
        pd.show();
        Intent intent = new Intent(FullTransactionHistory.this, Account.class);
        intent.putExtra("UserKey", user);
        startActivity(intent);
        finish();
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