
package entity;

import java.util.Date;

public class Transaction {
    private Account account_id;
    private Transaction transaction_id;
    private Date transaction_date;
    private String transaction_type;
    private double amount;

    // Constructor
    public Transaction(Account account, String description,String transactionType, double transactionAmount) {
        this.account_id = account;
        this.transaction_date = new Date(); // Current date and time
        this.transaction_type = transactionType;
        this.amount = transactionAmount;

    }
    public Transaction(Account account, String description,String transactionType, double transactionAmount,Date datetime) {
        this.account_id = account;
        this.transaction_date = datetime; // Current date and time
        this.transaction_type = transactionType;
        this.amount = transactionAmount;
    }

    // Getter methods
    public Account getAccount_id() {
        return account_id;
    }

    public Transaction getTransaction_id() {
        return transaction_id;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public double getAmount() {
        return amount;
    }
}

