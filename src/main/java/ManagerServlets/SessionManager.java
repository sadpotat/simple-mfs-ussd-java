package ManagerServlets;

import Controllers.*;
import Models.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SessionManager extends HttpServlet {
    @Override
    public void init() {
        AuthHeader auth = new AuthHeader("ussd", "ussd12345", "sadiadb3");
        Database.connectToDatabase(auth);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database db = Database.getDb();
        resp.setContentType("html/text");
        PrintWriter out = resp.getWriter();

        if (db==null){
            // error handling for when the server fails to connect to the db on servlet initialization
            resp.setStatus(500);
            out.println("Internal Server Error");
            out.close();
            return;
        }

        // getting initiator
        int initiator = RequestParsers.getQueryInt(req, "initiator");
        String input = RequestParsers.getQueryString(req, "input");

        if (input.equals("")){
            // indicates login
            // create database entry for the current session
            if (!SessionController.createSession(initiator)){
                Responses.internalServerError(resp, out);
                return;
            }
        }

        // getting session data
        GetFromDB getter = Database.getGetter();
        Session session = getter.getLastSession(initiator);
        String prevResponse = session.getLast_response();
        String sessionID = session.getSession_id();

        // verifying input
        // getting input regex from db
        Regex regexObj = getter.getRegexString(prevResponse);
        String regex = regexObj.getRegex();
        String errorMsg = regexObj.getError_msg();

        // handling database errors
        if(regex.equals("")){
            Responses.internalServerError(resp, out);
            return;
        }

        if(!input.matches(regex)){
            // send the last response again when regex does not match
            out.println(errorMsg);
            Response response = getter.getResponse(prevResponse);
            if (response.getType().equals("static")){
                Responses.sendResponse(response.getRes_str(), out);
                return;
            }
            // ignoring response text for forward type responses
            return;
        }

        // hashing PINs for security
        if (regex.equals("\\d{4}")){
            int hash = input.hashCode();
            input = Integer.toString(hash);
        }

        // getting initiator account type, required to fetch menu numbers
        Customer user = getter.getCustomer(initiator);
        String accType = user.getType();

        // getting the next response menu name
        String nextResponse = MenuController.getNextMenu(prevResponse, accType, input);

        if (nextResponse.equals("")){
            // handling errors
            Responses.internalServerError(resp, out);
            return;
        }

        // updating last response
        if(!SessionController.updateLastResponse(sessionID, nextResponse)){
            // handling errors
            Responses.internalServerError(resp, out);
            return;
        }

        // updating last input in session data
        if(!SessionController.updateLastInput(sessionID, input)){
            // handling errors
            Responses.internalServerError(resp, out);
            return;
        }

        // logging input to db
        if(!input.equals("") && !LogController.addToLog(sessionID, input)){
            // handling errors
            Responses.internalServerError(resp, out);
            return;
        }

        // sending next response
        try {
            Response response = getter.getResponse(nextResponse);
            String resStr = response.getRes_str();
            if (response.getType().equals("static")) {
                Responses.sendResponse(resStr, out);
                return;
            }

            // when type is 'forward', process request
            SessionController.processRequest(resp, out, initiator, sessionID, resStr);

        } catch (Exception e){
            Database.rollbackChanges();
            Responses.internalServerError(resp, out);
        }
    }

}
