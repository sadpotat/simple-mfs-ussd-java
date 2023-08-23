package Middleware.MobileRecharge.Request;

public interface ReqBody {
    void setTransactionID(String transactionID);
    String getTransactionID();
    void setSender(String sender);
    String getSender();
    void setReceiver(String receiver);
    String getReceiver();
    void setAmount(String amount);
    String getAmount();
}
