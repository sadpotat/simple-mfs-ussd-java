package Middleware.MobileRecharge.Response;

public interface ResponseBody {
    void setMessage(String msg);
    String getMessage();

    void setStatus(String stat);
    String getStatus();
    void setTrackingID(String id);
    String getTrackingID();
    void setTime(String tim);
    String getTime();

}
