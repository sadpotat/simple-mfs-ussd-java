package Services;

import Cache.CacheLoader;
import Cache.MobileRechargeResponseKeys;
import Controllers.Database;
import Controllers.LogController;
import Controllers.Responses;
import Helpers.HTTP;
import Helpers.ResponseParsers;
import Helpers.Utils;
import Middleware.MobileRecharge.Request.ReqBody;
import Middleware.MobileRecharge.Response.GeneralResponse;
import Middleware.MobileRecharge.Response.ResponseBody;
import Models.GetFromDB;
import Cache.Provider;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.HashMap;

public class MobileRecharge extends ServiceController {
    private final String serviceID = "trns_recharge";
    private String body;
    private String providerName;
    private Provider providerObj;
    private MobileRechargeResponseKeys keys;
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
        keys = cache.getMRResKeyObj(providerObj.getAPI());

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
        System.out.println("execute() starts here");
        // updating sender balance
        updateBalance();

        // creating object for req body
        ReqBody reqBody = createReqBodyObj();

        if (reqBody.hasNull()){
            // will send internal server error at sendSuccessMessage
            return;
        }

        // parsing obj to string
        body = Utils.convertToFormattedString(reqBody, providerObj.getReqType());

        try{
            // connecting to the telco topup API
            String content = providerObj.getReqType().equals("json") ? "application/json" : "text/xml";
            HttpURLConnection http = HTTP.sendPostRequest(content, providerObj.getAPI(), body);
            // converting bytestream response to String
            String resBody = HTTP.convertInputStream2String(http.getInputStream());

            // parsing the string into an object
            String contentType = http.getContentType();
            // parsing response to json objects
            ResponseBody response = parseToJsonObject(resBody, contentType);
            System.out.println(response.getStatus());
            System.out.println(keys.getStatus_ok());
            response.getStatus().equals(keys.getStatus_ok());

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
            System.out.println("database changes committed");
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

    private ResponseBody parseToJsonObject(String resBodyStr, String contentType){
        ResponseBody responseBody = new GeneralResponse();
        // parsing response to a hashmap
        HashMap<String, String> responseMap = ResponseParsers.mapResponse(resBodyStr, contentType);
        // getting key names from cache
        try{
            System.out.println(keys.getMessage());
            System.out.println(responseMap.get(keys.getMessage()));
            responseBody.setMessage("\"test\"");
            responseBody.setMessage(responseMap.get(keys.getMessage()));
            responseBody.setStatus(responseMap.get(keys.getStatus()));
            responseBody.setTrackingID(responseMap.get(keys.getTrackingID()));
            responseBody.setTime(responseMap.get(keys.getTime()));
        } catch (Exception e){
            responseBody = null;
        }

        return responseBody;
    }
    private ReqBody createReqBodyObj() {
        Class<?> clazz;
        ReqBody reqBody;
        try {
            clazz = Class.forName(providerObj.getClassName());
            // getting class constructor that takes specific argument types
            Constructor<?> constructor = clazz.getConstructor(String.class, String.class, String.class, String.class);
            // creating an instance using the constructor and provided arguments
            Object[] arguments = {sessionID, "myMFS_" + sender, Integer.toString(receiver), Double.toString(amount)};
            reqBody = (ReqBody) constructor.newInstance(arguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return reqBody;
    }

    @Override
    public void sendSuccessMessage(HttpServletResponse resp, PrintWriter out) {
        if (sendMessage==0)
            Responses.internalServerError(resp, out);
        else if (sendMessage==2){
            out.println("Mobile recharged successfully");
        } else {
            out.println("Failed to recharge mobile number");
        }
        out.close();
    }
}
