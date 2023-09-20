package Models;

import java.sql.*;

public class InsertIntoDB {
    private final PreparedStatement createSessionPS;
    private final PreparedStatement insertIntoLogPS;
    private final PreparedStatement updatePINPS;
    private final PreparedStatement updateServiceIDPS;
    private final PreparedStatement updateReceiverinTLogPS;
    private final PreparedStatement updateLastInputAndResponsePS;
    private final PreparedStatement updateCurrentPagePS;
    private final PreparedStatement createTemporaryAccPS;
    private final PreparedStatement addPinToAccPS;
    private final PreparedStatement addToBalanceTablePS;

    public InsertIntoDB(Connection conn) throws SQLException {
        String createSessionQuery = "insert into session_data(session_id, sim) values(?, ?)";
        String insertIntoLogQuery = "insert into session_log(session_id, uinput, LAST_UPDATE) values (?, ?, CURRENT_TIMESTAMP)";
        String updatePINQuery = "update passwords set password=? where cus_id=? and password=?";
        String updateServiceIDQuery = "update session_data set service_id=? where session_id=?";
        String updateReceiverInTLogQuery = "update transactions set receiver_id=? where session_id=?";
        String updateLastInputAndResponseQuery = "update session_data set last_input=?, last_resp=?, last_update=CURRENT_TIMESTAMP where session_id=?";
        String updateCurrentPageQuery = "update session_data set on_page=on_page+? where session_id=?";
        String createTemporaryAccountQuery = "insert into customers(cus_id, name, status, type) values(?, ?, 'ACTIVE', 'PERSONAL')";
        String addPinToAccQuery = "insert into passwords(cus_id, password) values(?, ?)";
        String addToBalanceQuery = "insert into balance(cus_id) values(?)";

        createSessionPS = conn.prepareStatement(createSessionQuery);
        insertIntoLogPS = conn.prepareStatement(insertIntoLogQuery);
        updatePINPS = conn.prepareStatement(updatePINQuery);
        updateServiceIDPS = conn.prepareStatement(updateServiceIDQuery);
        updateReceiverinTLogPS = conn.prepareStatement(updateReceiverInTLogQuery);
        updateLastInputAndResponsePS = conn.prepareStatement(updateLastInputAndResponseQuery);
        updateCurrentPagePS = conn.prepareStatement(updateCurrentPageQuery);
        createTemporaryAccPS = conn.prepareStatement(createTemporaryAccountQuery);
        addPinToAccPS = conn.prepareStatement(addPinToAccQuery);
        addToBalanceTablePS = conn.prepareStatement(addToBalanceQuery);
    }

    public void createSessionEntry(String sessionID, int number) throws SQLException {
        createSessionPS.setString(1, sessionID);
        createSessionPS.setInt(2, number);
        createSessionPS.executeUpdate();
    }

    public void updateServiceID(String sessionID, String serviceId) throws SQLException {
        updateServiceIDPS.setString(1, serviceId);
        updateServiceIDPS.setString(2, sessionID);
        updateServiceIDPS.executeUpdate();
    }

    public void updateLastInputAndResponse(String sessionId, String input, String lastResponse) throws SQLException {
        updateLastInputAndResponsePS.setString(1, input);
        updateLastInputAndResponsePS.setString(2, lastResponse);
        updateLastInputAndResponsePS.setString(3, sessionId);
        updateLastInputAndResponsePS.executeUpdate();
    }

    public void insertIntoLog(String sessionID, String input) throws SQLException {
        insertIntoLogPS.setString(1, sessionID);
        insertIntoLogPS.setString(2, input);
        insertIntoLogPS.executeUpdate();
    }

    public void updatePIN(int initiator, int oldPIN, int newPIN) throws SQLException {
        updatePINPS.setInt(1, newPIN);
        updatePINPS.setInt(2, initiator);
        updatePINPS.setInt(3, oldPIN);
        updatePINPS.executeUpdate();
    }

    public void updateCurrentPage(String sessionID, int offset) throws SQLException {
        updateCurrentPagePS.setInt(1, offset);
        updateCurrentPagePS.setString(2, sessionID);
        updateCurrentPagePS.executeUpdate();
    }

    public void createTemporaryAccount(int initiator, String name) throws SQLException {
        createTemporaryAccPS.setInt(1, initiator);
        createTemporaryAccPS.setString(2, name);
        createTemporaryAccPS.executeUpdate();
    }

    public void addPinToAccount(int sender, int pin) throws SQLException {
        addPinToAccPS.setInt(1,sender);
        addPinToAccPS.setInt(2,pin);
        addPinToAccPS.executeUpdate();
    }

    public void addAccToBalanceTable(int sender) throws SQLException {
        addToBalanceTablePS.setInt(1, sender);
        addToBalanceTablePS.executeUpdate();
    }
}