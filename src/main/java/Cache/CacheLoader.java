package Cache;

import Controllers.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class CacheLoader {
    private final Statement statement;
    private ResultSet rs;
    private HashMap<String, MenuRoute> menuRoutes;
    private HashMap<String, TType> modes;
    private HashMap<String, String> menuResponses;
    private HashMap<String, String> serviceClassNames;
    private static CacheLoader cache;

    public CacheLoader() throws SQLException {
        statement = Database.getConnectionObject().createStatement();
        menuRoutes = createMenuRoutes();
        modes = createModes();
        menuResponses = createMenuResponses();
        serviceClassNames = createServiceClasses();
    }

    public static CacheLoader getInstance() {
        try{
            if (cache==null) {
                cache = new CacheLoader();
                System.out.println("cache files created");
            }
            return cache;
        } catch (SQLException e) {
            return null;
        }
    }

    private HashMap<String, MenuRoute> createMenuRoutes() throws SQLException {
        rs = statement.executeQuery("select * from menu_routes");
        HashMap<String, MenuRoute> map = new HashMap<>();
        MenuRoute menuRoute;

        while(rs.next()){
            menuRoute = new MenuRoute(rs.getString("account_type"),
                    rs.getString("next_response"),
                    rs.getString("regex_for_input"),
                    rs.getString("error_msg"),
                    rs.getString("service_id"));
            // the key is a "prevResponse userInput" string
            map.put(rs.getString("prev_response") + " " + rs.getString("uinput"),
                    menuRoute);
        }

        return map;
    }

    private HashMap<String, TType> createModes() throws SQLException {
        rs = statement.executeQuery("select * from modes");
        HashMap<String, TType> map = new HashMap<>();
        TType mode;

        while(rs.next()){
            mode = new TType(rs.getInt("option_no"),
                    rs.getString("option_name"),
                    rs.getString("s_type"),
                    rs.getString("r_type"),
                    rs.getDouble("charges"));

            map.put(rs.getString("service_id"), mode);
        }

        return map;
    }

    private HashMap<String, String> createMenuResponses() throws SQLException {
        rs = statement.executeQuery("select * from responses");
        HashMap<String, String> map = new HashMap<>();

        while(rs.next()){
            map.put(rs.getString("menu"),rs.getString("res_str"));
        }

        return map;
    }

    private HashMap<String, String> createServiceClasses() throws SQLException {
        rs = statement.executeQuery("select * from service_classes");
        HashMap<String, String> map = new HashMap<>();

        while(rs.next()){
            map.put(rs.getString("service_id"), rs.getString("class"));
        }

        return map;
    }

    public HashMap<String, MenuRoute> getMenuRoutes() {
        return menuRoutes;
    }

    public void setMenuRoutes(HashMap<String, MenuRoute> menuRoutes) {
        this.menuRoutes = menuRoutes;
    }

    public HashMap<String, TType> getModes() {
        return modes;
    }

    public void setModes(HashMap<String, TType> modes) {
        this.modes = modes;
    }

    public HashMap<String, String> getMenuResponses() {
        return menuResponses;
    }

    public void setMenuResponses(HashMap<String, String> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public HashMap<String, String> getServiceClassNames() {
        return serviceClassNames;
    }

    public void setServiceClassNames(HashMap<String, String> serviceClassNames) {
        this.serviceClassNames = serviceClassNames;
    }
}
