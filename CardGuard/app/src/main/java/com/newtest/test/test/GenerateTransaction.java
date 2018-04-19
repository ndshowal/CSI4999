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
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

public class GenerateTransaction extends AppCompatActivity implements LocationListener {

    protected User user;
    private Transaction tx;
    private String transactionType;
    private String targetUser;
    private String locationPermission;
    private String fingerprintPermission;
    private EditText usernameInput, amountInput, memoInput;
    private TextView headerText;

    private SharedPreferences sp;
    SharedPreferences.Editor ed;

    private UserChecker uc;

    LocationManager lm;
    Location location;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private Cipher cipher;
    private String errorString;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private final String KEY_NAME = "key";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_transaction);

        user = getIntent().getParcelableExtra("UserKey");
        transactionType = getIntent().getExtras().getString("TypeKey");
        targetUser = getIntent().getExtras().getString("TargetKey");
        location = null;

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ed = sp.edit();
        locationPermission = sp.getString("locationPermission", "");
        fingerprintPermission = sp.getString("useFingerprint", "");

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

        if(targetUser != null) {
            usernameInput.setText(targetUser);
            usernameInput.setEnabled(false);
        }

        progressDialog = new ProgressDialog(GenerateTransaction.this);
        progressDialog.setMessage("Uploading your transaction...");
        progressDialog.setCancelable(false);

        //Create Send/Request button and set header text
        headerText = findViewById(R.id.header);
        Button sendBtn = findViewById(R.id.send_button);
        if (transactionType.equals("Send")) {
            if(targetUser != null) {
                headerText.setText("Send Funds to " + targetUser);
            } else {
                headerText.setText("Send Funds");
            }
            sendBtn.setText("Send");
        } else if (transactionType.equals("Request")) {
            if(targetUser != null) {
                headerText.setText("Request Funds from " + targetUser);
            } else {
                headerText.setText("Request Funds");
            }
            sendBtn.setText("Request");
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (valid()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.show();
                                }
                            });
                            User target = null;
                            try {
                                target = new GetUser(usernameInput.getText().toString()).getUser();

                                tx = null;

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



                                    switch(fingerprintPermission){
                                        case "true":
                                            generateFingerprintComponents();
                                            break;
                                        case "false":
                                            authenticated();
                                            break;
                                        default:
                                            authenticated();
                                            break;
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

                                    switch(fingerprintPermission){
                                        case "true":
                                            generateFingerprintComponents();
                                            break;
                                        case "false":
                                            authenticated();
                                            break;
                                        default:
                                            authenticated();
                                            break;
                                    }
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
        }

        //If user attempts to send money to themselves
        if(usernameInput.getText().toString().equals(user.getUsername())) {
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

        if(Float.parseFloat(amountInput.getText().toString()) == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    amountInput.setError("Transaction value cannot be zero!");
                }
            });
            valid = false;
        }

        return valid;
    }

    public void authenticated() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (tx != null && new TransactionUploader().upload(tx)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                            }
                        });

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
        }).start();
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
