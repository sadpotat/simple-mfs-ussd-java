package Controllers;

import Models.AuthHeader;
import Models.GetFromDB;
import Models.InsertIntoDB;

import java.sql.*;

public class Database {

    // oracle jdbc className: "oracle.jdbc.driver.OracleDriver"
    private static Connection conn = null;
    private static Database db;


    public Database(String className, String dbURL) throws ClassNotFoundException, SQLException {
        Class.forName(className);

        conn = DriverManager.getConnection(dbURL);
        GetFromDB.getInstance(conn);
        InsertIntoDB.getInstance(conn);
    }

    public static boolean connectToDatabase(AuthHeader auth) {
        try {
            String jdbcClassName = "oracle.jdbc.driver.OracleDriver";
            db = Database.getInstance(jdbcClassName, auth.getURL());
            System.out.println("getInstance");
            db.setAutoCommit(false);
            System.out.println("connected to db");
            return true;
        } catch (SQLException | NullPointerException s) {
            System.out.println(s);
            System.out.println("failed to connect to db");
            return false;
        }

    }

    public static Database getInstance(String className, String dbURL){
        if (db==null){
            try {
                db = new Database(className, dbURL);
            } catch (SQLException | ClassNotFoundException e){
                System.out.println(e);
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

    private void close() throws SQLException {
        System.out.println();
        System.out.println("Disconnecting from the database...");
        conn.close();
        db = null;
    }

    public static void closeDB() throws SQLException {
        db.close();
        db = null;
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


}
