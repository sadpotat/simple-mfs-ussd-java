package Models;

import Controllers.LogController;
import Controllers.TransactionController;

import java.io.PrintWriter;
import java.sql.SQLException;

public class Transaction {
    private int sender;
    private int receiver;
    private double amount;
    private String sessionID;

    private Customer senderObj;
    private Customer receiverObj;

    private TType type;

    public Transaction(){}

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TType getType() {
        return type;
    }

    public void setType(TType type) {
        this.type = type;
    }

    public Customer getSenderObj() {
        return senderObj;
    }

    public void setSenderObj(Customer senderObj) {
        this.senderObj = senderObj;
    }

    public Customer getReceiverObj() {
        return receiverObj;
    }

    public void setReceiverObj(Customer receiverObj) {
        this.receiverObj = receiverObj;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public boolean accountTypesOkay(){
//        if (type.getOptionNum()==1 || type.getOptionNum()==2)
//            return (senderObj.getType().equals(type.getR_type()) &&
//                    receiverObj.getType().equals(type.getS_type()));
        return (senderObj.getType().equals(type.getS_type()) &&
                receiverObj.getType().equals(type.getR_type()));
    }

    public void addCharges() {
        amount = amount * (1 + type.getCharges());
    }

    public void execute() throws SQLException {
        TransactionController transactionController;
        transactionController = new TransactionController(sender, receiver, amount, sessionID, getType().getOptionName());
        transactionController.transact();
        transactionController.updateBalance();
    }

    public boolean amountIsInBalance() {
        System.out.println(amount);
        System.out.println(senderObj.getBalance());
        return (amount < senderObj.getBalance());
    }

    public boolean isAllowed(PrintWriter out) {
        // verifying if the user can make transactions
        if (!receiverObj.getStatus().equals("ACTIVE")){
            out.println("Cannot make transactions, your account is " + senderObj.getStatus());
            out.close();
            return false;
        }

        // verifying PIN
        int hash = Integer.parseInt(LogController.getLastNthInput(sessionID,1));
        if (!TransactionController.verifyPIN(sender, hash)) {
            out.println("Wrong PIN");
            out.close();
            return false;
        }

        // verifying that the recipient exists
        if (receiverObj == null) {
            out.println("Recipient is not a registered account");
            out.close();
            return false;
        }

        // checking if a transaction would be allowed
        if (!accountTypesOkay()) {
            out.println("Transaction not allowed");
            out.close();
            return false;
        }

        // checking if the amount can be transacted
        if (!amountIsInBalance()){
            out.println("Insufficient balance");
            out.close();
            return false;
        }

        return true;
    }
}
