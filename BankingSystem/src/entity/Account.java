package entity;

public class Account {
    private static long lastAccNo = 1000; // Starting account number
    private long account_id;
    private String account_type;
    private double balance;
    private Customer customer_id;

    // Default constructor
    public Account() {
        this.account_id = generateAccount_id();
    }

    // Overloaded constructor
    public Account(String accountType, double accountBalance, Customer customer) {
        this.account_id = lastAccNo;
        this.account_type = accountType;
        this.balance = accountBalance;
        this.customer_id = customer;
    }

    public Account(long accNo, String accountType, double accountBalance, Customer customer) {
        this.account_id = accNo;
        this.account_type = accountType;
        this.balance = accountBalance;
        this.customer_id = customer;
    }

    // Getter methods
    public long getAccount_id() {
        return account_id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getCustomer() {
        return customer_id;
    }

    // Setter methods
    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public void setAccountBalance(double accountBalance) {
        this.balance = accountBalance;
    }

    public void setCustomer(Customer customer) {
        this.customer_id = customer;
    }

    // Method to generate a unique account number
    public static long generateAccount_id() {
        return ++lastAccNo;
    }

    // Method to print account information
    public void printAccountInfo() {
        System.out.println("Account Number: " + account_id);
        System.out.println("Account Type: " + account_type);
        System.out.println("Account Balance: $" + balance);
        System.out.println("Customer Information:");
        customer_id.printInfo();
    }

    public void withdraw(double amount) {
        // TODO Auto-generated method stub

    }
}