package Middleware.MobileRecharge.Request;

abstract class ReqBodyController implements ReqBody{
    public boolean hasNull() {
        return getTransactionID()==null || getSender()==null || getReceiver()==null || getAmount()==null;
    }
}
