package Middleware.Request;

import Cache.CacheLoader;

import java.lang.reflect.Method;
import java.util.HashMap;

public class GeneralRequest {
    private String trackingID;
    private String sender;
    private String receiver;
    private String amount;

    public GeneralRequest(String trackingID, String sender, String receiver, String amount) {
        this.trackingID = trackingID;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String formatReqBodyTemplate(String apiId, String bodyTemplate) {
        CacheLoader cache = CacheLoader.getInstance();
        HashMap<String, String> getters = cache.getGettersForAPI(apiId);
        Method method;
        try{
            for (String key: getters.keySet()){
                String getterName = getters.get(key);
                method = GeneralRequest.class.getMethod(getterName);
                String replaceVal = (String) method.invoke(this);
                bodyTemplate = bodyTemplate.replace(key, replaceVal);
            }
            return bodyTemplate;
        } catch (Exception e) {
            System.out.println("failed to format body");
            return null;
        }
    }
}
