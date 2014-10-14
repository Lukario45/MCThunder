package net.mcthunder.events.source;

import net.mcthunder.events.PlayerCommandEvent;
import net.mcthunder.interfaces.PlayerCommandEventListener;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerCommandEventSource {
    private List playerChatEventListeners = new ArrayList();

    public synchronized void addEventListener(PlayerChatEventListener listener) {
        if (listener.removeDefaultListener()) {
            playerChatEventListeners.remove(0);
        }
        playerChatEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerChatEventListener listener) {
        playerChatEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Server server, Session session, ClientChatPacket packet) {
        PlayerChatEvent event = new PlayerChatEvent(this);
        Iterator iterator = playerChatEventListeners.iterator();
        while (iterator.hasNext()) {
            ((PlayerChatEventListener) iterator.next()).onCommand(server, session, packet);
        }
    }
}
