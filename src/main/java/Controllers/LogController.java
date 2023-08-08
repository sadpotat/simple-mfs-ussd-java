package Controllers;

import Models.GetFromDB;
import Models.InsertIntoDB;

import java.sql.SQLException;

public class LogController {

    public static boolean addToLog(String sessionID, String input){
        try {
            InsertIntoDB insert = Database.getInsert();
            insert.insertIntoLog(sessionID, input);
            Database.commitChanges();
            return true;
        } catch (Exception e){
            System.out.println("Error creating log");
            Database.rollbackChanges();
            return false;
        }
    }

    public static String getLastNthInput(String sessionID, int n) {
        GetFromDB getter = Database.getGetter();
        try {
            return getter.getLastNthInputFromLog(sessionID, n);
        } catch (SQLException e) {
            System.out.println("could not fetch last nth input");
            return "";
        }
    }
}
