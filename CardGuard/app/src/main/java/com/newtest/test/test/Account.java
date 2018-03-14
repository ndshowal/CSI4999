package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Account extends AppCompatActivity {

    User user;

    TextView greeting;

    TextView t1_1;
    TextView t1_2;
    TextView t1_3;
    TextView t1_4;

    TextView t2_1;
    TextView t2_2;
    TextView t2_3;
    TextView t2_4;

    TextView t3_1;
    TextView t3_2;
    TextView t3_3;
    TextView t3_4;

    TextView t4_1;
    TextView t4_2;
    TextView t4_3;
    TextView t4_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        user = getIntent().getParcelableExtra("UserKey");
        System.out.println(user.toString());

        greeting = (TextView) findViewById(R.id.text_greeting);
        greeting.setText("Welcome, " + user.getUsername());

        //to create button to redirect to Sending and Receiving page
        Button newTransactionBtn = (Button)findViewById(R.id.new_transaction_button);

        newTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, SendRequestPage.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create button to redirect to settings page
        Button settingsBtn = (Button)findViewById(R.id.settings_button);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Settings.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create button to redirect to notifications page
        Button notificationsBtn = (Button)findViewById(R.id.notifications_button);

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Notifications.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
