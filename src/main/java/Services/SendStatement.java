package Services;

import Controllers.Database;
import Controllers.LogController;
import Controllers.Responses;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class SendStatement extends ServiceController {
    private ResultSet pastTransactions;
    private final String serviceID = "info_statement";

    public SendStatement(String session_id, int initiator){
        super(session_id, initiator);
    }

    @Override
    public void initialiseFromLog() {
        PIN = LogController.getLastNthInputInt(sessionID, 1);
    }

    @Override
    public boolean isAllowed(PrintWriter out){
        if (verifyPIN(sender, PIN))
            return true;
        out.println("Wrong PIN");
        out.close();
        return false;
    }

    @Override
    public void execute() throws SQLException {
        Connection conn = Database.getConnectionObject();
        Statement statement = conn.createStatement();
        String getTransactionsQuery = "select type, receiver_id, amount, time from transactions where sender_id=" + sender + " order by trans_number desc";
        pastTransactions = statement.executeQuery(getTransactionsQuery);
    }

    @Override
    public void sendSuccessMessage(HttpServletResponse resp, PrintWriter out){
        resp.setStatus(200);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        try{
            for(int i=0; i<10; i++){
                if(pastTransactions.next())
                    out.println(pastTransactions.getTimestamp("time").toLocalDateTime().format(formatter) +
                            ": " + pastTransactions.getString("type") +
                            " - " + pastTransactions.getString("receiver_id") +
                            " - " + pastTransactions.getString("amount"));
                else
                if (i==0)
                    out.println("No transaction history found");
                else
                    break;
            }
            out.close();
        } catch (SQLException e) {
            System.out.println("failed to get statement");
            Responses.internalServerError(resp, out);
        }
    }

}
