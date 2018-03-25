package CardGuard;

import java.util.ArrayList;

import java.util.ArrayList;

public class User {
    private String ID;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String accountType;
    private ArrayList<Transaction> transactions;
    
    // CONSTRUCTOR //
    public User(String ID, String username, String password, String firstName, String lastName,
            String emailAddress, String accountType) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.accountType = accountType;
    }

    /// Returns a string representation of this object
    @Override
    public String toString() {
        return "Username: " + getUsername() 
                + "\nFirst Name: " + getFirstName() 
                + "\nLast Name: " + getLastName() 
                + "\nEmail Address: " + getEmailAddress();
    }
    
    // GETTERS //
    public String getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    
    // SETTERS //
    public void setID(String in) {
        ID = in;
    }

    public void setPassword(String in) {
        password = in;
    }

    public void setUsername(String in) {
        username = in;
    }
    
    public void setFirstName(String in) {
        firstName = in;
    }
    
    public void setLastName(String in) {
        lastName = in;
    }
    
    public void setEmailAddress(String in) {
        emailAddress = in;
    }
    
    public void setAccountType(String in) {
        accountType = in;
    }
    
    // Adds a transaction to the arraylist of transactions for this user
    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }
}
