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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //Create cancel button
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, SignInSignUp.class);
                startActivity(intent);
                finish();
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

                        if(user != null) {
                            Intent intent = new Intent(Register.this, Account.class);
                            intent.putExtra("UserKey", (Parcelable) user);
                            startActivity(intent);
                            finish();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, "There was an error creating your account, please try again", Toast.LENGTH_SHORT).show();
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

    public Boolean validate() {
        final boolean[] valid = {true};

        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String username = usernameInput.getText().toString();
        String emailAddress = emailAddressInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if(firstName.isEmpty()) {
            firstNameInput.setError("Please enter your first name.");
            valid[0] = false;
        } else if(!firstNameInput.getText().toString().matches("[a-zA-Z]+")) {
            firstNameInput.setError("Name can only contain letters.");
            valid[0] = false;
        } else {
            firstNameInput.setError(null);
        }

        if(lastName.isEmpty()) {
            lastNameInput.setError("Please enter your last name.");
            valid[0] = false;
        } else if(!lastNameInput.getText().toString().matches("[a-zA-Z]+")) {
            lastNameInput.setError("Last name may only contain letters.");
            valid[0] = false;
        } else {
            lastNameInput.setError(null);
        }

        if(username.isEmpty()) {
            usernameInput.setError("Please enter your username");
            valid[0] = false;
        } else if(usernameInput.getText().toString().matches("[a-zA-Z0-9]-")) {
            usernameInput.setError("Usernames may only contain letters or numbers.");
            valid[0] = false;
        } else {
            usernameInput.setError(null);
        }

        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(emailAddressInput.getText().toString());

        if(emailAddress.isEmpty()) {
            emailAddressInput.setError("Please enter your email address");
            valid[0] = false;
        } if(!mat.matches()) {
            emailAddressInput.setError("Please enter a valid email address.");
        } else {
            emailAddressInput.setError(null);
        }

        if (password.isEmpty()) {
            passwordInput.setError("Please enter a password.");
            valid[0] = false;
        } else {
            passwordInput.setError(null);
        }

        if(confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Please confirm your password.");
            valid[0] = false;
        } else {
            confirmPasswordInput.setError(null);
        }

        if(!password.matches(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match.");
            valid[0] = false;
        } else {
            confirmPasswordInput.setError(null);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(new UserChecker().usernameExists(usernameInput.getText().toString())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                usernameInput.setError("An account with that username already exists.");
                                valid[0] = false;
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(new UserChecker().emailExists(emailAddressInput.getText().toString())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                emailAddressInput.setError("An account with that email address already exists.");
                                valid[0] = false;
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        return valid[0];
    }
}
