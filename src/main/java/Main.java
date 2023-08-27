import org.json.JSONObject;
import org.json.XML;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        GeneralResponse response = new GeneralResponse();
//        Method m = response.getClass().getMethod("setMessage", String.class);
//        System.out.println(m);
//        System.out.println(response.getMessage());
//        System.out.println(m.invoke(response, "test"));
//        System.out.println(response.getMessage());

        StringBuilder str = new StringBuilder();
        str.append("test" + "\n");
        str.append("test2" + "\n");
        str.append("test3" + "\n");
        System.out.println(str);
        System.out.println("1234".hashCode());
    }
}
