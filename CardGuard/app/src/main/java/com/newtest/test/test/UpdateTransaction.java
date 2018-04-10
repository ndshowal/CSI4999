package com.newtest.test.test;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;

import javax.xml.transform.Result;

public class UpdateTransaction extends AsyncTask {
    Transaction tx;

    private final String host = "cardguard-db.mysql.database.azure.com:3306";
    private final String database = "cardguard_db";
    private final String adminUsername = "cardguard-admin@cardguard-db";
    private final String adminPassword = "password12345!";

    private Connection connection;

    public UpdateTransaction(Transaction tx) {
        this.tx = tx;
    }

    public void approve(Boolean in) {
        if(in) {
            tx.setApproved(true);
            tx.setInProgress(false);
        } else {
            tx.setApproved(false);
            tx.setInProgress(false);
        }
    }

    public boolean update() throws SQLException {
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
            String txID = tx.getTransactionID();
            java.sql.Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            PreparedStatement updateCompletionDate = connection.prepareStatement(
                    "UPDATE `transaction` " +
                            "SET `completion_date`='" + currentDate + "' " +
                            "WHERE `transaction_ID`= '" + txID + "'");
        }
        return false;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
