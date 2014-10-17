package net.mcthunder.events.source;

import net.mcthunder.apis.Player;
import net.mcthunder.events.PlayerCommandEvent;
import net.mcthunder.interfaces.PlayerCommandEventListener;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerCommandEventSource {
    private static boolean removeDefault = false;
    private List playerCommandEventListeners = new ArrayList();

    public synchronized void addEventListener(PlayerCommandEventListener listener) {
        if (listener.removeDefaultListener() && !playerCommandEventListeners.isEmpty() && !removeDefault) {
            playerCommandEventListeners.remove(0);
            removeDefault = true;
        }
        playerCommandEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerCommandEventListener listener) {
        playerCommandEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Player player, ClientChatPacket packet) throws ClassNotFoundException {
        PlayerCommandEvent event = new PlayerCommandEvent(this);
        Iterator iterator = playerCommandEventListeners.iterator();
        while (iterator.hasNext())
            try {
                ((PlayerCommandEventListener) iterator.next()).onCommand(player, packet);
            } catch (ClassNotFoundException e) {
                player.sendMessageToPlayer("Unknown Command!");
            }
    }
}