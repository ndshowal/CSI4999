package com.newtest.test.test;

import android.Manifest;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Parcelable;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SignIn extends AppCompatActivity {
    private static final String TAG = "LoginPage";
    private static final String KEY_NAME = "yourKey";

    EditText usernameInput;
    EditText passwordInput;
    Button signInBtn;

    SignInConnection connection;

    User user;

    SharedPreferences sp;
    private String loginWithFingerprint;
    private String storedUsername;
    private String storedPassword;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private String errorString;
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signin);

        //Access username and password in SharedPreferences file called 'userInfo"
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        storedUsername = sp.getString("username", "");
        storedPassword = sp.getString("password", "");
        loginWithFingerprint = sp.getString("useFingerprint", "");

        System.out.println(storedUsername);
        System.out.println(storedPassword);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing you in...");
        progressDialog.setCancelable(false);

        //If username and password are not stored in SharedPreferences, login by entering credentials
        // else, username and password are found, automatically login
        if (!storedUsername.isEmpty() && !storedPassword.isEmpty()) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~ Credentials detected ~~~~~~~~~~~~~~~~~~~~");

            progressDialog.show();

            if(storedUsername != null) {
                usernameInput.setText(storedUsername);
            }

            loginWithPrefs(storedUsername, storedPassword);
        } else {
            generateUI();
        }
    }

     public void generateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Instantiate signInBtn
                signInBtn = findViewById(R.id.signin_button);
                signInBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        login();
                    }
                });
            }
        });
     }

    protected void login() {
        Log.d(TAG, "Login");
        if(validate()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection = new SignInConnection(usernameInput.getText().toString(), passwordInput.getText().toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        user = connection.connect();
                        if(user != null) {
                            if(user.getUserHash() == null) {
                                System.out.println("Setting user hash, no hash found");
                                user.setUserHash();
                            }

                            SharedPreferences.Editor ed = sp.edit();
                            ed.putString("username", usernameInput.getText().toString());
                            ed.putString("password", passwordInput.getText().toString());
                            ed.apply();

                            switch (loginWithFingerprint) {
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
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if(new UserChecker().usernameExists(usernameInput.getText().toString())) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    passwordInput.setError("Incorrect password, please try again.");
                                                    progressDialog.cancel();
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    usernameInput.setError("Username not found, please try again.");
                                                    progressDialog.cancel();
                                                }
                                            });
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //If app detects that login credentials are stored, automatically login with those
    protected void loginWithPrefs(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection = new SignInConnection(username, password);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try{
                    user = connection.connect();
                    if(user == null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                generateUI();
                                passwordInput.setError("Last known password incorrect, please re-enter password.");
                            }
                        });
                    } else {
                        switch (loginWithFingerprint) {
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
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    //Checks to make sure that the username and password fields have something in them
    public boolean validate() {
        boolean valid = true;

        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (username.isEmpty()) {
            usernameInput.setError("Please enter your username");
            valid = false;
        } else {
            usernameInput.setError(null);
        }

        if (password.isEmpty()) {
            passwordInput.setError("Please enter your password");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        return valid;
    }

    public void stopProgressDialog() {
        progressDialog.cancel();
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

    public void authenticated() {
        stopProgressDialog();
        Intent intent = new Intent(SignIn.this, Account.class);
        intent.putExtra("UserKey", user);
        intent.putExtra("SourceKey", "SignIn");

        startActivity(intent);
        finish();
    }
}
