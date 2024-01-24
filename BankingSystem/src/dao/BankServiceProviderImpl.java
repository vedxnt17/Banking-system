package dao;
import entity.*;
import exception.*;

import java.sql.Date;
import java.util.*;
public class BankServiceProviderImpl extends CustomerServiceProviderImpl implements IBankServiceProvider {
    //private Account[] accountList;
	//private List<Account> accountList;
	private Map<Long, Account> accountList;
    private String branchName;
    private String branchAddress;
    BankRepositoryImpl bankdb=null;
    public BankServiceProviderImpl(String branchName, String branchAddress) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        
        //this.accountList = new ArrayList<>();
        //this.accountList = new HashMap<>();
        bankdb=new BankRepositoryImpl();
        accountList=listAccounts();
        //this.accountList=new Account[0];
//        this.accountList = new Account[]{new SavingsAccount(4.5,new Customer(1,"avi","singh","avi@gmail.com",9898989898L,"armapur")),new CurrentAccount(-10000,new Customer(2,"ani","singh","ani@gmail.com",9898989899L,"panki")),new ZeroBalanceAccount(new Customer(3,"avinash","singh","avinash@gmail.com",9898989897L,"rawatpur"))}; // Initialize an empty array of accounts

    }

	@Override
    public Account createAccount(Customer customer, long accNo, String accType, double balance) {
        // Overriding createAccount to update accountList
    	// to check account number set manual or automatic
    	Account account=null;
        if ("Savings".equals(accType)) {
            account = new SavingsAccount(4.5, customer); // Assume 4.5% interest rate for now
        } else if ("Current".equals(accType)) {
            account = new CurrentAccount(0.0, customer); // Assume 0 overdraft limit for now
        } else if ("ZeroBalance".equals(accType)){
            account = new ZeroBalanceAccount(customer);
        }
        else
        {
        	System.out.println("Invalid Account Type");
        	return null;
        }

        // Set account number and balance
        account.setAccount_id(accNo);
        account.setAccountBalance(balance);

        // Update accountList
//        accountList = Arrays.copyOf(accountList, accountList.length + 1);
//        accountList[accountList.length - 1] = account;
        //accountList.add(account);
        accountList.put(accNo,account);
        bankdb.createAccount(customer, accNo, accType, (float) balance);
        // Return the created account
        return account;
    }

    @Override
    public Map<Long, Account> listAccounts() {
        // List all accounts in the bank
    	accountList=castToMap(bankdb.listAccounts());
    	
    	if(accountList.size()==0)
    	{
    		throw new NullPointerException("No Accounts created");
    	}
        return accountList;
    }

    private Map<Long, Account> castToMap(List<Account> listAccounts) {
		// TODO Auto-generated method stub
    	Map<Long,Account> hm=new HashMap<>();
    	hm=new HashMap<>();
    	for(int i=0;i<listAccounts.size();i++)
    	{
    		hm.put(listAccounts.get(i).getAccount_id(), listAccounts.get(i));
    		
    	}
		return hm;
	}

	@Override
    public void calculateInterest() {
        // Calculate interest for all accounts in the bank
    	
    	for (Map.Entry<Long, Account> entry : accountList.entrySet()) {
    		long accountNumber = entry.getKey();
            Account account = entry.getValue();
    		if (account instanceof SavingsAccount) {
                double interestRate = ((SavingsAccount) account).getInterestRate();
                double interest = (account.getBalance()/100) * interestRate;
                account.setAccountBalance(account.getBalance() + interest);
                accountList.put(accountNumber, account);
                System.out.println("Interest calculated for Savings Account " + account.getAccount_id() +
                        ": Rs." + interest);
            }
    	}
    	
        //for (Account account : accountList) {
            
        }
   
    public Account findAccountObject(long accountNumber)
    {
    	if(accountList.get(accountNumber)!=null)
    	{
    		return accountList.get(accountNumber);
    	}
    	return null;
    }
    public void setAccountObject(Account acc)
    {
    	accountList.put(acc.getAccount_id(),acc);
    	
    }
    
    @Override
    public double getAccountBalance(long accountNumber) {
    	Account acc=findAccountObject(accountNumber);
    	if(acc==null)
    	{
    		throw new InvalidAccountException("No account Found");
    	}
    	return bankdb.getAccountBalance(accountNumber);
    }
    
    public double deposit(long accountNumber, double amount) {
        // Deposit the specified amount into the account
        // Implement logic to update the account balance in storage or database
        // For demonstration purposes, return a dummy balance
    	Account acc=findAccountObject(accountNumber);
    	if(acc==null)
    	{
    		System.out.println("Receiver Account Invalid");
    		throw new InvalidAccountException("Receiver Account Invalid");
    	}
    	acc.setAccountBalance(acc.getBalance()+amount);
    	
//    	setAccountObject(acc);
    	Transaction tran=new Transaction(acc,"Deposit by self","Deposit",amount);
    	bankdb.deposit(accountNumber,(float) amount);
    	bankdb.addTransaction(tran);
    	accountList=listAccounts();
    	return bankdb.getAccountBalance(accountNumber);
    	
    }

    @Override
    public double withdraw(long accountNumber, double amount) {
    	Account account = findAccountObject(accountNumber);	
        if (account != null) {
//            // Check if the withdrawal violates the minimum balance rule
//            if (account instanceof SavingsAccount) {
//                double minimumBalance = 500.0; // Example minimum balance for savings account
//                if (account.getAccountBalance() - amount < minimumBalance) {
//                    System.out.println("Withdrawal failed. Minimum balance rule violated.");
//                    throw new InsufficientFundException("Insufficient Funds");
//                }
//            }
//            if (account instanceof CurrentAccount) {
//            	CurrentAccount acc=(CurrentAccount) account;
//                double overdraftLimit = acc.getOverdraftLimit(); // Example minimum balance for savings account
//                //System.out.println(account.getAccountBalance());
//                //System.out.println(overdraftLimit);
//                if (account.getAccountBalance() - amount < overdraftLimit) {
//                    System.out.println("Withdrawal failed. Overdraft Limit Exceeded.");
//                    throw new OverDraftLimitExcededException("Overdraft Limit Exceeded"); // Special value indicating failure due to minimum balance rule
//                }
//            }
//            
//            if (account instanceof ZeroBalanceAccount) {
//            	System.out.println("entered");
//            	ZeroBalanceAccount acc=(ZeroBalanceAccount) account;
//                double minbal = 0; // Example minimum balance for savings account
//                if (account.getAccountBalance() - amount < minbal) {
//                    System.out.println("Withdrawal failed. Insufficient Funds");
//                    throw new InsufficientFundException("Insufficient Funds");// Special value indicating failure due to minimum balance rule
//                }

            //account.setAccountBalance(account.getAccountBalance() - amount);
        	try
        	{
        		setAccountObject(account);
                Transaction tran=new Transaction(account,"withdraw by self","Withdraw",amount);
            	bankdb.withdraw(accountNumber,(float) amount);
            	bankdb.addTransaction(tran);
            	accountList=listAccounts();
            	return bankdb.getAccountBalance(accountNumber);
        	}
        	catch (InvalidAccountException e) {
    			// TODO Auto-generated catch block
        		System.out.println("Sender Account Invalid");
    			throw new InvalidAccountException("Sender Account Invalid");
    		}
        	catch (InsufficientFundException e) {
    			// TODO Auto-generated catch block
        		System.out.println("Insufficient Funds in sender account");
    			throw new InsufficientFundException("Insufficient Funds in sender account");
    		}
        	catch (OverDraftLimitExcededException e) {
    			// TODO Auto-generated catch block
        		System.out.println("Overdraft Limit Exceeded");
    			throw new OverDraftLimitExcededException("Overdraft Limit Exceeded");
    		}
            
            
        } else {
           throw new InvalidAccountException("Account Not Found");
        }
    }
    @Override
    public void transfer(long fromAccountNumber, long toAccountNumber, double amount) {
//    	Account senderAccount = findAccountObject(fromAccountNumber);
//    	Account receiverAccount = findAccountObject(toAccountNumber);
    	if(accountList.containsKey(fromAccountNumber)==false)
    	{
    		System.out.println("Sender Account Invalid");
    		throw new InvalidAccountException("Sender Account Invalid");
    	}
    	if(accountList.containsKey(toAccountNumber)==false)
    	{
    		System.out.println("Receiver Account Invalid");
    		throw new InvalidAccountException("Receiver Account Invalid");
    	}
    	try {
			double sendAmount=withdraw(fromAccountNumber,amount);
		} catch (InvalidAccountException e) {
			// TODO Auto-generated catch block
			throw new InvalidAccountException("Sender Account Invalid");
		}
    	catch (InsufficientFundException e) {
			// TODO Auto-generated catch block
			throw new InsufficientFundException("Insufficient Funds in sender account");
		}
    	catch (OverDraftLimitExcededException e) {
			// TODO Auto-generated catch block
			throw new OverDraftLimitExcededException("Overdraft Limit Exceeded");
		}
    	try
    	{
        	double newAmount=deposit(toAccountNumber,amount);
    	}
    	catch (InvalidAccountException e) {
			// TODO Auto-generated catch block
    		double newAmount=deposit(fromAccountNumber,amount);
    		System.out.println("Deposited back to Sender account, new balance Rs. "+newAmount);
			throw new InvalidAccountException("Receiver Account Invalid");
    	}

    	
        System.out.println("Transferred Rs." + amount + " from account " + fromAccountNumber + " to account " + toAccountNumber);
    }
    public String getAccountDetails(long accountNumber) {
    	Account account = findAccountObject(accountNumber);
    	if(account==null)
    	{
    		throw new InvalidAccountException("Invalid Account Number");
    	}
    	String customerdetails=" Customer first_name: "+account.getCustomer().getFirst_name()+" Customer last_name: "+account.getCustomer().getLast_name()+" Customer ID: "+account.getCustomer().getCustomer_id()+" Customer email: "+account.getCustomer().getEmail()+" Customer Phonenumber: "+account.getCustomer().getPhone_number()+" Customer address: "+account.getCustomer().getAddress();
    	String result=" Account Type: "+account.getAccount_type()+" Account Balance: "+account.getBalance();
        return "Account details for account number " + accountNumber+result+customerdetails;
    }
    @Override
	public List<Transaction> getTransactions(long accountNumber, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return bankdb.getTransactions(accountNumber, startDate, endDate);
	}
    
    // Additional methods...

	
    // Existing methods from BankServiceProviderImpl...

    // Additional methods...
}
