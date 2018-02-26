package CardGuard;

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
    private Boolean isProcessed;
    private Boolean isVerified;
    
    // CONSTRUCTOR //
    public Transaction(Date transactionStartDate, User initiator,
            User sender, User recipent, double transactionAmount, String memo, 
            Boolean isProcessed, Boolean isVerified) {
        this.transactionStartDate = transactionStartDate;
        this.initiator = initiator;
        this.sender = sender;
        this.recipient = recipient;
        this.transactionAmount = transactionAmount;
        this.memo = memo;
        this.isProcessed = isProcessed;
        this.isVerified = isVerified;
        createTransactionID();
    }
    
    public String toString() {
        return "Transaction ID: " + getTransactionID()
                + " Transaction Initiated:" + getTransactionStartDateString() 
                + " Transaction Completed:" + getTransactionCompleteDateString() 
                + " Initiated By: " + initiator.getUsername()
                + " Sender: " + sender.getUsername()
                + " Recipient: " + recipient.getUsername() 
                + " Transaction Amount: " + getTransactionAmount()
                + " Memo: " + getMemo()
                + " Processed: " + isProcessed()
                + " Verified: " + isVerified();
    }
    
    // Creates a hash of the Transaction Initiation Date, the usernames of the
    //  initiator of the transaction, the sender, and the recipient, and the
    //  transaction amount.
    
    public void createTransactionID() {
        String toBeHashed = getTransactionStartDate().toString()
                + initiator.getUsername() 
                + getSender().getUsername()
                + getRecipient().getUsername() 
                + String.valueOf(getTransactionAmount());
        int transactionID = toBeHashed.hashCode();
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
    
    public Boolean isProcessed() {
        return isProcessed;
    }
    
    public Boolean isVerified() {
        return isVerified;
    }
    
    // SETTERS //
    public void setTransactionStartDate(Date in) {
        transactionStartDate = in;
    }
    
    public void setTransactionCompleteDate(Date in) {
        transactionCompleteDate = in;
    }
}
