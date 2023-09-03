package Cache;

public class TType {
    private String optionName;
    private String s_type;
    private String r_type;
    private double charges;

    public TType() {
    }

    public TType(String optionName, String s_type, String r_type, double charges) {
        this.optionName = optionName;
        this.s_type = s_type;
        this.r_type = r_type;
        this.charges = charges;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getS_type() {
        return s_type;
    }

    public void setS_type(String s_type) {
        this.s_type = s_type;
    }

    public String getR_type() {
        return r_type;
    }

    public void setR_type(String r_type) {
        this.r_type = r_type;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }
}
