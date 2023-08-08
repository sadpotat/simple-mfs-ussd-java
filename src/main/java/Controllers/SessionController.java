package Controllers;

import Models.GetFromDB;
import Models.InsertIntoDB;
import Models.Session;

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
}
