package net.mcthunder.api;

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
    private static Config conf;

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
        if (dir.isFile()) {
            throw new IllegalArgumentException("Location must be a directory!");
        }
        if (!dir.exists()) {
            dir.mkdirs();
            tellConsole(LoggingLevel.INFO, "Creating file " + location);
        }
    }

    public static void createInitialDirs() {
        tellConsole(LoggingLevel.INFO, "Checking Directories.");
        makeDir("PlayerFiles");
        makeDir("worlds");
        makeDir("plugins");
        makeDir("logs");
        tellConsole(LoggingLevel.INFO, "Completed checking directories!");
    }

    public static void tellPublicIpAddress() {
        conf = new Config();
        conf.loadConfig();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://icanhazip.com/").openConnection().getInputStream()));
            String ip = in.readLine();
            in.close();
            tellConsole(LoggingLevel.INFO, "People may connect to the server with " + ip + ":" + conf.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tellConsole(LoggingLevel level, String message) {
        System.out.printf("%tH:%<tM:%<TS [%s] %s\r\n", new Date(), level.getName(), message);
    }

    public static long getLong(int x, int z) {
        long longData = (long) x << 32 | z & 0xFFFFFFFFL;
        return longData;
    }
}