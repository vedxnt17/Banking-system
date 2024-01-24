package dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

import entity.Account;
import entity.Customer;
import entity.Transaction;
import util.DBUtil;
import exception.*;

public class BankRepositoryImpl implements IBankRepository {

	Connection con=null;
	BankRepositoryImpl()
	{
		this.con=DBUtil.getDBConn();
	}

	@Override
	public void createAccount(Customer customer, long accNo, String accType, float balance) {
		
		try {
	        
	        String sql = "INSERT INTO Customers (customer_id,first_name, last_name, email, phone_number, address) VALUES (?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            
	        	preparedStatement.setInt(1, customer.getCustomer_id());
	            preparedStatement.setString(2, customer.getFirst_name());
	            preparedStatement.setString(3, customer.getLast_name());
	            preparedStatement.setString(4, customer.getEmail());
	            preparedStatement.setLong(5, customer.getPhone_number());
	            preparedStatement.setString(6, customer.getAddress());

	      
	            preparedStatement.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		try {
	       
	        String sql = "INSERT INTO Accounts (account_id, account_type, balance, customer_id) VALUES (?, ?, ?, ?)";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            preparedStatement.setLong(1, accNo);
	            preparedStatement.setString(2, accType);
	            preparedStatement.setFloat(3, balance);
	            preparedStatement.setInt(4, customer.getCustomer_id());

	      
	            preparedStatement.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	}

	@Override
	public List<Account> listAccounts() {
		
		List<Account> accounts = new ArrayList<>();

        try {
         
            String sql = "SELECT * FROM Accounts";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Extract data from the result set
                        long account_id = resultSet.getLong("account_id");
                        String account_type = resultSet.getString("account_type");
                        float balance = resultSet.getFloat("balance");

                        int customer_id = resultSet.getInt("customer_id");
                        Customer customer = getCustomerById(customer_id);
                        if(customer==null)
                        {
                        	throw new NullPointerException("No customer associated with account");
                        }
                     
                        Account account = new Account(account_type, balance, customer);
                        account.setAccount_id(account_id);
                        accounts.add(account);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
    
        }

        return accounts;
	}

	@Override
	public void calculateInterest() {
		
		try {
	     
	        String sql = "SELECT * FROM Accounts WHERE AccountType='savings'";
	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                while (resultSet.next()) {
	                    // Extract data from the result set

	                    long account_id = resultSet.getLong("account_id");
	                    String account_type=resultSet.getString("account_type");
	                    double balance=resultSet.getDouble("balance");
	                    int customer_id = resultSet.getInt("customer_id");
	                    double interestRate = 4.5;
	                    double interest = (balance / 100) * interestRate;
	                    System.out.print("Interest is Rs. "+interest);
	                    }
	                }
	            }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    
	    }
	}

