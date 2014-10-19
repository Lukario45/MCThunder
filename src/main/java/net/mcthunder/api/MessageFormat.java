package net.mcthunder.api;

import org.spacehq.mc.protocol.data.message.*;

/**
 * Created by Kevin on 10/18/2014.
 */
public class MessageFormat {
    public Message formatMessage(String message) {
        String[] brokenMessage = message.split("&");
        Message msg = new TextMessage("");
        for (int i = 1; i < brokenMessage.length; i++) {
            Character color = brokenMessage[i].charAt(0);
            switch (color) {
                case 'a':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.GREEN)));
                    break;
                case 'b':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.AQUA)));
                    break;
                case 'c':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.RED)));
                    break;
                case 'd':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.LIGHT_PURPLE)));
                    break;
                case 'e':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.YELLOW)));
                    break;
                case 'f':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.WHITE)));
                    break;
                case '1':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.BLACK)));
                    break;
                case '2':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_GREEN)));
                    break;
                case '3':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_AQUA)));
                    break;
                case '4':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_RED)));
                    break;
                case '5':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_PURPLE)));
                    break;
                case '6':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.GOLD)));
                    break;
                case '7':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.GRAY)));
                    break;
                case '8':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_GRAY)));
                    break;
                case '9':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.BLUE)));
                    break;
                case 'l':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.BOLD)));
                    break;
                case 'n':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.UNDERLINED)));
                    break;
                case 'o':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.ITALIC)));
                    break;
                case 'k':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.OBFUSCATED)));
                    break;
                case 'm':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.STRIKETHROUGH)));
                    break;
                case 'r':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.RESET)));
                    break;
                default:
                    msg.addExtra(new TextMessage(brokenMessage[0]).setStyle(new MessageStyle().setColor(ChatColor.WHITE)));
                    break;
            }
        }
        return msg;
    }
}
