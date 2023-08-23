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
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.HashMap;

public class MobileRecharge extends ServiceController {
    private final String serviceID = "trns_recharge";
    private String body;
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
        String providerName = getProviderName(sessionID);
        providerObj = cache.getProviderObj(providerName);
        keys = cache.getMRResKeyObj(providerObj.getAPI());

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
        // creating object for req body
        ReqBody reqBody = createReqBodyObj();

        // parsing obj to string
        body = Utils.convertToFormattedString(reqBody, providerObj.getReqType());

        try{
            // connecting to the telco topup API
            String content = providerObj.getReqType().equals("json") ? "application/json" : "text/xml";
            HttpResponse<String> httpResponse = HTTP.sendPostRequest(content, providerObj.getAPI(), body);
            // getting the body of the response as a string
            String resBody = httpResponse.getBody();
            // parsing the string into an object
            String contentType = ResponseParsers.getContentType(httpResponse);
            // parsing response to json objects
            ResponseBody response = parseToJsonObject(resBody, contentType);

            // insert new table in database for success messages and add it to cache //
            if (response==null){
                // internal server error
                sendMessage = 0;
            } else if (response.getStatus().equals(keys.getStatus_ok())) {
                // success
                sendMessage = 2;
                // inserting data into transaction table
                transact();
                // updating sender balance
                updateSenderBalance();
            } else {
                // error at telco side
                sendMessage = 1;
            }

        } catch (Exception e) {
            Database.rollbackChanges();
        }



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
            Object[] arguments = {sessionID, "myMFS_" + Integer.toString(sender), Integer.toString(receiver), Double.toString(amount)};
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
