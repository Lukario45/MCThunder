package net.mcthunder.packet.ingame.server.entity.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerPlayerUseBedPacket
        implements Packet {
    private int entityId;
    private int x;
    private int y;
    private int z;

    private ServerPlayerUseBedPacket() {
    }

    public ServerPlayerUseBedPacket(int entityId, int x, int y, int z) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.x = in.readInt();
        this.y = in.readUnsignedByte();
        this.z = in.readInt();
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeInt(this.x);
        out.writeByte(this.y);
        out.writeInt(this.z);
    }

    public boolean isPriority() {
        return false;
    }
}