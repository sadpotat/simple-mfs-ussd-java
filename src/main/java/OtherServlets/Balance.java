package OtherServlets;

import Controllers.LogController;
import Controllers.RequestParsers;
import Controllers.SessionController;
import Controllers.TransactionController;
import Models.GetFromDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Balance extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("html/text");
        PrintWriter out = resp.getWriter();
        GetFromDB getter = GetFromDB.getGetter();

        // getting values from query parameters
        int initiator = RequestParsers.getQueryInt(req, "initiator");

        // getting session data
        String sessionID = SessionController.getSessionID(initiator);

        // getting transaction data from the user's previous inputs
        int hash = Integer.parseInt(LogController.getLastNthInput(sessionID,1));

        // verifying PIN
        if (!TransactionController.verifyPIN(initiator, hash)) {
            out.println("Wrong PIN");
            out.close();
        }

        double balance = getter.getBalance(initiator);
        out.println("Current account balance: " + balance);
        out.close();
    }
}
