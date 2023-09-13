package Servlets;

import Cache.CacheLoader;
import Controllers.*;
import Helpers.RequestParsers;
import Helpers.Utils;
import Models.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

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
        InsertIntoDB insert = Database.getInsert();

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
        NextMenuAndID nextMenuAndID;
        String nextMenu;
        String serviceID;

        if (input.equals("n") || input.equals("p")){
            // the last menu is sent again
            nextMenu = prevResponse;
            serviceID = session.getServiceID();
        } else {
            nextMenuAndID = cache.getNextMenu(prevResponse, accType, input);

            if (nextMenuAndID ==null){
                // handling errors
                Responses.internalServerError(resp, out);
                return;
            }

            nextMenu = nextMenuAndID.getMenuNo();
            serviceID = nextMenuAndID.getServiceID();
        }

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
        String inputToLog = input;
        if(session.getCurrentPage() != -1 && !input.equals("n") && !input.equals("p")){
            // updates the page number when the user has moved on to the next menu
            // updates the option chosen to input = option*(1 + current page)
            String pageId = cache.getResponse(prevResponse);
            int entries = cache.getEntriesPerPage(pageId);
            inputToLog =  Integer.toString(Integer.parseInt(input) + session.getCurrentPage() * entries);
            // updating current page to -1
            try {
                insert.updateCurrentPage(sessionID, -(session.getCurrentPage()+1));
            } catch (SQLException e) {
                Responses.internalServerError(resp, out);
                return;
            }
        }
        if(!input.equals("") && !LogController.addToLog(sessionID, inputToLog)){
            // handling errors
            Responses.internalServerError(resp, out);
            return;
        }

        // sending next response
        try {
            String response = cache.getResponse(nextMenu);
            if (cache.isPaginated(nextMenu)) {
                // users to list
                // for menus that have to be paginated, the response field contains the paginating id
                String typeToPage = cache.getAccountsToPage(response);
                // entries per page
                int entries = cache.getEntriesPerPage(response);
                // current page
                int currentPage = session.getCurrentPage();
                // get max possible pages
                int totalEntries = getter.getNumberOfCustomersOfType(typeToPage);
                int maxPageNo =  totalEntries/entries;
                // update page no.
                if (input.equals("n") || currentPage==-1){
                    if(currentPage<maxPageNo){
                        insert.updateCurrentPage(sessionID, 1);
                        currentPage++;
                    }
                } else{
                    // input is "p"
                    if(currentPage==0){
                        out.println("Invalid input. Please try again.");
                    } else {
                        insert.updateCurrentPage(sessionID, -1);
                        currentPage--;
                    }
                }
                // generate the page
                String page = Utils.generatePage(typeToPage, currentPage, entries);
                if (page==null)
                    Responses.internalServerError(resp, out);
                else
                    Responses.sendResponse(page, out);
            } else if (!response.startsWith("/")){
                // static message
                Responses.sendResponse(response, out);
            } else {
                // responses starting with '/' indicate that a service will run
                serviceID = serviceID.equals("none") ? session.getServiceID(): serviceID;
                SessionController.processRequest(resp, out, initiator, sessionID, serviceID);
            }
        } catch (Exception e){
            Responses.internalServerError(resp, out);
        }
    }

}
