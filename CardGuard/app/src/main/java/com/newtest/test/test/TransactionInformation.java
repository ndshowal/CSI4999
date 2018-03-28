package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TransactionInformation extends AppCompatActivity {
    TextView notification,initiatedText, completedText, memoText;

    User user;
    Transaction tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_information);

        user = getIntent().getParcelableExtra("UserKey");
        tx = getIntent().getParcelableExtra("TxKey");

        initiatedText = findViewById(R.id.date_actual_label);
        completedText = findViewById(R.id.time_actual_label);
        notification = findViewById(R.id.notification_message);

        if(user.equals(tx.getInitiator())) {
            if(user.equals(tx.getRecipient())) {
                notification.setText("Your request from " + tx.getSender().getUsername() + " for " + tx.getFormattedAmount());
            } else if(user.equals(tx.getSender())) {
                notification.setText("You sent " + tx.getFormattedAmount() + " to " + tx.getRecipient().getUsername());
            }
        } else {
            if(user.equals((tx.getRecipient()))) {
                notification.setText("You received " + tx.getFormattedAmount() + " from " + tx.getSender());
            } else if(user.equals(tx.getRecipient())) {
                notification.setText(tx.getRecipient().getUsername() + " requested " + tx.getFormattedAmount() + " from you");
            }
        }

        initiatedText.setText(tx.getTransactionStartDateString());

        if(tx.getTransactionCompleteDate() != null) {
            completedText.setText(tx.getTransactionCompleteDateString());
        } else {
            completedText.setText("In progress");
        }

        //back button
        Button backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionInformation.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
