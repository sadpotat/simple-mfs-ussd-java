package Cache;

public class Provider {
    private String ApiId;
    private String reqTemplate;
    private String reqType;
    private String reqMethod;

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public String getApiId() {
        return ApiId;
    }

    public void setApiId(String apiId) {
        this.ApiId = apiId;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getReqTemplate() {
        return reqTemplate;
    }

    public void setReqTemplate(String reqTemplate) {
        this.reqTemplate = reqTemplate;
    }
}
