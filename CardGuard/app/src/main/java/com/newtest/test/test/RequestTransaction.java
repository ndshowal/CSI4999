package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestTransaction extends AppCompatActivity {

    User user;

    EditText usernameInput, amountInput, memoInput;

    DateFormat dateFormat;
    Date date;

    UserChecker uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendingpage);

        user = getIntent().getParcelableExtra("UserKey");

        usernameInput = findViewById(R.id.username_textbox);
        amountInput = findViewById(R.id.amount_textbox);
        memoInput = findViewById(R.id.memo_textbox);

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();

        //to create a send button
        Button sendBtn = (Button)findViewById(R.id.request_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Float amt = Float.parseFloat(amountInput.getText().toString());
                        uc = new UserChecker();
                        String toBeSearched = usernameInput.getText().toString();
                        try {
                            if(uc.usernameExists(toBeSearched)) {
                                System.out.println("Attempting to retrieve user profile....");
                                GetUser gu;
                                gu = new GetUser(usernameInput.getText().toString());

                                User target = gu.getUser();

                                Transaction tx = new Transaction(user, user , target,
                                        amt, memoInput.getText().toString(), date , null, true, false);

                                TransactionUploader tu = new TransactionUploader();
                                if(tu.upload(tx)) {
                                    System.out.println("Upload successful!");
                                    Intent intent = new Intent(RequestTransaction.this, RequestSent.class);
                                    intent.putExtra("UserKey", user);
                                    startActivity(intent);
                                } else {
                                    System.out.println("Transaction failed to upload");
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RequestTransaction.this, "No user with that username exists. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        //to create a cancel button
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestTransaction.this, SendRequestPage.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
