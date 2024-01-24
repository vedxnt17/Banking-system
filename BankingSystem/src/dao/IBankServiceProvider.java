package dao;

import java.util.*;

import entity.*;

public interface IBankServiceProvider {

    Account createAccount(Customer customer, long accNo, String accType, double balance);

    Map<Long, Account> listAccounts();

    void calculateInterest();
}
