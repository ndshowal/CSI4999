package com.newtest.test.test;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Double initialLatitude;
    private Double initialLongitude;
    private Double completionLatitude;
    private Double completionLongitude;
    
    // CONSTRUCTOR //
    public Transaction(String transactionID, User sender, User recipient, User initiator,
                       float transactionAmount, String memo, Date transactionStartDate, Date transactionCompleteDate,
            Boolean inProgress, Boolean confirmed) {
        this.transactionStartDate = transactionStartDate;
        this.transactionCompleteDate = transactionCompleteDate;
        this.initiator = initiator;
        this.sender = sender;
        this.recipient = recipient;
        this.transactionAmount = transactionAmount;
        this.memo = memo;
        this.inProgress = inProgress;
        this.confirmed = confirmed;
        this.transactionID = transactionID;
    }

    public Transaction(User sender, User recipient, User initiator,
                       float transactionAmount, String memo, Date transactionStartDate, Date transactionCompleteDate,
                       Boolean inProgress, Boolean confirmed) {
        this.transactionStartDate = transactionStartDate;
        this.transactionCompleteDate = transactionCompleteDate;
        this.initiator = initiator;
        this.sender = sender;
        this.recipient = recipient;
        this.transactionAmount = transactionAmount;
        this.memo = memo;
        this.inProgress = inProgress;
        this.confirmed = confirmed;
        setTransactionID();
    }

    protected Transaction(Parcel in) {
        transactionID = in.readString();
        long tmpTransactionStartDate = in.readLong();
        transactionStartDate = tmpTransactionStartDate != -1 ? new Date(tmpTransactionStartDate) : null;
        long tmpTransactionCompleteDate = in.readLong();
        transactionCompleteDate = tmpTransactionCompleteDate != -1 ? new Date(tmpTransactionCompleteDate) : null;
        initiator = (User) in.readValue(User.class.getClassLoader());
        sender = (User) in.readValue(User.class.getClassLoader());
        recipient = (User) in.readValue(User.class.getClassLoader());
        transactionAmount = in.readFloat();
        memo = in.readString();
        byte inProgressVal = in.readByte();
        inProgress = inProgressVal == 0x02 ? null : inProgressVal != 0x00;
        byte confirmedVal = in.readByte();
        confirmed = confirmedVal == 0x02 ? null : confirmedVal != 0x00;
        initialLatitude = in.readByte() == 0x00 ? null : in.readDouble();
        initialLongitude = in.readByte() == 0x00 ? null : in.readDouble();
        completionLatitude = in.readByte() == 0x00 ? null : in.readDouble();
        completionLongitude = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(transactionID);
        dest.writeLong(transactionStartDate != null ? transactionStartDate.getTime() : -1L);
        dest.writeLong(transactionCompleteDate != null ? transactionCompleteDate.getTime() : -1L);
        dest.writeValue(initiator);
        dest.writeValue(sender);
        dest.writeValue(recipient);
        dest.writeFloat(transactionAmount);
        dest.writeString(memo);
        if (inProgress == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (inProgress ? 0x01 : 0x00));
        }
        if (confirmed == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (confirmed ? 0x01 : 0x00));
        }
        if (initialLatitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(initialLatitude);
        }
        if (initialLongitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(initialLongitude);
        }
        if (completionLatitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(completionLatitude);
        }
        if (completionLongitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(completionLongitude);
        }
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
                + " \nConfirmed: " + isCompleted();
    }

    public String getSimpleDescription() {
        return getTransactionStartDateString().substring(0, 10)
                + " \nFrom: " + getSender().getUsername()
                + " To: " + getRecipient().getUsername()
                + " \nAmount: " + getFormattedAmount()
                + " \nFor: " + getMemo();
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

    public String getFormattedAmount() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(getTransactionAmount());
    }
    
    public String getMemo() {
        return memo;
    }
    
    public Boolean inProgress() {
        return inProgress;
    }
    
    public Boolean isCompleted() {
        return confirmed;
    }

    public Double getInitialLatitude() {
        return initialLatitude;
    }

    public Double getInitialLongitude() {
        return initialLongitude;
    }

    public Double getCompletionLatitude() {
        return completionLatitude;
    }

    public Double getCompletionLongitude() {
        return completionLongitude;
    }

    public Location getInitialLocation() {
        Location loc = null;
        loc.setLatitude(getInitialLatitude());
        loc.setLongitude(getInitialLongitude());

        return loc;
    }

    public Location getCompletionLocation() {
        Location loc = null;
        loc.setLatitude(getCompletionLatitude());
        loc.setLongitude(getCompletionLongitude());
        return loc;
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

            transactionID = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

    public void setApproved(Boolean in) {
        confirmed = in;
    }

    public void setInitialLatitude(Double in) {
        initialLatitude = in;
    }

    public void setInitialLongitude(Double in) {
        initialLongitude = in;
    }

    public void setInitialLocation(Location loc) {
        initialLatitude = loc.getLatitude();
        initialLongitude = loc.getLongitude();
    }

    public void setCompletionLatitude(Double in) {
        completionLatitude = in;
    }

    public void setCompletionLongitude(Double in) {
        completionLongitude = in;
    }

    public void setCompletionLocation(Location loc) {
        completionLatitude = loc.getLatitude();
        completionLongitude = loc.getLongitude();
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
