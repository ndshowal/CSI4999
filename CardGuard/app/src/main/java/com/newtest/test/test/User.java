package com.newtest.test.test;

import java.util.ArrayList;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String accountType;
    private ArrayList<Transaction> transactions;
    
    // CONSTRUCTOR //
    public User(String username, String firstName, String lastName,
            String emailAddress, String accountType) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.accountType = accountType;
    }

    /// Returns a string representation of this object
    @Override
    public String toString() {
        return "Username: " + getUsername() 
                + " First Name: " + getFirstName() 
                + " Last Name: " + getLastName() 
                + " Email Address" + getEmailAddress();
    }
    
    // GETTERS //
    public String getUsername() {
        return username;
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
