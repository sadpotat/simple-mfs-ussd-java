package NewClasses;

import Controllers.LogController;

import java.sql.SQLException;

public class SendMoney extends TransactionParent{
    private String serviceID = "tnrs_send";
    public SendMoney(String session_id, int initiator) {
        super(session_id, initiator);
        BillPay bill = new BillPay(session_id, initiator);
//        bill.
    }

    @Override
    public void initialiseFromLog() throws SQLException {
        int amnt = LogController.getLastNthInputInt(sessionID,3);
        int rec = LogController.getLastNthInputInt(sessionID,4);

        updatefields(rec, amnt);
    }
}
