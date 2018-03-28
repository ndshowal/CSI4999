package com.newtest.test.test;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

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
    private Date startDate;
    private Date completionDate;
    private boolean inProgress;
    private boolean completed;

    private java.util.Date sDate = new Date();
    Object param = new java.sql.Timestamp(sDate.getTime());

    Transaction tx;

    public boolean upload(Transaction tx) throws SQLException{
        this.tx = tx;
        transactionID = tx.getTransactionID();
        senderName = tx.getSender().getUsername();
        recipientName = tx.getRecipient().getUsername();
        initiatorName = tx.getInitiator().getUsername();
        transactionAmount = tx.getTransactionAmount();
        memo = tx.getMemo();
        startDate = tx.getTransactionStartDate();
        completionDate = tx.getTransactionCompleteDate();
        inProgress = tx.inProgress();
        completed = tx.isCompleted();

        java.sql.Date sqlDate = new java.sql.Date(startDate.getTime());

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

        if(connection != null) {
            try {
                int rowsUpdated = 0;

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions" +
                        "(transaction_ID, sender, recipient, initiator, transaction_amount, " +
                        "memo, start_date, completion_date, in_progress, confirmed)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?)");
                preparedStatement.setString(1, transactionID);
                preparedStatement.setString(2, senderName);
                preparedStatement.setString(3, recipientName);
                preparedStatement.setString(4, initiatorName);
                preparedStatement.setFloat(5, transactionAmount);
                preparedStatement.setString(6, memo);
                preparedStatement.setObject(7, sqlDate);
                preparedStatement.setDate(8, null);
                preparedStatement.setBoolean(9, inProgress);
                preparedStatement.setBoolean(10, completed);
                rowsUpdated += preparedStatement.executeUpdate();

                return true;
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

        return false;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
