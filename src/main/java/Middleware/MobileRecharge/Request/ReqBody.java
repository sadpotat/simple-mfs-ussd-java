package Middleware.MobileRecharge.Request;

public interface ReqBody {
    String getTransactionID();
    String getSender();
    String getReceiver();
    String getAmount();
    boolean hasNull();
}
