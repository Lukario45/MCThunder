package net.mcthunder.events.server;

import net.mcthunder.listeners.ServerListener;
import net.mcthunder.packet.essentials.Server;

public class ServerClosingEvent
        implements ServerEvent {
    private Server server;

    public ServerClosingEvent(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }

    public void call(ServerListener listener) {
        listener.serverClosing(this);
    }
}