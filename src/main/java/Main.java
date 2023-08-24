import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        GeneralResponse response = new GeneralResponse();
//        Method m = response.getClass().getMethod("setMessage", String.class);
//        System.out.println(m);
//        System.out.println(response.getMessage());
//        System.out.println(m.invoke(response, "test"));
//        System.out.println(response.getMessage());
        HashMap<String, String> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        map.put("key3", "val3");
        map.put("key4", "val4");
        for (String s : map.keySet()) {
            System.out.println(s);
        }



    }
}
