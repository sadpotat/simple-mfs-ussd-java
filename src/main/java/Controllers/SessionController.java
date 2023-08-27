package Controllers;

import Cache.CacheLoader;
import Helpers.Utils;
import Models.InsertIntoDB;
import Services.*;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class SessionController {
    public static boolean createSession(int initiator) {
        String sessionID = Utils.createSessionID();
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.createSessionEntry(sessionID, initiator);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            Database.rollbackChanges();
            return false;
        }
    }

    public static void processRequest(HttpServletResponse resp, PrintWriter out, int initiator, String sessionID, String serviceID) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NullPointerException {
        CacheLoader cache = CacheLoader.getInstance();
        String className = cache.getServiceClassName(serviceID);
        Class<?> clazz = Class.forName(className);

        // getting class constructor that takes specific argument types
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
        // creating an instance using the constructor and provided arguments
        Object[] arguments = {sessionID, initiator};
        Service service = (Service) constructor.newInstance(arguments);

        try{
            // running the service
            service.initialiseFromLog();
            if (service.isAllowed(out)){
                service.execute();
                Database.commitChanges();
                service.sendSuccessMessage(resp, out);
            }
        } catch (Exception e) {
            Responses.internalServerError(resp, out);
        }
    }

    public static boolean updateServiceID(String sessionID, String serviceID) {
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.updateServiceID(sessionID, serviceID);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to update serviceID");
            Database.rollbackChanges();
            return false;
        }
    }

    public static boolean updateInputAndResponse(String sessionID, String input, String lastResponse) {
        InsertIntoDB insert = Database.getInsert();
        try{
            insert.updateLastInputAndResponse(sessionID, input, lastResponse);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to update last response and input");
            Database.rollbackChanges();
            return false;
        }
    }
}
