package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
import java.time.zone.ZoneOffsetTransition;

public class TransactionInformation extends AppCompatActivity {
    TextView notification,initiatedText, completedText, memoText;

    User user;
    Transaction tx;
    Button confirmBtn;
    Button denyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasaction_information);

        user = getIntent().getParcelableExtra("UserKey");
        tx = getIntent().getParcelableExtra("TxKey");
        initiatedText = findViewById(R.id.initiated_actual_label);
        completedText = findViewById(R.id.accepted_actual_label);
        notification = findViewById(R.id.notification_message);
        memoText = findViewById(R.id.memo_actual_label);

        String username = user.getUsername();

        //Set message text
        //If user is the initiator of the transaction...
        if(username.equals(tx.getInitiator().getUsername())) {
            //If user is the sender
            if(username.equals(tx.getRecipient().getUsername())) {
                notification.setText("Your request from " + tx.getSender().getUsername() + " for " + tx.getFormattedAmount());
            //If user is the recipient
            } else if(username.equals(tx.getSender().getUsername())) {
                notification.setText("You sent " + tx.getFormattedAmount() + " to " + tx.getRecipient().getUsername());
            }
        //If user did not initiate the transaction...
        } else {
            //If user is the recipient of the transaction
            if(username.equals((tx.getRecipient().getUsername()))) {
                notification.setText("You received " + tx.getFormattedAmount() + " from " + tx.getSender().getUsername());
            //If user is the sender
            } else if(username.equals(tx.getSender().getUsername())) {
                notification.setText(tx.getSender().getUsername() + " requested " + tx.getFormattedAmount() + " from you");
            }
        }

        initiatedText.setText(tx.getTransactionStartDateString());
        completedText.setText(tx.getTransactionCompleteDateString());
        memoText.setText(tx.getMemo());

        if(tx.getTransactionCompleteDate() != null) {
            completedText.setText(tx.getTransactionCompleteDateString());
        } else {
            completedText.setText("Waiting for transaction to be confirmed");
        }

        //Confirm Button
        confirmBtn = findViewById(R.id.confirm_button);
        //Deny Button
        denyBtn = findViewById(R.id.deny_button);

        // Hide the confirm/deny buttons if user is the initiator of the transaction
        if(username.equals(tx.getInitiator().getUsername()) || tx.isCompleted()) {
            confirmBtn.setVisibility(View.INVISIBLE);
            denyBtn.setVisibility(View.INVISIBLE);
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(new UpdateTransaction(tx).update()) {

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Back Button
        Button backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
