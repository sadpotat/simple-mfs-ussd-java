package Controllers;

import Models.AuthHeader;
import Models.GetFromDB;
import Models.InsertIntoDB;

import java.sql.*;

public class Database {

    // oracle jdbc className: "oracle.jdbc.driver.OracleDriver"
    private static Connection conn = null;
    private static Database db;

    private static GetFromDB getter;
    private static InsertIntoDB insert;


    public Database(String className, String dbURL) throws ClassNotFoundException, SQLException {
        Class.forName(className);

        conn = DriverManager.getConnection(dbURL);
        getter = new GetFromDB(conn);
        insert = new InsertIntoDB(conn);
    }

    public static void connectToDatabase(AuthHeader auth) {
        try {
            String jdbcClassName = "oracle.jdbc.driver.OracleDriver";
            db = Database.getInstance(jdbcClassName, auth.getURL());
            System.out.println("getInstance");
            db.setAutoCommit(false);
            System.out.println("connected to db");
        } catch (Exception s) {
            System.out.println("failed to connect to db");
        }

    }

    public static Database getInstance(String className, String dbURL){
        if (db==null){
            try {
                db = new Database(className, dbURL);
            } catch (Exception e){
                db = null;
            }
        }
        return db;
    }

    public static Database getDb() {
        return db;
    }

    public static Connection getConnectionObject(){
        return conn;
    }

    public void setAutoCommit(boolean bool) throws SQLException {
        conn.setAutoCommit(bool);
    }

    private void commit() throws SQLException {
        System.out.println();
        System.out.println("Committing changes...");
        conn.commit();
    }

    private void rollback() throws SQLException {
        System.out.println();
        System.out.println("Rolling back changes...");
        conn.rollback();
    }

    public static void commitChanges() throws SQLException {
        db.commit();
    }
    public static void rollbackChanges() {
        try {
            db.rollback();
        } catch (SQLException e) {
            System.out.println("failed to rollback");
        }
    }

    public static GetFromDB getGetter() {
        return getter;
    }

    public static InsertIntoDB getInsert() {
        return insert;
    }

}
