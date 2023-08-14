package Controllers;

import Models.GetFromDB;
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

    public static boolean updateLastResponse(String sessionID, String value) {
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.enterIntoSessionString(sessionID, "last_resp", value);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to update last response");
            Database.rollbackChanges();
            return false;
        }
    }

    public static boolean updateLastInput(String sessionId, String input) {
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.enterIntoSessionString(sessionId, "last_input", input);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to update last_response");
            Database.rollbackChanges();
            return false;


        }
    }

    public static void processRequest(HttpServletResponse resp, PrintWriter out, int initiator, String sessionID, String serviceID) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        GetFromDB getter = Database.getGetter();
        String className = getter.getServiceClassFromID(serviceID);

        Class<?> clazz = Class.forName(className);

        // getting class constructor that takes specific argument types
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class);

        // creating an instance using the constructor and provided arguments
        Object[] arguments = {sessionID, initiator};
        Service service = (Service) constructor.newInstance(arguments);

        // running the service
        service.initialiseFromLog();
        if (service.isAllowed(out)){
            service.execute();
            service.sendSuccessMessage(resp, out);
        }

    }

    public static boolean updateServiceID(String sessionID, String serviceID) {
        InsertIntoDB insert = Database.getInsert();
        try {
            insert.enterIntoSessionString(sessionID, "service_id", serviceID);
            Database.commitChanges();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to update serviceID");
            Database.rollbackChanges();
            return false;
        }
    }
}
