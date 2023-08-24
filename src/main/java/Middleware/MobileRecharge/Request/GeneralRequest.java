package Middleware.MobileRecharge.Request;

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

    public String getTemplateRequestString(String template){
        return template.replace("val_1", trackingID).replace("val_2", sender).replace("val_3", receiver).replace("val_4", amount);
    }
}
