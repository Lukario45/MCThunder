package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.game.essentials.BlockChangeRecord;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerMultiBlockChangePacket
        implements Packet {
    private BlockChangeRecord[] records;

    private ServerMultiBlockChangePacket() {
    }

    public ServerMultiBlockChangePacket(BlockChangeRecord[] records) {
        if ((records == null) || (records.length == 0)) {
            throw new IllegalArgumentException("Records must contain at least 1 value.");
        }

        this.records = records;
    }

    public BlockChangeRecord[] getRecords() {
        return this.records;
    }

    public void read(NetIn in) throws IOException {
        int chunkX = in.readInt();
        int chunkZ = in.readInt();
        this.records = new BlockChangeRecord[in.readShort()];
        in.readInt();
        for (int index = 0; index < this.records.length; index++) {
            short coords = in.readShort();
            short block = in.readShort();
            int x = (chunkX << 4) + (coords >> 12 & 0xF);
            int y = coords & 0xFF;
            int z = (chunkZ << 4) + (coords >> 8 & 0xF);
            int id = block >> 4 & 0xFFF;
            int metadata = block & 0xF;
            this.records[index] = new BlockChangeRecord(x, y, z, id, metadata);
        }
    }

    public void write(NetOut out) throws IOException {
        int chunkX = this.records[0].getX() >> 4;
        int chunkZ = this.records[0].getZ() >> 4;
        out.writeInt(chunkX);
        out.writeInt(chunkZ);
        out.writeShort(this.records.length);
        out.writeInt(this.records.length * 4);
        for (BlockChangeRecord record : this.records) {
            out.writeShort(record.getX() - (chunkX << 4) << 12 | record.getZ() - (chunkZ << 4) << 8 | record.getY());
            out.writeShort((record.getId() & 0xFFF) << 4 | record.getMetadata() & 0xF);
        }
    }

    public boolean isPriority() {
        return false;
    }
}