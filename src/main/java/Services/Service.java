package Services;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;

public interface Service {
    public void initialiseFromLog() throws SQLException;
    public boolean isAllowed(PrintWriter out);
    public void execute() throws SQLException ;
    public void sendSuccessMessage(HttpServletResponse resp, PrintWriter out);

}
