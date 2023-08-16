package Services;

import Cache.CacheLoader;
import Controllers.Database;
import Controllers.LogController;
import Models.GetFromDB;
import Models.InsertIntoDB;

import java.sql.SQLException;

public class MobileRecharge extends ServiceController {
    private final String serviceID = "trns_recharge";
    public MobileRecharge(String session_id, int initiator) {
        super(session_id, initiator);
    }
    @Override
    public void initialiseFromLog() throws SQLException {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,2);
        int rec = getProviderAcc(sessionID);

        updateFields(rec, amnt, serviceID);
    }

    public static int getProviderAcc(String sessionID){
        CacheLoader cache = CacheLoader.getInstance();
        GetFromDB getter = Database.getGetter();
        // getting provider information from db
        String simType = LogController.getLastNthInputInt(sessionID, 4)==1 ? "PREPAID": "POSTPAID";
        String provider = LogController.getLastNthInputString(sessionID, 5);

        try {
            String providerName = cache.getProviderName(provider);
            String name = providerName + "_" + simType;
            return getter.getProviderID(name);
        } catch (Exception e) {
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
