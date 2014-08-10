package net.mcthunder.packet.essentials;

import net.mcthunder.listeners.ConnectionListener;

/**
 * Created by Kevin on 8/9/2014.
 */
public abstract interface SessionFactory {
    public abstract Session createClientSession(Client paramClient);

    public abstract ConnectionListener createServerListener(Server paramServer);
}
