package Models;

public class Regex {
    private String regex;
    private String error_msg;

    public Regex(){
        regex = "";
        error_msg = "";
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
