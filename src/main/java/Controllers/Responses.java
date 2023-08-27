package Controllers;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class Responses {

    public static void internalServerError(HttpServletResponse resp, PrintWriter out){
        resp.setStatus(500);
        resp.setContentType("text/plain");
        out.println("Internal Server Error");
        out.close();
    }

    public static void sendResponse(String resStr, PrintWriter out) {
        String[] responses = resStr.split("__");
        for (String line: responses)
            out.println(line);
        out.close();
    }
}
