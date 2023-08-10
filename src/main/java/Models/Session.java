package Models;

import java.time.LocalDateTime;

public class Session {
    private String session_id;
    private int sim;
    private String last_input;
    private String last_response;
    private LocalDateTime last_update;

    private String serviceID;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getLast_input() {
        return last_input;
    }

    public void setLast_input(String last_input) {
        this.last_input = last_input;
    }

    public String getLast_response() {
        return last_response;
    }

    public void setLast_response(String last_response) {
        this.last_response = last_response;
    }

    public LocalDateTime getLast_update() {
        return last_update;
    }

    public void setLast_update(LocalDateTime last_update) {
        this.last_update = last_update;
    }

    public int getSim() {
        return sim;
    }

    public void setSim(int sim) {
        this.sim = sim;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
