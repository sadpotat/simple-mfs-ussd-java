package NewClasses;

import Controllers.Database;
import Controllers.LogController;
import Models.GetFromDB;
import Models.InsertIntoDB;

import java.sql.SQLException;

public class MobileRecharge extends TransactionParent{
    private final String serviceID = "trns_recharge";
    public MobileRecharge(String session_id, int initiator) {
        super(session_id, initiator);
    }
    @Override
    public void initialiseFromLog() throws SQLException {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,2);
        int rec = getProviderAcc(sessionID);

        updatefields(rec, amnt);
    }

    public static int getProviderAcc(String sessionID){
        GetFromDB getter = Database.getGetter();

        // getting provider information from db
        String simType = LogController.getLastNthInputInt(sessionID, 4)==1 ? "PREPAID": "POSTPAID";
        String provider = LogController.getLastNthInputString(sessionID, 5);

        try {
            String providerName = getter.getProviderAccName(provider);
            String name = providerName + "_" + simType;
            return getter.getProviderID(name);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public void execute() throws SQLException {
        super.execute();
        // updating transaction log to container recipient mobile number instead of provider account
        int receiver = LogController.getLastNthInputInt(sessionID,3);
        InsertIntoDB insert = Database.getInsert();
        insert.updateReceiverInTLog(sessionID, receiver);
    }
}
