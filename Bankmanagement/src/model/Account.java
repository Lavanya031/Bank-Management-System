package model;
public class Account {
    private int accountNo;
    private String name;
    private double balance;

    public Account(int accountNo, String name, double balance) {
        this.accountNo = accountNo;
        this.name = name;
        this.balance = balance;
    }

    public int getAccountNo() { return accountNo; }
    public String getName() { return name; }
    public double getBalance() { return balance; }

    @Override
    public String toString() {
        return "Account No: " + accountNo + ", Name: " + name + ", Balance: ₹" + balance;
    }
    
}
