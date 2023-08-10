package Controllers;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class Responses {

    public static void internalServerError(HttpServletResponse resp, PrintWriter out){
        resp.setStatus(500);
        out.println("Internal Server Error");
        out.close();
    }

    public static void sessionTimedOut(HttpServletResponse resp, PrintWriter out) {
        resp.setStatus(403);
        out.println("Session Timed Out");
        out.close();
    }

    public static void sendResponse(String resStr, PrintWriter out) {
        String[] responses = resStr.split("__");
        for (String line: responses)
            out.println(line);
        out.close();
    }
}
