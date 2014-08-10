package net.mcthunder.listeners;

import net.mcthunder.events.server.*;

/**
 * Created by Kevin on 8/9/2014.
 */
public abstract interface ServerListener {
    public abstract void serverBound(ServerBoundEvent paramServerBoundEvent);

    public abstract void serverClosing(ServerClosingEvent paramServerClosingEvent);

    public abstract void serverClosed(ServerClosedEvent paramServerClosedEvent);

    public abstract void sessionAdded(SessionAddedEvent paramSessionAddedEvent);

    public abstract void sessionRemoved(SessionRemovedEvent paramSessionRemovedEvent);
}