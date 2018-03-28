package com.newtest.test.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BillingInformation extends AppCompatActivity {

    User user;

    SharedPreferences sp;

    EditText cardNumInput, expirationInput, nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_information);

        user = getIntent().getParcelableExtra("UserKey");

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();

        cardNumInput = findViewById(R.id.card_number_textbox);
        expirationInput = findViewById(R.id.expiration_date_textbox);
        nameInput = findViewById(R.id.cardholder_name_textbox);

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
                ed.putString("CardNum", String.valueOf(cardNumInput.getText()));
                ed.putString("CardExpirationDate", String.valueOf(expirationInput));
                ed.putString("CardHolderName", String.valueOf(nameInput));
                ed.apply();

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
