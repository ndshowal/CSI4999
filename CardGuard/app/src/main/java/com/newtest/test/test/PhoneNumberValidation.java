package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PhoneNumberValidation extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_validation);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a submit button
        Button submitBtn = (Button)findViewById(R.id.submit_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneNumberValidation.this, CardScan.class));

                Intent intent = new Intent(PhoneNumberValidation.this, CardScan.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

    }
}
