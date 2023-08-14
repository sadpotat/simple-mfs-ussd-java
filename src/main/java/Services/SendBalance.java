package Services;

import Controllers.Database;
import Controllers.LogController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class SendBalance extends ServiceController {
    private double balance;
    private final String serviceID = "info_balance";

    public SendBalance(String session_id, int initiator){
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() {
        PIN = LogController.getLastNthInputInt(sessionID, 1);
    }

    @Override
    public boolean isAllowed(PrintWriter out){
        if (verifyPIN(sender, PIN))
            return true;
        out.println("Wrong PIN");
        out.close();
        return false;
    }

    @Override
    public void execute(){
        balance = Database.getGetter().getBalance(sender);
    }

    @Override
    public void sendSuccessMessage(HttpServletResponse resp, PrintWriter out){
        resp.setStatus(200);
        out.println("Current balance: " + balance);
        out.close();
    }

}
