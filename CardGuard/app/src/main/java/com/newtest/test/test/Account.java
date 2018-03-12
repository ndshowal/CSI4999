package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        //to create button to redirect to Sending and Receiving page
        Button btn = (Button)findViewById(R.id.new_transaction_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, SendReceivePage.class));
            }
        });

        //to create button to redirect to settings page
        Button btn2 = (Button)findViewById(R.id.settings_button);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, Settings.class));
            }
        });

        //to create button to redirect to notifications page
        Button btn3 = (Button)findViewById(R.id.notifications_button);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, Notifications.class));
            }
        });
    }
}
