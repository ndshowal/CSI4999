package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReceivingPage extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiving_page);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a request button
        Button requestBtn = (Button)findViewById(R.id.request_button);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivingPage.this, RequestSent.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create a cancel button
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivingPage.this, SendRequestPage.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
