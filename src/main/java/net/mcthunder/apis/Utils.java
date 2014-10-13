package net.mcthunder.apis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kevin on 8/9/2014.
 */
public class Utils {
    public static void tellConsole(String type, String message) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(date + " [" + type + "]: " + message);
    }

}
