package Helpers;

import Middleware.MobileRecharge.Request.ReqBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utils {

    public static Map<String, String> queryToMap(String query) {
        // copied and pasted code, will look into it later
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public static String createSessionID(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int bound = characters.length();
        int length = 12;

        Random rand = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i=0; i<length; i++){
            builder.append(characters.charAt(rand.nextInt(bound)));
        }
        return builder.toString();
    }



    public static String convertToFormattedString(ReqBody reqBody, String content) throws JsonProcessingException {
        //content should be either "json" or "xml"
        String body;
        if (content.contains("json")){
            Gson gson = new Gson();
            body = gson.toJson(reqBody);
        } else {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            try {
                body = xmlMapper.writeValueAsString(reqBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return body;
    }
}
