package net.mcthunder.events.server;

import net.mcthunder.listeners.abstrsct.ServerListener;

public class ServerAdapter
        implements ServerListener {
    public void serverBound(ServerBoundEvent event) {
    }

    public void serverClosing(ServerClosingEvent event) {
    }

    public void serverClosed(ServerClosedEvent event) {
    }

    public void sessionAdded(SessionAddedEvent event) {
    }

    public void sessionRemoved(SessionRemovedEvent event) {
    }
}