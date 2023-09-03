package Services;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;

public interface Service {
    void initialiseFromLog() throws SQLException;
    boolean isAllowed(HttpServletResponse resp, PrintWriter out);
    void execute() throws SQLException ;
    void sendSuccessMessage(HttpServletResponse resp, PrintWriter out);

}
