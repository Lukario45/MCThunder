package net.mcthunder.events.server;

import net.mcthunder.listeners.ServerListener;
import net.mcthunder.packet.essentials.Server;

public class ServerBoundEvent
        implements ServerEvent {
    private Server server;

    public ServerBoundEvent(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }

    public void call(ServerListener listener) {
        listener.serverBound(this);
    }
}