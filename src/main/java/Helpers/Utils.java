package Helpers;

import Cache.CacheLoader;
import Controllers.Database;
import Models.GetFromDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utils {

    public static Map<String, String> queryToMap(String query) {
        // copied and pasted code, will look into it later
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public static String createSessionID(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int bound = characters.length();
        int length = 12;

        Random rand = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i=0; i<length; i++){
            builder.append(characters.charAt(rand.nextInt(bound)));
        }
        return builder.toString();
    }

    public static String generatePage(String typeToPage, int currentPage, int maxPage, int entries) {
        GetFromDB getter = Database.getGetter();
        CacheLoader cache = CacheLoader.getInstance();
        ResultSet rs;
        try {
            rs = getter.getClients(typeToPage, currentPage, entries);
            StringBuilder sb = new StringBuilder();
            sb.append(cache.getChooseOptionTextForPage(typeToPage)).append("\n");
            int start = entries*currentPage;
            while (rs.next()){
                start++;
                sb.append(start).append(" ").append(rs.getString("name")).append("\n");
            }

            if(start == entries*currentPage) {
                // indicates that no entries were found
                sb.append("No items to load");
                sb.append("\n").append("p previous");
                return sb.toString();
            }

            sb.append("\n");
            if (currentPage<maxPage-1)
                sb.append("n next");
            if (currentPage!=0)
                sb.append("\n").append("p previous");
            return sb.toString();
        } catch (SQLException e) {
            return null;
        }
    }
}
