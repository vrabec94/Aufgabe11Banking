package main;

public class Transfer {

    private int transferCount;
    public static int outgoingAccountNr;
    public static int incomingAccountNr;
    private int total;
    private int failedAttempts;

    Transfer(int outgoingAccountNr, int incomingAccountNr, int totalTransfer, int transferNr) {
        this.outgoingAccountNr = outgoingAccountNr;
        this.incomingAccountNr = incomingAccountNr;
        this.total = totalTransfer;
        this.transferCount = transferNr;
    }
    public int getTransferCount() {
        return transferCount;
    }

    int getOutgoingAccountNr() {
        return outgoingAccountNr;
    }

    int getIncomingAccountNr() {
        return incomingAccountNr;
    }

    int getTotal() {
        return total;
    }

    int getFailedAttempts() {
        return failedAttempts;
    }

    void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}
