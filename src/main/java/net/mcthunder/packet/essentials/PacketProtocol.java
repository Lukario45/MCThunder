package net.mcthunder.packet.essentials;

/**
 * Created by Kevin on 8/9/2014.
 */


import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;


public abstract class PacketProtocol {
    private final Map<Integer, Class<? extends Packet>> incoming = new HashMap();
    private final Map<Class<? extends Packet>, Integer> outgoing = new HashMap();

    public abstract boolean needsPacketSizer();

    public abstract boolean needsPacketEncryptor();

    public abstract PacketHead getPacketHeader();

    public abstract PacketEncrypter getEncryption();

    public abstract void newClientSession(Client paramClient, Session paramSession);

    public abstract void newServerSession(Server paramServer, Session paramSession);

    public final void clearPackets() {
        this.incoming.clear();
        this.outgoing.clear();
    }

    public final void register(int id, Class<? extends Packet> packet) {
        registerIncoming(id, packet);
        registerOutgoing(id, packet);
    }

    public final void registerIncoming(int id, Class<? extends Packet> packet) {
        this.incoming.put(Integer.valueOf(id), packet);
        try {
            createIncomingPacket(id);
        } catch (IllegalStateException e) {
            this.incoming.remove(Integer.valueOf(id));
            throw new IllegalArgumentException(e.getMessage(), e.getCause());
        }
    }

    public final void registerOutgoing(int id, Class<? extends Packet> packet) {
        this.outgoing.put(packet, Integer.valueOf(id));
    }

    public final Packet createIncomingPacket(int id) throws IllegalStateException {
        if ((id < 0) || (!this.incoming.containsKey(Integer.valueOf(id))) || (this.incoming.get(Integer.valueOf(id)) == null)) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        Class packet = (Class) this.incoming.get(Integer.valueOf(id));
        try {
            Constructor constructor = packet.getDeclaredConstructor(new Class[0]);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return (Packet) constructor.newInstance(new Object[0]);
        } catch (NoSuchMethodError e) {
            throw new IllegalStateException("Packet \"" + id + ", " + packet.getName() + "\" does not have a no-params constructor for instantiation.");
        } catch (Exception e) {
        }
        throw new IllegalStateException("Failed to instantiate packet \"" + id + ", " + packet.getName() + "\".");


    }

    public final int getOutgoingId(Class<? extends Packet> packet) {
        if ((!this.outgoing.containsKey(packet)) || (this.outgoing.get(packet) == null)) {
            throw new IllegalArgumentException("Unregistered outgoing packet class: " + packet.getName());
        }

        return ((Integer) this.outgoing.get(packet)).intValue();
    }
}