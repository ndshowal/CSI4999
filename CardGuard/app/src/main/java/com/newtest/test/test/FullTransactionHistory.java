package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FullTransactionHistory extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_transaction_history);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a back button
        Button backBtn = (Button)findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullTransactionHistory.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //Adds a button entry for each transaction to the scroll view on the Account page
        for(final Transaction tx : user.getTransactions()) {
            Button transactionInfoBtn = new Button(this);
            transactionInfoBtn.setText(tx.getSimpleDescription());

            LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0,10,0,10);
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
