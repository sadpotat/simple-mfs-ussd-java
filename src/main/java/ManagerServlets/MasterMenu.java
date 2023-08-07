package ManagerServlets;

import Controllers.*;
import Models.Customer;
import Models.GetFromDB;
import Models.Response;
import Models.Session;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MasterMenu extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html");
        resp.setStatus(200);
        PrintWriter out = resp.getWriter();

        // getting input
        int initiator = RequestParsers.getQueryInt(req, "initiator");
        String input = RequestParsers.getQueryString(req, "input");

        // getting session data
        GetFromDB getter = GetFromDB.getGetter();
        Session session = getter.getLastSession(initiator);

        // verifying input
        String regex = getter.getRegexString(session.getLast_response());

        if(regex.equals("")){
            Responses.internalServerError(resp, out);
            return;
        }

        if(!input.matches(regex)){
            // handle routing when regex does not match
            out.println("Not a valid input, please try again");
            Response response = GetFromDB.getGetter().getResponse(session.getLast_response());
            if (response.getType().equals("static")){
                Responses.sendResponse(response.getRes_str(), out);
            }
            // forwarding to servlet if the response is not a static type
            RequestDispatcher rd = req.getRequestDispatcher(response.getRes_str());
            rd.include(req, resp);
            return;
        }

        if (regex.equals("\\d\\d\\d\\d")){
            int hash = input.hashCode();
            input = Integer.toString(hash);
        }

        // getting initiator account info
        Customer user = getter.getCustomer(initiator);

        // getting the next response menu name
        String prevResponse = session.getLast_response();
        String nextResponse = SessionController.getNextMenu(user.getType(), prevResponse, input);
        if(nextResponse.equals(""))
            nextResponse = SessionController.getNextMenu(user.getType(), prevResponse, "-1");

        // handling errors
        if (nextResponse.equals("")){
            Responses.internalServerError(resp, out);
            return;
        }

        // updating last response
        if(!SessionController.updateLastResponse(session.getSession_id(), nextResponse)){
            Responses.internalServerError(resp, out);
            return;
        }

        // updating last input in session data
        if(!SessionController.updateLastInput(session.getSession_id(), input)){
            Responses.internalServerError(resp, out);
            return;
        }

        // logging input to db
        if(!input.equals("") && !LogController.addToLog(session.getSession_id(), input)){
            Responses.internalServerError(resp, out);
            return;
        }

        // sending next menu
        try {
            Response response = GetFromDB.getGetter().getResponse(nextResponse);
            if (response.getType().equals("static")){
                Responses.sendResponse(response.getRes_str(), out);
            }
            // when type is 'forward', forward request
            RequestDispatcher rd = req.getRequestDispatcher(response.getRes_str());
            rd.forward(req,resp);
        } catch (Exception e){
            Responses.internalServerError(resp, out);
        }
    }
}
