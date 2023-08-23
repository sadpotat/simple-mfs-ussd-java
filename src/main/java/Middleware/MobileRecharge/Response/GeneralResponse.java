package Middleware.MobileRecharge.Response;

public class GeneralResponse implements ResponseBody{
    private String text;
    private String status;
    private String trackingid;
    private String time;

    @Override
    public void setMessage(String msg) {
        text = msg;
    }

    @Override
    public String getMessage() {
        return text;
    }

    @Override
    public void setStatus(String stat) {
        status = stat;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setTrackingID(String id) {
        trackingid = id;
    }

    @Override
    public String getTrackingID() {
        return trackingid;
    }

    @Override
    public void setTime(String tim) {
        time = tim;
    }

    @Override
    public String getTime() {
        return time;
    }
}
