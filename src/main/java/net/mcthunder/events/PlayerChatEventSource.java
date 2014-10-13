package net.mcthunder.events;

import net.mcthunder.interfaces.PlayerChatEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerChatEventSource {
    private List playerChatEventListeners = new ArrayList();

    public synchronized void addEventListener(PlayerChatEventListener listener) {
        playerChatEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerChatEventListener listener) {
        playerChatEventListeners.remove(listener);
    }

    private synchronized void fireEvent() {
        PlayerChatEvent event = new PlayerChatEvent(this);
        Iterator iterator = playerChatEventListeners.iterator();
        while (iterator.hasNext()) {
            ((PlayerChatEventListener) iterator.next()).handlePlayerChatEvent(event);
        }
    }
}
