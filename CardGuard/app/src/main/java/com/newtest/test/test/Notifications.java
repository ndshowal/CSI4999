package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Notifications extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a respond button
        Button respondBtn = (Button)findViewById(R.id.respond_button3);

        respondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notifications.this, NotificationResponse.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
