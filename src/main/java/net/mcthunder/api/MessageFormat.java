package net.mcthunder.api;

import org.spacehq.mc.protocol.data.message.*;

import java.util.ArrayList;

/**
 * Created by Kevin on 10/18/2014.
 */
public class MessageFormat {
    public Message formatMessage(String message) {
        if(!message.contains("&"))
            return new TextMessage(message);
        String[] brokenMessage = message.split("&");
        Message msg = new TextMessage("");
        ChatColor prev = ChatColor.WHITE;
        ArrayList<ChatFormat> formats = new ArrayList<ChatFormat>();
        for (String m : brokenMessage) {
            if (m.equals("")) {
                msg.addExtra(new TextMessage("&").setStyle(new MessageStyle().setColor(prev)));
                continue;
            }
            Character color = m.charAt(0);
            switch (color) {
                case 'a':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("a", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.GREEN)));
                    break;
                case 'b':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("b", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.AQUA)));
                    break;
                case 'c':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("c", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.RED)));
                    break;
                case 'd':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("d", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.LIGHT_PURPLE)));
                    break;
                case 'e':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("e", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.YELLOW)));
                    break;
                case 'f':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("f", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.WHITE)));
                    break;
                case '0':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("0", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.BLACK)));
                    break;
                case '1':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("1", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_BLUE)));
                    break;
                case '2':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("2", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_GREEN)));
                    break;
                case '3':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("3", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_AQUA)));
                    break;
                case '4':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("4", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_RED)));
                    break;
                case '5':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("5", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_PURPLE)));
                    break;
                case '6':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("6", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.GOLD)));
                    break;
                case '7':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("7", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.GRAY)));
                    break;
                case '8':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("8", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.DARK_GRAY)));
                    break;
                case '9':
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("9", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.BLUE)));
                    break;
                case 'l':
                    formats.add(ChatFormat.BOLD);
                    msg.addExtra(new TextMessage(m.replaceFirst("l", "")).setStyle(new MessageStyle().setFormats(formats)));
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
                    formats = new ArrayList<ChatFormat>();
                    msg.addExtra(new TextMessage(m.replaceFirst("r", "")).setStyle(new MessageStyle().setColor(prev = ChatColor.RESET)));
                    break;
                default:
                    msg.addExtra(new TextMessage("&" + m).setStyle(new MessageStyle().setColor(prev)));
                    break;
            }
        }
        return msg;
    }
}
