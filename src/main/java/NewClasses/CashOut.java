package NewClasses;

import Controllers.LogController;

import java.sql.SQLException;

public class CashOut extends TransactionParent{

    private String serviceID = "trns_cout";
    public CashOut(String session_id, int initiator) {
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() throws SQLException {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,2);
        int rec = LogController.getLastNthInputInt(sessionID,3);

        updatefields(rec, amnt);
    }
}
