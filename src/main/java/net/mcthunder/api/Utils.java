package net.mcthunder.api;

import net.mcthunder.MCThunder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by Kevin on 8/9/2014.
 */

public class Utils {
    private static MessageFormat format = new MessageFormat();

    public static String getIP() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip == null ? null : ip.getHostAddress();
    }

    public static void makeDir(String location) {
        File dir = new File(location);
        if (dir.isFile())
            throw new IllegalArgumentException("Location must be a directory!");
        if (!dir.exists()) {
            dir.mkdirs();
            tellConsole(LoggingLevel.INFO, "Creating directory " + location);
        }
    }

    public static void createInitialDirs() {
        tellConsole(LoggingLevel.INFO, "Checking directories.");
        makeDir("PlayerFiles");
        makeDir("worlds");
        makeDir("plugins");
        makeDir("logs");
        tellConsole(LoggingLevel.INFO, "Checking directories complete!");
    }

    public static void tellPublicIpAddress() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://icanhazip.com/").openConnection().getInputStream()));
            tellConsole(LoggingLevel.INFO, "People may connect to the server with " + in.readLine() + ":" + MCThunder.getPort());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tellConsole(LoggingLevel level, String message) {
        System.out.printf("%tH:%<tM:%<TS [%s] %s\r\n", new Date(), level.getName(), format.toConsole(message));
    }

    public static void tellConsole(LoggingLevel level, Object m) {
        try {
            tellConsole(level, m.toString());
        } catch (Exception e) {//Could not cast to string
            e.printStackTrace();
        }
    }

    public static long getLong(int x, int z) {
        return (long) x << 32 | z & 0xFFFFFFFFL;
    }
}