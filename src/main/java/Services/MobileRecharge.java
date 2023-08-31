package Services;

import Cache.CacheLoader;
import Cache.Status;
import Controllers.Database;
import Controllers.LogController;
import Controllers.Responses;
import Helpers.HTTP;
import Helpers.ResponseParsers;
import Middleware.Request.GeneralRequest;
import Middleware.Response.GeneralResponse;
import Models.GetFromDB;
import Cache.Provider;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.sql.SQLException;

public class MobileRecharge extends ServiceController {
    private final String serviceID = "trns_recharge";
    private String providerName;
    private Provider providerObj;
    private Status keys;
    private int sendMessage;
    public MobileRecharge(String session_id, int initiator) {
        super(session_id, initiator);
    }
    @Override
    public void initialiseFromLog() throws SQLException {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,2);
        int rec = LogController.getLastNthInputInt(sessionID,3);
        providerName = getProviderName(sessionID);
        providerObj = cache.getProviderObj(providerName);
        keys = cache.getMRResKeyObj(providerObj.getApiId());

        updateFields(rec, amnt, serviceID);
    }

    public static String getProviderName(String sessionID){
        CacheLoader cache = CacheLoader.getInstance();
        // getting provider information from db
        String simType = LogController.getLastNthInputInt(sessionID, 4)==1 ? "PREPAID": "POSTPAID";
        String provider = LogController.getLastNthInputString(sessionID, 5);

        try {
            String providerName = cache.getProviderName(provider);
            return providerName + "_" + simType;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isAllowed(PrintWriter out) {
        // verifying if the user can make transactions
        if (!senderObj.getStatus().equals("ACTIVE")){
            out.println("Cannot make transactions, your account is " + senderObj.getStatus());
            out.close();
            return false;
        }

        // verifying PIN
        int hash = LogController.getLastNthInputInt(sessionID,1);
        if (!verifyPIN(sender, hash)) {
            out.println("Wrong PIN");
            out.close();
            return false;
        }

        // checking if the amount can be transacted
        if (!amountIsInBalance()){
            out.println("Insufficient balance");
            out.close();
            return false;
        }

        return true;
    }

    @Override
    public void execute() throws SQLException {
        // updating sender balance
        updateBalance();

        // creating object to be sent as request
        GeneralRequest reqBody = new GeneralRequest(sessionID, "myMFS_" + sender,
                Integer.toString(receiver), Double.toString(amount));

        // formatting the object to a String
        String body = reqBody.getTemplateRequestString(providerObj.getReqTemplate());

        try{
            // connecting to the telco topup API
            String content = providerObj.getReqType();
            String URL = cache.getUrlFromApiId(providerObj.getApiId());
            String reqMethod = providerObj.getReqMethod();
            HttpURLConnection http = HTTP.sendRequest(content, URL, reqMethod, body);
            // converting bytestream response to String
            String resBody = HTTP.convertInputStream2String(http.getInputStream());

            // parsing the string into an object
            String contentType = http.getContentType();
            // parsing response to json objects
            GeneralResponse response = ResponseParsers.parseToJsonObject(providerObj.getApiId(), resBody, contentType);

            // insert new table in database for success messages and add it to cache //
            if (response==null){
                // internal server error
                sendMessage = 0;
            } else if (response.getStatus().equals(keys.getStatus_ok())) {
                // success
                sendMessage = 2;
            } else {
                // error at telco side
                sendMessage = 1;
            }
            // inserting data into transaction table
            // down here because rollback does not affect insertions for some reason
            transact();
            Database.commitChanges();
        } catch (Exception e) {
            Database.rollbackChanges();
        }
    }

    @Override
    protected void updateBalance() throws SQLException {
        // when the sender is an agent or a bank, balance is -1

        // updating sender balance
        if (getter.getBalance(sender)>0) updateSenderBalance();

        // updating receiver balance
        updateReceiverBalance();
    }

    @Override
    protected void updateReceiverBalance() throws SQLException {
        GetFromDB getter = Database.getGetter();
        int providerWallet = getter.getProviderID(providerName);
        updateReceiverBalance.setInt(1, providerWallet);
        updateReceiverBalance.setDouble(2, amount);
        updateReceiverBalance.setInt(3, providerWallet);
        updateReceiverBalance.executeUpdate();
    }
    @Override
    public void sendSuccessMessage(HttpServletResponse resp, PrintWriter out) {
        resp.setContentType("text/plain");
        if (sendMessage==0)
            Responses.internalServerError(resp, out);
        else if (sendMessage==2){
            resp.setStatus(200);
            out.println("Mobile recharged successfully");
        } else {
            resp.setStatus(500);
            out.println("Failed to recharge mobile number");
        }
        out.close();
    }
}
