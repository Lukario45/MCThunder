package net.mcthunder.interfaces;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

/**
 * Created by Kevin on 10/14/2014.
 */
public interface PlayerCommandEventListener {
    public boolean removeDefaultListener();

    public void onCommand(Server server, Session session, ClientChatPacket packet);
}
