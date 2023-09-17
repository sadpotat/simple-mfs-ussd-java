package Services;

import Controllers.LogController;
import Controllers.SessionController;
import Helpers.RequestParsers;
import Models.Session;

import java.sql.SQLException;

public class BillPay extends ServiceController{

    public String serviceID = "trns_bill";

    public BillPay(String session_id, int initiator){
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() throws SQLException {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,2);
        int chosenBillerInMenu = LogController.getLastNthInputInt(sessionID,3);

        int totalBillers = getter.getNumberOfCustomersOfType("BILLER");
        int rec;
        if(chosenBillerInMenu<=totalBillers)
            rec = getter.getNthCustomerIdOfTypeFromOption(chosenBillerInMenu, "BILLER");
        else
            rec = -1;
        updateFields(rec, amnt, serviceID);
    }


}
