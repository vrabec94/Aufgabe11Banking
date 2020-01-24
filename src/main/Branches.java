package main;

import java.util.*;

public class Branches implements Runnable {

    private static LinkedList<Transfer> allTransfers = new LinkedList<>();
    private final List<Transfer> Queue;
    private final int queueCapacity;

    Branches(List<Transfer> sharedQueue, int size) {
        Queue = sharedQueue;
        this.queueCapacity = size;
        setAllTransfers(generateTransfers());
    }
    public static void setAllTransfers(LinkedList<Transfer> allTransfers) {
        Branches.allTransfers = allTransfers;
    }
    @Override
    public void run() {
        while(!allTransfers.isEmpty()) {
                try {
                    produce(allTransfers.get(0));
                } catch (InterruptedException e) {
                    System.out.println("Thread Number: " + Thread.currentThread().getName() + " is terminated. ");
                    e.printStackTrace();
                }

        }
    }

    private void produce(Transfer transfer) throws InterruptedException {
        synchronized (Queue) {
            while (Queue.size() == queueCapacity) {
                System.out.println("Queue is full " + Thread.currentThread().getName() + " is waiting, size of Queue: " + Queue.size());
                Queue.wait();
            }
            //Thread.sleep(1000);
            Queue.add(transfer);
            System.out.println("Transfer Number: " + transfer.getTransferCount() + " added to Queue by Thread Number: " + Thread.currentThread().getName() + " Transfers left: " + allTransfers.size());
            allTransfers.remove(transfer);
            Queue.notifyAll();
        }
    }

    private static LinkedList<Transfer> generateTransfers() {
        System.out.println("Producing Transfer.");
        int numberOfTransfers = 1000000;
        for (int i = 0; i < numberOfTransfers; i++) {

            int toTransfer = randomInt(0, 10000);
            int outgoing = randomInt(1, Headquarter.numberOfAccounts);
            int incoming = randomInt(1, Headquarter.numberOfAccounts);

            Transfer transfer = new Transfer(outgoing, incoming, toTransfer, i);

            if (transfer.getOutgoingAccountNr() == transfer.getIncomingAccountNr()) {
                i--;
                continue;
            }
            allTransfers.add(transfer);
        } return allTransfers;
    }


    /**
     * generates random amount and price of product
     * @param min - minimum in range of random
     * @param max - maximum in range of random
     * @return random integer
     */
    private static int randomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
