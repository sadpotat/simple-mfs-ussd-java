package ManagerServlets;

import Controllers.Database;
import Controllers.RequestParsers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SessionManager extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database db = Database.getDb();
        resp.setContentType("html/text");
        PrintWriter out = resp.getWriter();

        if (db==null){
            resp.setStatus(500);
            out.println("Internal Server Error");
            out.close();
            return;
        }

        // getting initiator
        String input = RequestParsers.getQueryString(req, "input");

        if (input.equals("")){
            // indicates login
            RequestDispatcher rd = req.getRequestDispatcher("/login");
            rd.forward(req, resp);
            return;
        }

        // forwarding request to the Router for the menu no.
        RequestDispatcher rd = req.getRequestDispatcher("menu");
        rd.forward(req, resp);
    }
}
