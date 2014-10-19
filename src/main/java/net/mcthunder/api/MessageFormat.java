package net.mcthunder.api;

import org.spacehq.mc.protocol.data.message.*;

/**
 * Created by Kevin on 10/18/2014.
 */
public class MessageFormat {
    public Message formatMessage(String message) {
        if(!message.contains("&"))
            return new TextMessage(message);
        String[] brokenMessage = message.split("&");
        Message msg = new TextMessage("");
        for (String m : brokenMessage) {
            if (m.equals(""))
                continue;
            Character color = m.charAt(0);
            switch (color) {
                case 'a':
                    msg.addExtra(new TextMessage(m.replaceFirst("a", "")).setStyle(new MessageStyle().setColor(ChatColor.GREEN)));
                    break;
                case 'b':
                    msg.addExtra(new TextMessage(m.replaceFirst("b", "")).setStyle(new MessageStyle().setColor(ChatColor.AQUA)));
                    break;
                case 'c':
                    msg.addExtra(new TextMessage(m.replaceFirst("c", "")).setStyle(new MessageStyle().setColor(ChatColor.RED)));
                    break;
                case 'd':
                    msg.addExtra(new TextMessage(m.replaceFirst("d", "")).setStyle(new MessageStyle().setColor(ChatColor.LIGHT_PURPLE)));
                    break;
                case 'e':
                    msg.addExtra(new TextMessage(m.replaceFirst("e", "")).setStyle(new MessageStyle().setColor(ChatColor.YELLOW)));
                    break;
                case 'f':
                    msg.addExtra(new TextMessage(m.replaceFirst("f", "")).setStyle(new MessageStyle().setColor(ChatColor.WHITE)));
                    break;
                case '0':
                    msg.addExtra(new TextMessage(m.replaceFirst("0", "")).setStyle(new MessageStyle().setColor(ChatColor.BLACK)));
                    break;
                case '1':
                    msg.addExtra(new TextMessage(m.replaceFirst("1", "")).setStyle(new MessageStyle().setColor(ChatColor.DARK_BLUE)));
                    break;
                case '2':
                    msg.addExtra(new TextMessage(m.replaceFirst("2", "")).setStyle(new MessageStyle().setColor(ChatColor.DARK_GREEN)));
                    break;
                case '3':
                    msg.addExtra(new TextMessage(m.replaceFirst("3", "")).setStyle(new MessageStyle().setColor(ChatColor.DARK_AQUA)));
                    break;
                case '4':
                    msg.addExtra(new TextMessage(m.replaceFirst("4", "")).setStyle(new MessageStyle().setColor(ChatColor.DARK_RED)));
                    break;
                case '5':
                    msg.addExtra(new TextMessage(m.replaceFirst("5", "")).setStyle(new MessageStyle().setColor(ChatColor.DARK_PURPLE)));
                    break;
                case '6':
                    msg.addExtra(new TextMessage(m.replaceFirst("6", "")).setStyle(new MessageStyle().setColor(ChatColor.GOLD)));
                    break;
                case '7':
                    msg.addExtra(new TextMessage(m.replaceFirst("7", "")).setStyle(new MessageStyle().setColor(ChatColor.GRAY)));
                    break;
                case '8':
                    msg.addExtra(new TextMessage(m.replaceFirst("8", "")).setStyle(new MessageStyle().setColor(ChatColor.DARK_GRAY)));
                    break;
                case '9':
                    msg.addExtra(new TextMessage(m.replaceFirst("9", "")).setStyle(new MessageStyle().setColor(ChatColor.BLUE)));
                    break;
                case 'l':
                    msg.addExtra(new TextMessage(m.replaceFirst("l", "")).setStyle(new MessageStyle().addFormat(ChatFormat.BOLD)));
                    break;
                case 'n':
                    msg.addExtra(new TextMessage(m.replaceFirst("n", "")).setStyle(new MessageStyle().addFormat(ChatFormat.UNDERLINED)));
                    break;
                case 'o':
                    msg.addExtra(new TextMessage(m.replaceFirst("o", "")).setStyle(new MessageStyle().addFormat(ChatFormat.ITALIC)));
                    break;
                case 'k':
                    msg.addExtra(new TextMessage(m.replaceFirst("k", "")).setStyle(new MessageStyle().addFormat(ChatFormat.OBFUSCATED)));
                    break;
                case 'm':
                    msg.addExtra(new TextMessage(m.replaceFirst("m", "")).setStyle(new MessageStyle().addFormat(ChatFormat.STRIKETHROUGH)));
                    break;
                case 'r':
                    msg.addExtra(new TextMessage(m.replaceFirst("r", "")).setStyle(new MessageStyle().setColor(ChatColor.RESET)));
                    break;
                default:
                    msg.addExtra(new TextMessage("&" + m).setStyle(new MessageStyle().setColor(ChatColor.WHITE)));
                    break;
            }
        }
        return msg;
    }
}