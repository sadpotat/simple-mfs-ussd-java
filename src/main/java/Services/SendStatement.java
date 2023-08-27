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
    private String miniStatement;
    private final String serviceID = "info_statement";
    private int sendMessage;

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

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        try{
            for(int i=0; i<10; i++){
                if(pastTransactions.next())
                    sb.append(pastTransactions.getTimestamp("time").toLocalDateTime().format(formatter))
                            .append(": ")
                            .append(pastTransactions.getString("type"))
                            .append(" - ")
                            .append(pastTransactions.getString("receiver_id"))
                            .append(" - ")
                            .append(pastTransactions.getString("amount"));
                else{
                    if (i==0)
                        sb.append("No transaction history found");
                    else
                        break;
                }
            }
            miniStatement = sb.toString();
            sendMessage = 1;
        } catch (SQLException e) {
            sendMessage = 0;
            System.out.println("failed to get statement");
        }


    }

    @Override
    public void sendSuccessMessage(HttpServletResponse resp, PrintWriter out){
        if (sendMessage==1){
            resp.setStatus(200);
            resp.setContentType("text/plain");
            out.println(miniStatement);
            out.close();
        } else
            Responses.internalServerError(resp, out);
    }

}
