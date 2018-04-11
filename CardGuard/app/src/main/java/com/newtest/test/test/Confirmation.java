package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Confirmation extends AppCompatActivity {
    TextView messageText;

    User user;
    String confirmation;
    Transaction tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        user = getIntent().getParcelableExtra("UserKey");
        tx = getIntent().getParcelableExtra("TxKey");

        confirmation = getIntent().getExtras().getString("TxMessage");
        messageText = findViewById(R.id.message_label);
        messageText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        switch(confirmation) {
            case "sent funds":
                messageText.setText("You have sent " + tx.getFormattedAmount() + " to " + tx.getRecipient().getUsername() + "!");
                break;
            case "requested funds":
                messageText.setText("You have requested " + tx.getFormattedAmount() + " from " + tx.getSender().getUsername() + "!");
                break;
            case "send funds approved":
                messageText.setText("You approved " + tx.getInitiator().getUsername() +"'s request for " + tx.getFormattedAmount() + ".");
                break;
            case "send funds denied":
                messageText.setText("You denied " + tx.getInitiator().getUsername() + "'s request for " + tx.getFormattedAmount() + ".");
                break;
            case "receive funds approved":
                messageText.setText("You accepted " + tx.getFormattedAmount() + " from " + tx.getInitiator().getUsername() + "!");
                break;
            case "receive funds denied":
                messageText.setText("You denied " + tx.getFormattedAmount() + " from " + tx.getInitiator().getUsername() + ".");
                break;
        }


        //to create a return to account button
        Button returnBtn = (Button)findViewById(R.id.return_button);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Confirmation.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
                finish();
            }
        });
    }
}
