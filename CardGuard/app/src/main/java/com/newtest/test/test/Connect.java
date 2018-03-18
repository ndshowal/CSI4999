package com.newtest.test.test;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connect {

    public void read () {
        String host = "cardguard-db.mysql.database.azure.com";
        String database = "cardguard_db";
        String user = "cardguard-admin@cardguard-db";
        String password = "password12345!";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(Exception ex) {
            Log.e(ex.toString(), String.valueOf(ex));
        }

        Connection connection = null;

        try {
            String url = String.format("jdbc:mysql://%s/%s", host, database);

            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("useSSL", "true");
            properties.setProperty("verifyServerCertificate", "true");
            properties.setProperty("requireSSL", "false");

            // get connection
            connection = DriverManager.getConnection(url, properties);

        } catch (SQLException ex) {
            Log.e("Error", String.valueOf(ex));
        }

    }
}