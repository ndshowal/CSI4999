package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Notifications extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        user = getIntent().getParcelableExtra("UserKey");
        //to create a back button
        Button backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get the transaction list for user, for each transaction that is inProgress(), create a button for it and add it to the ScrollView
        for(final Transaction t : user.getTransactions()) {
            Button transactionInfoBtn = new Button(this);

            if (t.inProgress()) {
                if(user.getUsername().equals(t.getInitiator().getUsername())) {
                    //If user is the sender
                    if(user.getUsername().equals(t.getRecipient().getUsername())) {
                        transactionInfoBtn.setText("Your request from " + t.getSender().getUsername() + " for " + t.getFormattedAmount());
                        //If user is the recipient
                    } else if(user.getUsername().equals(t.getSender().getUsername())) {
                        transactionInfoBtn.setText("You sent " + t.getFormattedAmount() + " to " + t.getRecipient().getUsername());
                    }
                    //If user did not initiate the transaction...
                } else {
                    //If user is the recipient of the transaction
                    if(user.getUsername().equals((t.getRecipient().getUsername()))) {
                        transactionInfoBtn.setText("You received " + t.getFormattedAmount() + " from " + t.getSender().getUsername());
                        //If user is the sender                    } else if(user.getUsername().equals(t.getSender().getUsername())) {
                        transactionInfoBtn.setText(t.getSender().getUsername() + " requested " + t.getFormattedAmount() + " from you");
                    }
                }

                LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(0,10,0,10);
                ll.addView(transactionInfoBtn, lp);

                transactionInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Notifications.this, TransactionInformation.class);
                        intent.putExtra("UserKey", user);
                        intent.putExtra("TxKey", t);
                        startActivity(intent);
                    }
                });
            }

        }
    }
}
