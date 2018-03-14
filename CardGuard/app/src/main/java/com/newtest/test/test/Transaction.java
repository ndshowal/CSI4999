package com.newtest.test.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    
    private String transactionID;
    private Date transactionStartDate;
    private Date transactionCompleteDate;
    private User initiator;
    private User sender;
    private User recipient;
    private double transactionAmount;
    private String memo;
    private Boolean inProgress;
    private Boolean confirmed;
    
    // CONSTRUCTOR //
    public Transaction(Date transactionStartDate, User initiator,
            User sender, User recipient, double transactionAmount, String memo,
            Boolean inProgress, Boolean confirmed) {
        this.transactionStartDate = transactionStartDate;
        this.initiator = initiator;
        this.sender = sender;
        this.recipient = recipient;
        this.transactionAmount = transactionAmount;
        this.memo = memo;
        this.inProgress = inProgress;
        this.confirmed = confirmed;
        setTransactionID();
    }

    //Returns a string representation of this object
    @Override
    public String toString() {
        return "Transaction ID: " + getTransactionID()
                + " Transaction Initiated:" + getTransactionStartDateString() 
                + " Transaction Completed:" + getTransactionCompleteDateString() 
                + " Initiated By: " + initiator.getUsername()
                + " Sender: " + sender.getUsername()
                + " Recipient: " + recipient.getUsername() 
                + " Transaction Amount: " + getTransactionAmount()
                + " Memo: " + getMemo()
                + " In Progress: " + inProgress()
                + " Confirmed: " + isConfirmed();
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
    
    public double getTransactionAmount() {
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

    public void setTransactionAmount(Double in) {
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
}
