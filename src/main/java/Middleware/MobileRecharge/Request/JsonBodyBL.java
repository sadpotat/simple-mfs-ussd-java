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
    public String getTransactionID() {
        return trackingid;
    }

    @Override
    public String getSender() {
        return paymentfrom;
    }

    @Override
    public String getReceiver() {
        return paymentto;
    }

    @Override
    public String getAmount() {
        return amount;
    }
}
