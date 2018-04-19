package com.newtest.test.test;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerprintSettings extends AppCompatActivity {
    User user;

    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private SharedPreferences sp;
    private String errorString;
    private String fingerprintPreference;

    boolean permissionFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textView = findViewById(R.id.text_fingerprint_instructions);

        user = getIntent().getParcelableExtra("UserKey");

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        fingerprintPreference = sp.getString("useFingerprint", "");

        switch (fingerprintPreference) {
            case "true":
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Fingerprint Authentication")
                        .setMessage("Continue to use your fingerprint for verification?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                permissionFlag = true;
                                authenticated();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                permissionFlag = false;
                                generateFingerprintComponents();
                            }
                        }).create().show();
                break;
            case "false":
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Fingerprint Authentication")
                        .setMessage("Use your fingerprint to verify transactions and to login to the app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                permissionFlag = true;
                                generateFingerprintComponents();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                permissionFlag = false;
                                authenticated();
                            }
                        }).create().show();
                break;
            default:
                new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("Fingerprint Authentication")
                            .setMessage("Use your fingerprint to verify transactions and to login to the app!")
                            .setPositiveButton("Sure!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    permissionFlag = true;
                                    generateFingerprintComponents();
                                }
                            })
                            .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    permissionFlag = false;
                                    authenticated();
                                }
                            }).create().show();
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
                } catch (FingerprintException e) {
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
    private void generateKey() throws FingerprintException {
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
            throw new FingerprintException(exc);
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

    public void authenticated() {
        SharedPreferences.Editor ed = sp.edit();

        if(permissionFlag) {
            ed.putString("useFingerprint", "true");
            ed.apply();
        } else if(!permissionFlag){
            ed.putString("useFingerprint", "false");
            ed.apply();
        }

        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("UserKey", user);
        startActivity(intent);
        finish();
    }

    public static class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("UserKey", user);
        startActivity(intent);
        finish();
    }
}