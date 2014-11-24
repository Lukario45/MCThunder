package net.mcthunder.events.source;

import net.mcthunder.events.PlayerLoggingInEvent;
import net.mcthunder.interfaces.PlayerLoggingInEventListener;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerLoggingInEventSource {
    private List<PlayerLoggingInEventListener> playerLoggingInEventListeners = new ArrayList<>();

    public synchronized void addEventListener(PlayerLoggingInEventListener listener) {
        this.playerLoggingInEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerLoggingInEventListener listener) {
        this.playerLoggingInEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Session session) throws ClassNotFoundException {
        PlayerLoggingInEvent event = new PlayerLoggingInEvent(this);
        for (Object playerLoggingInEventListener : playerLoggingInEventListeners)
            ((PlayerLoggingInEventListener) playerLoggingInEventListener).onLogin(session);
    }
}