	@Override
	public float getAccountBalance(long account_id) {
		try {
	       
	        String sql = "SELECT balance FROM Accounts WHERE account_id = ?";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            // Set the parameter for the SQL statement
	            preparedStatement.setLong(1, account_id);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    // Extract the account balance from the result set
	                    float accountBalance = resultSet.getFloat("balance");
	                    return accountBalance;
	                } else {
	                    // Account not found
	                    System.out.println("Account not found with account number: " + account_id);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}

	@Override
	public float deposit(long account_id, float amount) {
		// TODO Auto-generated method stub
		try {
	        // Assuming you have a table named 'Accounts' with columns 'account_id', 'AccountBalance'
	        String sql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            preparedStatement.setFloat(1, amount);
	            preparedStatement.setLong(2, account_id);

	            // Execute the SQL statement
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                // Deposit successful, return the new account balance
	                float newBalance = getAccountBalance(account_id);
	                System.out.println("Database Updated Deposit successful. New balance: RS. " + newBalance);
	                return newBalance;
	            } else {
	                // Account not found
	                System.out.println("Account not found with account number: " + account_id);
	                throw new InvalidAccountException("Account not found");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();	    }
	    return 0;
	}

	@Override
	public float withdraw(long account_id, float amount) {
		
		try {
	        String sqlSelect = "SELECT balance, AccountType FROM Accounts WHERE account_id = ?";
	        String sqlUpdate = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ?";

	        try (PreparedStatement selectStatement = con.prepareStatement(sqlSelect);
	             PreparedStatement updateStatement = con.prepareStatement(sqlUpdate)) {

	            selectStatement.setLong(1, account_id);

	            try (ResultSet resultSet = selectStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    float currentBalance = resultSet.getFloat("balance");
	                    String accountType = resultSet.getString("account_type");

	                    if ("Savings".equals(accountType) && currentBalance - amount < 500.0) {
	                        System.out.println("Withdrawal failed. Minimum balance rule violated.");
	                        throw new InsufficientFundException("Withdrawal failed. Minimum balance rule violated");
	                    }

	                    if ("Current".equals(accountType) && currentBalance - amount < -10000.0) {
	                        System.out.println("Withdrawal failed. Overdraft limit exceeded.");
	                        throw new OverDraftLimitExcededException("Withdrawal failed. Overdraft limit exceeded.");
	                    }

	                    if ("ZeroBalance".equals(accountType) && currentBalance - amount < 0) {
	                        System.out.println("Withdrawal failed. Minimum balance rule violated.");
	                        throw new InsufficientFundException("Withdrawal failed. Minimum balance rule violated");
	                    }
	                    updateStatement.setFloat(1, amount);
	                    updateStatement.setLong(2, account_id);

	                    // Execute the UPDATE statement
	                    int rowsAffected = updateStatement.executeUpdate();

	                    if (rowsAffected > 0) {
	                        // Withdrawal successful, return the new account balance
	                        float newBalance = getAccountBalance(account_id);
	                        System.out.println("Withdrawal successful. New balance: Rs ." + newBalance);
	                        return newBalance;
	                    }
	                } else {
	                    // Account not found
	                    System.out.println("Account not found with account number: " + account_id);
	                    throw new InvalidAccountException("Account not found");
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}

	@Override
	public void transfer(long fromAccount_id, long toAccount_id, float amount) {
		// TODO Auto-generated method stub

		try {
	        // Withdraw from sender's account
	        float senderBalance = withdraw(fromAccount_id, amount);

	        if (senderBalance != 0) {
	            deposit(toAccount_id, amount);
	            System.out.println("Transfer successful. Rs." + amount + " transferred from account " + fromAccount_id + " to account " + toAccount_id);
	        } else {
	            System.out.println("Transfer failed. Insufficient funds in sender's account.");
	        }
	    } catch (InvalidAccountException e) {
	    	System.out.println(e.getMessage());
	    } catch (InsufficientFundException e) {
	        System.out.println(e.getMessage());
	    } catch (OverDraftLimitExcededException e) {
	        System.out.println(e.getMessage());
	    }
	}

	@Override
	public String getAccountDetails(long account_id) {
		// TODO Auto-generated method stub
	    try {
	        String sql = "SELECT A.account_id, A.account_type, A.balance, C.customer_id, C.first_name, C.last_name, C.email, C.phone_number, C.address " +
	                     "FROM Accounts A JOIN Customers C ON A.customer_id = C.customer_id " +
	                     "WHERE A.account_id = ?";

	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setLong(1, account_id);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    // Extract information from the result set
	                    long accountId = resultSet.getLong("account_id");
	                    String accountType = resultSet.getString("account_type");
	                    float accountBalance = resultSet.getFloat("balance");

	                    long customer_id = resultSet.getLong("customer_id");
	                    String first_name = resultSet.getString("first_name");
	                    String last_name = resultSet.getString("last_name");
	                    String email = resultSet.getString("email");
	                    long phone_number = resultSet.getLong("phone_number");
	                    String address = resultSet.getString("address");

	                    // Construct the account details string
	                    StringBuilder detailsBuilder = new StringBuilder();
	                    detailsBuilder.append("Account Number: ").append(account_id).append("\n");
						String account_type = "";
						detailsBuilder.append("Account Type: ").append(account_type).append("\n");
						int balance = 0;
						detailsBuilder.append("Account Balance: ").append(balance).append("\n");
	                    detailsBuilder.append("Customer Details:\n");
						detailsBuilder.append("Customer ID: ").append(customer_id).append("\n");
						detailsBuilder.append("First Name: ").append(first_name).append("\n");
						detailsBuilder.append("Last Name: ").append(last_name).append("\n");
	                    detailsBuilder.append("email: ").append(email).append("\n");
						detailsBuilder.append("Phone Number: ").append(phone_number).append("\n");
	                    detailsBuilder.append("address: ").append(address);

	                    return detailsBuilder.toString();
	                } else {
	                    // Account not found
	                    return "Account not found with account number: " + account_id;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "An error occurred while fetching account details.";
	    }
	}

	@Override
	public List<Transaction> getTransactions(long account_id, String fromDate, String toDate) {
		// TODO Auto-generated method stub
		List<Transaction> transactions = new ArrayList<>();

	    try {
	        // Convert String dates to Date objects
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date startDate = dateFormat.parse(fromDate);
	        Date endDate = dateFormat.parse(toDate);

	        String sql = "SELECT * FROM Transactions WHERE account_id = ? AND transaction_date BETWEEN ? AND ?";
	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setLong(1, account_id);
	            statement.setTimestamp(2, new Timestamp(startDate.getTime()));
	            statement.setTimestamp(3, new Timestamp(endDate.getTime()));

	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    String transactionType = resultSet.getString("transaction_type");
	                    double transactionAmount = resultSet.getDouble("amount");
	                    String description = resultSet.getString("transaction_id");
	                    Date dateTime = resultSet.getTimestamp("transaction_date");
	                    Account acc=getAccount(account_id);
	                    Transaction transaction = new Transaction(acc, description,transactionType, transactionAmount, dateTime);
	                    transactions.add(transaction);
	                }
	            }
	        }
	    } catch (ParseException | SQLException e) {
	        e.printStackTrace();
	    }

	    return transactions;
	}

	private Customer getCustomerById(int customer_id) {
		try {
	        String sql = "SELECT * FROM Customers WHERE customer_id = ?";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            // Set the parameter for the SQL statement
	            preparedStatement.setInt(1, customer_id);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    // Extract data from the result set
	                    String first_name = resultSet.getString("first_name");
	                    String last_name = resultSet.getString("last_name");
	                    String email = resultSet.getString("email");
	                    long phone_number = resultSet.getLong("phone_number");
	                    String address = resultSet.getString("address");

	                    // Create and return a Customer object with the retrieved details
	                    return new Customer(customer_id, first_name, last_name, email, phone_number, address);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle exceptions appropriately, e.g., log them or throw a custom exception
	    }

	    return null;
    }

	public Account getAccount(long account_id) {
	    try {
	        String sql = "SELECT A.account_id, A.account_type, A.balance, C.customer_id, C.first_name, C.last_name, C.email, C.phone_number, C.address " +
	                     "FROM Accounts A JOIN Customers C ON A.customer_id = C.customer_id " +
	                     "WHERE A.account_id = ?";

	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setLong(1, account_id);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    // Extract information from the result set
	                    long accountId = resultSet.getLong("account_id");
	                    String accountType = resultSet.getString("account_type");
	                    double accountBalance = resultSet.getDouble("balance");

	                    int customer_id = resultSet.getInt("customer_id");
	                    String first_name = resultSet.getString("first_name");
	                    String last_name = resultSet.getString("last_name");
	                    String email = resultSet.getString("email");
	                    long phone_number = resultSet.getLong("phone_number");
	                    String address = resultSet.getString("address");

	                    return new Account(accountId, accountType, accountBalance, new Customer(customer_id, first_name, last_name, email, phone_number, address));
	                } else {
	                    // Account not found
	                    return null;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle exceptions appropriately, e.g., log them or throw a custom exception
	        System.out.println("An error occurred while fetching account details.");
	        return null;
	    }
	}
	void addTransaction(Transaction transaction)
	{
		try {
	        String sql = "INSERT INTO Transactions (account_id, transaction_id, transaction_date, transaction_type, amount) VALUES (?, ?, ?, ?, ?)";

	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setLong(1, transaction.getAccount_id().getAccount_id());
	            statement.setString(2, String.valueOf(transaction.getTransaction_id()));
	            statement.setTimestamp(3, new Timestamp(transaction.getTransaction_date().getTime()));
	            statement.setString(4, transaction.getTransaction_type());
	            statement.setDouble(5, transaction.getAmount());

	            int rowsAffected = statement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Transaction added successfully.");
	            } else {
	                System.out.println("Failed to add transaction.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle exceptions appropriately, e.g., log them or throw a custom exception
	        System.out.println("An error occurred while adding the transaction.");
	    }
	}

}
