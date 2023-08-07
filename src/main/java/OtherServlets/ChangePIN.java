package OtherServlets;

import Controllers.*;
import Models.InsertIntoDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ChangePIN extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("html/text");
        PrintWriter out = resp.getWriter();

        // getting values from query parameters
        int initiator = RequestParsers.getQueryInt(req, "initiator");

        // getting session data
        String sessionID = SessionController.getSessionID(initiator);

        // getting transaction data from the user's previous inputs
        int confirmPIN = Integer.parseInt(LogController.getLastNthInput(sessionID,1));
        int newPIN = Integer.parseInt(LogController.getLastNthInput(sessionID,2));
        int oldPIN = Integer.parseInt(LogController.getLastNthInput(sessionID,3));

        // verifying PIN
        if (!TransactionController.verifyPIN(initiator, oldPIN)) {
            out.println("Wrong PIN");
            out.close();
            return;
        }

        // checking if the two PINs match
        if (newPIN != confirmPIN){
            out.println("The PINs do not match");
            out.close();
            return;
        }

        // updating pin
        InsertIntoDB insert = InsertIntoDB.getInsert();
        try{
            insert.updatePIN(initiator, oldPIN, newPIN);
            out.println("Your PIN has been changed");
            out.close();
        } catch (Exception e){
            System.out.println("could not update PIN");
            Responses.internalServerError(resp, out);
        }
    }
}
