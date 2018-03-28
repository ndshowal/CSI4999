package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TransactionSent extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactionsent);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a return to account button
        Button returnBtn = (Button)findViewById(R.id.return_button);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionSent.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });
    }
}
