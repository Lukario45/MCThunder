package net.mcthunder.events.session;

import net.mcthunder.listeners.SessionListener;
import net.mcthunder.packet.essentials.Session;

/**
 * Created by Kevin on 8/9/2014.
 */
public class ConnectedEvent
        implements SessionEvent {
    private Session session;

    public ConnectedEvent(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return this.session;
    }

    public void call(SessionListener listener) {
        listener.connected(this);
    }
}