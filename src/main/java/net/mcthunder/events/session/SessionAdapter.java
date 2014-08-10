package net.mcthunder.events.session;

import net.mcthunder.listeners.SessionListener;

/**
 * Created by Kevin on 8/9/2014.
 */
public class SessionAdapter
        implements SessionListener {
    public void packetReceived(PacketReceivedEvent event) {
    }

    public void packetSent(PacketSentEvent event) {
    }

    public void connected(ConnectedEvent event) {
    }

    public void disconnecting(DisconnectingEvent event) {
    }

    public void disconnected(DisconnectedEvent event) {
    }
}