package packetlib.event.server;

import packetlib.Server;
import packetlib.Session;

/**
 * Called when a session is removed and disconnected from the server.
 */
public class SessionRemovedEvent implements ServerEvent {

    private Server server;
    private Session session;

    public SessionRemovedEvent(Server server, Session session) {
        this.server = server;
        this.session = session;
    }

    /**
     * Gets the server involved in this event.
     *
     * @return The event's server.
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Gets the session involved in this event.
     *
     * @return The event's session.
     */
    public Session getSession() {
        return this.session;
    }

    @Override
    public void call(ServerListener listener) {
        listener.sessionRemoved(this);
    }

}
