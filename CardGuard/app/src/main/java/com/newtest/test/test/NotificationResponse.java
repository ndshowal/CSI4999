package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationResponse extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_response);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a back button
        Button backBtn = (Button)findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationResponse.this, Notifications.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create an accept button
        Button acceptBtn = (Button)findViewById(R.id.accept_button);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationResponse.this, FingerprintAuthentication.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create a decline button
        Button declineBtn = (Button)findViewById(R.id.decline_button);

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationResponse.this, FingerprintAuthentication.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
