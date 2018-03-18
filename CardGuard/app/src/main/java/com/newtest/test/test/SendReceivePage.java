package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SendReceivePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendreceivepage);

        //to create a send button
        Button sendBtn = (Button)findViewById(R.id.send_button);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendReceivePage.this, SendingPage.class));
            }
        });

        //to create a receive button
        Button receiveBtn = (Button)findViewById(R.id.receive_button);

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendReceivePage.this, ReceivingPage.class));
            }
        });
    }
}
