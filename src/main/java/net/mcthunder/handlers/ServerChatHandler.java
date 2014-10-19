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
    MessageFormat format;


    public ServerChatHandler() {
        format = new MessageFormat();

    }

    public void handleChat(Player player, ClientChatPacket packet) {
        try {
            String message = packet.getMessage();
            tellConsole(LoggingLevel.CHAT, player.gameProfile().getName() + ": " + message);
            String fullMessage = "&e[" + player.gameProfile().getName() + "]:&r " + message;
            for (Player p : MCThunder.playerHashMap.values())
                p.getSession().send(new ServerChatPacket(format.formatMessage(fullMessage)));
        } catch (IllegalArgumentException e) {

        }
    }

    public void sendMessage(Server server, String message) {
        List<Session> sessionList = server.getSessions();
        for (Session s : sessionList) {
            s.send(new ServerChatPacket(format.formatMessage(message)));
        }
        tellConsole(LoggingLevel.CHAT, message);
    }

    public void sendPrivateMessage(Session session, String privMessage) {
        session.send(new ServerChatPacket(format.formatMessage(privMessage)));
    }
}