package com.newtest.test.test;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class TransactionInformation extends AppCompatActivity implements LocationListener {
    private final String KEY_NAME = "key";
    private TextView notification, initiatedText, completedText, memoText;

    protected User user;
    private Transaction tx, newTx;
    private Button confirmBtn;
    private Button denyBtn;

    private String acceptedFlag;

    protected String sourceKey;

    private LocationManager lm;
    private Location location;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private String locationPermission;
    private String fingerprintPermission;
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager fingerprintManager;
    private String errorString;
    private KeyguardManager keyguardManager;

    private TransactionUploader tu;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasaction_information);

        user = getIntent().getParcelableExtra("UserKey");
        tx = getIntent().getParcelableExtra("TxKey");
        sourceKey = getIntent().getExtras().getString("SourceKey");

        initiatedText = findViewById(R.id.initiated_actual_label);
        completedText = findViewById(R.id.accepted_actual_label);
        notification = findViewById(R.id.notification_message);
        memoText = findViewById(R.id.memo_actual_label);

        final String username = user.getUsername();

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ed = sp.edit();
        locationPermission = sp.getString("locationPermission", "");
        fingerprintPermission = sp.getString("useFingerprint", "");

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
                notification.setText("You requested " + tx.getFormattedAmount() + " from " + tx.getSender().getUsername());
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
            TextView tv = findViewById(R.id.accepted_label);
            if(!tx.isAccepted()) {
                tv.setText("Denied on: ");
            }
            completedText.setText(tx.getTransactionCompleteDateString());
        } else {
            TextView tv = findViewById(R.id.accepted_label);
            tv.setText("Unconfirmed: ");
            completedText.setText("Waiting for transaction to be confirmed by " + tx.getNotInitiator().getUsername());
        }

        //Confirm Button
        confirmBtn = findViewById(R.id.confirm_button);
        //Deny Button
        denyBtn = findViewById(R.id.deny_button);

        // Hide the confirm/deny buttons if user is the initiator of the transaction
        if (username.equals(tx.getInitiator().getUsername()) || !tx.inProgress()) {
            confirmBtn.setVisibility(View.INVISIBLE);
            denyBtn.setVisibility(View.INVISIBLE);
        }

        progressDialog = new ProgressDialog(TransactionInformation.this);
        progressDialog.setMessage("Updating transaction...");
        progressDialog.setCancelable(false);

        ////////// CONFIRM BUTTON //////////////
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.show();
                    }
                });
                acceptedFlag = "true";

                switch (fingerprintPermission) {
                    case "true":
                        generateFingerprintComponents();
                        break;
                    case "false":
                        authenticated();
                        break;
                    default:
                        authenticated();
                }


            }
        });

        /////////// DENY BUTTON /////////////
        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.show();
                    }
                });
                acceptedFlag = "false";

                switch (fingerprintPermission) {
                    case "true":
                        generateFingerprintComponents();
                        break;
                    case "false":
                        authenticated();
                        break;
                    default:
                        authenticated();
                }


            }
        });

        //Back Button
        final Button backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog(TransactionInformation.this);
                pd.setMessage("Retrieving account information...");
                pd.show();
                switch(sourceKey) {
                    case "Account":
                        Intent intent = new Intent(TransactionInformation.this, Account.class);
                        intent.putExtra("UserKey", user);
                        startActivity(intent);
                        finish();
                        break;
                    case "Full Transaction History":
                        intent = new Intent(TransactionInformation.this, FullTransactionHistory.class);
                        intent.putExtra("UserKey", user);
                        startActivity(intent);
                        finish();
                        break;
                    case "Notifications":
                        intent = new Intent(TransactionInformation.this, Notifications.class);
                        intent.putExtra("UserKey", user);
                        startActivity(intent);
                        finish();
                        break;
                    case "Map":
                        intent = new Intent(TransactionInformation.this, MapsActivity.class);
                        intent.putExtra("UserKey", user);
                        startActivity(intent);
                        finish();
                        break;
                }
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

    public void authenticated() {
        switch (acceptedFlag) {
            case "true":
                java.util.Date utilDate = tx.getTransactionStartDate();
                java.sql.Timestamp tempTimestamp = new java.sql.Timestamp(utilDate.getTime());

                newTx = new Transaction(tx.getTransactionID(), tx.getSender(), tx.getRecipient(), tx.getInitiator(),
                        tx.getTransactionAmount(), tx.getMemo(), tempTimestamp, new Timestamp(System.currentTimeMillis()), false, true);
                if (location != null) {
                    newTx.setCompletionLatitude(location.getLatitude());
                    newTx.setCompletionLongitude(location.getLongitude());
                } else {
                    newTx.setCompletionLongitude(0.0);
                    newTx.setCompletionLongitude(0.0);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tu = new TransactionUploader();
                            if (tu.upload(newTx)) {
                                //If sending party = user, recipient balance ^, user balance v
                                if(newTx.getSender().getUsername().equals(user.getUsername())) {
                                    tu.updateBalances(tx.getRecipient(), user, tx.getTransactionAmount());
                                //If recipient party = user, user balance ^, sending party v
                                } else if(newTx.getRecipient().getUsername().equals(user.getUsername())) {
                                    tu.updateBalances(user, tx.getSender(), tx.getTransactionAmount());
                                }
                            } else {
                                new AlertDialog.Builder(TransactionInformation.this)
                                        .setTitle("Error")
                                        .setMessage("There was an error updating this transaction.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                confirmBtn.setError("");
                                            }
                                        }).create().show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(TransactionInformation.this, Confirmation.class);
                        intent.putExtra("UserKey", user);
                        intent.putExtra("TxKey", newTx);

                        if(!tx.getInitiator().getUsername().equals(user.getUsername()) && tx.getSender().getUsername().equals(user.getUsername())) {
                            intent.putExtra("TxMessage", "send funds approved");
                        } else if(!tx.getInitiator().getUsername().equals(user.getUsername()) && tx.getRecipient().getUsername().equals(user.getUsername())) {
                            intent.putExtra("TxMessage", "receive funds approved");
                        }

                        progressDialog.cancel();
                        startActivity(intent);
                        finish();
                    }
                }).start();
                break;

            case "false":
                utilDate = tx.getTransactionStartDate();
                tempTimestamp = new java.sql.Timestamp(utilDate.getTime());

                newTx = new Transaction(tx.getTransactionID(), tx.getSender(), tx.getRecipient(), tx.getInitiator(),
                        tx.getTransactionAmount(), tx.getMemo(), tempTimestamp, new Timestamp(System.currentTimeMillis()), false, false);
                if (location != null) {
                    newTx.setCompletionLatitude(location.getLatitude());
                    newTx.setCompletionLongitude(location.getLongitude());
                } else {
                    newTx.setCompletionLongitude(0.0 + Math.random());
                    newTx.setCompletionLongitude(0.0 + Math.random());
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(new TransactionUploader().upload(newTx)) {
                                Intent intent = new Intent(TransactionInformation.this, Confirmation.class);
                                intent.putExtra("UserKey", user);
                                intent.putExtra("TxKey", newTx);

                                if(newTx.getSender().getUsername().equals(user.getUsername())) {
                                    intent.putExtra("TxMessage", "send funds denied");
                                } else if(newTx.getRecipient().getUsername().equals(user.getUsername())) {
                                    intent.putExtra("TxMessage", "receive funds denied");
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.cancel();
                                    }
                                });

                                startActivity(intent);
                                finish();
                            } else {
                                new AlertDialog.Builder(TransactionInformation.this)
                                        .setTitle("Error")
                                        .setMessage("There was an error updating this transaction.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                denyBtn.setError("");
                                            }
                                        }).create().show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    public void generateFingerprintComponents() {
        //Verify that the device is running Marshmallow (SDK 23) or higher before executing any fingerprint-related code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            //Verify all fingerprint prerequistes are met
            if(checkFingerprintPrerequisites()) {
                try {
                    generateKey();
                } catch (FingerprintSettings.FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    // Show the fingerprint dialog. The user has the option to use the fingerprint with
                    // crypto, or you can fall back to using a server-side verified password.
                    FingerprintAuthenticationDialogFragment fragment
                            = new FingerprintAuthenticationDialogFragment(this);
                    fragment.setCryptoObject(new FingerprintManager.CryptoObject(cipher));
                    boolean useFingerprintPreference = true;
                    if (useFingerprintPreference) {
                        fragment.setStage(
                                FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                    } else {
                        fragment.setStage(
                                FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
                    }
                    fragment.show(getFragmentManager(), "");
                } else {
                    // This happens if the lock screen has been disabled or or a fingerprint got
                    // enrolled. Thus show the dialog to authenticate with their password first
                    // and ask the user if they want to authenticate with fingerprints in the
                    // future
                    FingerprintAuthenticationDialogFragment fragment
                            = new FingerprintAuthenticationDialogFragment(this);
                    fragment.setCryptoObject(new FingerprintManager.CryptoObject(cipher));
                    fragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
                    fragment.show(getFragmentManager(), "");
                }
            } else {
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Fingerprint Error")
                        .setMessage(errorString)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).create().show();
            }
        }
    }

    private Boolean checkFingerprintPrerequisites() {
        //Checks for a fingerprint sensor on the device. If no sensor exists, inform user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.isHardwareDetected()) {
                errorString = "Your device doesn't support fingerprint authentication";
                return false;
            }
        }
        //Checks if fingerprint permission is given to app. If not, inform user to turn it on
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            // If app doesn't have fingerprint scanner permission, inform user
            errorString = "Please enable fingerprint permission in your device's settings.";
            return false;
        }

        //Check that the user has registered at least one fingerprint. If not, inform them that they
        // need to register a fingerprint before continuing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                errorString = "No fingerprint configured. Please register at least one fingerprint in your device's settings";
                return false;
            }
        }

        return true;
    }

    //Create the generateKey method that we’ll use to gain access to the Android keystore and generate the encryption key//
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintSettings.FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintSettings.FingerprintException(exc);
        }
    }

    //Cipher initialization
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            //Return true if the cipher has been initialized successfully
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            //Return false if cipher initialization failed
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onBackPressed() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Retrieving account information...");
        pd.show();
        switch(sourceKey) {
            case "Account":
                Intent intent = new Intent(TransactionInformation.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
                finish();
                break;
            case "Full Transaction History":
                intent = new Intent(TransactionInformation.this, FullTransactionHistory.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
                finish();
                break;
            case "Notifications":
                intent = new Intent(TransactionInformation.this, Notifications.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
                finish();
                break;
            case "Map":
                intent = new Intent(TransactionInformation.this, MapsActivity.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
                finish();
                break;
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
