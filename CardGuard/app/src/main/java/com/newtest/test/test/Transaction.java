package com.newtest.test.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.IDN;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Build.ID;

public class Transaction implements Parcelable {
    
    private String transactionID;
    private Date transactionStartDate;
    private Date transactionCompleteDate;
    private User initiator;
    private User sender;
    private User recipient;
    private float transactionAmount;
    private String memo;
    private Boolean inProgress;
    private Boolean confirmed;
    
    // CONSTRUCTOR //
    public Transaction(String transactionID, User sender, User recipient, User initiator,
                       float transactionAmount, String memo, Date transactionStartDate, Date transactionCompleteDate,
            Boolean inProgress, Boolean confirmed) {
        this.transactionStartDate = transactionStartDate;
        this.initiator = initiator;
        this.sender = sender;
        this.recipient = recipient;
        this.transactionAmount = transactionAmount;
        this.memo = memo;
        this.inProgress = inProgress;
        this.confirmed = confirmed;
        this.transactionID = transactionID;
    }

    protected Transaction(Parcel in) {
        transactionID = in.readString();
        initiator = in.readParcelable(User.class.getClassLoader());
        sender = in.readParcelable(User.class.getClassLoader());
        recipient = in.readParcelable(User.class.getClassLoader());
        transactionAmount = in.readFloat();
        memo = in.readString();
        byte tmpInProgress = in.readByte();
        inProgress = tmpInProgress == 0 ? null : tmpInProgress == 1;
        byte tmpConfirmed = in.readByte();
        confirmed = tmpConfirmed == 0 ? null : tmpConfirmed == 1;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    //Returns a string representation of this object
    @Override
    public String toString() {
        return "Transaction ID: " + getTransactionID()
                + " \nTransaction Initiated:" + getTransactionStartDateString()
                + " \nTransaction Completed:" + getTransactionCompleteDateString()
                + " \nInitiated By: " + initiator.getUsername()
                + " \nSender: " + sender.getUsername()
                + " \nRecipient: " + recipient.getUsername()
                + " \nTransaction Amount: " + getTransactionAmount()
                + " \nMemo: " + getMemo()
                + " \nIn Progress: " + inProgress()
                + " \nConfirmed: " + isConfirmed();
    }

    // GETTERS //
    public String getTransactionID() {
        return transactionID;
    }
    
    public Date getTransactionStartDate() {
        return transactionStartDate;
    }

    public String getTransactionStartDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String transactionStartDateString = sdf.format(transactionStartDate);
        
        return transactionStartDateString;
    }
    
    public Date getTransactionCompleteDate() {
        if(transactionCompleteDate != null) {
            return transactionCompleteDate;
        } else {
            return null;
        }
    }
    
    public String getTransactionCompleteDateString() {
        if(getTransactionCompleteDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
            String transactionCompleteDateString = sdf.format(transactionCompleteDate);
        
            return transactionCompleteDateString;
        } else {
            return "Transaction not yet completed.";
        }
    }
    
    public User getInitiator() {
        return initiator;
    }
    
    public User getSender() {
        return sender;
    }
    
    public User getRecipient() {
        return recipient;
    }
    
    public float getTransactionAmount() {
        return transactionAmount;
    }
    
    public String getMemo() {
        return memo;
    }
    
    public Boolean inProgress() {
        return inProgress;
    }
    
    public Boolean isConfirmed() {
        return confirmed;
    }

    // SETTERS //

    // Creates a hash of the Transaction Initiation Date, the usernames of the
    //  initiator of the transaction, the sender, and the recipient, and the
    //  transaction amount.
    public void setTransactionID() {
        String toBeHashed = getTransactionStartDate().toString()
                + initiator.getUsername()
                + getSender().getUsername()
                + getRecipient().getUsername()
                + String.valueOf(getTransactionAmount());
        int transactionID = toBeHashed.hashCode();
    }

    public void setTransactionStartDate(Date in) {
        transactionStartDate = in;
    }
    
    public void setTransactionCompleteDate(Date in) {
        transactionCompleteDate = in;
    }

    public void setInitiator(User in) {
        initiator = in;
    }

    public void setSender(User in) {
        sender = in;
    }

    public void setRecipient(User in) {
        recipient = in;
    }

    public void setTransactionAmount(Float in) {
        transactionAmount = in;
    }

    public void setMemo(String in) {
        memo = in;
    }

    public void setInProgress(Boolean in) {
        inProgress = in;
    }

    public void setConfirmed(Boolean in) {
        confirmed = in;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTransactionID());
        parcel.writeString(getTransactionStartDateString());
        parcel.writeString(getTransactionCompleteDateString());
        parcel.writeParcelable(getSender(), i);
        parcel.writeParcelable(getRecipient(), i);
        parcel.writeParcelable(getInitiator(), i);
        parcel.writeFloat(getTransactionAmount());
        parcel.writeString(getMemo());
        parcel.writeByte((byte) (inProgress() ? 1 : 0));
        parcel.writeByte((byte) (isConfirmed() ? 1 : 0));
    }
}
