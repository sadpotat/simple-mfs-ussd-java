package Middleware.MobileRecharge.Request;

public class JsonBodyGP extends ReqBodyController {
    private String transactionid;
    private String agent;
    private String recipient;
    private String topup;


    public JsonBodyGP(String transactionid, String agent, String paymentto, String amount) {
        this.transactionid = transactionid;
        this.agent = agent;
        this.recipient = paymentto;
        this.topup = amount;
    }

    @Override
    public String getTransactionID() {
        return transactionid;
    }

    @Override
    public String getSender() {
        return agent;
    }

    @Override
    public String getReceiver() {
        return recipient;
    }

    @Override
    public String getAmount() {
        return topup;
    }
}
