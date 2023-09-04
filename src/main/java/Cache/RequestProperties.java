package Cache;

import java.util.HashMap;

public class RequestProperties {
    private String bodyTemplate;
    private String reqMethod;
    private String body;
    private int timeout;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private HashMap<String, String> Headers;

    public HashMap<String, String> getHeaders() {
        return Headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        Headers = headers;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }
}
