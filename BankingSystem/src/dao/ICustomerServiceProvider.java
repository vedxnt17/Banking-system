package dao;
import java.sql.Date;
import java.util.*;
import entity.*;
public interface ICustomerServiceProvider {

    double getAccountBalance(long accountNumber);

    double deposit(long accountNumber, double amount);

    double withdraw(long accountNumber, double amount);

    void transfer(long fromAccountNumber, long toAccountNumber, double amount);

    String getAccountDetails(long accountNumber);

	List<Transaction> getTransactions(long accountNumber, String startDate, String endDate);

}
