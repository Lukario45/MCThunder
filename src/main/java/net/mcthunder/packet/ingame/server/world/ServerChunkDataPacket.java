package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.game.essentials.Chunk;
import net.mcthunder.packet.essentials.*;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ServerChunkDataPacket
        implements Packet {
    private int x;
    private int z;
    private Chunk[] chunks;
    private byte[] biomeData;

    private ServerChunkDataPacket() {
    }

    public ServerChunkDataPacket(int x, int z) {
        this(x, z, new Chunk[16], new byte[256]);
    }

    public ServerChunkDataPacket(int x, int z, Chunk[] chunks) {
        this(x, z, chunks, null);
    }

    public ServerChunkDataPacket(int x, int z, Chunk[] chunks, byte[] biomeData) {
        if (chunks.length != 16) {
            throw new IllegalArgumentException("Chunks length must be 16.");
        }

        boolean noSkylight = false;
        boolean skylight = false;
        for (int index = 0; index < chunks.length; index++) {
            if (chunks[index] != null) {
                if (chunks[index].getSkyLight() == null)
                    noSkylight = true;
                else {
                    skylight = true;
                }
            }
        }

        if ((noSkylight) && (skylight)) {
            throw new IllegalArgumentException("Either all chunks must have skylight values or none must have them.");
        }

        this.x = x;
        this.z = z;
        this.chunks = chunks;
        this.biomeData = biomeData;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public byte[] getBiomeData() {
        return this.biomeData;
    }

    public boolean isFullChunk() {
        return this.biomeData != null;
    }

    public void read(NetIn in)
            throws IOException {
        this.x = in.readInt();
        this.z = in.readInt();
        boolean fullChunk = in.readBoolean();
        int chunkMask = in.readShort();
        int extendedChunkMask = in.readShort();
        byte[] deflated = in.readBytes(in.readInt());

        int chunkCount = 0;
        for (int count = 0; count < 16; count++) {
            chunkCount += (chunkMask >> count & 0x1);
        }

        int len = 12288 * chunkCount;
        if (fullChunk) {
            len += 256;
        }

        byte[] data = new byte[len];

        Inflater inflater = new Inflater();
        inflater.setInput(deflated, 0, deflated.length);
        try {
            inflater.inflate(data);
        } catch (DataFormatException e) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }

        ParsedChunkData chunkData = NetUtil.dataToChunks(new NetworkChunkData(chunkMask, extendedChunkMask, fullChunk, false, data));
        this.chunks = chunkData.getChunks();
        this.biomeData = chunkData.getBiomes();
    }

    public void write(NetOut out)
            throws IOException {
        NetworkChunkData data = NetUtil.chunksToData(new ParsedChunkData(this.chunks, this.biomeData));

        Deflater deflater = new Deflater(-1);
        byte[] deflated = new byte[data.getData().length];
        int len = data.getData().length;
        try {
            deflater.setInput(data.getData(), 0, data.getData().length);
            deflater.finish();
            len = deflater.deflate(deflated);
        } finally {
            deflater.end();
        }

        out.writeInt(this.x);
        out.writeInt(this.z);
        out.writeBoolean(data.isFullChunk());
        out.writeShort(data.getMask());
        out.writeShort(data.getExtendedMask());
        out.writeInt(len);
        out.writeBytes(deflated, len);
    }

    public boolean isPriority() {
        return false;
    }
}