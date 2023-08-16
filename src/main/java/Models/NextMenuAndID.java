package Models;

public class NextMenuAndID {
    private String menuNo;
    private String serviceID;

    public NextMenuAndID(String menuNo, String serviceID) {
        this.menuNo = menuNo;
        this.serviceID = serviceID;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
