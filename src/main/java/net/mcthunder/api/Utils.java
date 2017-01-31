package net.mcthunder.api;

import net.mcthunder.MCThunder;
import net.mcthunder.block.Block;
import net.mcthunder.block.ItemDrop;
import net.mcthunder.block.Material;
import net.mcthunder.entity.DroppedItem;
import net.mcthunder.inventory.ItemStack;
import org.spacehq.mc.auth.data.GameProfile.Property;
import org.spacehq.opennbt.tag.builtin.Tag;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Kevin on 8/9/2014.
 */

public class Utils {
    private static HashMap<UUID,Property> skins = new HashMap<>();
    private static int ln = 0;

    public static DroppedItem getDropped(Block b){
        HashMap<String,ItemDrop> values = new HashMap<>();
        for (ItemDrop i: ItemDrop.values()){
            values.put(i.getName(),i);
    }
        try{
        if (values.values().contains(ItemDrop.valueOf(b.getType().getName()))){
            return new DroppedItem(b.getLocation(),new ItemStack(Material.valueOf(values.get(b.getType().getName()).getDropped()),1));

        } else {

            return new DroppedItem(b.getLocation(), new ItemStack(b.getType(), 1));

        } }catch (IllegalArgumentException e){
            return new DroppedItem(b.getLocation(), new ItemStack(b.getType(), 1));
        }


    }

    public static String getIP() {
        if (MCThunder.getConfig().getHost().equals("127.0.0.1")){

        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip == null ? null : ip.getHostAddress();
        } else {
            return MCThunder.getConfig().getHost();
        }
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
    public static void loadPlugins(){

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
            String connectIP = in.readLine() + ":" + MCThunder.getPort();
            tellConsole(LoggingLevel.INFO, "People may connect to the server with " + connectIP);
            if (MCThunder.getGuiMode()){
                MCThunder.getGui().getIpText().setText(connectIP);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tellConsole(LoggingLevel level, String message) {
        System.out.print(MessageFormat.toConsole(String.format("%tH:%<tM:%<TS [%s] %s\r\n", new Date(), level.getName(), message.trim())));
        if (MCThunder.getGuiMode()) {
            MCThunder.getGui().getConsolePane().setText(MCThunder.getGui().getConsolePane().getText() + String.format("%tH:%<tM:%<TS [%s] %s\r\n", new Date(),
                    level.getName(), MessageFormat.formatMessage(message).getFullText().trim()));
            message = String.format("%tH:%<tM:%<TS [%s] %s\r\n", new Date(), level.getName(), message.trim());
            for (String col : Arrays.asList("&A", "&B", "&C", "&D", "&E", "&F", "&L", "&N", "&M", "&O", "&K", "&R"))
                message = message.replaceAll(col, col.toLowerCase());
            int plength = message.length();
            message = message.replaceAll("&r", "&0").replaceAll("&m", "").replaceAll("&n", "").replaceAll("&o", "").replaceAll("&l", "").replaceAll("&k", "");
            ln += plength - message.length();
            String[] brokenMessage = message.split("&");
            StyledDocument sdoc = MCThunder.getGui().getConsolePane().getStyledDocument();
            boolean first = true;
            for (String m : brokenMessage) {
                if (m.equals("")) {
                    if (!first)
                        ln--;
                    first = false;
                    continue;
                }
                Character color = m.charAt(0);
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                if (!first || message.startsWith("&"))
                    switch (color) {
                        case 'a':
                            StyleConstants.setForeground(attrs, new Color(85, 255, 85));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case 'b':
                            StyleConstants.setForeground(attrs, new Color(85, 255, 255));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case 'c':
                            StyleConstants.setForeground(attrs, new Color(255, 85, 85));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case 'd':
                            StyleConstants.setForeground(attrs, new Color(255, 85, 255));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case 'e':
                            StyleConstants.setForeground(attrs, new Color(255, 255, 85));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case 'f':
                            StyleConstants.setForeground(attrs, new Color(255, 255, 255));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '0':
                            StyleConstants.setForeground(attrs, new Color(0, 0, 0));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '1':
                            StyleConstants.setForeground(attrs, new Color(0, 0, 170));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '2':
                            StyleConstants.setForeground(attrs, new Color(0, 170, 0));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '3':
                            StyleConstants.setForeground(attrs, new Color(0, 170, 170));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '4':
                            StyleConstants.setForeground(attrs, new Color(170, 0, 0));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '5':
                            StyleConstants.setForeground(attrs, new Color(170, 0, 170));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '6':
                            StyleConstants.setForeground(attrs, new Color(255, 170, 0));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '7':
                            StyleConstants.setForeground(attrs, new Color(170, 170, 170));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '8':
                            StyleConstants.setForeground(attrs, new Color(85, 85, 85));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        case '9':
                            StyleConstants.setForeground(attrs, new Color(85, 85, 255));
                            sdoc.setCharacterAttributes(ln, m.length() - 1, attrs, false);
                            break;
                        default:
                            StyleConstants.setForeground(attrs, new Color(0, 0, 0));
                            sdoc.setCharacterAttributes(ln, m.length(), attrs, false);
                            ln += 2;
                            break;
                    }
                if(!first)
                    ln += m.length();
                else
                    ln += m.length() - 2;
                first = false;
            }
            MCThunder.getGui().getConsolePane().setStyledDocument(sdoc);
            MCThunder.getGui().getConsolePane().setCaretPosition(MCThunder.getGui().getConsolePane().getDocument().getLength());
        }
    }

    public static void tellConsole(LoggingLevel level, Object m) {
        try {
            tellConsole(level, m.toString());
        } catch (Exception e) {//Could not cast to string
            e.printStackTrace();
        }
    }

    public static void tellConsole(LoggingLevel level, Tag t) {
        tellConsole(level, t.getValue());
    }

    public static UUID getUUIDfromString(String name) {
        if (name == null)
            return null;
        UUID uuid = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection().getInputStream()));
            String id = "";
            int count = 0;
            for (char c : in.readLine().toCharArray()) {
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

    public static Property getSkin(UUID uuid) {//TODO: Remove skins from cache after a minute
        if (uuid == null)
            return null;
        if (skins.containsKey(uuid))
            return skins.get(uuid);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" +
                    uuid.toString().replaceAll("-", "") + "?unsigned=false").openConnection().getInputStream()));
            String value = "";
            String signature = "";
            int count = 0;
            for (char c : in.readLine().toCharArray()) {
                if (c == '"')
                    count++;
                else if (count == 17)
                    value += c;
                else if (count == 21)
                    signature += c;
                if (count > 21)
                    break;
            }
            in.close();
            skins.put(uuid, new Property("textures", value, signature));
            return skins.get(uuid);
        } catch (Exception ignored) { }
        return null;
    }

    public static long getLong(int x, int z) {
        return (long) x << 32 | z & 0xFFFFFFFFL;
    }
}