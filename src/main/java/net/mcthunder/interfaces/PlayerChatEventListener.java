package net.mcthunder.interfaces;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.List;

/**
 * Created by Kevin on 10/13/2014.
 */
public interface PlayerChatEventListener {
    public boolean removeDefaultListener();

    public void onChat(Server server, Session session, ClientChatPacket packet, List<Session> sessionsList);
}
