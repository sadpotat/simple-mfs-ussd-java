package Models;

public class AuthHeader {
    private String username;
    private String password;
    private String dbname;

    public AuthHeader(String user, String pw, String db){
        username = user;
        password = pw;
        dbname = db;
    }

    public AuthHeader(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getURL(){
        return "jdbc:oracle:thin:" + username + "/" + password + "@localhost:1522:" + dbname;
    }
}
