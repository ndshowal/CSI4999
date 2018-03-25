package com.newtest.test.test;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class UpdateConnection extends AsyncTask {
    private final String TAG = "RegisterConnection";
    private Connection connection;

    //DB Connection strings
    private final String host = "cardguard-db.mysql.database.azure.com:3306";
    private final String database = "cardguard_db";
    private final String adminUsername = "cardguard-admin@cardguard-db";
    private final String adminPassword = "password12345!";

    //For DB query, nothing to do with actual DB connection
    private String ID;
    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    private String password;
    private String accountType;

    //Also nothing to do with DB connection
    private User user;

    public UpdateConnection(String ID, String firstName, String lastName, String username, String emailAddress, String password) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.accountType = "Standard";

        user = null;

        System.out.println("Created RegisterConnection object successfully.");
    }

    public User connect() throws SQLException {

        // check that the driver is installed
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("JDBC Driver not found.");
            ex.printStackTrace();
        }

        System.out.println("MySQL JDBC driver detected in library path.");

        // Initialize connection object
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

            // Get connection
            connection = DriverManager.getConnection(url, properties);

            System.out.println(properties.toString());
        } catch (SQLException ex) {
            throw new SQLException("Failed to create connection to database.", ex);
        }

        if (connection != null) {
            // SQL Queries - 1st check if any users exist with the given username, and then again
            //  if any exist with the given email address. If both checks pass, update entry
            //  of users table.
            try {
                int rowsUpdated = 0;

                System.out.println("Attempting to query database...");

                // If both checks pass, update entry
                if(!checkExistingEmailAddress(connection)) {
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users " +
                            "SET first_name='" + firstName + "', " +
                            "last_name='" + lastName + "', " +
                            "username='" + username + "', " +
                            "email_address='" + emailAddress + "', " +
                            "password='" + password + "' " +
                            "WHERE id=" + ID +";");
                    rowsUpdated += preparedStatement.executeUpdate();

                    // Close statement when done (Best practices)
                    preparedStatement.close();

                    System.out.println("Rows updated: " + rowsUpdated);
                }

                // Query the DB again, same code as SignInConnection - merely to ensure a new user
                //  object is instantiated only if new entry was created
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(
                        "SELECT * FROM users "
                                + "WHERE username='" + username +"';");
                while (results.next()) {
                    String ID = results.getString(1);
                    String username = results.getString(2);
                    String password = results.getString(3);
                    String emailAddress = results.getString(4);
                    String firstName = results.getString(5);
                    String lastName = results.getString(6);
                    String accountType = results.getString(7);

                    statement.close();  // Close statement
                    user = new User(ID, username, password, firstName, lastName, emailAddress, accountType);
                }
            } catch (SQLException ex) {
                System.out.println("Query Error:");
                ex.printStackTrace();
            }

            if(user != null) {
                System.out.println(user.toString());
            }
        }

        connection.close(); // Close connection
        return user;
    }

    // MySQL Query - checks the table for an existing entry with a specified username
    public Boolean checkExistingUsername(Connection connection) {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(
                    "SELECT * FROM users "
                            + "WHERE username='" + username +"';");

            if(results.next()) {
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return false;
    }

    // MySQL Query - checks the table for an existing entry with a specified email address
    public Boolean checkExistingEmailAddress(Connection connection) {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(
                    "SELECT * FROM users "
                            + "WHERE email_address='" + emailAddress + "';");
            if(results.next()) {
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
