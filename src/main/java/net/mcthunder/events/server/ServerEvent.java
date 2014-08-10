package net.mcthunder.events.server;

import net.mcthunder.listeners.abstrsct.ServerListener;

public abstract interface ServerEvent {
    public abstract void call(ServerListener paramServerListener);
}