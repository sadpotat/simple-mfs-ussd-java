package Services;

import Controllers.LogController;

import java.sql.SQLException;

public class SendMoney extends ServiceController {
    private final String serviceID = "trns_send";
    public SendMoney(String session_id, int initiator) {
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() throws SQLException {
        int amnt = LogController.getLastNthInputInt(sessionID,3);
        int rec = LogController.getLastNthInputInt(sessionID,4);

        updateFields(rec, amnt, serviceID);
    }
}
