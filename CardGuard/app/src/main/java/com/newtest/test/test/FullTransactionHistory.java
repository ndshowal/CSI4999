package com.newtest.test.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;

public class FullTransactionHistory extends AppCompatActivity {

    private User user;
    private String searchKey;
    private EditText searchText;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_transaction_history);

        //Hide the keyboard when activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        user = getIntent().getParcelableExtra("UserKey");
        searchKey = getIntent().getExtras().getString("SearchKey");

        //Variable for updating UI
        flag = 0;

        //Thread for updating user's transaction list
        new Thread(new Runnable() {
            @Override
            public void run() {
                AsyncTask tp = new TransactionPuller(user, new TransactionPuller.AsyncResponse() {
                    @Override
                    public void processFinished(String output) {
                    }
                }).execute();
            }
        }).start();

        updateUI();
    }

    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Only runs once (when activity is started)
                if (flag == 0) {

<<<<<<< HEAD
                    //Create Search Button
                    final Button searchBtn = findViewById(R.id.search_button);
                    searchBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Hides the keyboard on click (for aesthetics)
                            InputMethodManager inputMethodManager = (InputMethodManager) FullTransactionHistory.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            try {
                                inputMethodManager.hideSoftInputFromWindow(FullTransactionHistory.this.getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception ex) {
                            }
                            searchText = findViewById(R.id.search_textbox);
                            sortByUser(searchText.getText().toString());
                        }
                    });
=======
 //transaction maps button
        Button mapBtn = (Button)findViewById(R.id.transaction_map_button);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullTransactionHistory.this, ActivityMaps.class);
                intent.putExtra("UserKey", user);
                startActivity(intent);
            }
        });

        System.out.println("Transactions in user list: " + user.getTransactions().size());
>>>>>>> 1a84bb335b67affd9fd375add478b8c88baabc1e

                    //Create a back button
                    Button backBtn = findViewById(R.id.back_button);
                    backBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FullTransactionHistory.this, Account.class);
                            intent.putExtra("UserKey", user);
                            startActivity(intent);
                        }
                    });

                    //Adds a button entry for each transaction to the scroll view on the Account page, or informs user they don't have any yet
                    if (user.getTransactions().size() == 0) {
                        TextView noTransactionsMessage = new TextView(FullTransactionHistory.this);
                        noTransactionsMessage.setText("You have no transactions yet! Press 'New Transaction' to get started, or wait for someone to begin one with you!");

                        //Define the layout and add the message to it
                        LinearLayout ll = findViewById(R.id.button_layout);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, 10, 0, 10);
                        ll.addView(noTransactionsMessage, lp);
                    } else {
                        //For each transaction, create a button and add it to the defined layout
                        for (final Transaction tx : user.getTransactions()) {
                            Button transactionInfoBtn = new Button(FullTransactionHistory.this);
                            transactionInfoBtn.setText(tx.getSimpleDescription());

                            LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            lp.setMargins(0, 10, 0, 10);
                            ll.addView(transactionInfoBtn, lp);

                            transactionInfoBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(FullTransactionHistory.this, TransactionInformation.class);
                                    intent.putExtra("UserKey", user);
                                    intent.putExtra("TxKey", tx);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    //Called if user searches for a username
                } else if (flag == 1) {
                    LinearLayout ll = findViewById(R.id.button_layout);
                    ll.removeAllViews();
                    for (final Transaction tx : user.getTransactions(searchKey)) {
                        Button transactionInfoBtn = new Button(FullTransactionHistory.this);
                        transactionInfoBtn.setText(tx.getSimpleDescription());

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, 10, 0, 10);
                        ll.addView(transactionInfoBtn, lp);

                        transactionInfoBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FullTransactionHistory.this, TransactionInformation.class);
                                intent.putExtra("UserKey", user);
                                intent.putExtra("TxKey", tx);
                                startActivity(intent);
                            }
                        });
                    }
                    flag = 0;
                }
            }
        });
    }

    //Search for a username, if username exists, reload the page and pass in that username (used to sort the table)
    // If username doesn't exist, let the user know via error dialog in the search field
    public void sortByUser(final String in) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (searchText.getText().toString().equals("")) {
                        searchKey = in;
                        flag = 1;
                        updateUI();
                    } else if (new UserChecker().usernameExists(in)) {
                        searchKey = in;
                        flag = 1;
                        updateUI();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchText.setError("Username doesn't exist/is incorrect");
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}