package com.newtest.test.test;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

public class AccountSettings extends AppCompatActivity {
    private final String TAG = "Account Settings";
    private User user;

    private UpdateConnection connection;

    private String ID;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText usernameInput;
    private EditText emailAddressInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);

        user = getIntent().getParcelableExtra("UserKey");
        ID = user.getID();

        firstNameInput = findViewById(R.id.input_first_name);
        lastNameInput = findViewById(R.id.input_last_name);
        //usernameInput = findViewById(R.id.input_username);
        emailAddressInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        confirmPasswordInput = findViewById(R.id.input_confirmation_password);

        firstNameInput.setText(user.getFirstName());
        lastNameInput.setText(user.getLastName());
        emailAddressInput.setText(user.getEmailAddress());

        Button cancelBtn = findViewById(R.id.cancel_button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettings.this, Settings.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create a save changes button
        Button saveChangesBtn = (Button)findViewById(R.id.save_changes_button);

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });
    }

    protected void updateSettings() {
        if (validate()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection = new UpdateConnection(ID, firstNameInput.getText().toString(),
                                lastNameInput.getText().toString(),
                                user.getUsername(),
                                emailAddressInput.getText().toString(),
                                passwordInput.getText().toString());
                        user = connection.connect();
                        Intent intent = new Intent(AccountSettings.this, Account.class);
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

    protected Boolean validate() {
        Boolean valid = true;

        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        //String username = usernameInput.getText().toString();
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

        /*if (username.isEmpty()) {
            usernameInput.setError("Please enter your username");
            valid = false;
        } else {
            usernameInput.setError(null);
        }*/

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
