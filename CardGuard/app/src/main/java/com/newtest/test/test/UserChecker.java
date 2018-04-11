package com.newtest.test.test;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Properties;

public class UserChecker extends AsyncTask {
    private Connection connection;
    boolean exists;

    public UserChecker() {
        exists = false;

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
    }

    protected boolean hasBalance(String in) {
        String hash = in;

        if(connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                        "SELECT * FROM balances "
                                + "WHERE user_ID='" + hash + "';");

                if (result.next()) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //Returns the raw float value of the the user's balance
    protected float getBalance(User user) {
        String hash = user.getUserHash();

        float bal = 0;

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                        "SELECT balance FROM balances "
                                + "WHERE user_ID='" + hash + "';");

                while (result.next()) {
                    bal = result.getFloat(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bal;
    }

    //Returns a formatted String version of the user's balance
    protected String getFormattedBalance(User user) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(getBalance(user));
    }

    boolean usernameExists(final String in) throws SQLException {
        String username = in;

        if (connection != null) {
            try {
                System.out.println("Attempting to query database...");

                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(
                        "SELECT username FROM users "
                                + "WHERE username='" + username + "';");

                while (results.next()) {
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    boolean emailExists(final String in) throws SQLException {
        String emailAddress = in;

        if (connection != null) {
            try {
                System.out.println("Attempting to query database...");

                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(
                        "SELECT email_address FROM users "
                                + "WHERE email_address='" + emailAddress + "';");

                while (results.next()) {
                    return true;
                }
            } catch (Exception ex) {
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
