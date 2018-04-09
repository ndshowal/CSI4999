package com.newtest.test.test;

import android.content.Intent;
import android.os.AsyncTask;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        //Get user object from parcel
        user = getIntent().getParcelableExtra("UserKey");

        //Set greeting text
        greeting = (TextView) findViewById(R.id.text_greeting);
        greeting.setText("Welcome, " + user.getUsername() + "!");

        //Thread for querying database to update transaction list
        new Thread(new Runnable() {
            @Override
            public void run() {
                tp = new TransactionPuller(user, new TransactionPuller.AsyncResponse() {
                    @Override
                    public void processFinished(String output) {}}).execute();
            }
        }).start();

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

        //Create button to redirect to settings page
        Button settingsBtn = findViewById(R.id.settings_button);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Settings.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //Create button to redirect to notifications page
        Button notificationsBtn = findViewById(R.id.notifications_button);

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Notifications.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //Create button to go to the fullTransactionHistory page
        Button fullTransactionHistoryBtn = findViewById(R.id.full_transaction_history_button);

        fullTransactionHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, FullTransactionHistory.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        System.out.println("Transactions in user list: " + user.getTransactions().size());

        //Used to determine how many buttons to place in the ScrollView for this activity, buttons will link to the corresponding
        // transaction information page
        switch(user.getTransactions().size()) {
            case 0:
                generateButtons(0);
                break;
            case 1:
                generateButtons(1);
                break;
            case 2:
                generateButtons(2);
                break;
            case 3:
                generateButtons(3);
                break;
            default:
                generateButtons(4);

        }
    }

    //Generates buttons to populate the Scroll View with up to 3 transactions, or informs the user that they don't have any yet
    private void generateButtons(int n) {
        //If no transactions, generate a TextView and place it in the ScrollView:
        if(n == 0) {
            TextView noTransactionsMessage = new TextView(Account.this);
            noTransactionsMessage.setText("You have no transactions yet! Press 'New Transaction' to get started, or wait for someone to begin one with you!");

            LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 10, 0, 10);
            ll.addView(noTransactionsMessage, lp);

        //If at least 1 transaction, generate the button(s):
        } else {
            for (int i = 0; i < n; i++) {
                Button transactionInfoBtn = new Button(Account.this);
                final Transaction tx = user.getTransactions().get(i);
                transactionInfoBtn.setText(tx.getSimpleDescription());

                LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
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
