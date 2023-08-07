package test;

public class Main {
    public static void main(String[] args){
        String input = "610";
        String exp = "([np]|[6-9]|10)";
//        Pattern pattern = Pattern.compile(exp);
//        System.out.println(pattern.matcher(input).matches());
        System.out.println(input.matches(exp));
//        String response = "test__test__test";
//        String[] arr = response.split("__");
//        for (String str:arr)
//            System.out.println(str);
    }
}
