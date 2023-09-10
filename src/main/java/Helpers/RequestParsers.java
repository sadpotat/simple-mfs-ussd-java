package Helpers;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RequestParsers {

    public static int getQueryInt(HttpServletRequest req, String param){
        Map<String, String> params = Utils.queryToMap(req.getQueryString());
        try {
            String data = params.get(param);
            return Integer.parseInt(data);
        } catch (Exception e){
            System.out.println("could not find " + param + " in request query");
            return -1;
        }
    }

    public static String getQueryString(HttpServletRequest req, String param) {
        Map<String, String> params = Utils.queryToMap(req.getQueryString());
        try {
            return params.get(param).toLowerCase();
        } catch (Exception e){
            System.out.println("could not find " + param + " in request query");
            return "";
        }
    }
}
