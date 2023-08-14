package test;

import Controllers.Database;
import Models.AuthHeader;
import Models.Customer;

import javax.swing.plaf.nimbus.State;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
//        String input = "6109";
//        String exp = "([np]|[6-9]|10)";
//        System.out.println(input.matches("\\d\\d\\d\\d"));
//        System.out.println(input.matches("\\d{4}"));
//        System.out.println(input.matches("[0-9][0-9][0-9][0-9]"));
//        System.out.println(input.matches("[0-9]{4}"));

        AuthHeader auth = new AuthHeader("ussd", "ussd12345", "sadiadb3");
        Database.connectToDatabase(auth);
        Statement statement = Database.getConnectionObject().createStatement();
        ResultSet rs = statement.executeQuery("select * from customers");

        HashMap<Integer, Customer> map = new HashMap<Integer, Customer>();
        Customer customer;
        while(rs.next()){
            customer = new Customer(rs.getInt("cus_id"),
                    rs.getString("name"),
                    rs.getString("status"),
                    rs.getString("type"));
            map.put(rs.getInt("cus_id"), customer);
        }

        System.out.println(map);
        System.out.println(map.get(125).getType());
    }
}
