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
        setContentView(R.layout.notifications);

        user = getIntent().getParcelableExtra("UserKey");

        // Get the transaction list for user, for each transaction that is inProgress(), create a button for it and add it to the ScrollView
        for(final Transaction t : user.getTransactions()) {
            Button transactionInfoBtn = new Button(this);
            if (t.inProgress()) {
                transactionInfoBtn.setText(t.getSimpleDescription());

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
