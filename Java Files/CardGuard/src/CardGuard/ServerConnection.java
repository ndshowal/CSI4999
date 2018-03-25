package CardGuard;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.util.Properties;

public class ServerConnection {
    private Connection connection;

    //DB Connection strings
    private final String host = "cardguard-db.mysql.database.azure.com:3306";
    private final String database = "cardguard_db";
    private final String adminUsername = "cardguard-admin@cardguard-db";
    private final String adminPassword = "password12345!";

    //For DB query, nothing to do with actual DB connection
    private String username;
    private String password;

    //Also nothing to do with DB connection
    private User user;

    public ServerConnection(String username, String password) {
        this.username = username;
        this.password = password;

        user = null;
    }

    public User connect() throws SQLException {

        // check that the driver is installed
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
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
            // SQL Query
            try {
                System.out.println("Attempting to query database...");
                
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
                System.out.println("Query Error");
                System.out.print(ex.toString());
            }
        }

        return user;
    }
}
