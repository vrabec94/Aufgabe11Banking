package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransferManager {

    static int numberOfBranchThreads = 5;
    static int numberOfHeadQuarterThreads = 10;

    public static void main (String[] args){
        long startTime = System.currentTimeMillis();
        Headquarter.generateRandomAccounts();
        List<Transfer> Queue = new ArrayList<>();
        int queueCapacity = 100;

        Thread[] producing = new Thread[numberOfBranchThreads];
        Thread[] consuming = new Thread[numberOfHeadQuarterThreads];

        for (int i = 0; i < numberOfBranchThreads; i++) {
            producing[i] = new Thread(new Branches(Queue, queueCapacity));
            producing[i].start();
        }

        for (int i = 0; i < numberOfHeadQuarterThreads; i++) {
            consuming[i] = new Thread(new Headquarter(Queue));
            consuming[i].start();
        }

        for (int i = 0; i < numberOfBranchThreads; i++) {
            try {
                producing[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < numberOfHeadQuarterThreads; i++) {
            try {
                consuming[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        long total = 0;
        for (Map.Entry<Integer, Account> accountEntry : Headquarter.AllAccounts.entrySet()) {
            total += accountEntry.getValue().getBalance();
        }
        System.out.println("Time needed: " + elapsedTime + " Milliseconds. ");
        System.out.println("Total of all Bank Accounts: " + total + " ct. ");
        System.out.println("Successful Transfers: " + Headquarter.counter);
        System.out.println("Unsuccessful Transfers: " + Headquarter.failedCounter);
    }
}
