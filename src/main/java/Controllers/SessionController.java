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
                SendBalance balance = new SendBalance(sessionID, initiator);
                balance.initialiseFromLog();
                if (balance.isAllowed(out)){
                    balance.execute();
                    balance.sendSuccessMessage(resp, out);
                }
                break;

            case "info_statement":
                SendStatement miniStatement = new SendStatement(sessionID, initiator);
                miniStatement.initialiseFromLog();
                if (miniStatement.isAllowed(out)){
                    miniStatement.execute();
                    miniStatement.sendSuccessMessage(resp, out);
                }
                break;

            case "pin_change":
                PINChange pinChange = new PINChange(sessionID, initiator);
                pinChange.initialiseFromLog();
                if (pinChange.isAllowed(out)){
                    pinChange.execute();
                    pinChange.sendSuccessMessage(resp, out);
                }
                break;

            case "trns_cout":
                CashOut cout = new CashOut(sessionID, initiator);
                cout.initialiseFromLog();
                if (cout.isAllowed(out)){
                    cout.execute();
                    cout.sendSuccessMessage(resp, out);
                }
                break;

            case "trns_send":
                SendMoney send = new SendMoney(sessionID, initiator);
                send.initialiseFromLog();
                if (send.isAllowed(out)){
                    send.execute();
                    send.sendSuccessMessage(resp, out);
                }
                break;

            case "trns_pay":
                Payment pay = new Payment(sessionID, initiator);
                pay.initialiseFromLog();
                if(pay.isAllowed(out)){
                    pay.execute();
                    pay.sendSuccessMessage(resp, out);
                }
                break;

            case "trns_bill":
                BillPay bill = new BillPay(sessionID, initiator);
                bill.initialiseFromLog();
                if(bill.isAllowed(out)){
                    bill.execute();
                    bill.sendSuccessMessage(resp, out);
                }
                break;

            case "trns_emi":
                EMIPayment emi = new EMIPayment(sessionID, initiator);
                emi.initialiseFromLog();
                if(emi.isAllowed(out)){
                    emi.execute();
                    emi.sendSuccessMessage(resp, out);
                }
                break;

            case "trns_recharge":
                MobileRecharge recharge = new MobileRecharge(sessionID, initiator);
                recharge.initialiseFromLog();
                if(recharge.isAllowed(out)){
                    recharge.execute();
                    recharge.sendSuccessMessage(resp, out);
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
