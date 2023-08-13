package NewClasses;

import Controllers.Database;
import Controllers.LogController;
import Models.InsertIntoDB;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PINChange extends TransactionParent{
    private double balance;
    private int newPIN;
    private int confirmPIN;
    private String serviceID = "pin_change";

    public PINChange(String session_id, int initiator){
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() throws SQLException {
        confirmPIN = LogController.getLastNthInputInt(sessionID, 1);
        newPIN = LogController.getLastNthInputInt(sessionID, 2);
        PIN = LogController.getLastNthInputInt(sessionID, 3);
    }

    @Override
    public boolean isAllowed(PrintWriter out){
        // checking if old pin is okay
        if (!verifyPIN(sender, PIN)) {
            out.println("Wrong PIN");
            out.close();
            return false;
        }

        // checking if the two PINs match
        if (newPIN != confirmPIN){
            out.println("The PINs do not match");
            out.close();
            return false;
        }

        return true;
    }

    @Override
    public void execute() throws SQLException {
        // updating pin
        InsertIntoDB insert = Database.getInsert();
        insert.updatePIN(sender, PIN, newPIN);
    }

    @Override
    public void sendSuccessMessage(HttpServletResponse resp, PrintWriter out){
        resp.setStatus(200);
        out.println("Your PIN has been changed");
        out.close();
    }

}
