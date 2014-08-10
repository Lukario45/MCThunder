package net.mcthunder.events.session;

import net.mcthunder.listeners.SessionListener;
import net.mcthunder.packet.essentials.Session;

/**
 * Created by Kevin on 8/9/2014.
 */
public class DisconnectedEvent
        implements SessionEvent {
    private Session session;
    private String reason;

    public DisconnectedEvent(Session session, String reason) {
        this.session = session;
        this.reason = reason;
    }

    public Session getSession() {
        return this.session;
    }

    public String getReason() {
        return this.reason;
    }

    public void call(SessionListener listener) {
        listener.disconnected(this);
    }
}