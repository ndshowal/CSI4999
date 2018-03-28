package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;

public class Account extends AppCompatActivity {
    User user;

    TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        user = getIntent().getParcelableExtra("UserKey");

        greeting = (TextView) findViewById(R.id.text_greeting);
        greeting.setText("Welcome, " + user.getUsername() + "!");

        new Thread(new Runnable() {
            @Override
            public void run() {
                TransactionPuller tp = new TransactionPuller(user);
                try {
                    tp.updateTransactions();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //to create button to redirect to Sending and Receiving page
        Button newTransactionBtn = findViewById(R.id.new_transaction_button);

        newTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, SendRequestPage.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create button to redirect to settings page
        Button settingsBtn = findViewById(R.id.settings_button);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Settings.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create button to redirect to notifications page
        Button notificationsBtn = findViewById(R.id.notifications_button);

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Notifications.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create button to go to the fullTransactionHistory page
        Button fullTransactionHistoryBtn = findViewById(R.id.fulltransactionhistory_button);

        fullTransactionHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, FullTransactionHistory.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //Adds a button entry for each transaction to the scroll view on the Account page
        for(int i = 0; i < 3; i++) {
            Button transactionInfoBtn = new Button(this);
            final Transaction tx = user.getTransactions().get(i);
            transactionInfoBtn.setText(tx.getSimpleDescription());

            LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0,10,0,10);
            ll.addView(transactionInfoBtn, lp);

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
