package net.mcthunder.events.source;

import net.mcthunder.entity.Player;
import net.mcthunder.events.PlayerChatEvent;
import net.mcthunder.events.interfaces.PlayerChatEventListenerInterface;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerChatEventSource {
    private static boolean removeDefault = false;
    private List playerChatEventListeners = new ArrayList();

    public synchronized void addEventListener(PlayerChatEventListenerInterface listener) {
        if (listener.removeDefaultListener() && !playerChatEventListeners.isEmpty() && !removeDefault) {
            playerChatEventListeners.remove(0);
            removeDefault = true;
        }
        playerChatEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerChatEventListenerInterface listener) {
        playerChatEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Player player, ClientChatPacket packet) {
        PlayerChatEvent event = new PlayerChatEvent(this);
        Iterator iterator = playerChatEventListeners.iterator();
        while (iterator.hasNext())
            ((PlayerChatEventListenerInterface) iterator.next()).onChat(player, packet);
    }
}