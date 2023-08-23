package Helpers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class HTTP {
    public static HttpResponse<String> sendPostRequest(String content, String API, String body) throws UnirestException {
            ;
            Unirest.setTimeouts(5000, 5000);
            HttpResponse<String> httpResponse = Unirest.post(API)
                    .header("Content-Type", content)
                    .body(body)
                    .asString();
            return  httpResponse;
    }
}
