package Controllers;

import Models.GetFromDB;
import Models.InsertIntoDB;

import java.sql.SQLException;

public class LogController {

    public static boolean addToLog(String sessionID, String input){
        try {
            InsertIntoDB insert = Database.getInsert();
            insert.insertIntoLog(sessionID, input);
            return true;
        } catch (Exception e){
            System.out.println("Error creating log");
            return false;
        }
    }

    public static int getLastNthInputInt(String sessionID, int n) {
        GetFromDB getter = Database.getGetter();
        try {
            return getter.getLastNthInputFromLogInt(sessionID, n);
        } catch (SQLException e) {
            System.out.println("could not fetch last nth input");
            return -1;
        }
    }
    public static String getLastNthInputString(String sessionID, int n) {
        GetFromDB getter = Database.getGetter();
        try {
            return getter.getLastNthInputFromLogString(sessionID, n);
        } catch (SQLException e) {
            System.out.println("could not fetch last nth input");
            return "";
        }
    }
}
