package net.mcthunder.events.server;

import net.mcthunder.listeners.ServerListener;
import net.mcthunder.packet.essentials.Server;
import net.mcthunder.packet.essentials.Session;

public class SessionAddedEvent
        implements ServerEvent {
    private Server server;
    private Session session;

    public SessionAddedEvent(Server server, Session session) {
        this.server = server;
        this.session = session;
    }

    public Server getServer() {
        return this.server;
    }

    public Session getSession() {
        return this.session;
    }

    public void call(ServerListener listener) {
        listener.sessionAdded(this);
    }
}