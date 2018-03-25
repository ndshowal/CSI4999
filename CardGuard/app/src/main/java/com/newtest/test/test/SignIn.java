package com.newtest.test.test;

import android.content.Intent;
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
    Button signUpBtn;

    SignInConnection connection;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signin);



        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.input_password);

        //Instantiate signInBtn
        signInBtn = (Button)findViewById(R.id.signin_button);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //to create a sign up button
        signUpBtn = (Button)findViewById(R.id.signup_button);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
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
                        Intent intent = new Intent(SignIn.this, Account.class);
                        intent.putExtra("UserKey", (Parcelable) user);

                        startActivity(intent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                    }
                }
            }).start();
        }
    }

    public void signUp() {
        Log.d(TAG, "SignUp");
        startActivity(new Intent(SignIn.this, Register.class));
    }

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
