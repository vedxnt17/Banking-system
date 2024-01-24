package dao;

import java.util.Date;
import java.util.List;
import entity.*;
public interface IBankRepository {
    void createAccount(Customer customer, long accNo, String accType, float balance);

    List<Account> listAccounts();

    void calculateInterest();

    float getAccountBalance(long accountNumber);

    float deposit(long accountNumber, float amount);

    float withdraw(long accountNumber, float amount);

    void transfer(long fromAccountNumber, long toAccountNumber, float amount);

    String getAccountDetails(long accountNumber);

	List<Transaction> getTransactions(long accountNumber, String fromDate, String toDate);
}

