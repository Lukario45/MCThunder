package net.mcthunder.packet.ingame.server.entity.spawn;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerSpawnExpOrbPacket
        implements Packet {
    private int entityId;
    private double x;
    private double y;
    private double z;
    private int exp;

    private ServerSpawnExpOrbPacket() {
    }

    public ServerSpawnExpOrbPacket(int entityId, double x, double y, double z, int exp) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.exp = exp;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public int getExp() {
        return this.exp;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readVarInt();
        this.x = (in.readInt() / 32.0D);
        this.y = (in.readInt() / 32.0D);
        this.z = (in.readInt() / 32.0D);
        this.exp = in.readShort();
    }

    public void write(NetOut out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeInt((int) (this.x * 32.0D));
        out.writeInt((int) (this.y * 32.0D));
        out.writeInt((int) (this.z * 32.0D));
        out.writeShort(this.exp);
    }

    public boolean isPriority() {
        return false;
    }
}