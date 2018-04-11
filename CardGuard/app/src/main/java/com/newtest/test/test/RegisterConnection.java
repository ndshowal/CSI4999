package com.newtest.test.test;

import android.os.AsyncTask;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class RegisterConnection extends AsyncTask {
    private final String TAG = "RegisterConnection";
    private Connection connection;

    //DB Connection strings
    private final String host = "cardguard-db.mysql.database.azure.com:3306";
    private final String database = "cardguard_db";
    private final String adminUsername = "cardguard-admin@cardguard-db";
    private final String adminPassword = "password12345!";

    //For DB query, nothing to do with actual DB connection
    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    private String password;
    private String accountType;

    //Also nothing to do with DB connection
    private User user;

    public RegisterConnection(String firstName, String lastName, String username, String emailAddress, String password) throws SQLException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.accountType = "Standard";

        user = null;

        System.out.println("Created RegisterConnection object successfully.");

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
    }

    public User connect() throws SQLException {
        if (connection != null) {
            // SQL Queries - 1st check if any users exist with the given username, and then again
            //  if any exist with the given email address. If both checks pass, create new entry
            //  into users table.
            try {
                int rowsUpdated = 0;

                System.out.println("Attempting to query database...");


                // If both checks pass, create new user entry
                if(!(new UserChecker().usernameExists(username)) && !(new UserChecker().emailExists(emailAddress))) {
                    String toBeHashed = firstName + lastName + username;
                    String userHash = toBeHashed;
                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        byte[] hashed = md.digest(toBeHashed.getBytes());

                        StringBuilder hexString = new StringBuilder();

                        for (int i = 0; i < hashed.length; i++) {
                            String hex = Integer.toHexString(0xFF & hashed[i]);
                            if (hex.length() == 1) {
                                hexString.append('0');
                            }
                            hexString.append(hex);
                        }
                        userHash = hexString.toString();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        userHash = toBeHashed;
                    }

                    PreparedStatement createUserTableEntry = connection.prepareStatement("INSERT INTO users(first_name, last_name, username, email_address, password, account_type, user_ID)" +
                            " VALUES(?,?,?,?,?,?,?)");
                    createUserTableEntry.setString(1, firstName);
                    createUserTableEntry.setString(2, lastName);
                    createUserTableEntry.setString(3, username);
                    createUserTableEntry.setString(4, emailAddress);
                    createUserTableEntry.setString(5, password);
                    createUserTableEntry.setString(6, accountType);
                    createUserTableEntry.setString(7, userHash);

                    createUserTableEntry.executeUpdate();

                    //And create a new entry in the balance table
                    PreparedStatement createBalanceTableEntry = connection.prepareStatement("INSERT INTO balances(user_ID, balance) VALUES(?,?)");

                    createBalanceTableEntry.setString(1, userHash);
                    createBalanceTableEntry.setFloat(2, 0);

                    createBalanceTableEntry.executeUpdate();

                } else {
                    //Return null if user wasn't created due to name/email conflict
                    return null;
                }

                // Query the DB again, same code as SignInConnection - returns the new user object
                //  but only if new entry was created
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(
                        "SELECT * FROM users "
                                + "WHERE username='" + username
                                +"' AND password ='" + password +"';");
                while (results.next()) {
                    String ID = results.getString(1);
                    String userHash = results.getString(2);
                    String username = results.getString(3);
                    String password = results.getString(4);
                    String emailAddress = results.getString(5);
                    String firstName = results.getString(6);
                    String lastName = results.getString(7);
                    String accountType = results.getString(8);

                    user = new User(ID, username, password, firstName, lastName, emailAddress, accountType);
                }
            } catch (SQLException ex) {
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
