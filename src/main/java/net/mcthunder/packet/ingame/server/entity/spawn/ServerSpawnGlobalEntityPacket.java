package net.mcthunder.packet.ingame.server.entity.spawn;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerSpawnGlobalEntityPacket
        implements Packet {
    private int entityId;
    private Type type;
    private int x;
    private int y;
    private int z;

    private ServerSpawnGlobalEntityPacket() {
    }

    public ServerSpawnGlobalEntityPacket(int entityId, Type type, int x, int y, int z) {
        this.entityId = entityId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Type getType() {
        return this.type;
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
        this.entityId = in.readVarInt();
        this.type = Type.values()[(in.readByte() - 1)];
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
    }

    public void write(NetOut out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(this.type.ordinal() + 1);
        out.writeInt(this.x);
        out.writeInt(this.y);
        out.writeInt(this.z);
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Type {
        LIGHTNING_BOLT;
    }
}