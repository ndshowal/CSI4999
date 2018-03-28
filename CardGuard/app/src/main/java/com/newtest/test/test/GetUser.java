package com.newtest.test.test;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Anthony on 3/28/2018.
 */

public class GetUser extends AsyncTask {
    private Connection connection;

    //DB Connection strings
    final String host = "cardguard-db.mysql.database.azure.com:3306";
    final String database = "cardguard_db";
    final String adminUsername = "cardguard-admin@cardguard-db";
    final String adminPassword = "password12345!";

    String username;
    User user;

    public GetUser(String in) {
        username = in;
        user = null;
    }

    public User getUser() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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

                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(
                        "SELECT * FROM users "
                                + "WHERE username='" + username + "';");

                while (results.next()) {
                    String ID = results.getString(1);
                    String username = results.getString(2);
                    String password = results.getString(3);
                    String emailAddress = results.getString(4);
                    String firstName = results.getString(5);
                    String lastName = results.getString(6);
                    String accountType = results.getString(7);

                    user = new User(ID, username, password, firstName, lastName, emailAddress, accountType);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return user;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
