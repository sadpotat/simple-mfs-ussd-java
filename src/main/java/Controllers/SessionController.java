package Controllers;

import Models.InsertIntoDB;
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

    public static void processRequest(HttpServletResponse resp, PrintWriter out, int initiator, String sessionID, String serviceID) throws SQLException {
        Transaction transaction;
        switch (serviceID){
            case "info_balance":
                TransactionController.sendBalance(sessionID, initiator, out);
                break;

            case "info_statement":
                TransactionController.sendStatement(sessionID, initiator, out);
                break;

            case "pin_change":
                InsertIntoDB insert = Database.getInsert();
                insert.changePIN(sessionID, initiator, out);
                Database.commitChanges();
                break;

            case "trns_cout":
            case "trns_send":
            case "trns_payt":
            case "trns_bill":
            case "trns_emi":
                // Creating the transaction object
                transaction = TransactionController.createTransactionOBJ(sessionID, initiator);

                // transacting
                if (transaction.isAllowed(out))
                    transaction.execute();

                Database.commitChanges();
                out.println("Transaction Successful");
                out.close();
                break;

            case "trns_recharge":
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

    public static boolean updateServiceID(String sessionID, String serviceID) {
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.enterIntoSessionString(sessionID, "service_id", serviceID);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to update serviceID");
            Database.rollbackChanges();
            return false;
        }
    }
}
