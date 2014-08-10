package net.mcthunder.events.session;

import net.mcthunder.listeners.SessionListener;
import net.mcthunder.packet.essentials.Packet;
import net.mcthunder.packet.essentials.Session;

/**
 * Created by Kevin on 8/9/2014.
 */
public class PacketSentEvent
        implements SessionEvent {
    private Session session;
    private Packet packet;

    public PacketSentEvent(Session session, Packet packet) {
        this.session = session;
        this.packet = packet;
    }

    public Session getSession() {
        return this.session;
    }

    public <T extends Packet> T getPacket() {
        try {
            return (T) this.packet;
        } catch (ClassCastException e) {
        }
        throw new IllegalStateException("Tried to get packet as the wrong type. Actual type: " + this.packet.getClass().getName());
    }

    public void call(SessionListener listener) {
        listener.packetSent(this);
    }
}