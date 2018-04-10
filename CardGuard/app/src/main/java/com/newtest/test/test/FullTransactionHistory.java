package com.newtest.test.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FullTransactionHistory extends AppCompatActivity {

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_transaction_history);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        user = getIntent().getParcelableExtra("UserKey");

        //Thread for updating user's transaction list
        new Thread(new Runnable() {
            @Override
            public void run() {
                AsyncTask tp = new TransactionPuller(user, new TransactionPuller.AsyncResponse() {
                    @Override
                    public void processFinished(String output) {}}).execute();
            }
        }).start();

        //Create a back button
        Button backBtn = (Button) findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullTransactionHistory.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

 //transaction maps button
        Button mapBtn = (Button)findViewById(R.id.transaction_map_button);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullTransactionHistory.this, ActivityMaps.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        System.out.println("Transactions in user list: " + user.getTransactions().size());

        //Adds a button entry for each transaction to the scroll view on the Account page, or informs user they don't have any yet
        if(user.getTransactions().size() == 0) {
            TextView noTransactionsMessage = new TextView(FullTransactionHistory.this);
            noTransactionsMessage.setText("You have no transactions yet! Press 'New Transaction' to get started, or wait for someone to begin one with you!");

            LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 10, 0, 10);
            ll.addView(noTransactionsMessage, lp);
        } else {
            for (final Transaction tx : user.getTransactions()) {
                Button transactionInfoBtn = new Button(this);
                transactionInfoBtn.setText(tx.getSimpleDescription());

                LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, 10, 0, 10);
                ll.addView(transactionInfoBtn, lp);

                transactionInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FullTransactionHistory.this, TransactionInformation.class);
                        intent.putExtra("UserKey", user);
                        intent.putExtra("TxKey", tx);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}