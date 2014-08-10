package net.mcthunder.protocol.packet.ingame.server.entity.player;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerPlayerUseBedPacket implements Packet {

    private int entityId;
    private int x;
    private int y;
    private int z;

    @SuppressWarnings("unused")
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

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readInt();
        this.x = in.readInt();
        this.y = in.readUnsignedByte();
        this.z = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.entityId);
        out.writeInt(this.x);
        out.writeByte(this.y);
        out.writeInt(this.z);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
