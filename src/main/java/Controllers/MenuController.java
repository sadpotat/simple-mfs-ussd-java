package Controllers;

import Models.GetFromDB;

public class MenuController {

    public static String getNextMenu(String prevResponse, String accountType, String input){
        // getting the next response menu name
        GetFromDB getter = Database.getGetter();
        // first, check if there is a specific response for the entered input
        String nextResponse = getter.getNextResponseMenu(accountType, prevResponse, input);
        if(nextResponse.equals(""))
            // if not, indicates the input might be data. Search again for responses of input '-1'
            nextResponse = getter.getNextResponseMenu(accountType, prevResponse, "-1");

        return nextResponse;
    }
}
