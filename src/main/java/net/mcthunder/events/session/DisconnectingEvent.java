package net.mcthunder.events.session;

import net.mcthunder.listeners.SessionListener;
import net.mcthunder.packet.essentials.Session;

/**
 * Created by Kevin on 8/9/2014.
 */
public class DisconnectingEvent
        implements SessionEvent {
    private Session session;
    private String reason;

    public DisconnectingEvent(Session session, String reason) {
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
        listener.disconnecting(this);
    }
}