package com.newtest.test.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SignInSignUp extends AppCompatActivity {

    private Button btnSignIn, btnSignUp;
    SharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pre_signin_signup);

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String storedUsername = sp.getString("username","");
        String storedPassword = sp.getString("password", "");

        System.out.println(storedUsername);
        System.out.println(storedPassword);

        //If username and password are not stored in SharedPreferences, login by entering credentials
        // else, username and password are found, automatically login
        if(!storedUsername.isEmpty() && !storedPassword.isEmpty()) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~ Credentials detected ~~~~~~~~~~~~~~~~~~~~");
            startActivity(new Intent(SignInSignUp.this, SignIn.class));
        }
        btnSignIn = findViewById(R.id.signin_button);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInSignUp.this, SignIn.class));
            }
        });

        btnSignUp = findViewById(R.id.signup_button);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInSignUp.this, Register.class));
            }
        });
    }
}
