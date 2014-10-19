package net.mcthunder.apis;

import org.spacehq.mc.protocol.data.message.*;

/**
 * Created by Kevin on 10/18/2014.
 */
public class MessageFormat {
    public Message formatMessage(String message) {
        String[] brokenMessage = message.split("&");
        Message msg = new TextMessage("");


        //tellConsole(LoggingLevel.DEBUG, color.toString());
        //Message msg = Message.fromString(color.toString());


        for (int i = 1; i < brokenMessage.length; i++) {
            Character color = brokenMessage[i].charAt(0);
            //int i = 0;
            // while (i < brokenMessage.length) {
            switch (color) {

                case 'a':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.GREEN)));
                    // i++;
                    break;
                case 'b':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.AQUA)));
                    // i++;
                    break;
                case 'c':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.RED)));
                    // i++;
                    break;
                case 'd':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.LIGHT_PURPLE)));
                    //  i++;
                    break;
                case 'e':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.YELLOW)));
                    //  i++;
                    break;
                case 'f':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.WHITE)));
                    //  i++;
                    break;
                case '1':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.BLACK)));
                    //  i++;
                    break;
                case '2':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_GREEN)));
                    //  i++;
                    break;
                case '3':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_AQUA)));
                    //  i++;
                    break;
                case '4':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_RED)));
                    //  i++;
                    break;
                case '5':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_PURPLE)));
                    //  i++;
                    break;
                case '6':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.GOLD)));
                    //   i++;
                    break;
                case '7':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.GRAY)));
                    //  i++;
                    break;
                case '8':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.DARK_GRAY)));
                    //  i++;

                    break;
                case '9':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.BLUE)));
                    // i++;
                    break;
                case 'l':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.BOLD)));
                    //i++;
                    break;
                case 'n':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.UNDERLINED)));
                    // i++;
                    break;
                case 'o':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.ITALIC)));
                    // i++;
                    break;

                case 'k':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.OBFUSCATED)));
                    // i++;
                    break;
                case 'm':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().addFormat(ChatFormat.STRIKETHROUGH)));
                    // i++;
                    break;
                case 'r':
                    msg.addExtra(new TextMessage(brokenMessage[i]).setStyle(new MessageStyle().setColor(ChatColor.RESET)));
                    //i++;
                    break;
                default:
                    msg.addExtra(new TextMessage(brokenMessage[i]));
                    //i++;
                    break;

            }
        }


        return msg;
    }
}
