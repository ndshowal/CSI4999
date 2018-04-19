package com.newtest.test.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AlertDialog;
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
                    User s = t.getSender();
                    User r = t.getRecipient();
                    User notUser = null;

                    //If sender == the user, notUser = recipient
                    if (s.getUsername().equals(user.getUsername())) {
                        notUser = r;
                    //If recipient == the user, notUser = sender
                    } else if(r.getUsername().equals(user.getUsername())) {
                        notUser = s;
                    }

                    //If contacts is empty, add notUser to it
                    if(contacts.size() == 0 ) {
                        contacts.add(notUser);
                    //Else, set comparison flag = 0
                    } else {
                        int flag = 0;
                        //For each User in the contacts list
                        for (User c : contacts) {
                            //If notUser exists and isn't the current User in contacts, set flag to 1
                            if (notUser != null && c.getUsername().equals(notUser.getUsername())) {
                                flag = 1;
                            }
                        }
                        //If flag = 0, add notUser to contacts list
                        if(flag == 0) {
                            contacts.add(notUser);
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
                    int count = 0;
                    for (final User u : contacts) {
                        count++;

                        Button userBtn = new Button(NewTransaction.this);
                        userBtn.setTextSize(16);
                        userBtn.setText(u.getUsername());
                        userBtn.setPadding(10, 10, 10, 10);

                        if(count % 2 == 0) {
                            userBtn.setBackgroundResource(R.drawable.button_alt);
                        }

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(10, 10, 10, 10);
                        ll.addView(userBtn, lp);

                        userBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new AlertDialog.Builder(NewTransaction.this)
                                        .setTitle("Initiate transaction with " + u.getUsername() + "?")
                                        .setMessage("Select \"Request Funds\" or \"Send Funds\" to begin a transaction with " + u.getUsername())
                                        .setPositiveButton("Send Funds", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(NewTransaction.this, GenerateTransaction.class);
                                                intent.putExtra("UserKey", user);
                                                intent.putExtra("TypeKey", "Send");
                                                intent.putExtra("TargetKey", u.getUsername());
                                                startActivity(intent);
                                            }
                                        }).setNegativeButton("Request Funds", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(NewTransaction.this, GenerateTransaction.class);
                                                intent.putExtra("UserKey", user);
                                                intent.putExtra("TypeKey", "Request");
                                                intent.putExtra("TargetKey", u.getUsername());
                                                startActivity(intent);
                                            }
                                }).create().show();
                            }
                        });
                    }
                }
            }
        });
    }
}
