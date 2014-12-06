package net.mcthunder.api;

import net.mcthunder.MCThunder;
import org.spacehq.mc.auth.properties.Property;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

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

    public static UUID getUUIDfromString(String name) {
        if (name == null)
            return null;
        UUID uuid = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection().getInputStream()));
            String temp = in.readLine();
            String id = "";
            int count = 0;
            for (char c : temp.toCharArray()) {
                if (c == '"')
                    count++;
                else if (count == 3)
                    id += c;
                if (count > 3)
                    break;
            }
            uuid = UUID.fromString(id.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
            in.close();
        } catch (Exception ignored) { }
        return uuid;
    }

    public static Property getSkin(UUID uuid) {//Can only request same skin once per minute so will need to keep track of them
        if (uuid == null)
            return null;
        Property p = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" +
                    uuid.toString().replaceAll("-", "") + "?unsigned=false").openConnection().getInputStream()));
            String temp = in.readLine();
            String value = "";
            String signature = "";
            int count = 0;
            for (char c : temp.toCharArray()) {
                if (c == '"')
                    count++;
                else if (count == 17)
                    value += c;
                else if (count == 21)
                    signature += c;
                if (count > 21)
                    break;
            }
            p = new Property("textures", value, signature);
            in.close();
        } catch (Exception ignored) { }
        return p;
    }

    public static long getLong(int x, int z) {
        return (long) x << 32 | z & 0xFFFFFFFFL;
    }
}