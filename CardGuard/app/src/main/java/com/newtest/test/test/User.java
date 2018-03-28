package com.newtest.test.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;import java.util.ArrayList;

public class User implements Parcelable{
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
        this.transactions = new ArrayList<Transaction>();
    }

    protected User(Parcel in) {
        ID = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
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
