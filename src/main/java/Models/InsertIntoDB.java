package Models;

import java.sql.*;

public class InsertIntoDB {
    private final Statement statement;
    private final PreparedStatement createSessionPS;
    private final PreparedStatement insertIntoLogPS;
    private final PreparedStatement updatePINPS;
    private final PreparedStatement updateReceiverinTLogPS;

    public InsertIntoDB(Connection conn) throws SQLException {
        statement = conn.createStatement();

        String createSessionQuery = "insert into session_data(session_id, sim) values(?, ?)";
        String insertIntoLogQuery = "insert into session_log(session_id, uinput, LAST_UPDATE) values (?, ?, CURRENT_TIMESTAMP)";
        String updatePINQuery = "update passwords set password=? where cus_id=? and password=?";
        String updateReceiverInTLogQuery = "update transactions set receiver_id=? where session_id=?";

        createSessionPS = conn.prepareStatement(createSessionQuery);
        insertIntoLogPS = conn.prepareStatement(insertIntoLogQuery);
        updatePINPS = conn.prepareStatement(updatePINQuery);
        updateReceiverinTLogPS = conn.prepareStatement(updateReceiverInTLogQuery);
    }

    public void createSessionEntry(String sessionID, int number) throws SQLException {
        createSessionPS.setString(1, sessionID);
        createSessionPS.setInt(2, number);
        createSessionPS.executeUpdate();
    }

    public void enterIntoSessionString(String sessionId, String column, String value) throws SQLException {
        String queryString = "update session_data set " + column + "='" + value + "', last_update=CURRENT_TIMESTAMP where session_id='" + sessionId + "'";
        statement.executeUpdate(queryString);
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

    public void updateReceiverInTLog(String sessionID, int receiver) throws SQLException {
        updateReceiverinTLogPS.setInt(1, receiver);
        updateReceiverinTLogPS.setString(2, sessionID);
        updateReceiverinTLogPS.executeUpdate();
    }
}