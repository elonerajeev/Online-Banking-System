import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Account {
    private final String accountNumber;
    private final String accountHolder;
    private double balance;
    private final String password;

    public Account(String accountNumber, String accountHolder, double initialBalance, String password) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.password = password;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public boolean verifyPassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(Account target, double amount) {
        if (withdraw(amount)) {
            target.deposit(amount);
            return true;
        }
        return false;
    }
}

class Bank {
    private final Map<String, Account> accounts;
    private final Map<String, TransactionHistory> transactionHistories;

    public Bank() {
        accounts = new HashMap<>();
        transactionHistories = new HashMap<>();
    }

    public void createAccount(String accountNumber, String accountHolder, double initialBalance, String password) {
        if (!accounts.containsKey(accountNumber)) {
            Account account = new Account(accountNumber, accountHolder, initialBalance, password);
            accounts.put(accountNumber, account);
            transactionHistories.put(accountNumber, new TransactionHistory());
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Account already exists.");
        }
    }

    public void deposit(String accountNumber, double amount) {
        if (accounts.containsKey(accountNumber)) {
            Account account = accounts.get(accountNumber);
            account.deposit(amount);
            transactionHistories.get(accountNumber).addTransaction("Deposit", amount);
            System.out.println("Deposit successful. New balance: " + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    public void withdraw(String accountNumber, double amount) {
        if (accounts.containsKey(accountNumber)) {
            Account account = accounts.get(accountNumber);
            if (account.withdraw(amount)) {
                transactionHistories.get(accountNumber).addTransaction("Withdrawal", amount);
                System.out.println("Withdrawal successful. New balance: " + account.getBalance());
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    public void transfer(String fromAccount, String toAccount, double amount) {
        if (accounts.containsKey(fromAccount) && accounts.containsKey(toAccount)) {
            Account sourceAccount = accounts.get(fromAccount);
            Account targetAccount = accounts.get(toAccount);
            if (sourceAccount.transfer(targetAccount, amount)) {
                transactionHistories.get(fromAccount).addTransaction("Transfer to " + toAccount, amount);
                transactionHistories.get(toAccount).addTransaction("Transfer from " + fromAccount, amount);
                System.out.println("Transfer successful. New balance in " + fromAccount + ": " + sourceAccount.getBalance());
                System.out.println("New balance in " + toAccount + ": " + targetAccount.getBalance());
            } else {
                System.out.println("Transfer failed. Insufficient balance in the source account.");
            }
        } else {
            System.out.println("One or both accounts not found.");
        }
    }

    public double checkBalance(String accountNumber) {
        if (accounts.containsKey(accountNumber)) {
            return accounts.get(accountNumber).getBalance();
        } else {
            System.out.println("Account not found.");
            return 0;
        }
    }

    public List<String> getTransactionHistory(String accountNumber) {
        if (transactionHistories.containsKey(accountNumber)) {
            return transactionHistories.get(accountNumber).getTransactionHistory();
        } else {
            System.out.println("Account not found.");
            return new ArrayList<>();
        }
    }
}

class TransactionHistory {
    private final List<String> transactions;

    public TransactionHistory() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(String transactionType, double amount) {
        transactions.add(transactionType + ": " + amount);
    }

    public List<String> getTransactionHistory() {
        return transactions;
    }
}

public class OnlineBankingSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Online Banking App!");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. View Transaction History");
            System.out.println("7. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String accountHolder = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline
                    System.out.print("Set a password: ");
                    String password = scanner.nextLine();
                    bank.createAccount(accountNumber, accountHolder, initialBalance, password);
                    break;
                case 2:
                    // Deposit implementation
                    System.out.print("Enter account number: ");
                    String depositAccountNumber = scanner.nextLine();
                    System.out.print("Enter the amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline
                    bank.deposit(depositAccountNumber, depositAmount);
                    break;

                case 3:
                    // Withdraw implementation
                    System.out.print("Enter account number: ");
                    String withdrawAccountNumber = scanner.nextLine();
                    System.out.print("Enter the amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline
                    bank.withdraw(withdrawAccountNumber, withdrawAmount);
                    break;

                case 4:
                    // Transfer implementation
                    System.out.print("Enter source account number: ");
                    String fromAccount = scanner.nextLine();
                    System.out.print("Enter target account number: ");
                    String toAccount = scanner.nextLine();
                    System.out.print("Enter the amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline
                    bank.transfer(fromAccount, toAccount, transferAmount);
                    break;
                case 5:
                    // Check Balance implementation
                    System.out.print("Enter account number: ");
                    String checkBalanceAccountNumber = scanner.nextLine();
                    double balance = bank.checkBalance(checkBalanceAccountNumber);
                    System.out.println("Current balance: " + balance);
                    break;
                case 6:
                    // View Transaction History implementation
                    System.out.print("Enter account number: ");
                    String historyAccountNumber = scanner.nextLine();
                    List<String> transactions = bank.getTransactionHistory(historyAccountNumber);
                    if (!transactions.isEmpty()) {
                        System.out.println("Transaction History:");
                        for (String transaction : transactions) {
                            System.out.println(transaction);
                        }
                    }
                    break;
                case 7:
                    System.out.println("Thank you for using the Online Banking App!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
