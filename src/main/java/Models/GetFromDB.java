package Models;

import java.sql.*;

public class GetFromDB {
    private ResultSet rs;
    private final PreparedStatement getBalance;
    private final PreparedStatement getCustomerPS;
    private final PreparedStatement getTModeObj;
    private final PreparedStatement getLastSessionPS;
    private final PreparedStatement getLastNthInputPS;
    private final PreparedStatement verifyPINPS;
    private final PreparedStatement getNextMenuPS;
    private final PreparedStatement getRegexPS;
    private final PreparedStatement getResponseStringPS;
    private final PreparedStatement getProviderNamePS;
    private final PreparedStatement getProviderIDPS;

    public GetFromDB(Connection conn) throws SQLException {

        // query strings
        String getBalanceQuery = "select * from balance where cus_id=?";
        String getCustomerQuery = "select * from customers where cus_id=?";
        String getTModeObjQuery = "select * from modes where option_no=?";
        String getLastSessionQuery = "select * from session_data where sim=? order by last_update DESC";
        String getLastNthInputQuery = "select uinput from session_log where session_id=? order by last_update desc OFFSET ? ROWS FETCH NEXT 1 ROWS ONLY";
        String verifyPINQuery = "select * from passwords where cus_id=? and password=?";
        String getNextMenuQuery = "select next_response from menu_routes where account_type=? and prev_response=? and uinput=?";
        String getRegexQuery = "select regex_for_input, error_msg from menu_routes where prev_response=? fetch first 1 rows only";
        String getResponseStringQuery = "select res_str, type from responses where menu=?";
        String getProviderNameQuery = "select name from provider where menu=?";
        String getProviderIDQuery = "select cus_id from customers where name=?";

        // initialising prepared statements
        getBalance = conn.prepareStatement(getBalanceQuery);
        getCustomerPS = conn.prepareStatement(getCustomerQuery);
        getTModeObj = conn.prepareStatement(getTModeObjQuery);
        getLastSessionPS = conn.prepareStatement(getLastSessionQuery);
        getLastNthInputPS = conn.prepareStatement(getLastNthInputQuery);
        verifyPINPS = conn.prepareStatement(verifyPINQuery);
        getNextMenuPS = conn.prepareStatement(getNextMenuQuery);
        getRegexPS = conn.prepareStatement(getRegexQuery);
        getResponseStringPS = conn.prepareStatement(getResponseStringQuery);
        getProviderNamePS = conn.prepareStatement(getProviderNameQuery);
        getProviderIDPS = conn.prepareStatement(getProviderIDQuery);
    }

    public double getBalance(int user) {
        double balance;
        try{
            getBalance.setInt(1, user);
            rs = getBalance.executeQuery();
            rs.next();
            balance = rs.getDouble("balance");
        } catch (SQLException e){
            return -1;
        }
        return balance;
    }

    public Customer getCustomer(int ID) {
        Customer customer = new Customer();
        try {
            getCustomerPS.setInt(1, ID);
            rs = getCustomerPS.executeQuery();
            if (rs.next()) {
                try {
                    customer.setCus_id(rs.getInt("cus_id"));
                    customer.setName(rs.getString("name"));
                    customer.setType(rs.getString("type"));
                    customer.setStatus(rs.getString("status"));
                    customer.setBalance(getBalance(rs.getInt("cus_id")));
                    return customer;
                } catch (Exception e) {
                    System.out.println("failed to get customer");
                }
            }
        } catch (SQLException s){
            System.out.println(s);
            System.out.println("Could not execute PreparedStatements in getCustomer");
        }
        return null;
    }

    public TType getTTypeObjectFromDB(int transMode){
        try {
            getTModeObj.setInt(1, transMode);
            rs = getTModeObj.executeQuery();
            TType type = new TType();
            rs.next();
            type.setOptionNum(rs.getInt("option_no"));
            type.setOptionName(rs.getString("option_name"));
            type.setS_type(rs.getString("s_type"));
            type.setR_type(rs.getString("r_type"));
            type.setCharges(rs.getDouble("charges"));
            return type;
        } catch (Exception e){
            return null;
        }
    }

    public Session getLastSession(int initiator) {
        Session session = new Session();
        try {
            getLastSessionPS.setInt(1, initiator);
            rs = getLastSessionPS.executeQuery();
            rs.next();
            session.setSession_id(rs.getString("session_id"));
            session.setSim(rs.getInt("sim"));
            session.setLast_input(rs.getString("last_input"));
            session.setLast_response(rs.getString("last_resp"));
            session.setLast_update(rs.getTimestamp("last_update").toLocalDateTime());
        } catch (SQLException e) {
            System.out.println("Could not get Session object");
            session = null;
        }
        return session;
    }

    public boolean accountPinExists(int initiator, int hash) {
        try {
            verifyPINPS.setInt(1, initiator);
            verifyPINPS.setInt(2, hash);
            rs = verifyPINPS.executeQuery();
            return rs.next();
        } catch (SQLException s){
            System.out.println("failed to verify PIN");
        }
        return false;
    }

    public String getLastNthInputFromLog(String sessionID, int n) throws SQLException {
        getLastNthInputPS.setString(1, sessionID);
        getLastNthInputPS.setInt(2, n-1);
        rs = getLastNthInputPS.executeQuery();
        rs.next();
        return rs.getString("uinput");
    }

    //method overloading
    public String getNextResponseMenu(String accountType, String prevMenu, String input) {
        try{
            getNextMenuPS.setString(1, accountType);
            getNextMenuPS.setString(2, prevMenu);
            getNextMenuPS.setString(3, input);
            rs = getNextMenuPS.executeQuery();
            rs.next();
            return rs.getString("next_response");
        } catch (SQLException e) {
            return "";
        }
    }

    public Regex getRegexString(String lastResponse) {
        Regex regexObj = new Regex();
        try {
            getRegexPS.setString(1, lastResponse);
            rs = getRegexPS.executeQuery();
            rs.next();
            regexObj.setRegex(rs.getString("regex_for_input"));
            regexObj.setError_msg(rs.getString("error_msg"));
        } catch (SQLException e) {
            System.out.println("Could not fetch regex");
        }
        return regexObj;
    }

    public Response getResponse(String menuName) {
        Response response = new Response();
        try {
            getResponseStringPS.setString(1, menuName);
            rs = getResponseStringPS.executeQuery();
            rs.next();
            response.setRes_str(rs.getString("res_str"));
            response.setType(rs.getString("type"));
            return response;
        } catch (SQLException e){
            System.out.println("failed to fetch response from db");
            return null;
        }
    }

    public String getProviderName(String provider) throws SQLException {
        getProviderNamePS.setString(1, provider);
        rs = getProviderNamePS.executeQuery();
        rs.next();
        return rs.getString("name");
    }

    public int getProviderID(String name) throws SQLException {
        getProviderIDPS.setString(1, name);
        rs = getProviderIDPS.executeQuery();
        rs.next();
        return rs.getInt("cus_id");
    }
}
