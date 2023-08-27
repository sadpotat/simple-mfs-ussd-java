package Helpers;

import Cache.CacheLoader;
import Middleware.Response.GeneralResponse;
import org.json.JSONObject;
import org.json.XML;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class ResponseParsers {
//    public static String getContentType(HttpResponse<String> httpResponse){
//        // getting the object that contains content type
//        Object obj = httpResponse.getHeaders().values().toArray()[4];
//        // splitting characters to separate the contents string
//        String[] str = obj.toString().split("[\\[;\\]]");
//        return str[1];
//    }

    public static HashMap<String, Object> mapResponse(String API, String resBodyStr, String contentType){
        CacheLoader cache = CacheLoader.getInstance();
        String switchVal = contentType.contains("json") ? "1" : "2";
        JSONObject jsonResponse;
        switch (switchVal){
            case "1":
                // deserializing json string to object
                jsonResponse = new JSONObject(resBodyStr);
                return (HashMap<String, Object>) jsonResponse.toMap();

            case "2":
                // deserializing xml string to object
                jsonResponse = XML.toJSONObject(resBodyStr);
                // getting xml root key
                String key = cache.getXmlResponseRoot(API);
                // getting items inside root key
                if (!key.equals("none"))
                    jsonResponse = jsonResponse.getJSONObject(key);
                return (HashMap<String, Object>) jsonResponse.toMap();
            default:
                return null;
        }
    }

    public static GeneralResponse parseToJsonObject(String API, String resBodyStr, String contentType){
        GeneralResponse responseBody = new GeneralResponse();
        // parsing response to a hashmap
        HashMap<String, Object> responseMap = mapResponse(API, resBodyStr, contentType);
        // getting keys and their setters from cache
        CacheLoader cache = CacheLoader.getInstance();
        HashMap<String, String> setters = cache.getSettersForAPI(API);

        Method method;
        try{
            for (String key: responseMap.keySet()) {
                String setterName = setters.get(key);
                method = responseBody.getClass().getMethod(setterName, String.class);
                method.invoke(responseBody, responseMap.get(key));
            }
        } catch (Exception e){
            responseBody = null;
        }
        return responseBody;
    }
}
