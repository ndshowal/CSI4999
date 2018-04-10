package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewTransaction extends AppCompatActivity {

    User user;

    TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        user = getIntent().getParcelableExtra("UserKey");

        headerText = findViewById(R.id.header);
        headerText.setTextSize(32);

        //Create Send Button
        Button sendBtn = (Button)findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTransaction.this, GenerateTransaction.class);
                intent.putExtra("UserKey", user);
                intent.putExtra("TypeKey", "Send");
                startActivity(intent);
            }
        });

        //Create Receive Button
        Button receiveBtn = (Button)findViewById(R.id.request_button);
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTransaction.this, GenerateTransaction.class);
                intent.putExtra("UserKey", user);
                intent.putExtra("TypeKey", "Request");
                startActivity(intent);
            }
        });

        //Create Cancel Button
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTransaction.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
