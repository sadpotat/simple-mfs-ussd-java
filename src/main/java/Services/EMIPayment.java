package Services;

import Controllers.LogController;

import java.sql.SQLException;

public class EMIPayment extends ServiceController {
    private final String serviceID = "trns_emi";
    public EMIPayment(String session_id, int initiator) {
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() throws SQLException {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,2);
        int rec = LogController.getLastNthInputInt(sessionID,3);

        updatefields(rec, amnt, serviceID);
    }
}
