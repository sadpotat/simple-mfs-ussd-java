package Models;

import Controllers.Database;

public class Customer {
    private int cus_id;
    private String name;
    private String status;
    private String type;
    private double balance;

    public Customer(){}
    public Customer(int id, String cname, String stat, String typ) {
        cus_id = id;
        name = cname;
        status = stat;
        type = typ;
        balance = Database.getGetter().getBalance(cus_id);
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
