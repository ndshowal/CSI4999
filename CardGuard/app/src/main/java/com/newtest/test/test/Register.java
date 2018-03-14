package com.newtest.test.test;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;

public class Register extends AppCompatActivity {
    private final String TAG = "Register Activity";

    RegisterConnection connection;
    User user;

    EditText firstNameInput;
    EditText lastNameInput;
    EditText usernameInput;
    EditText emailAddressInput;
    EditText passwordInput;
    EditText confirmPasswordInput;

    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameInput = findViewById(R.id.input_first_name);
        lastNameInput = findViewById(R.id.input_last_name);
        usernameInput = findViewById(R.id.input_username);
        emailAddressInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        confirmPasswordInput = findViewById(R.id.input_confirmation_password);

        registerBtn = (Button)findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    protected void register() {
        if (validate()) {
            Toast.makeText(Register.this, "Registering your new account...", Toast.LENGTH_LONG  ).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection = new RegisterConnection(firstNameInput.getText().toString(),
                                lastNameInput.getText().toString(),
                                usernameInput.getText().toString(),
                                emailAddressInput.getText().toString(),
                                passwordInput.getText().toString());
                        user = connection.connect();
                        Intent intent = new Intent(Register.this, Account.class);
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

    public Boolean validate() {
        boolean valid = true;

        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String username = usernameInput.getText().toString();
        String emailAddress = emailAddressInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if(firstName.isEmpty()) {
            firstNameInput.setError("Please enter your first name.");
            valid = false;
        } else {
            firstNameInput.setError(null);
        }

        if(lastName.isEmpty()) {
            lastNameInput.setError("Please enter your last name.");
            valid = false;
        } else {
            lastNameInput.setError(null);
        }

        if (username.isEmpty()) {
            usernameInput.setError("Please enter your username");
            valid = false;
        } else {
            usernameInput.setError(null);
        }

        if(emailAddress.isEmpty()) {
            emailAddressInput.setError("Please enter your email address");
            valid = false;
        } else {
            emailAddressInput.setError(null);
        }

        if (password.isEmpty()) {
            passwordInput.setError("Please enter your password");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        if(confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Please confirm your password");
            valid = false;
        } else {
            confirmPasswordInput.setError(null);
        }

        if(!password.matches(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match.");
            valid = false;
        } else {
            confirmPasswordInput.setError(null);
        }

        return valid;
    }
}
