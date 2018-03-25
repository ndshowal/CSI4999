package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FullTransactionHistory extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_transaction_history);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a back button
        Button backBtn = (Button)findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullTransactionHistory.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
