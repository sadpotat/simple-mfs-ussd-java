package Services;

import Controllers.LogController;

import java.sql.SQLException;

public class Payment extends ServiceController {
    private final String serviceID = "trns_pay";

    public Payment(String session_id, int initiator) {
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() throws SQLException {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,3);
        int rec = LogController.getLastNthInputInt(sessionID,4);

        updateFields(rec, amnt, serviceID);
    }
}
