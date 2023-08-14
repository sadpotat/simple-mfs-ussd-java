package Cache;

public class MenuRoute {
    private String accType;
    private String nextResponse;
    private String regex;
    private String errorMessage;
    private String serviceID;

    public MenuRoute(String accType, String nextResponse, String regex, String errorMessage, String serviceID){
        this.accType = accType;
        this.nextResponse = nextResponse;
        this.regex = regex;
        this.errorMessage = errorMessage;
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

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
