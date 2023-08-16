package Servlets;

import Cache.CacheLoader;
import Controllers.*;
import Models.*;

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
        CacheLoader cache = CacheLoader.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Database db = Database.getDb();
        resp.setContentType("html/text");
        PrintWriter out = resp.getWriter();

        CacheLoader cache = CacheLoader.getInstance();

        if (db==null || cache==null){
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
        // getting input regex from cache
        Regex regexObj = cache.getRegexObj(prevResponse);
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
            String response = cache.getResponse(prevResponse);
            Responses.sendResponse(response, out);
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
        NextMenuAndID nextMenuAndID = cache.getNextMenu(prevResponse, accType, input);

        if (nextMenuAndID ==null){
            // handling errors
            Responses.internalServerError(resp, out);
            return;
        }

        String nextMenu = nextMenuAndID.getMenuNo();
        String serviceID = nextMenuAndID.getServiceID();

//        // updating last response
//        if(!SessionController.updateLastResponse(sessionID, nextMenu)){
//            // handling errors
//            Responses.internalServerError(resp, out);
//            return;
//        }
//
//        // updating last input in session data
//        if(!SessionController.updateLastInput(sessionID, input)){
//            // handling errors
//            Responses.internalServerError(resp, out);
//            return;
//        }

        // updating last input and last response
        if(!SessionController.updateInputAndResponse(sessionID, input, nextMenu)){
            // handling errors
            Responses.internalServerError(resp, out);
            return;
        }

        // updating serviceID if a service was chosen
        if(!serviceID.equals("none") && !SessionController.updateServiceID(sessionID, serviceID)){
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
            String response = cache.getResponse(nextMenu);
            if (!response.startsWith("/")){
                Responses.sendResponse(response, out);
                return;
            }

            // responses starting with '/' indicate that a service will run
            serviceID = serviceID.equals("none") ? session.getServiceID(): serviceID;
            SessionController.processRequest(resp, out, initiator, sessionID, serviceID);

        } catch (Exception e){
            Database.rollbackChanges();
            Responses.internalServerError(resp, out);
        }
    }

}
