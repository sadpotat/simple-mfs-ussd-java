package Cache;

public class Provider {
    private String ApiId;
    private String className;
    private String reqTemplate;
    private String reqType;

    public String getApiId() {
        return ApiId;
    }

    public void setApiId(String apiId) {
        this.ApiId = apiId;
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

    public String getReqTemplate() {
        return reqTemplate;
    }

    public void setReqTemplate(String reqTemplate) {
        this.reqTemplate = reqTemplate;
    }
}
