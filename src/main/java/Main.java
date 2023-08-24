import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        String body = "<XmlRobiRes>\n" +
                "  <resp>Top-up Success</resp>\n" +
                "  <success>Yes</success>\n" +
                "  <transactionid>91xINctb2YAf</transactionid>\n" +
                "  <localtime>24-08-2023 10:42</localtime>\n" +
                "</XmlRobiRes>\n";

        // removing brackets to separate tags
        String noBrackets = body.replaceAll(">\\s*<", "split_here");
        String[] split1 = noBrackets.split("split_here");
        // removing bracketing tag
        String[] newArray = Arrays.copyOfRange (split1, 1, (split1.length)-1);
        HashMap<String, String> map = new HashMap<>();
        for(String tag: newArray){
        // removing brackets again so that elements are in the order:
        // starting_tag_1, value1, ending_tag_1, starting_tag_2, ....
            String [] subarr = tag.split(">|</");
            map.put(subarr[0], subarr[1]);
        }
        System.out.println(map);
    }
}
