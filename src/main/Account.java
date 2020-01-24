package main;

public class Account {

    public int accountnr;
    private int balance;

    Account(int accountnr, int balance){
        this.accountnr = accountnr;
        this.balance = balance;
    }

    public int getAccountnr() {
        return accountnr;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
