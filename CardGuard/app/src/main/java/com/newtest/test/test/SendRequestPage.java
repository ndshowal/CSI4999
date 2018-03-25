package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SendRequestPage extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendreceivepage);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a send button
        Button sendBtn = (Button)findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendRequestPage.this, SendingPage.class));

                Intent intent = new Intent(SendRequestPage.this, SendingPage.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create a receive button
        Button receiveBtn = (Button)findViewById(R.id.request_button);
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendRequestPage.this, ReceivingPage.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create a cancel button
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendRequestPage.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
