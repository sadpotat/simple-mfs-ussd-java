package Services;

import Controllers.Database;
import Controllers.LogController;
import Controllers.Responses;
import Helpers.HTTP;
import Helpers.ResponseParsers;
import Middleware.Request.GeneralRequest;
import Middleware.Response.GeneralResponse;
import Models.GetFromDB;
import Cache.RequestProperties;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.sql.SQLException;

public class MobileRecharge extends ServiceController {
    private final String serviceID = "trns_recharge";
    private String providerMenu;
    private RequestProperties reqPropertiesObj;
    private String statusOkayMsg;
    private int sendMessage;
    private String apiId;
    public MobileRecharge(String session_id, int initiator) {
        super(session_id, initiator);
    }
    @Override
    public void initialiseFromLog() throws SQLException {
        // getting transaction info from log
        int amnt = LogController.getLastNthInputInt(sessionID,2);
        int rec = LogController.getLastNthInputInt(sessionID,3);
        providerMenu = getProviderMenuNo(sessionID);
        apiId = cache.getProviderApiId(providerMenu);
        reqPropertiesObj = cache.getReqPropObj(apiId);
        statusOkayMsg = cache.getStatusOkayMsg(apiId);

        updateFields(rec, amnt, serviceID);
    }

    public static String getProviderMenuNo(String sessionID){
        // getting provider information from db
        try {
            String simType = LogController.getLastNthInputString(sessionID, 4);
            String providerNum = LogController.getLastNthInputString(sessionID, 5);
            return providerNum + simType;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean isAllowed(HttpServletResponse resp, PrintWriter out) {
        // verifying if the user can make transactions
        if (!senderObj.getStatus().equals("ACTIVE")){
            resp.setStatus(403);
            out.println("Cannot make transactions, your account is " + senderObj.getStatus());
            out.close();
            return false;
        }

        // in case failed to fetch reqPropertiesObj
        if (reqPropertiesObj ==null){
            Responses.internalServerError(resp, out);
            return false;
        }

        // verifying PIN
        int hash = LogController.getLastNthInputInt(sessionID,1);
        if (!verifyPIN(sender, hash)) {
            resp.setStatus(400);
            out.println("Wrong PIN");
            out.close();
            return false;
        }

        // checking if the amount can be transacted
        if (!amountIsInBalance()){
            resp.setStatus(403);
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
        GeneralRequest reqBody = new GeneralRequest();
        reqBody.setTrackingID(sessionID);
        reqBody.setSender("myMFS_" + sender);
        reqBody.setReceiver(Integer.toString(receiver));
        reqBody.setAmount(Double.toString(amount));

        // formatting the object to a String
        String body = reqBody.formatReqBodyTemplate(apiId, reqPropertiesObj.getBodyTemplate());
        reqPropertiesObj.setBody(body);

        // getting url
        String urlStr = cache.getUrlFromApiId(apiId);
        reqPropertiesObj.setUrl(urlStr);

        try{
            // connecting to the telco topup API
            HttpURLConnection http = HTTP.sendRequest(reqPropertiesObj);
            // converting bytestream response to String
            String resBody = HTTP.convertInputStream2String(http.getInputStream());

            // parsing the string into an object
            String contentType = http.getContentType();
            // parsing response to json objects
            GeneralResponse response = ResponseParsers.parseToJsonObject(apiId, resBody, contentType);

            // insert new table in database for success messages and add it to cache //
            if (response==null){
                // internal server error
                sendMessage = 0;
            } else if (response.getStatus().equals(statusOkayMsg)) {
                // success
                sendMessage = 2;
            } else {
                // error at telco side
                sendMessage = 1;
            }
            // inserting data into transaction table
            // down here because rollback does not affect insertions for some reason
            transact();
        } catch (Exception e) {
            System.out.println("failed to execute mobile recharge");
        }
    }

    @Override
    protected void updateBalance() throws SQLException {
        // when the sender is an agent or a bank, balance is -1

        // updating sender balance
        if (getter.getBalance(sender)>0) updateSenderBalance();

        // updating receiver balance
        this.updateReceiverBalance();
    }

    @Override
    protected void updateReceiverBalance() throws SQLException {
        GetFromDB getter = Database.getGetter();
        int providerWallet = getter.getProviderID(providerMenu);
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
