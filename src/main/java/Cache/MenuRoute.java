package Cache;

public class MenuRoute {
    private String nextResponse;
    private String serviceID;

    public MenuRoute(String nextResponse, String serviceID){
        this.nextResponse = nextResponse;
        this.serviceID = serviceID;
    }

    public String getNextResponse() {
        return nextResponse;
    }

    public void setNextResponse(String nextResponse) {
        this.nextResponse = nextResponse;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
