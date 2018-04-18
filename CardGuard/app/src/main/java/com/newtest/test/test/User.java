package com.newtest.test.test;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ProgressBar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;import java.util.ArrayList;

public class User implements Parcelable{
    private String ID;
    private String userHash;
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
        this.transactions = new ArrayList<Transaction>();

        setUserHash();
    }

    // CONSTRUCTOR //
    public User(String ID, String userHash, String username, String password, String firstName, String lastName,
                String emailAddress, String accountType) {
        this.ID = ID;
        this.userHash = userHash;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();

        setUserHash();
    }

    protected User(Parcel in) {
        ID = in.readString();
        userHash = in.readString();
        password = in.readString();
        username = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        emailAddress = in.readString();
        accountType = in.readString();
        if (in.readByte() == 0x01) {
            transactions = new ArrayList<Transaction>();
            in.readList(transactions, Transaction.class.getClassLoader());
        } else {
            transactions = null;
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /// Returns a string representation of this object
    @Override
    public String toString() {
        return "Username: " + getUsername()
                + " First Name: " + getFirstName()
                + " Last Name: " + getLastName()
                + " Email Address: " + getEmailAddress();
    }

    // GETTERS //
    public String getID() {
        return ID;
    }

    protected String getUserHash() {
        return userHash;
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

    //Returns the list of transactions where the passed in string matches one of the 3 possible
    // username actors of a transaction
    public ArrayList<Transaction> getTransactions(String in) {
        ArrayList<Transaction> temp = new ArrayList<>();

        if(in.equals("")) {
            return getTransactions();
        }

        for (Transaction tx : getTransactions()) {
            if (tx.getInitiator().getUsername().equals(in) ||
                    tx.getSender().getUsername().equals(in) ||
                    tx.getRecipient().getUsername().equals(in)) {
                temp.add(tx);
            }
        }

        return temp;
    }

    //Creates a hash by concatenating the user's first name, last name and username into
    // a single word and running it through the MD5 algorithm. This hash is used to store fund
    // balance information into the database
    public void setUserHash() {
        String toBeHashed = firstName + lastName + username;
        userHash = toBeHashed;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(userHash);
        dest.writeString(password);
        dest.writeString(username);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(emailAddress);
        dest.writeString(accountType);
        if (transactions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(transactions);
        }
    }
}
