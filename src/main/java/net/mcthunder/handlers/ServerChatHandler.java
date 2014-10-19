package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.apis.LoggingLevel;
import net.mcthunder.apis.MessageFormat;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.List;

import static net.mcthunder.apis.Utils.tellConsole;

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
            //MessageFormat formatter = new MessageFormat();
            String message = packet.getMessage();
            tellConsole(LoggingLevel.CHAT, player.gameProfile().getName() + ": " + message);
            String fullMessage = "&e[" + player.gameProfile().getName() + "]:&r " + message;
            //Message msg = new TextMessage(player.gameProfile().getName() + ": ").setStyle(new MessageStyle().setColor(ChatColor.YELLOW));
            //Message body = new TextMessage(message).setStyle(new MessageStyle().setColor(ChatColor.WHITE));
            //msg.addExtra(body);
            //System.out.println(msg.toJsonString());
            for (Player p : MCThunder.playerHashMap.values())
                p.getSession().send(new ServerChatPacket(format.formatMessage(fullMessage)));
        } catch (IllegalArgumentException e) {

        }
    }

    public void sendMessage(Server server, String message) {
        List<Session> sessionList = server.getSessions();
        //Message msg = new TextMessage(message).setStyle(new MessageStyle().setColor(ChatColor.AQUA));
        for (Session s : sessionList) {
            s.send(new ServerChatPacket(format.formatMessage(message)));
        }
        tellConsole(LoggingLevel.CHAT, "[SERVER] " +  message);
    }

    public void sendPrivateMessage(Session session, String privMessage) {
        // Message privMsg = new TextMessage(privMessage);
        session.send(new ServerChatPacket(format.formatMessage(privMessage)));
    }
}