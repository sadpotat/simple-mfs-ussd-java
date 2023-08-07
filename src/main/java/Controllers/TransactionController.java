package Controllers;

import Models.GetFromDB;

import java.sql.*;

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
        fetcher = GetFromDB.getGetter();
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
        GetFromDB getter = GetFromDB.getGetter();
        return getter.accountPinExists(initiator,hash);
    }

    public static ResultSet getPrevTransactions(int initiator) throws SQLException {
        Connection conn = Database.getConnectionObject();
        Statement statement = conn.createStatement();
        String getTransactionsQuery = "select type, receiver_id, amount, time from transactions where sender_id=" + initiator + " order by trans_number desc";
        return statement.executeQuery(getTransactionsQuery);
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
