package Helpers;

import com.mashape.unirest.http.HttpResponse;

import java.util.HashMap;

public class ResponseParsers {
    public static String getContentType(HttpResponse<String> httpResponse){
        // getting the object that contains content type
        Object obj = httpResponse.getHeaders().values().toArray()[4];
        // splitting characters to separate the contents string
        String[] str = obj.toString().split("[\\[;\\]]");
        return str[1];
    }

    public static HashMap<String, String> mapResponse(String resBodyStr, String contentType){
        HashMap<String, String> map = new HashMap<>();
        switch (contentType){
            case "application/json":
                // removing starting and ending brackets
                String[] keyValues = resBodyStr.split("[\\{\\}]")[1].split(",");
                //
                for(String keyVal: keyValues){
                    String[] keyValSeparated = keyVal.replaceAll("\"", "").split(":");
                    map.put(keyValSeparated[0].strip().toLowerCase(), keyValSeparated[1].strip());
                }
                break;
            case "text/xml":
                break;
        }
        return map;
    }
}
