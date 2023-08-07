package Controllers;

import Models.GetFromDB;
import Models.InsertIntoDB;
import Models.Session;

import java.sql.SQLException;

public class SessionController {
    public static boolean createSession(int initiator) {
        String sessionID = Utils.createSessionID();
        InsertIntoDB insert = InsertIntoDB.getInsert();
        try {
            insert.createSessionEntry(sessionID, initiator);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            Database.rollbackChanges();
            return false;
        }
    }

    public static String getSessionID(int initiator) {
        GetFromDB getter = GetFromDB.getGetter();
        Session session = getter.getLastSession(initiator);
        return session.getSession_id();
    }

    public static boolean updateLastResponse(String sessionID, String value) {
        InsertIntoDB insert = InsertIntoDB.getInsert();
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
        InsertIntoDB insert = InsertIntoDB.getInsert();
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

    public static String getNextMenu(String account_type, String prev_menu, int input){
        GetFromDB getter = GetFromDB.getGetter();
        return getter.getNextResponseMenu(account_type, prev_menu, input);
    }

    // method overloading
    public static String getNextMenu(String account_type, String prev_menu, String input){
        GetFromDB getter = GetFromDB.getGetter();
        return getter.getNextResponseMenu(account_type, prev_menu, input);
    }

    public static int getInputAsInt(String lastInput) {
        if (Utils.isNumeric(lastInput) && lastInput.length()<2)
            return Integer.parseInt(lastInput);
        return -1;
    }
}
