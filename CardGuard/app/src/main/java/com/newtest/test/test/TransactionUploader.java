package com.newtest.test.test;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import javax.xml.transform.Result;

//Class for creating a new transaction and uploading it to the database
public class TransactionUploader extends AsyncTask {
    private Connection connection;

    //DB Connection strings
    private final String host = "cardguard-db.mysql.database.azure.com:3306";
    private final String database = "cardguard_db";
    private final String adminUsername = "cardguard-admin@cardguard-db";
    private final String adminPassword = "password12345!";

    private String transactionID;
    private String senderName;
    private String recipientName;
    private String initiatorName;
    private float transactionAmount;
    private String memo;
    private Timestamp startDate;
    private Timestamp completionDate;
    private boolean inProgress;
    private boolean completed;
    private Double initialLatitude, initialLongitude, completionLatitude, completionLongitude;

    Transaction tx;
    User user;

    public TransactionUploader() throws SQLException {
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
            throw new SQLException("Failed to create connection to database.", ex);
        }
    }

    public boolean upload(Transaction tx) throws SQLException{
        this.tx = tx;
        transactionID = tx.getTransactionID();
        senderName = tx.getSender().getUsername();
        recipientName = tx.getRecipient().getUsername();
        initiatorName = tx.getInitiator().getUsername();
        transactionAmount = tx.getTransactionAmount();
        memo = tx.getMemo();
        startDate = (Timestamp) tx.getTransactionStartDate();
        completionDate = (Timestamp) tx.getTransactionCompleteDate();
        inProgress = tx.inProgress();
        completed = tx.isCompleted();
        initialLatitude = tx.getInitialLatitude();
        initialLongitude = tx.getInitialLongitude();
        completionLatitude = tx.getCompletionLatitude();
        completionLongitude = tx.getCompletionLongitude();



        if(connection != null) {
            try {
                PreparedStatement deleteStatement = connection.prepareStatement(
                        "DELETE FROM transactions WHERE transaction_ID='" + transactionID + "';");

                deleteStatement.execute();

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions" +
                        "(transaction_ID, sender, recipient, initiator, transaction_amount, " +
                        "memo, start_date, completion_date, in_progress, confirmed, " +
                        "initial_latitude, initial_longitude, completion_latitude, completion_longitude)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                preparedStatement.setString(1, transactionID);
                preparedStatement.setString(2, senderName);
                preparedStatement.setString(3, recipientName);
                System.out.println(recipientName);
                preparedStatement.setString(4, initiatorName);
                preparedStatement.setFloat(5, transactionAmount);
                preparedStatement.setString(6, memo);
                preparedStatement.setTimestamp(7, startDate);
                preparedStatement.setTimestamp(8, completionDate);
                preparedStatement.setBoolean(9, inProgress);
                preparedStatement.setBoolean(10, completed);
                if(initialLatitude != null && initialLongitude != null) {
                    preparedStatement.setDouble(11, initialLatitude);
                    preparedStatement.setDouble(12, initialLongitude);
                } else {
                    preparedStatement.setDouble(11, 0.0);
                    preparedStatement.setDouble(12, 0.0);
                }
                if(completionLatitude != null && completionLongitude != null) {
                    preparedStatement.setDouble(13, completionLatitude);
                    preparedStatement.setDouble(14, completionLongitude);
                } else {
                    preparedStatement.setDouble(13, 0.0);
                    preparedStatement.setDouble(14, 0.0);
                }

                preparedStatement.execute();

                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    public boolean updateBalances(User user1, User user2, float amt) {
        UserChecker uc = new UserChecker();
        float newBalance1 = uc.getBalance(user1) + amt;
        float newBalance2 = uc.getBalance(user2) - amt;
        if(connection != null) {
            try {
                PreparedStatement updateBalance1 = connection.prepareStatement(
                        "UPDATE balances " +
                                "SET balance='" + newBalance1 + "' " +
                                "WHERE user_ID='" + user1.getUserHash() + "';");
                updateBalance1.executeUpdate();

                PreparedStatement updateBalance2 = connection.prepareStatement(
                        "UPDATE balances " +
                                "SET balance='" + newBalance2 + "' " +
                                "WHERE user_ID='" + user2.getUserHash() + "';");
                updateBalance2.executeUpdate();

                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
