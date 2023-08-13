package NewClasses;

import Controllers.Database;
import Controllers.LogController;
import Models.Customer;
import Models.GetFromDB;
import Models.TType;

import java.io.PrintWriter;
import java.sql.*;

abstract class TransactionParent {
    protected final GetFromDB getter;
    protected final PreparedStatement insertTransaction;
    protected final PreparedStatement updateSenderBalance;
    protected final PreparedStatement updateReceiverBalance;
    protected final int sender;
    protected final Customer senderObj;
    protected int receiver;
    protected Customer receiverObj;
    protected double amount;
    protected final String sessionID;
    protected TType tType;
    protected String serviceID;

    public TransactionParent(String session_id, int initiator) throws SQLException {
        sessionID = session_id;
        getter = Database.getGetter();
        Connection conn = Database.getConnectionObject();
        sender = initiator;
        senderObj = getter.getCustomer(initiator);

        // query strings
        String addTranQuery = "insert into transactions (amount, sender_id, receiver_id, session_id, type, time) values (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String updateSenderQuery = "update balance set balance=(select balance from balance where cus_id=?)-? where cus_id=?";
        String updateReceiverQuery = "update balance set balance=(select balance from balance where cus_id=?)+? where cus_id=?";

        // initialising prepared statements
        insertTransaction = conn.prepareStatement(addTranQuery);
        updateSenderBalance = conn.prepareStatement(updateSenderQuery);
        updateReceiverBalance = conn.prepareStatement(updateReceiverQuery);
    }

    // must be called before asking for PIN
    abstract void initialiseFromLog();

    protected void updatefields(int rec, int amnt){
        receiver = rec;
        receiverObj = getter.getCustomer(rec);
        amount = amnt;
        tType = getter.getTTypeObjectFromDB(serviceID);
        addCharges();
    }

    protected void addCharges() {
        amount = amount * (1 + tType.getCharges());
    }

    public boolean isAllowed(PrintWriter out) {
        // verifying if the user can make transactions
        if (!receiverObj.getStatus().equals("ACTIVE")){
            out.println("Cannot make transactions, your account is " + senderObj.getStatus());
            out.close();
            return false;
        }

        // verifying PIN
        int hash = LogController.getLastNthInputInt(sessionID,1);
        if (!verifyPIN(sender, hash)) {
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

    protected static boolean verifyPIN(int initiator, int hash) {
        GetFromDB getter = Database.getGetter();
        return getter.accountPinExists(initiator,hash);
    }

    protected boolean accountTypesOkay(){
        return (senderObj.getType().equals(tType.getS_type()) &&
                receiverObj.getType().equals(tType.getR_type()));
    }

    protected boolean amountIsInBalance() {
        System.out.println(amount);
        System.out.println(senderObj.getBalance());
        return (amount < senderObj.getBalance());
    }

    public void execute() throws SQLException {
        transact();
        updateBalance();
    }

    protected void transact() throws SQLException {
        insertTransaction.setDouble(1, amount);
        insertTransaction.setInt(2, sender);
        insertTransaction.setInt(3, receiver);
        insertTransaction.setString(4, sessionID);
        insertTransaction.setString(5, tType.getOptionName());
        insertTransaction.executeUpdate();
    }

    protected void updateBalance() throws SQLException {
        // when the sender is an agent or a bank, balance is -1

        // updating sender balance
        if (getter.getBalance(sender)>0) updateSenderBalance();

        // updating receiver balance
        if (getter.getBalance(receiver)>0) updateReceiverBalance();
    }

    protected void updateSenderBalance() throws SQLException {
        updateSenderBalance.setInt(1, sender);
        updateSenderBalance.setDouble(2, amount);
        updateSenderBalance.setInt(3, sender);
        updateSenderBalance.executeUpdate();
    }
    protected void updateReceiverBalance() throws SQLException {
        updateReceiverBalance.setInt(1, receiver);
        updateReceiverBalance.setDouble(2, amount);
        updateReceiverBalance.setInt(3, receiver);
        updateReceiverBalance.executeUpdate();
    }

}