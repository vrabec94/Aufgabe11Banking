package main;

import java.util.*;

public class Headquarter implements Runnable {

    static Map<Integer, Account> AllAccounts = new HashMap<>();
    static int numberOfAccounts = 1000;
    private final List<Transfer> Queue;
    public static int counter;
    public static int failedCounter;

    Headquarter(List<Transfer> sharedQueue) {
        this.Queue = sharedQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                consume();
            } catch (InterruptedException ex) {
                System.out.println("Thread Number: " + Thread.currentThread().getName() + " is terminated. ");
                ex.printStackTrace();
            }
        }
    }

    private void consume() throws InterruptedException {
        synchronized (Queue) {
            while (Queue.isEmpty()) {
                System.out.println("Queue is empty " + Thread.currentThread().getName() + " is waiting, size of Queue: " + Queue.size());
                Queue.wait(5000);
                if (Queue.isEmpty()) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            //Thread.sleep(1000);
            Transfer transfer = Queue.remove(0);
            boolean successful = transferMoney(transfer);
            if (!successful) {
                if (transfer.getFailedAttempts() == 3) return;
                else {
                    Queue.add(transfer);
                    transfer.setFailedAttempts(transfer.getFailedAttempts() + 1);
                }
            } else {
                System.out.println("Processed Transfer Number: " + transfer.getTransferCount());
                counter += 1;
            }
            Queue.notifyAll();
        }
    }

    public static void generateRandomAccounts() {
        for (int i = 0; i < numberOfAccounts; i++) {
            Account account = new Account(i + 1, 10000);
            AllAccounts.put(i + 1, account);
            //System.out.println("bank.Account Nr: " + account.getAccountnr() + " with Balance: " + account.getBalance() + " created. ");
        }
    }

    private boolean transferMoney(Transfer toTransfer) {
        //System.out.println(toTransfer.getTotal() + "ct to transfer from AccountNr: " + toTransfer.getOutgoingAccountNr() + " to AccountNr: " + toTransfer.getIncomingAccountNr());
        for (Map.Entry<Integer, Account> accountNr : AllAccounts.entrySet()) {
            if (accountNr.getKey() == toTransfer.getOutgoingAccountNr()) {
                //System.out.println("Outgoing bank.Account: ");
                //System.out.println("Old Balance: " + account.getBalance());
                int newBalance = (accountNr.getValue().getBalance() - toTransfer.getTotal());
                if (newBalance < 0) {
                    //System.out.println("False");
                    return false;
                } else accountNr.getValue().setBalance(newBalance);
                //System.out.print("New Balance: " + account.getBalance());
                //System.out.println();
            }
            if (accountNr.getValue().getAccountnr() == toTransfer.getIncomingAccountNr()) {
                //System.out.println("Incoming bank.Account: ");
                //System.out.println("Old Balance: " + account.getBalance());
                int newBalance = (int) (accountNr.getValue().getBalance() + toTransfer.getTotal());
                accountNr.getValue().setBalance(newBalance);
                //System.out.print("New Balance: " + account.getBalance());
                //System.out.println();
            }
        } return true;
    }
}
