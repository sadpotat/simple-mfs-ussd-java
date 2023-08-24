package Helpers;

import Cache.CacheLoader;
import Middleware.Response.GeneralResponse;
import com.mashape.unirest.http.HttpResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
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
        String switchVal = contentType.contains("json") ? "1" : "2";
        switch (switchVal){
            case "1":
                // removing starting and ending brackets
                String[] keyValues = resBodyStr.split("[\\{\\}]")[1].split(",");
                //
                for(String keyVal: keyValues){
                    String[] keyValSeparated = keyVal.replaceAll("\"", "").split(":");
                    map.put(keyValSeparated[0].strip().toLowerCase(), keyValSeparated[1].strip());
                }
                break;
            case "2":
                // removing brackets to separate tags
                String noBrackets = resBodyStr.replaceAll(">\\s*<", "split_here");
                String[] split1 = noBrackets.split("split_here");
                // removing bracketing tag
                String[] newArray = Arrays.copyOfRange (split1, 1, (split1.length)-1);
                for(String tag: newArray){
                    // removing brackets again so that elements are in the order:
                    // starting_tag_1, value1, ending_tag_1, starting_tag_2, ....
                    String [] subarr = tag.split(">|</");
                    map.put(subarr[0], subarr[1]);
                }
                break;
        }
        return map;
    }

    public static GeneralResponse parseToJsonObject(String API, String resBodyStr, String contentType){
        GeneralResponse responseBody = new GeneralResponse();
        // parsing response to a hashmap
        HashMap<String, String> responseMap = mapResponse(resBodyStr, contentType);
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
