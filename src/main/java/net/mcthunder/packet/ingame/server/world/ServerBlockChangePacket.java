package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.game.essentials.BlockChangeRecord;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerBlockChangePacket
        implements Packet {
    private BlockChangeRecord record;

    private ServerBlockChangePacket() {
    }

    public ServerBlockChangePacket(BlockChangeRecord record) {
        this.record = record;
    }

    public BlockChangeRecord getRecord() {
        return this.record;
    }

    public void read(NetIn in) throws IOException {
        this.record = new BlockChangeRecord(in.readInt(), in.readUnsignedByte(), in.readInt(), in.readVarInt(), in.readUnsignedByte());
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.record.getX());
        out.writeByte(this.record.getY());
        out.writeInt(this.record.getZ());
        out.writeVarInt(this.record.getId());
        out.writeByte(this.record.getMetadata());
    }

    public boolean isPriority() {
        return false;
    }
}