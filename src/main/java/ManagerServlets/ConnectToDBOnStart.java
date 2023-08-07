package ManagerServlets;

import Controllers.Database;
import Models.AuthHeader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ConnectToDBOnStart extends HttpServlet {
    @Override
    public void init() throws ServletException {
        AuthHeader auth = new AuthHeader("ussd", "ussd12345", "sadiadb3");
        Database.connectToDatabase(auth);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        if (Database.getDb() == null ){
            resp.setStatus(500);
            out.println("Database not connected");
            out.close();
            return;
    }
            resp.setStatus(200);
            out.println("Database connected");
            out.close();
    }
}
