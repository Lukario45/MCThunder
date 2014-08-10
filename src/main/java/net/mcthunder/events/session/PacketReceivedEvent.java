package net.mcthunder.events.session;

import net.mcthunder.listeners.SessionListener;
import net.mcthunder.packet.essentials.Packet;
import net.mcthunder.packet.essentials.Session;

/**
 * Created by Kevin on 8/9/2014.
 */
public class PacketReceivedEvent
        implements SessionEvent {
    private Session session;
    private Packet packet;

    public PacketReceivedEvent(Session session, Packet packet) {
        this.session = session;
        this.packet = packet;
    }

    public <T extends Packet> getPacket() {
        try {
            return this.packet;
        } catch (ClassCastException e) {
        }
        throw new IllegalStateException("Tried to get packet as the wrong type. Actual type: " + this.packet.getClass().getName());
    }

    public Session getSession() {
        return this.session;
    }

    public void call(SessionListener listener) {
        listener.packetReceived(this);
    }
}