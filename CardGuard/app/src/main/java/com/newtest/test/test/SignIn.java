package com.newtest.test.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

public class SignIn extends AppCompatActivity {
    private static final String TAG = "LoginPage";

    EditText usernameInput;
    EditText passwordInput;
    Button signInBtn;

    SignInConnection connection;

    User user;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Access username and password in SharedPreferences file called 'userInfo"
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String storedUsername = sp.getString("username","");
        String storedPassword = sp.getString("password", "");

        System.out.println(storedUsername);
        System.out.println(storedPassword);

        //If username and password are not stored in SharedPreferences, login by entering credentials
        // else, username and password are found, automatically login
        if(!storedUsername.isEmpty() && !storedPassword.isEmpty()) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~ Credentials detected ~~~~~~~~~~~~~~~~~~~~");
            loginWithPrefs(storedUsername, storedPassword);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_signin);


            usernameInput = findViewById(R.id.username_input);
            passwordInput = findViewById(R.id.password_input);

            //Instantiate signInBtn
            signInBtn = findViewById(R.id.signin_button);
            signInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
        }
     }

    protected void login() {
        Log.d(TAG, "Login");
        if(validate()) {
            Toast.makeText(SignIn.this, "Signing you in...", Toast.LENGTH_LONG).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    connection = new SignInConnection(usernameInput.getText().toString(), passwordInput.getText().toString());
                    try {
                        user = connection.connect();
                        if(!(user == null)) {
                            Intent intent = new Intent(SignIn.this, Account.class);
                            intent.putExtra("UserKey", (Parcelable) user);

                            SharedPreferences.Editor ed = sp.edit();
                            ed.putString("username", usernameInput.getText().toString());
                            ed.putString("password", passwordInput.getText().toString());
                            ed.apply();

                            startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignIn.this, "Username/Password incorrect. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
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
                connection = new SignInConnection(username, password);
                try{
                    user = connection.connect();
                    Intent intent = new Intent(SignIn.this, Account.class);
                    intent.putExtra("UserKey", (Parcelable) user);
                    intent.putExtra("SourceKey", "SignIn");

                    startActivity(intent);
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
}
