package Cache;

public class Provider {
    private String API;
    private String className;
    private String resClassName;
    private String reqType;

    public String getAPI() {
        return API;
    }

    public void setAPI(String API) {
        this.API = API;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getResClassName() {
        return resClassName;
    }

    public void setResClassName(String resClassName) {
        this.resClassName = resClassName;
    }
}
