package Middleware.Response;

public class GeneralResponse{
    private String text;
    private String status;
    private String trackingid;
    private String time;

    public void setMessage(String msg) {
        text = msg;
    }

    public String getMessage() {
        return text;
    }

    public void setStatus(String stat) {
        status = stat;
    }

    public String getStatus() {
        return status;
    }

    public void setTrackingID(String id) {
        trackingid = id;
    }

    public String getTrackingID() {
        return trackingid;
    }

    public void setTime(String tim) {
        time = tim;
    }

    public String getTime() {
        return time;
    }
}
