package packetlib.event.session;

import packetlib.Session;

/**
 * Called when the session is about to disconnect.
 */
public class DisconnectingEvent implements SessionEvent {

    private Session session;
    private String reason;

    public DisconnectingEvent(Session session, String reason) {
        this.session = session;
        this.reason = reason;
    }

    /**
     * Gets the session involved in this event.
     *
     * @return The event's session.
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Gets the reason given for the session disconnecting.
     *
     * @return The event's reason.
     */
    public String getReason() {
        return this.reason;
    }

    @Override
    public void call(SessionListener listener) {
        listener.disconnecting(this);
    }

}
