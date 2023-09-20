package Services;

import Controllers.Database;
import Controllers.LogController;
import Controllers.SessionController;
import Models.GetFromDB;
import Models.InsertIntoDB;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;

public class CreateAccount extends ServiceController {
    private int pin;
    private int rePin;
    private String name;

    public CreateAccount(String session_id, int initiator) {
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() throws SQLException {
        name = LogController.getLastNthInputString(sessionID, 2);
        rePin = LogController.getLastNthInputInt(sessionID, 3);
        pin = LogController.getLastNthInputInt(sessionID, 4);
    }

    @Override
    public boolean isAllowed(HttpServletResponse resp, PrintWriter out) {
        return pin==rePin;
    }

    @Override
    public void execute() throws SQLException {
        InsertIntoDB insert = Database.getInsert();
        // creating new account in db
        insert.createTemporaryAccount(sender, name);
        insert.addPinToAccount(sender, pin);
        insert.addAccToBalanceTable(sender);
    }

    @Override
    public void sendSuccessMessage(HttpServletResponse resp, PrintWriter out) {
        resp.setStatus(200);
        out.println("Account created successfully.");
    }
}
