package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;
import java.sql.Timestamp;

public class GenerateTransaction extends AppCompatActivity {

    User user;
    String transactionType;
    EditText usernameInput, amountInput, memoInput;
    TextView headerText;

    UserChecker uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_transaction);

        user = getIntent().getParcelableExtra("UserKey");
        transactionType = getIntent().getExtras().getString("TypeKey");



        usernameInput = findViewById(R.id.username_textbox);
        amountInput = findViewById(R.id.amount_textbox);
        memoInput = findViewById(R.id.memo_textbox);

        //Create Send/Request button and set header text
        headerText = findViewById(R.id.header);
        Button sendBtn = (Button)findViewById(R.id.send_button);
        if(transactionType.equals("Send")) {
            headerText.setText("Send Funds");
            sendBtn.setText("Send");
        } else if(transactionType.equals("Request")) {
            headerText.setText("Request Funds");
            sendBtn.setText("Request");
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(valid()) {
                            User target = null;
                            try {
                                target = new GetUser(usernameInput.getText().toString()).getUser();
                                Transaction tx = null;

                                if(transactionType.equals("Send")) {
                                    tx = new Transaction(user, target, user,
                                            Float.parseFloat(amountInput.getText().toString()), memoInput.getText().toString(),
                                            new Timestamp(System.currentTimeMillis()), null, true, false);
                                } else if(transactionType.equals("Request")) {
                                    tx = new Transaction(target, user, user,
                                            Float.parseFloat(amountInput.getText().toString()), memoInput.getText().toString(),
                                            new Timestamp(System.currentTimeMillis()), null, true, false);
                                }

                                if (tx != null && new TransactionUploader().upload(tx)) {
                                    Intent intent = new Intent(GenerateTransaction.this, TransactionSent.class);
                                    intent.putExtra("UserKey", user);
                                    startActivity(intent);
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
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);
        cancelBtn.setTextSize(24);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        if (usernameInput == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    usernameInput.setError("Please input a username");
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
        return valid;
    }

}
