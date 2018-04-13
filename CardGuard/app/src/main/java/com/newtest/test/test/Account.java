package com.newtest.test.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Account extends AppCompatActivity {
    User user;
    TextView greeting;

    AsyncTask tp;
    int flag;
    Button fullTransactionHistoryBtn;
    Button notificationsBtn;

    private String userBalance;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Get user object from parcel
        user = getIntent().getParcelableExtra("UserKey");

        //For updating the UI
        flag = 0;

        //Create button to redirect to Sending and Receiving page
        Button newTransactionBtn = findViewById(R.id.new_transaction_button);

        newTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, NewTransaction.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //Create button to redirect to activity_settings page
        Button settingsBtn = findViewById(R.id.settings_button);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Settings.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //Create button to redirect to activity_notifications page
        notificationsBtn = findViewById(R.id.notifications_button);
        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Notifications.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //Create button to go to the fullTransactionHistory page
        fullTransactionHistoryBtn = findViewById(R.id.full_transaction_history_button);

        fullTransactionHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, FullTransactionHistory.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        userBalance = "";
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                userBalance = "Your current balance is: " + new UserChecker().getFormattedBalance(user);
                tp = new TransactionPuller(user, new TransactionPuller.AsyncResponse() {
                    @Override
                    public void processFinished(String output) {}}).execute();
                flag = 1;
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });

        t1.start();

        try {
            t1.join();
            t2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_out)
                .setMessage("Are you sure you want to sign out? Press back again to proceed.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();

                        ed.putString("username" , "");
                        ed.putString("password", "");
                        ed.apply();

                        startActivity(new Intent(Account.this, SignInSignUp.class));
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }}).create().show();
    }

    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Set greeting text
                greeting = findViewById(R.id.text_greeting);
                greeting.setText("");

                greeting.setText("Welcome, " + user.getUsername() + "!\n"
                        +  userBalance);
                greeting.setTextSize(20);

                for(Transaction tx : user.getTransactions()) {
                    if(tx.inProgress() && !tx.getInitiator().getUsername().equals(user.getUsername())) {
                        notificationsBtn.setError("");
                        break;
                    }
                }

                if(flag == 0 || flag == 1) {
                    //Used to determine how many buttons to place in the ScrollView for this activity, buttons will link to the corresponding
                    // transaction information page
                    switch(user.getTransactions().size()) {
                        case 0:
                            fullTransactionHistoryBtn.setVisibility(View.INVISIBLE);
                            generateButtons(0);
                            break;
                        case 1:
                            generateButtons(1);
                            break;
                        case 2:
                            generateButtons(2);
                            break;
                        default:
                            generateButtons(3);
                    }
                }
            }
        });

    }

    //Generates buttons to populate the Scroll View with up to 3 transactions, or informs the user that they don't have any yet
    private void generateButtons(int n) {
        //If no transactions, generate a TextView and place it in the ScrollView and hide fullTransactionHistoryButton
        if(n == 0) {
            LinearLayout ll = findViewById(R.id.button_layout);
            ll.removeAllViews();

            TextView noTransactionsMessage = new TextView(Account.this);
            if(noTransactionsMessage.getText().equals("")) {
                noTransactionsMessage.setText("You have no transactions yet! Press 'New Transaction' to get started, or wait for someone to begin one with you!");
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 10, 0, 10);
            noTransactionsMessage.setGravity(View.TEXT_ALIGNMENT_CENTER);
            ll.addView(noTransactionsMessage, lp);

        //If at least 1 transaction, generate the button(s):
        } else {
            LinearLayout ll = findViewById(R.id.button_layout);
            ll.removeAllViews();

            for (int i = 0; i < n; i++) {
                Button transactionInfoBtn = new Button(Account.this);
                final Transaction tx = user.getTransactions().get(i);
                transactionInfoBtn.setText(tx.getSimpleDescription());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, 10, 0, 10);
                ll.addView(transactionInfoBtn, lp);

                //If "clicked", redirect to Transaction Information page
                transactionInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Account.this, TransactionInformation.class);
                        intent.putExtra("UserKey", user);
                        intent.putExtra("TxKey", tx);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
