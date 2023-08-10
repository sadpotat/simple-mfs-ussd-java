package Controllers;

import Models.GetFromDB;
import Models.InsertIntoDB;
import Models.Transaction;

import java.io.PrintWriter;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class TransactionController {
    private final GetFromDB fetcher;
    private final PreparedStatement insertTransaction;
    private final PreparedStatement updateSenderBalance;
    private final PreparedStatement updateReceiverBalance;
    private final int sender;
    private final int receiver;
    private final double amount;
    private final String sessionID;
    private final String transactionType;

    public TransactionController(int s, int r, double a, String id, String type) throws SQLException {
        fetcher = Database.getGetter();
        sender = s;
        receiver = r;
        amount = a;
        sessionID = id;
        transactionType = type;
        Connection conn = Database.getConnectionObject();

        // query strings
        String addTranQuery = "insert into transactions (amount, sender_id, receiver_id, session_id, type, time) values (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String updateSenderQuery = "update balance set balance=(select balance from balance where cus_id=?)-? where cus_id=?";
        String updateReceiverQuery = "update balance set balance=(select balance from balance where cus_id=?)+? where cus_id=?";

        // initialising prepared statements
        insertTransaction = conn.prepareStatement(addTranQuery);
        updateSenderBalance = conn.prepareStatement(updateSenderQuery);
        updateReceiverBalance = conn.prepareStatement(updateReceiverQuery);
    }

    public static boolean verifyPIN(int initiator, int hash) {
        GetFromDB getter = Database.getGetter();
        return getter.accountPinExists(initiator,hash);
    }

    public static ResultSet getPrevTransactions(int initiator) throws SQLException {
        Connection conn = Database.getConnectionObject();
        Statement statement = conn.createStatement();
        String getTransactionsQuery = "select type, receiver_id, amount, time from transactions where sender_id=" + initiator + " order by trans_number desc";
        return statement.executeQuery(getTransactionsQuery);
    }

    public static void sendBalance(String sessionID, int initiator, PrintWriter out) {
        int hash = Integer.parseInt(LogController.getLastNthInput(sessionID,1));

        // verifying PIN
        if (!verifyPIN(initiator, hash)) {
            out.println("Wrong PIN");
            out.close();
            return;
        }

        double balance = Database.getGetter().getBalance(initiator);
        out.println("Current account balance: " + balance);
        out.close();
    }

    public static void sendStatement(String sessionID, int initiator, PrintWriter out) throws SQLException {
        // getting transaction data from the user's previous inputs
        int hash = Integer.parseInt(LogController.getLastNthInput(sessionID,1));

        // verifying PIN
        if (!verifyPIN(initiator, hash)) {
            out.println("Wrong PIN");
            out.close();
        }

        // getting previous transactions
        ResultSet rs = getPrevTransactions(initiator);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for(int i=0; i<10; i++){
            if(rs.next())
                out.println(rs.getTimestamp("time").toLocalDateTime().format(formatter) +
                        ": " + rs.getString("type") +
                        " - " + rs.getString("receiver_id") +
                        " - " + rs.getString("amount"));
            else
            if (i==0)
                out.println("No transaction history found");
            else
                break;
        }
        out.close();
    }

    public static void sendTransactionInfo(String sessionID, PrintWriter out) {
        String reference = LogController.getLastNthInput(sessionID,1);
        int amount = Integer.parseInt(LogController.getLastNthInput(sessionID,2));
        int receiver = Integer.parseInt(LogController.getLastNthInput(sessionID,3));

        // displaying response
        out.println("Receiver: " + receiver +
                "\nAmount: " + amount +
                "\nReference: " + reference +
                "\nEnter PIN: ");
        out.close();
    }

    public static Transaction createTransactionOBJ(String sessionID, int initiator) {
        // getting transaction info from log
        int amount = Integer.parseInt(LogController.getLastNthInput(sessionID,3));
        int receiver = Integer.parseInt(LogController.getLastNthInput(sessionID,4));
        int tranType = Integer.parseInt(LogController.getLastNthInput(sessionID,5));

        Models.Transaction transaction = new Models.Transaction();

        initialise(transaction, sessionID, initiator, receiver, amount, tranType);

        return transaction;
    }


    public static Transaction createTransactionOBJForRecharge(String sessionID, int initiator){
        // getting transaction info from log
        int amount = Integer.parseInt(LogController.getLastNthInput(sessionID,3));
        int tranType = Integer.parseInt(LogController.getLastNthInput(sessionID,7));
        int receiver = TransactionController.getProviderAcc(sessionID);

        Models.Transaction transaction = new Models.Transaction();

        initialise(transaction, sessionID, initiator, receiver, amount, tranType);

        return transaction;
    }

    private static void initialise(Transaction transaction, String sessionID, int initiator, int receiver, double amount, int tranType) {
        GetFromDB getter = Database.getGetter();

        // setting sender and receiver data from db
        transaction.setSender(initiator);
        transaction.setSenderObj(getter.getCustomer(initiator));
        transaction.setReceiver(receiver);
        transaction.setReceiverObj(getter.getCustomer(receiver));
        transaction.setSessionID(sessionID);
        transaction.setType(getter.getTTypeObjectFromDB(tranType));
        transaction.setSessionID(sessionID);
        transaction.setAmount(amount);
        transaction.addCharges();
    }


    public static int getProviderAcc(String sessionID){
        GetFromDB getter = Database.getGetter();

        // getting provider information from db
        String simType = Integer.parseInt(LogController.getLastNthInput(sessionID, 5))==1 ? "PREPAID": "POSTPAID";
        String provider = LogController.getLastNthInput(sessionID, 6);

        try {
            String providerName = getter.getProviderName(provider);
            String name = providerName + "_" + simType;
            return getter.getProviderID(name);
        } catch (SQLException e) {
            return -1;
        }
    }

    public static void replaceProviderIDWithRechargedNumber(String sessionID) throws SQLException {
        int receiver = Integer.parseInt(LogController.getLastNthInput(sessionID,4));
        InsertIntoDB insert = Database.getInsert();
        insert.updateReceiverInTLog(sessionID, receiver);
    }

    public void transact() throws SQLException {
        insertTransaction.setDouble(1, amount);
        insertTransaction.setInt(2, sender);
        insertTransaction.setInt(3, receiver);
        insertTransaction.setString(4, sessionID);
        insertTransaction.setString(5, transactionType);
        int m = insertTransaction.executeUpdate();
        if (m==1){
            System.out.println("Controllers.TransactionController recorded successfully");
        } else
            System.out.println("Controllers.TransactionController failed to record");
    }

    public void updateSenderBalance() throws SQLException {
        updateSenderBalance.setInt(1, sender);
        updateSenderBalance.setDouble(2, amount);
        updateSenderBalance.setInt(3, sender);
        updateSenderBalance.executeUpdate();
    }
    public void updateReceiverBalance() throws SQLException {
        updateReceiverBalance.setInt(1, receiver);
        updateReceiverBalance.setDouble(2, amount);
        updateReceiverBalance.setInt(3, receiver);
        updateReceiverBalance.executeUpdate();
    }

    public void updateBalance() throws SQLException {
        // when the sender is an agent or a bank, balance is -1

        // updating sender balance
        if (fetcher.getBalance(sender)>0) updateSenderBalance();

        // updating receiver balance
        if (fetcher.getBalance(receiver)>0) updateReceiverBalance();
    }

}
