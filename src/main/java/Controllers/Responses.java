package Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class Responses {

    public static void internalServerError(HttpServletResponse resp, PrintWriter out){
        resp.setStatus(500);
        out.println("Internal Server Error");
        out.close();
    }

    public static void sendResponse(String resStr, PrintWriter out) {
        String[] responses = resStr.split("__");
        for (String line: responses)
            out.println(line);
        out.close();
    }

//    public static void sendJSON(HttpServletResponse resp, PrintWriter out, Object obj) {
//        resp.setStatus(200);
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(obj);
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        out.print(jsonStr);
//        out.close();
//    }
//    public static void sendXML(HttpServletResponse resp, PrintWriter out, Object obj) {
//        resp.setStatus(200);
//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            String xmlStr = xmlMapper.writeValueAsString(obj);
//
//            resp.setContentType("text/xml");
//            resp.setCharacterEncoding("UTF-8");
//            out.print(xmlStr);
//            out.close();
//
//        } catch (JsonProcessingException e) {
//            internalServerError(resp, out);
//        }
//    }
}
