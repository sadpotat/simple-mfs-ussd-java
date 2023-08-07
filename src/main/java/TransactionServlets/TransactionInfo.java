package TransactionServlets;

import Controllers.LogController;
import Controllers.RequestParsers;
import Controllers.SessionController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TransactionInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        // getting session id
        int initiator = RequestParsers.getQueryInt(req, "initiator");
        String sessionID = SessionController.getSessionID(initiator);

        String reference = LogController.getLastNthInput(sessionID,1);
        int amount = Integer.parseInt(LogController.getLastNthInput(sessionID,2));
        int receiver = Integer.parseInt(LogController.getLastNthInput(sessionID,3));

        // displaying response
        out.println("Receiver: " + receiver +
                "\nAmount: " + amount +
                "\nReference: " + reference +
                "\nEnter PIN: ");
        out.close();
    }
}
