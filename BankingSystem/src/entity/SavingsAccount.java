package entity;

public class SavingsAccount extends Account {
    private double interestRate;

    // Default constructor for SavingsAccount
    public SavingsAccount() {
        super("Savings", 500.0, null);
    }

    // Overloaded constructor for SavingsAccount
    public SavingsAccount(double interestRate, Customer customer) {
        super("Savings", 500.0, customer);
        this.interestRate = interestRate;
    }

    // Getter and setter for interest rate
    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    // Method to print savings account information
    @Override
    public void printAccountInfo() {
        super.printAccountInfo();
        System.out.println("Interest Rate: " + interestRate + "%");
    }
}

