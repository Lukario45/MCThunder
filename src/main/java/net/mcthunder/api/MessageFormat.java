package net.mcthunder.api;

import org.spacehq.mc.protocol.data.message.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kevin on 10/18/2014.
 */
public class MessageFormat {
    public Message formatMessage(String message) {
        if (!message.contains("&"))
            return new TextMessage(message);
        List<String> colors = Arrays.asList("&A", "&B", "&C", "&D", "&E", "&F", "&L", "&N", "&M", "&O", "&K", "&R");
        for (String col : colors)
            message = message.replaceAll(col, col.toLowerCase());
        message += " ";//Sneaky work around to get it to show if only &s
        String[] brokenMessage = message.split("&");
        Message msg = new TextMessage("");
        ChatColor prev = ChatColor.WHITE;
        ArrayList<ChatFormat> formats = new ArrayList<>();
        boolean first = true;
        for (String m : brokenMessage) {
            if (m.equals("")) {
                if (!first)
                    msg.addExtra(new TextMessage("&").setStyle(new MessageStyle().setColor(prev).setFormats(formats)));
                first = false;
                continue;
            }
            Character color = m.charAt(0);
            switch (color) {
                case 'a':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("a", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.GREEN)));
                    break;
                case 'b':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("b", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.AQUA)));
                    break;
                case 'c':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("c", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.RED)));
                    break;
                case 'd':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("d", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.LIGHT_PURPLE)));
                    break;
                case 'e':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("e", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.YELLOW)));
                    break;
                case 'f':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("f", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.WHITE)));
                    break;
                case '0':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("0", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.BLACK)));
                    break;
                case '1':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("1", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_BLUE)));
                    break;
                case '2':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("2", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_GREEN)));
                    break;
                case '3':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("3", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_AQUA)));
                    break;
                case '4':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("4", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_RED)));
                    break;
                case '5':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("5", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_PURPLE)));
                    break;
                case '6':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("6", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.GOLD)));
                    break;
                case '7':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("7", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.GRAY)));
                    break;
                case '8':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("8", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_GRAY)));
                    break;
                case '9':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("9", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.BLUE)));
                    break;
                case 'l':
                    formats.add(ChatFormat.BOLD);
                    msg.addExtra(new TextMessage(m.replaceFirst("l", "")).setStyle(new MessageStyle().setColor(prev).setFormats(formats)));
                    break;
                case 'n':
                    formats.add(ChatFormat.UNDERLINED);
                    msg.addExtra(new TextMessage(m.replaceFirst("n", "")).setStyle(new MessageStyle().setColor(prev).setFormats(formats)));
                    break;
                case 'o':
                    formats.add(ChatFormat.ITALIC);
                    msg.addExtra(new TextMessage(m.replaceFirst("o", "")).setStyle(new MessageStyle().setColor(prev).setFormats(formats)));
                    break;
                case 'k':
                    formats.add(ChatFormat.OBFUSCATED);
                    msg.addExtra(new TextMessage(m.replaceFirst("k", "")).setStyle(new MessageStyle().setColor(prev).setFormats(formats).setFormats(formats)));
                    break;
                case 'm':
                    formats.add(ChatFormat.STRIKETHROUGH);
                    msg.addExtra(new TextMessage(m.replaceFirst("m", "")).setStyle(new MessageStyle().setColor(prev).setFormats(formats)));
                    break;
                case 'r':
                    formats = new ArrayList<>();
                    msg.addExtra(new TextMessage(m.replaceFirst("r", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.RESET)));
                    break;
                default:
                    if (!first)
                        msg.addExtra(new TextMessage("&" + m).setStyle(new MessageStyle().setColor(prev).setFormats(formats)));
                    else
                        msg.addExtra(new TextMessage(m).setStyle(new MessageStyle().setColor(prev).setFormats(formats)));
                    break;
            }
            first = false;
        }
        msg.addExtra(new TextMessage("").setStyle(new MessageStyle().setColor(prev).setFormats(formats)));
        return msg;
    }

    public String toConsole(String message) {
        message = message + "&r";
        message = message.replaceAll("&r", "\u001B[0m");
        message = message.replaceAll("&0", "\u001B[30m");//Possibly should replace with a lighter version or with white so it is visible
        message = message.replaceAll("&1", "\u001B[34m");
        message = message.replaceAll("&2", "\u001B[32m");
        message = message.replaceAll("&3", "\u001B[36m");
        message = message.replaceAll("&4", "\u001B[31m");
        message = message.replaceAll("&5", "\u001B[35m");
        message = message.replaceAll("&6", "\u001B[33m");
        message = message.replaceAll("&7", "\u001B[37m");
        message = message.replaceAll("&8", "\u001B[30;1m");
        message = message.replaceAll("&9", "\u001B[34;1m");
        message = message.replaceAll("&a", "\u001B[32;1m");
        message = message.replaceAll("&b", "\u001B[36;1m");
        message = message.replaceAll("&c", "\u001B[31;1m");
        message = message.replaceAll("&d", "\u001B[35;1m");
        message = message.replaceAll("&e", "\u001B[33;1m");
        message = message.replaceAll("&f", "\u001B[37;1m");
        message = message.replaceAll("&l", "").replaceAll("&n", "").replaceAll("&o", "").replaceAll("&k", "").replaceAll("&m", "");
        return message;
    }
}