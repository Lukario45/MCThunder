package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.MessageFormat;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Session;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/7/2014.
 */
public class ServerChatHandler {
    MessageFormat format = new MessageFormat();

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
        sendMessage("&e" + player.getName() + ":&r " + message);
    }

    public void sendMessage(String message) {
        ServerChatPacket packet = new ServerChatPacket(format.formatMessage(message));//Only create packet once
        for (Player p : MCThunder.getPlayers())
            p.sendPacket(packet);
        tellConsole(LoggingLevel.CHAT, message);
    }

    public void sendMessage(Session session, String message) {
        session.send(new ServerChatPacket(format.formatMessage(message)));
    }
}