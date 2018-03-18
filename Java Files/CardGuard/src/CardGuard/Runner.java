package CardGuard;

import java.sql.SQLException;

public class Runner {
    public static void main(String[] args) throws SQLException {
        ServerConnection conn = new ServerConnection("ampolito", "qwert");
        
        User user = conn.connect();
        System.out.println(user.toString());
    }
    
}
