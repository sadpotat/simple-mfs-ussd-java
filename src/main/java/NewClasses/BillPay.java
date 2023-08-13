package NewClasses;

import Controllers.LogController;
import Controllers.TransactionController;

import java.sql.SQLException;

public class BillPay extends TransactionParent {

    public String serviceID = "trns_bill";

    public BillPay(String session_id, int initiator) throws SQLException {
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,2);
        int rec = LogController.getLastNthInputInt(sessionID,3);

        updatefields(rec, amnt);
    }
}
