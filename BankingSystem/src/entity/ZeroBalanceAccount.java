package entity;

public class ZeroBalanceAccount extends Account {
    // Default constructor for ZeroBalanceAccount
    public ZeroBalanceAccount() {
        super("Zero Balance", 0.0, null);
    }

    // Overloaded constructor for ZeroBalanceAccount
    public ZeroBalanceAccount(Customer customer) {
        super("Zero Balance", 0.0, customer);
    }

    // Method to print zero balance account information
    @Override
    public void printAccountInfo() {
        super.printAccountInfo();
    }
}
