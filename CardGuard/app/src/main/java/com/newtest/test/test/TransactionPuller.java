package com.newtest.test.test;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Anthony on 3/28/2018.
 */

public class TransactionPuller extends AsyncTask {
    private Connection connection;

    private ArrayList<Transaction> txList;

    User user;

    private boolean newTransactionsAvailable;

    public TransactionPuller(User user) {
        this.user = user;
        newTransactionsAvailable = false;
    }

    public ArrayList<Transaction> updateTransactions() throws SQLException {
        //DB Connection strings
        final String host = "cardguard-db.mysql.database.azure.com:3306";
        final String database = "cardguard_db";
        final String adminUsername = "cardguard-admin@cardguard-db";
        final String adminPassword = "password12345!";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("JDBC Driver not found.");
            ex.printStackTrace();
        }

        connection = null;

        try {
            String url = String.format("jdbc:mysql://%s/%s", host, database);

            // Set connection properties
            Properties properties = new Properties();
            properties.setProperty("user", adminUsername);
            properties.setProperty("password", adminPassword);
            properties.setProperty("useSSL", "true");
            properties.setProperty("verifyServerCertificate", "true");
            properties.setProperty("requireSSL", "false");

            // Connect to MySQL server with set parameters
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if(connection != null) {
            try {
                System.out.println("Attempting to query database...");

                try {
                    // Query for transactions the user was involved in
                    Statement txStatement = connection.createStatement();
                    ResultSet txResults = txStatement.executeQuery(
                            "SELECT * FROM transactions "
                                    + "WHERE sender='" + user.getUsername() + "' "
                                    + "OR recipient='" + user.getUsername() + "';");

                    // While query isn't empty:
                    while (txResults.next()) {
                        System.out.println("Transaction found");
                        //Read result to necessary transaction variables
                        String transactionID = txResults.getString(1);
                        String senderName = txResults.getString(2);
                        String recipientName = txResults.getString(3);
                        String initiatorName = txResults.getString(4);
                        Float transactionAmount = txResults.getFloat(5);
                        String memo = txResults.getString(6);
                        Date startDate = txResults.getDate(7);
                        Date completionDate = txResults.getDate(8);
                        Boolean inProgress = txResults.getBoolean(9);
                        Boolean confirmed = txResults.getBoolean(10);

                        // Because users are only represented by their names in the transactions
                        //  table, they must be created by querying again
                        User sender = null;
                        User recipient = null;
                        User initiator = null;

                        try {

                            //Query to create sender user
                            Statement usersStatement = connection.createStatement();
                            ResultSet userResults = usersStatement.executeQuery(
                                    "SELECT * FROM users "
                                            + "WHERE username='" + senderName +"';");

                            while(userResults.next()) {
                                String ID = userResults.getString(1);
                                String username = userResults.getString(2);
                                String password = userResults.getString(3);
                                String emailAddress = userResults.getString(4);
                                String firstName = userResults.getString(5);
                                String lastName = userResults.getString(6);
                                String accountType = userResults.getString(7);
                                sender = new User(ID, username, password, firstName, lastName, emailAddress, accountType);
                            }
                        } catch (SQLException ex) {
                            Log.e(TAG, ex.toString());
                            ex.printStackTrace();
                        }

                        try {
                            // Query to create recipient user
                            Statement usersStatement = connection.createStatement();
                            ResultSet userResults = usersStatement.executeQuery(
                                    "SELECT * FROM users "
                                            + "WHERE username='" + recipientName +"';");

                            while (userResults.next()) {
                                String ID = userResults.getString(1);
                                String username = userResults.getString(2);
                                String password = userResults.getString(3);
                                String emailAddress = userResults.getString(4);
                                String firstName = userResults.getString(5);
                                String lastName = userResults.getString(6);
                                String accountType = userResults.getString(7);

                                recipient = new User(ID, username, password, firstName, lastName, emailAddress, accountType);
                            }
                        } catch (SQLException ex) {
                            Log.e(TAG, ex.toString());
                            ex.printStackTrace();
                        }

                        try {
                            // Query to create initiator user
                            Statement usersStatement = connection.createStatement();
                            ResultSet userResults = usersStatement.executeQuery(
                                    "SELECT * FROM users "
                                            + "WHERE username='" + initiatorName +"';");
                            while(userResults.next()) {
                                String ID = userResults.getString(1);
                                String username = userResults.getString(2);
                                String password = userResults.getString(3);
                                String emailAddress = userResults.getString(4);
                                String firstName = userResults.getString(5);
                                String lastName = userResults.getString(6);
                                String accountType = userResults.getString(7);

                                initiator = new User(ID, username, password, firstName, lastName, emailAddress, accountType);
                            }
                        } catch (SQLException ex) {
                            Log.e(TAG, ex.toString());
                            ex.printStackTrace();
                        }

                        if(sender != null && recipient != null && initiator != null) {
                            Transaction tx = new Transaction(transactionID, sender, recipient, initiator,
                                    transactionAmount, memo, startDate, completionDate,
                                    inProgress, confirmed);

                            try {
                                for(Transaction t : user.getTransactions()) {
                                    if(t.getTransactionID() != tx.getTransactionID()) {
                                        user.addTransaction(tx);
                                        newTransactionsAvailable = true;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Log.e(TAG, ex.toString());
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return txList;
    }

    public boolean isNewTransactionsAvailable() {
        return newTransactionsAvailable;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
