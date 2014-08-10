package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.game.essentials.tags.CompoundTag;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerUpdateTileEntityPacket
        implements Packet {
    private int x;
    private int y;
    private int z;
    private Type type;
    private CompoundTag nbt;

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

    public void read(NetIn in) throws IOException {
        this.x = in.readInt();
        this.y = in.readShort();
        this.z = in.readInt();
        this.type = Type.values()[(in.readUnsignedByte() - 1)];
        this.nbt = NetUtil.readNBT(in);
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.x);
        out.writeShort(this.y);
        out.writeInt(this.z);
        out.writeByte(this.type.ordinal() + 1);
        NetUtil.writeNBT(out, this.nbt);
    }

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