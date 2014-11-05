package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.MessageFormat;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.List;

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
        sendMessage(MCThunder.getServer(), "&e" + player.gameProfile().getName() + ":&r " + message);
    }

    public void sendMessage(Server server, String message) {
        List<Session> sessionList = server.getSessions();
        ServerChatPacket packet = new ServerChatPacket(format.formatMessage(message));//Only create packet once
        for (Session s : sessionList)
            s.send(packet);
        tellConsole(LoggingLevel.CHAT, message);
    }

    public void sendPrivateMessage(Session session, String privMessage) {
        session.send(new ServerChatPacket(format.formatMessage(privMessage)));
    }
}