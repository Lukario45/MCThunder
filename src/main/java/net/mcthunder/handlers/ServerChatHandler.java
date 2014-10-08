package net.mcthunder.handlers;

import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.data.message.ChatColor;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.data.message.MessageStyle;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;

import java.util.List;

import static net.mcthunder.apis.Utils.tellConsole;

/**
 * Created by Kevin on 10/7/2014.
 */
public class ServerChatHandler {
    private String sender;

    public ServerChatHandler() {

    }

    public void handleChat(Server server, Session session, ClientChatPacket packet, PacketReceivedEvent event, List<Session> sessionsList) {
        try {


            GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
            String userName = profile.getName();
            String message = packet.getMessage();
            tellConsole("CHAT", userName + ": " + message);
            Message msg = new TextMessage(userName).setStyle(new MessageStyle().setColor(ChatColor.YELLOW));
            Message body = new TextMessage(": " + message).setStyle(new MessageStyle().setColor(ChatColor.WHITE));
            msg.addExtra(body);
            sessionsList = server.getSessions();
            for (Session s : sessionsList) {
                s.send(new ServerChatPacket(msg));
            }
        } catch (IllegalArgumentException e) {

        }
        //tellConsole("DEBUG", "Session dump: ");
        //for (Session s : sessionsList) {
        //  tellConsole("DEBUG", "Found session for user " + session.getFlag(ProtocolConstants.PROFILE_KEY));

        // }


    }

}
