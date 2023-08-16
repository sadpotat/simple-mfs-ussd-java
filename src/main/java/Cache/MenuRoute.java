package Cache;

public class MenuRoute {
    private String accType;
    private String nextResponse;
    private String serviceID;

    public MenuRoute(String accType, String nextResponse, String serviceID){
        this.accType = accType;
        this.nextResponse = nextResponse;
        this.serviceID = serviceID;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
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
