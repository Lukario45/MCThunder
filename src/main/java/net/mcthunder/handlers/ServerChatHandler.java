package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.MessageFormat;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/7/2014.
 */
public class ServerChatHandler {
    public void handleChat(Player player, ClientChatPacket packet) {
        String message = packet.getMessage();
        if(message.endsWith(">") && ! message.equals(">")) {
            String appended = player.getAppended() + " " + message.substring(0, message.length() - 1);
            player.setAppended(appended.trim());
            player.sendMessage("&aMessage appended.");
            return;

        } else if (!player.getAppended().equals("")) {
            message = player.getAppended() + " " + message;
            player.setAppended("");
        }
        String format = MCThunder.getConfig().getChatFormat();
        format = format.replaceAll("\\{WORLD\\}", player.getWorld().getName());
        format = format.replaceAll("\\{NAME\\}", player.getDisplayName());
        //todo format = format.replaceAll("\\{RANK\\}",);
        int i = format.indexOf("{MESSAGE}");
        if(i != -1)
            format = format.substring(0, i) + message + format.substring(i + 9);
        sendMessage(format);
    }

    public void sendMessage(String message) {
        ServerChatPacket packet = new ServerChatPacket(MessageFormat.formatMessage(message));//Only create packet once
        for (Player p : MCThunder.getPlayers())
            p.sendPacket(packet);
        tellConsole(LoggingLevel.CHAT, message.trim());
    }
}