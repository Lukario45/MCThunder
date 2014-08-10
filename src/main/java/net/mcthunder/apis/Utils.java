package net.mcthunder.apis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kevin on 8/9/2014.
 */
public class Utils {


    public static void tellConsole(String type, String message) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d = new Date();
        String date = dateFormat.format(d);
        System.out.println(date + " [" + type + "]: " + message);
    }

}
