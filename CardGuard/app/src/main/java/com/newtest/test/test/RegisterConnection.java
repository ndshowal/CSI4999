package com.newtest.test.test;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class RegisterConnection extends AsyncTask {
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

    public RegisterConnection(String firstName, String lastName, String username, String emailAddress, String password) {
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

        //System.out.println("MySQL JDBC driver detected in library path.");

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
            // SQL Query
            try {
                int rowsUpdated = 0;

                System.out.println("Attempting to query database...");

                Statement statement1 = connection.createStatement();
                ResultSet results1 = statement1.executeQuery(
                        "SELECT * FROM users "
                                + "WHERE username='" + username +"';");
                Statement statement2 = connection.createStatement();
                ResultSet results2 = statement2.executeQuery(
                        "SELECT * FROM users "
                                + "WHERE email_address='" + emailAddress + "';");

                if(!results1.next() && !results2.next()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(first_name, last_name, username, email_address, password, account_type)" +
                            " VALUES(?,?,?,?,?,?)");
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setString(3, username);
                    preparedStatement.setString(4, emailAddress);
                    preparedStatement.setString(5, password);
                    preparedStatement.setString(6, accountType);
                    rowsUpdated += preparedStatement.executeUpdate();

                    System.out.println("Rows updated: " + rowsUpdated);
                }

                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(
                        "SELECT * FROM users "
                                + "WHERE username='" + username
                                +"' AND password ='" + password +"';");
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
            } catch (SQLException ex) {
                System.out.println("Query Error:");
                ex.printStackTrace();
            }

            if(user != null) {
                System.out.println(user.toString());
            }
        }

        return user;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
