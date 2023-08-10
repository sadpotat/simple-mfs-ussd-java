package Controllers;

import Models.GetFromDB;
import Models.NextResponse;

public class MenuController {

    public static NextResponse getNextMenu(String prevResponse, String accountType, String input){
        // getting the next response menu name
        GetFromDB getter = Database.getGetter();
        // first, check if there is a specific response for the entered input
        NextResponse nextResponse = getter.getNextResponse(accountType, prevResponse, input);
        if(nextResponse==null)
            // if not, indicates the input might be data. Search again for responses of input '-1'
            nextResponse = getter.getNextResponse(accountType, prevResponse, "-1");

        return nextResponse;
    }
}
