package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PhoneNumberRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_registration);

        Button btn = (Button)findViewById(R.id.button10);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneNumberRegistration.this, PhoneNumberValidation.class));
            }
        });
    }
}
