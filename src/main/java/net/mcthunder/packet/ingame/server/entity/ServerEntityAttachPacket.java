package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityAttachPacket
        implements Packet {
    private int entityId;
    private int attachedToId;
    private boolean leash;

    private ServerEntityAttachPacket() {
    }

    public ServerEntityAttachPacket(int entityId, int attachedToId, boolean leash) {
        this.entityId = entityId;
        this.attachedToId = attachedToId;
        this.leash = leash;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getAttachedToId() {
        return this.attachedToId;
    }

    public boolean getLeash() {
        return this.leash;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.attachedToId = in.readInt();
        this.leash = in.readBoolean();
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeInt(this.attachedToId);
        out.writeBoolean(this.leash);
    }

    public boolean isPriority() {
        return false;
    }
}