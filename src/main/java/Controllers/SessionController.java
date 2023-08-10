package Controllers;

import Models.GetFromDB;
import Models.InsertIntoDB;
import Models.Session;
import Models.Transaction;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;

public class SessionController {
    public static boolean createSession(int initiator) {
        String sessionID = Utils.createSessionID();
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.createSessionEntry(sessionID, initiator);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            Database.rollbackChanges();
            return false;
        }
    }

    public static boolean updateLastResponse(String sessionID, String value) {
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.enterIntoSessionString(sessionID, "last_resp", value);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to update last response");
            Database.rollbackChanges();
            return false;
        }
    }

    public static boolean updateLastInput(String sessionId, String input) {
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.enterIntoSessionString(sessionId, "last_input", input);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to update last_response");
            Database.rollbackChanges();
            return false;
        }
    }

    public static void processRequest(HttpServletResponse resp, PrintWriter out, int initiator, String sessionID, String resStr) throws SQLException {
        Transaction transaction;
        switch (resStr){
            case "/balance":
                TransactionController.sendBalance(sessionID, initiator, out);
                break;

            case "/statement":
                TransactionController.sendStatement(sessionID, initiator, out);
                break;

            case "/info":
                TransactionController.sendTransactionInfo(sessionID, out);
                break;

            case "/changepin":
                InsertIntoDB insert = Database.getInsert();
                insert.changePIN(sessionID, initiator, out);
                Database.commitChanges();
                break;

            case "/transact":
                // Creating the transaction object
                transaction = TransactionController.createTransactionOBJ(sessionID, initiator);

                // transacting
                if (transaction.isAllowed(out))
                    transaction.execute();

                Database.commitChanges();
                out.println("Transaction Successful");
                out.close();
                break;

            case "/recharge":
                // Creating the transaction object
                transaction = TransactionController.createTransactionOBJForRecharge(sessionID, initiator);

                // transacting
                if (transaction.isAllowed(out))
                    transaction.execute();

                // updating transaction log to show the number that was recharged
                TransactionController.replaceProviderIDWithRechargedNumber(sessionID);

                Database.commitChanges();
                out.println("Number Recharged Successfully");
                out.close();
                break;

            default:
                Responses.internalServerError(resp, out);
        }
    }
}
