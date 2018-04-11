package com.newtest.test.test;

import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NewTransaction extends AppCompatActivity {

    User user;
    ArrayList<User> contacts;

    TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        user = getIntent().getParcelableExtra("UserKey");
        contacts = new ArrayList<>();
        headerText = findViewById(R.id.header);
        headerText.setTextSize(32);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(Transaction t : user.getTransactions()) {
                    if(!t.getInitiator().getUsername().equals(user.getUsername())) {
                        if(contacts.size() == 0) {
                            contacts.add(t.getInitiator());
                            return;
                        } else {
                            for(User u : contacts) {
                                if(!u.getUsername().equals(t.getInitiator().getUsername())) {
                                    contacts.add(u);
                                    return;
                                }
                            }
                        }
                    }

                    if(!t.getSender().getUsername().equals(user.getUsername())) {
                        if(contacts.size() == 0) {
                            contacts.add(t.getSender());
                            return;
                        } else {
                            for(User u : contacts) {
                                if(!u.getUsername().equals(t.getSender().getUsername())) {
                                    contacts.add(u);
                                    return;
                                }
                            }
                        }
                    }

                    if(!t.getRecipient().getUsername().equals(user.getUsername())) {
                        if(contacts.size() == 0) {
                            contacts.add(t.getRecipient());
                            return;
                        } else {
                            for(User u : contacts) {
                                if(!u.getUsername().equals(t.getRecipient().getUsername())) {
                                    contacts.add(u);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });

        t1.start();

        try {
            t1.join();
            t2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create Send Button
        Button sendBtn = (Button)findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTransaction.this, GenerateTransaction.class);
                intent.putExtra("UserKey", user);
                intent.putExtra("TypeKey", "Send");
                startActivity(intent);
            }
        });

        //Create Receive Button
        Button receiveBtn = (Button)findViewById(R.id.request_button);
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTransaction.this, GenerateTransaction.class);
                intent.putExtra("UserKey", user);
                intent.putExtra("TypeKey", "Request");
                startActivity(intent);
            }
        });

        //Create Cancel Button
        Button cancelBtn = (Button)findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTransaction.this, Account.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();
    }

    protected void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout ll = findViewById(R.id.button_layout);
                ll.removeAllViews();

                if(contacts.size() == 0) {
                    TextView contactsText = findViewById(R.id.contacts_label);
                    contactsText.setVisibility(View.INVISIBLE);

                    TextView messageText = new TextView(NewTransaction.this);
                    messageText.setText("You don't have any contacts yet. Build your contacts list by transacting with someone!");

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(10, 10, 10, 10);
                    ll.addView(messageText, lp);
                } else {
                    for (User u : contacts) {
                        Button userBtn = new Button(NewTransaction.this);
                        userBtn.setTextSize(16);
                        userBtn.setText(u.getUsername());
                        userBtn.setPadding(10, 10, 10, 10);

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(10, 10, 10, 10);
                        ll.addView(userBtn, lp);

                        userBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                }
            }
        });
    }
}
