package ManagerServlets;

import Controllers.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        // getting sender phone number
        int number = RequestParsers.getQueryInt(req, "initiator");

        // create database entry for the current session
        if (!SessionController.createSession(number)){
            Responses.internalServerError(resp, out);
            return;
        }

        // forwarding the request to the router
        RequestDispatcher rd = req.getRequestDispatcher("/menu");
        rd.forward(req, resp);
    }
}
