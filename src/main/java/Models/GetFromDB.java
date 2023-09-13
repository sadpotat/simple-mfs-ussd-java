package Models;

import java.sql.*;

public class GetFromDB {
    private ResultSet rs;
    private final PreparedStatement getBalance;
    private final PreparedStatement getCustomerPS;
    private final PreparedStatement getLastSessionPS;
    private final PreparedStatement getLastNthInputPS;
    private final PreparedStatement verifyPINPS;
    private final PreparedStatement getProviderIDPS;
    private final PreparedStatement getPageEntries;
    private final PreparedStatement countNumOfCustomersOfType;
    private final PreparedStatement getNthCustomerOfType;

    public GetFromDB(Connection conn) throws SQLException {

        // query strings
        String getBalanceQuery = "select * from balance where cus_id=?";
        String getCustomerQuery = "select * from customers where cus_id=?";
        String getLastSessionQuery = "select * from session_data where sim=? order by last_update DESC";
        String getLastNthInputQuery = "select uinput from session_log where session_id=? and not uinput='n' and not uinput='p' order by last_update desc OFFSET ? ROWS FETCH NEXT 1 ROWS ONLY";
        String verifyPINQuery = "select * from passwords where cus_id=? and password=?";
        String getProviderIDQuery = "select cus_id from provider where menu=?";
        String getPageEntriesQuery = "select name from customers where type=? order by cus_id offset ?*? rows fetch next ? rows only";
        String countNumOfCustomersOfTypeQuery = "select count(cus_id) total from customers where type=?";
        String getNthCustomerOfTypeQuery = "select cus_id from customers where type=? order by cus_id offset ?-1 rows fetch next 1 rows only";

        // initialising prepared statements
        getBalance = conn.prepareStatement(getBalanceQuery);
        getCustomerPS = conn.prepareStatement(getCustomerQuery);
        getLastSessionPS = conn.prepareStatement(getLastSessionQuery);
        getLastNthInputPS = conn.prepareStatement(getLastNthInputQuery);
        verifyPINPS = conn.prepareStatement(verifyPINQuery);
        getProviderIDPS = conn.prepareStatement(getProviderIDQuery);
        getPageEntries = conn.prepareStatement(getPageEntriesQuery);
        countNumOfCustomersOfType = conn.prepareStatement(countNumOfCustomersOfTypeQuery);
        getNthCustomerOfType = conn.prepareStatement(getNthCustomerOfTypeQuery);
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
            System.out.println("Could not execute PreparedStatements in getCustomer");
        }
        return null;
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
            session.setServiceID(rs.getString("service_id"));
            session.setCurrentPage(rs.getInt("on_page"));
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

    public int getLastNthInputFromLogInt(String sessionID, int n) throws SQLException {
        getLastNthInputPS.setString(1, sessionID);
        getLastNthInputPS.setInt(2, n-1);
        rs = getLastNthInputPS.executeQuery();
        rs.next();
        return rs.getInt("uinput");
    }
    public String getLastNthInputFromLogString(String sessionID, int n) throws SQLException {
        getLastNthInputPS.setString(1, sessionID);
        getLastNthInputPS.setInt(2, n-1);
        rs = getLastNthInputPS.executeQuery();
        rs.next();
        return rs.getString("uinput");
    }

    public int getProviderID(String menu) throws SQLException {
        getProviderIDPS.setString(1, menu);
        rs = getProviderIDPS.executeQuery();
        rs.next();
        return rs.getInt("cus_id");
    }

    public ResultSet getClients(String typeToPage, int currentPage, int entries) throws SQLException {
//        "select name from customers where type=? order by cus_id offset ?*? rows fetch next ? rows only"
        getPageEntries.setString(1, typeToPage);
        getPageEntries.setInt(2, currentPage);
        getPageEntries.setInt(3, entries);
        getPageEntries.setInt(4, entries);
        return getPageEntries.executeQuery();
    }

    public int getNumberOfCustomersOfType(String typeToPage) throws SQLException {
        countNumOfCustomersOfType.setString(1, typeToPage);
        rs = countNumOfCustomersOfType.executeQuery();
        rs.next();
        return rs.getInt("total");
    }

    public int getNthCustomerIdOfType(int n, String type) throws SQLException {
        getNthCustomerOfType.setString(1, type);
        getNthCustomerOfType.setInt(2, n);
        rs = getNthCustomerOfType.executeQuery();
        rs.next();
        return rs.getInt("cus_id");
    }
}
