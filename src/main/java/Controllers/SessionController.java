package Controllers;

import Models.InsertIntoDB;
import Models.Transaction;
import NewClasses.*;

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
                CashOut cout = new CashOut(sessionID, initiator);
                cout.initialiseFromLog();
                if (cout.isAllowed(out)){
                    cout.execute();
                }
                break;

            case "trns_send":
                SendMoney send = new SendMoney(sessionID, initiator);
                send.initialiseFromLog();
                if (send.isAllowed(out)){
                    send.execute();
                }
                break;

            case "trns_pay":
                Payment pay = new Payment(sessionID, initiator);
                pay.initialiseFromLog();
                if(pay.isAllowed(out)){
                    pay.execute();
                }
                break;

            case "trns_bill":
                BillPay bill = new BillPay(sessionID, initiator);
                bill.initialiseFromLog();
                if(bill.isAllowed(out)){
                    bill.execute();
                }
                break;

            case "trns_emi":
                EMIPayment emi = new EMIPayment(sessionID, initiator);
                emi.initialiseFromLog();
                if(emi.isAllowed(out)){
                    emi.execute();
                }
                break;

            case "trns_recharge":
                MobileRecharge recharge = new MobileRecharge(sessionID, initiator);
                recharge.initialiseFromLog();
                if(recharge.isAllowed(out)){
                    recharge.execute();
                }
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
