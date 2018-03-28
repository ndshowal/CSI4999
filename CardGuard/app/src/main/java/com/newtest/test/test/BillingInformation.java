package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BillingInformation extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_information);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a button to scan user's card
        Button scanCardBtn = (Button)findViewById(R.id.scan_card_button);
        scanCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillingInformation.this, CardScan.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });


        //to create a button to save changes

        Button saveChangesBtn = (Button)findViewById(R.id.save_changes_button);
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BillingInformation.this, "Billing Information Updated!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(BillingInformation.this, Settings.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        //to create a cancel button
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillingInformation.this, Settings.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

    }
}
