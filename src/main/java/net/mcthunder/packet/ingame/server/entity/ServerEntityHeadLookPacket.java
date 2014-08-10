package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityHeadLookPacket
        implements Packet {
    protected int entityId;
    protected float headYaw;

    private ServerEntityHeadLookPacket() {
    }

    public ServerEntityHeadLookPacket(int entityId, float headYaw) {
        this.entityId = entityId;
        this.headYaw = headYaw;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.headYaw = (in.readByte() * 360 / 256.0F);
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeByte((byte) (int) (this.headYaw * 256.0F / 360.0F));
    }

    public boolean isPriority() {
        return false;
    }
}