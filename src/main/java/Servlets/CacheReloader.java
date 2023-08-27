package Servlets;

import Cache.CacheLoader;
import Controllers.Responses;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class CacheReloader extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setStatus(200);
        PrintWriter out = resp.getWriter();
        try {
            CacheLoader.cacheReload();
            out.println("Reloaded cache files");
            out.close();
        } catch (SQLException e) {
            Responses.internalServerError(resp, out);
        }
    }
}
