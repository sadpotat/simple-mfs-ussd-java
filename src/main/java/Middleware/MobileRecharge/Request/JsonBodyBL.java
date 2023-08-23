package Middleware.MobileRecharge.Request;

public class JsonBodyBL extends ReqBodyController {
    private String trackingid;
    private String paymentfrom;
    private String paymentto;
    private String amount;


    public JsonBodyBL(String trackingid, String paymentfrom, String paymentto, String amount) {
        this.trackingid = trackingid;
        this.paymentfrom = paymentfrom;
        this.paymentto = paymentto;
        this.amount = amount;
    }


    @Override
    public void setTransactionID(String transactionID) {
        trackingid = transactionID;
    }

    @Override
    public String getTransactionID() {
        return trackingid;
    }

    @Override
    public void setSender(String sender) {
        paymentfrom = sender;
    }

    @Override
    public String getSender() {
        return paymentfrom;
    }

    @Override
    public void setReceiver(String receiver) {
        paymentto = receiver;
    }

    @Override
    public String getReceiver() {
        return paymentto;
    }

    @Override
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String getAmount() {
        return amount;
    }
}
