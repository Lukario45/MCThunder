package net.mcthunder.handlers;

import net.mcthunder.apis.LoggingLevel;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.data.message.*;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.List;

import static net.mcthunder.apis.Utils.tellConsole;

/**
 * Created by Kevin on 10/7/2014.
 */
public class ServerChatHandler {
    private String sender;

    public ServerChatHandler() {

    }

    public void handleChat(Server server, Session session, ClientChatPacket packet, List<Session> sessionsList) {
        try {
            GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
            String userName = profile.getName();
            String message = packet.getMessage();
            tellConsole(LoggingLevel.CHAT, userName + ": " + message);
            Message msg = new TextMessage(userName + ": ").setStyle(new MessageStyle().setColor(ChatColor.YELLOW));
            Message body = new TextMessage(message).setStyle(new MessageStyle().setColor(ChatColor.WHITE).addFormat(ChatFormat.ITALIC));
            msg.addExtra(body);
            sessionsList = server.getSessions();
            for (Session s : sessionsList)
                s.send(new ServerChatPacket(msg));
        } catch (IllegalArgumentException e) {

        }
    }

    public void sendMessage(Server server, String message) {
        List<Session> sessionList = server.getSessions();
        Message msg = new TextMessage(message).setStyle(new MessageStyle().setColor(ChatColor.AQUA));
        for (Session s : sessionList) {
            s.send(new ServerChatPacket(msg));
        }
        tellConsole(LoggingLevel.CHAT, "[SERVER] " + message);
    }

    public void sendPrivateMessage(Session session, String privMessage) {
        Message privMsg = new TextMessage(privMessage);
        session.send(new ServerChatPacket(privMsg));
    }
}