package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BillingInformation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_information);

        //to create a button to scan user's card
        Button scanCardBtn = (Button)findViewById(R.id.scan_card_button);

        scanCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillingInformation.this, CardScan.class));
            }
        });


        //to create a button to save changes

        Button saveChangesBtn = (Button)findViewById(R.id.save_changes_button);

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillingInformation.this, Settings.class));
            }
        });

        //to create a cancel button
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillingInformation.this, Settings.class);

                startActivity(intent);
            }
        });

    }
}
