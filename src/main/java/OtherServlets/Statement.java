package OtherServlets;

import Controllers.*;
import Models.GetFromDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;

public class Statement extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("html/text");
        PrintWriter out = resp.getWriter();

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

        // getting previous transactions
        try {
            ResultSet rs = TransactionController.getPrevTransactions(initiator);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            for(int i=0; i<10; i++){
                if(rs.next())
                    out.println(rs.getTimestamp("time").toLocalDateTime().format(formatter) +
                        ": " + rs.getString("type") +
                        " - " + rs.getString("receiver_id") +
                        " - " + rs.getString("amount"));
                else
                    if (i==0)
                        out.println("No transaction history found");
                    else
                        break;
            }
            out.close();
        } catch (Exception e) {
            System.out.println(e);
            Responses.internalServerError(resp, out);
        }
    }
}
