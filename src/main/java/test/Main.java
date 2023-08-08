package test;

public class Main {
    public static void main(String[] args){
        String input = "6109";
        String exp = "([np]|[6-9]|10)";
        System.out.println(input.matches("\\d\\d\\d\\d"));
        System.out.println(input.matches("\\d{4}"));
        System.out.println(input.matches("[0-9][0-9][0-9][0-9]"));
        System.out.println(input.matches("[0-9]{4}"));
    }
}
