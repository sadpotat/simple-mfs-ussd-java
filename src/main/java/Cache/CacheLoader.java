package Cache;

import Controllers.Database;
import Models.NextMenuAndID;
import Models.Regex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class CacheLoader {
    private final Statement statement;
    private ResultSet rs;
    private HashMap<String, MenuRoute> menuRoutes;
    private HashMap<String, Regex> menuRegex;
    private HashMap<String, TType> modes;
    private HashMap<String, String> menuResponses;
    private HashMap<String, String> serviceClassNames;
    private HashMap<String, HashMap<String, String>> setterNames;
    private HashMap<String, Provider> providerObjects;
    private HashMap<String, Status> statusOkayMessages;
    private HashMap<String, String> APIURLS;
    private static CacheLoader cache;

    public CacheLoader() throws SQLException {
        statement = Database.getConnectionObject().createStatement();
        menuRoutes = createMenuRoutes();
        modes = createModes();
        menuResponses = createMenuResponses();
        serviceClassNames = createServiceClasses();
        providerObjects = createProviderObjects();
        menuRegex = createMenuRegex();
        statusOkayMessages = createStatusOkayMessages();
        setterNames = createSetterNames();
        APIURLS = createAPIURLS();
    }

    private HashMap<String, String> createAPIURLS() throws SQLException {
        rs = statement.executeQuery("select * from integrated_apis");
        HashMap<String, String> map = new HashMap<>();
        while (rs.next()){
            map.put(rs.getString("id"), rs.getString("url"));
        }
        return map;
    }

    public String getUrlFromApiId(String id){
        return APIURLS.get(id);
    }

    private HashMap<String, HashMap<String, String>> createSetterNames() throws SQLException {
        ResultSet api = statement.executeQuery("select distinct api from API_RESPONSE_VALUE_SETTERS");
        // list of APIs in the table
        ArrayList<String> apiList = new ArrayList<>();
        while(api.next()){
            apiList.add(api.getString("api"));
        }

        HashMap<String, HashMap<String, String>> map = new HashMap<>();
        for(String API: apiList){
            rs = statement.executeQuery("select key, setter from API_RESPONSE_VALUE_SETTERS where api='" + API + "'");
            HashMap<String, String> keyVals = new HashMap<>();
            while(rs.next()){
                keyVals.put(API+rs.getString("key"), rs.getString("setter"));
            }
            map.put(API, keyVals);
        }
        return map;
    }

    public HashMap<String, String> getSettersForAPI(String API){
        return setterNames.get(API);
    }

    private HashMap<String, Status> createStatusOkayMessages() throws SQLException {
        rs = statement.executeQuery("select * from API_STATUS_VALUES");
        HashMap<String, Status> map = new HashMap<>();
        Status keys;

        while(rs.next()){
            keys = new Status();
            keys.setStatus_ok(rs.getString("status_ok_if"));
            keys.setXmlRoot(rs.getString("xml_root"));
            map.put(rs.getString("api"), keys);
        }

        return map;
    }

    public static void cacheReload() throws SQLException {
        cache = new CacheLoader();
    }

    private HashMap<String, Regex> createMenuRegex() throws SQLException {
        rs = statement.executeQuery("select * from menu_regex");
        HashMap<String, Regex> map = new HashMap<>();
        Regex regexObj;

        while(rs.next()){
            regexObj = new Regex(rs.getString("regex"),
                    rs.getString("error_msg"));
            map.put(rs.getString("menu"), regexObj);
        }

        return map;
    }

    private HashMap<String, Provider> createProviderObjects() throws SQLException {
        rs = statement.executeQuery("select * from provider left join request_data on provider.api_id = request_data.api");
        HashMap<String, Provider> map = new HashMap<>();

        while(rs.next()){
            Provider provider = new Provider();
            provider.setApiId(rs.getString("API_ID"));
            provider.setReqType(rs.getString("req_type"));
            provider.setReqTemplate(rs.getString("req_template"));
            provider.setReqMethod(rs.getString("method"));
            map.put(rs.getString("menu"), provider);
        }
        return map;
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
            mode = new TType(rs.getString("option_name"),
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

    public Regex getRegexObj(String prevResponse){
        return menuRegex.get(prevResponse);
    }

    public void setServiceClassNames(HashMap<String, String> serviceClassNames) {
        this.serviceClassNames = serviceClassNames;
    }

    public String getResponse(String prevResponse) {
        return menuResponses.get(prevResponse);
    }

    public NextMenuAndID getNextMenu(String prevResponse, String accType, String input) {
        NextMenuAndID nextMenuAndID;
        MenuRoute route;
        route = menuRoutes.get(prevResponse + " " + input);
        if (route==null)
            route = menuRoutes.get(prevResponse + " -1");

        nextMenuAndID = new NextMenuAndID(route.getNextResponse(), route.getServiceID());
        return nextMenuAndID;
    }

    public String getServiceClassName(String serviceID) {
        return serviceClassNames.get(serviceID);
    }

    public TType getTType(String serviceID) {
        return modes.get(serviceID);
    }

    public Status getStatusObj(String API){
        return statusOkayMessages.get(API);
    }
    public Provider getProviderObj(String menu) {
        return providerObjects.get(menu);
    }

    public String getXmlResponseRoot(String api) {
        return statusOkayMessages.get(api).getXmlRoot();
    }

    public String getStatusOkayMsg(String apiId) {
        return statusOkayMessages.get(apiId).getStatus_ok();
    }
}
