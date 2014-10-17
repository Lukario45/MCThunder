package net.mcthunder.events.listeners;

import net.mcthunder.handlers.ServerChatHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.List;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerChatEventListener implements net.mcthunder.interfaces.PlayerChatEventListener {
    private ServerChatHandler serverChatHandler;

    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onChat(Server server, Session session, ClientChatPacket packet, List<Session> sessionsList) {
        serverChatHandler = new ServerChatHandler();
        serverChatHandler.handleChat(server, session, packet, sessionsList);
    }
}