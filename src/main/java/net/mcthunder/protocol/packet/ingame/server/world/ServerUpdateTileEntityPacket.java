package net.mcthunder.protocol.packet.ingame.server.world;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;
import net.mcthunder.protocol.util.NetUtil;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

import java.io.IOException;

public class ServerUpdateTileEntityPacket implements Packet {

    private int x;
    private int y;
    private int z;
    private Type type;
    private CompoundTag nbt;

    @SuppressWarnings("unused")
    private ServerUpdateTileEntityPacket() {
    }

    public ServerUpdateTileEntityPacket(int breakerEntityId, int x, int y, int z, Type type, CompoundTag nbt) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.nbt = nbt;
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

    public Type getType() {
        return this.type;
    }

    public CompoundTag getNBT() {
        return this.nbt;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readInt();
        this.y = in.readShort();
        this.z = in.readInt();
        this.type = Type.values()[in.readUnsignedByte() - 1];
        this.nbt = NetUtil.readNBT(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.x);
        out.writeShort(this.y);
        out.writeInt(this.z);
        out.writeByte(this.type.ordinal() + 1);
        NetUtil.writeNBT(out, this.nbt);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    public static enum Type {
        MOB_SPAWNER,
        COMMAND_BLOCK,
        BEACON,
        SKULL,
        FLOWER_POT;
    }

}
