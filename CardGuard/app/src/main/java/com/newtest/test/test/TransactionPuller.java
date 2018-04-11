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

public class TransactionPuller extends AsyncTask<Void, Void, Void> {
    private ArrayList<Transaction> txList;
    User user;

    private Connection connection;

    public AsyncResponse delegate = null;

    //AsyncTask interface
    public interface AsyncResponse {
        void processFinished(String output);
    }

    // CONSTRUCTOR
    public TransactionPuller(User user, AsyncResponse delegate) {
        this.delegate = delegate;
        txList = new ArrayList<>();
        this.user = user;
    }

    @Override
    // Queries database for the full list of transactions associated with the current user's username. If the downloaded
    // list is different from the current user's transaction list, set the user's list to the new one.
    protected Void doInBackground(Void... voids) {
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

        //Instantiate connection
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

        //If connection was successful:
        if(connection != null) {
            try {
                try {
                    // Query for transactions the user was involved in
                    Statement txStatement = connection.createStatement();
                    ResultSet txResults = txStatement.executeQuery(
                            "SELECT * FROM transactions "
                                    + "WHERE sender='" + user.getUsername() + "' "
                                    + "OR recipient='" + user.getUsername() + "'" +
                                    " ORDER BY start_date DESC;");

                    // While query isn't empty:
                    while (txResults.next()) {
                        //Read result to necessary transaction variables
                        String transactionID = txResults.getString(1);
                        String senderName = txResults.getString(2);
                        String recipientName = txResults.getString(3);
                        String initiatorName = txResults.getString(4);
                        Float transactionAmount = txResults.getFloat(5);
                        String memo = txResults.getString(6);
                        Date startDate = txResults.getTimestamp(7);
                        Date completionDate = txResults.getTimestamp(8);
                        Boolean inProgress = txResults.getBoolean(9);
                        Boolean confirmed = txResults.getBoolean(10);
                        Double initialLatitude = txResults.getDouble(11);
                        Double initialLongitude = txResults.getDouble(12);
                        Double completionLatitude = txResults.getDouble(13);
                        Double completionLongitude = txResults.getDouble(14);

                        // Because users are only represented by their names in the transactions
                        //  table, they must be created (as a field for each Transaction object) by querying again
                        User sender = null;
                        User recipient = null;
                        User initiator = null;

                        try {
                            //Query to create sender user
                            Statement usersStatement = connection.createStatement();
                            ResultSet userResults = usersStatement.executeQuery(
                                    "SELECT * FROM users "
                                            + "WHERE username='" + senderName + "';");

                            while (userResults.next()) {
                                String ID = userResults.getString(1);
                                String userHash = userResults.getString(2);
                                String username = userResults.getString(3);
                                String password = userResults.getString(4);
                                String emailAddress = userResults.getString(5);
                                String firstName = userResults.getString(6);
                                String lastName = userResults.getString(7);
                                String accountType = userResults.getString(8);
                                sender = new User(ID, userHash, username, password, firstName, lastName, emailAddress, accountType);
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
                                            + "WHERE username='" + recipientName + "';");

                            while (userResults.next()) {
                                String ID = userResults.getString(1);
                                String userHash = userResults.getString(2);
                                String username = userResults.getString(3);
                                String password = userResults.getString(4);
                                String emailAddress = userResults.getString(5);
                                String firstName = userResults.getString(6);
                                String lastName = userResults.getString(7);
                                String accountType = userResults.getString(8);

                                recipient = new User(ID, userHash, username, password, firstName, lastName, emailAddress, accountType);
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
                                            + "WHERE username='" + initiatorName + "';");
                            while (userResults.next()) {
                                String ID = userResults.getString(1);
                                String userHash = userResults.getString(2);
                                String username = userResults.getString(3);
                                String password = userResults.getString(4);
                                String emailAddress = userResults.getString(5);
                                String firstName = userResults.getString(6);
                                String lastName = userResults.getString(7);
                                String accountType = userResults.getString(8);

                                initiator = new User(ID, userHash, username, password, firstName, lastName, emailAddress, accountType);
                            }
                        } catch (SQLException ex) {
                            Log.e(TAG, ex.toString());
                            ex.printStackTrace();
                        }

                        //If a Transaction is valid (it has a sender, recipient, and initiator), instantiate a new Transaction
                        // and add it to the temporary transaction list txList
                        if (sender != null && recipient != null && initiator != null) {
                            Transaction tx = new Transaction(transactionID, sender, recipient, initiator,
                                    transactionAmount, memo, startDate, completionDate,
                                    inProgress, confirmed);

                            tx.setInitialLatitude(initialLatitude);
                            tx.setInitialLongitude(initialLongitude);
                            try {
                                tx.setCompletionLatitude(completionLatitude);
                                tx.setCompletionLongitude(completionLongitude);
                            } catch (Exception ex) {
                                tx.setCompletionLocation(null);
                            }

                            txList.add(tx);
                        }
                    }

                    if(newTransactionsAvailable()) {
                        updateTransactions();
                    }
                } catch (SQLException ex) {
                    Log.e(TAG, ex.toString());
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return null;
    }

    //Function for determining if new transaction list is different from current one
    public boolean newTransactionsAvailable() {
        if(user.getTransactions() != txList) {
            return true;
        } else {
            return false;
        }
    }

    //Set user's transaction list to the newest one (the one downloaded)
    public void updateTransactions() {
        for(int i = user.getTransactions().size() - 1; i >= 0; user.getTransactions().size(), i--) {
            user.getTransactions().remove(i);
        }
        for(Transaction t : txList) {
            user.addTransaction(t);
        }
    }
}
