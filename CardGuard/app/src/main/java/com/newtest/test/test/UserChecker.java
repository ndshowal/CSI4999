package com.newtest.test.test;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class UserChecker extends AsyncTask {
    private Connection connection;
    boolean exists;

    public UserChecker() {
        exists = false;
    }

    public boolean usernameExists(final String in) throws SQLException{
                //DB Connection strings
                final String host = "cardguard-db.mysql.database.azure.com:3306";
                final String database = "cardguard_db";
                final String adminUsername = "cardguard-admin@cardguard-db";
                final String adminPassword = "password12345!";

                String username = in;

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

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery(
                                "SELECT * FROM users "
                                        + "WHERE username='" + username + "';");

                        while(results.next()) {
                            exists = true;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        return exists;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
