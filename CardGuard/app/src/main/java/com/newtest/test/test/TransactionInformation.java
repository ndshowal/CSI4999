package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TransactionInformation extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_information);

        user = getIntent().getParcelableExtra("UserKey");

        //back button
        Button backBtn = (Button)findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionInformation.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
