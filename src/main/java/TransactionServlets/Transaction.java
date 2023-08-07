package TransactionServlets;

import Controllers.*;
import Models.GetFromDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Transaction extends HttpServlet {
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
        int amount = Integer.parseInt(LogController.getLastNthInput(sessionID,3));
        int receiver = Integer.parseInt(LogController.getLastNthInput(sessionID,4));
        int type = Integer.parseInt(LogController.getLastNthInput(sessionID,5));

        // Creating the transaction object
        Models.Transaction transaction = new Models.Transaction();

        try {

            // getting sender and receiver data from db
            transaction.setSender(initiator);
            transaction.setSenderObj(getter.getCustomer(initiator));
            transaction.setReceiver(receiver);
            transaction.setReceiverObj(getter.getCustomer(receiver));
            transaction.setSessionID(sessionID);

            // verifying if the user can make transactions
            if (!transaction.getReceiverObj().getStatus().equals("ACTIVE")){
                out.println("Cannot make transactions, your account is " + transaction.getSenderObj().getStatus());
                out.close();
                return;
            }

            // verifying PIN
            if (!TransactionController.verifyPIN(initiator, hash)) {
                out.println("Wrong PIN");
                out.close();
                return;
            }

            // setting transaction information
            transaction.setType(getter.getTTypeObjectFromDB(type));
            transaction.setSessionID(sessionID);

            // adding charges
            transaction.setAmount(amount);
            transaction.addCharges();

            // verifying that the recipient exists
            if (transaction.getReceiverObj() == null) {
                out.println("Recipient is not a registered account");
                out.close();
                return;
            }

            // checking if a transaction would be allowed
            if (!transaction.accountTypesOkay()) {
                out.println("Transaction not allowed");
                out.close();
                return;
            }

            // checking if the amount can be transacted
            if (!transaction.amountIsInBalance()){
                throw new RuntimeException("Insufficient balance");
            }

            // transacting
            transaction.execute();

            // Committing changes
            Database.commitChanges();

            out.println("Transaction Successful");
            out.close();

        } catch (Exception e) {
            // rolling back changes
            Database.rollbackChanges();

            out.println("Transaction not allowed");
            out.close();
        }
    }
}
