package net.mcthunder.events.source;

import net.mcthunder.events.PlayerLoggingInEvent;
import net.mcthunder.events.interfaces.PlayerLoggingInEventListenerInterface;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerLoggingInEventSource {
    private List<PlayerLoggingInEventListenerInterface> playerLoggingInEventListeners = new ArrayList<>();

    public synchronized void addEventListener(PlayerLoggingInEventListenerInterface listener) {
        this.playerLoggingInEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerLoggingInEventListenerInterface listener) {
        this.playerLoggingInEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Session session) throws ClassNotFoundException {
        PlayerLoggingInEvent event = new PlayerLoggingInEvent(this);
        for (Object playerLoggingInEventListener : playerLoggingInEventListeners)
            ((PlayerLoggingInEventListenerInterface) playerLoggingInEventListener).onLogin(session);
    }
}
