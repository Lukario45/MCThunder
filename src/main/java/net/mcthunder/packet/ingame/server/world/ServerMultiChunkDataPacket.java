package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.game.essentials.Chunk;
import net.mcthunder.packet.essentials.*;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ServerMultiChunkDataPacket
        implements Packet {
    private int[] x;
    private int[] z;
    private Chunk[][] chunks;
    private byte[][] biomeData;

    private ServerMultiChunkDataPacket() {
    }

    public ServerMultiChunkDataPacket(int[] x, int[] z, Chunk[][] chunks, byte[][] biomeData) {
        if (biomeData == null) {
            throw new IllegalArgumentException("BiomeData cannot be null.");
        }

        if ((x.length != chunks.length) || (z.length != chunks.length)) {
            throw new IllegalArgumentException("X, Z, and Chunk arrays must be equal in length.");
        }

        boolean noSkylight = false;
        boolean skylight = false;
        for (int index = 0; index < chunks.length; index++) {
            Chunk[] column = chunks[index];
            if (column.length != 16) {
                throw new IllegalArgumentException("Chunk columns must contain 16 chunks each.");
            }

            for (int y = 0; y < column.length; y++) {
                if (column[y] != null) {
                    if (column[y].getSkyLight() == null)
                        noSkylight = true;
                    else {
                        skylight = true;
                    }
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

    public int getColumns() {
        return this.chunks.length;
    }

    public int getX(int column) {
        return this.x[column];
    }

    public int getZ(int column) {
        return this.z[column];
    }

    public Chunk[] getChunks(int column) {
        return this.chunks[column];
    }

    public byte[] getBiomeData(int column) {
        return this.biomeData[column];
    }

    public void read(NetIn in)
            throws IOException {
        short columns = in.readShort();
        int deflatedLength = in.readInt();
        boolean skylight = in.readBoolean();
        byte[] deflatedBytes = in.readBytes(deflatedLength);

        byte[] inflated = new byte[196864 * columns];
        Inflater inflater = new Inflater();
        inflater.setInput(deflatedBytes, 0, deflatedLength);
        try {
            inflater.inflate(inflated);
        } catch (DataFormatException e) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }

        this.x = new int[columns];
        this.z = new int[columns];
        this.chunks = new Chunk[columns][];
        this.biomeData = new byte[columns][];

        int pos = 0;
        for (int count = 0; count < columns; count++) {
            int x = in.readInt();
            int z = in.readInt();
            int chunkMask = in.readShort();
            int extendedChunkMask = in.readShort();

            int chunks = 0;
            int extended = 0;
            for (int ch = 0; ch < 16; ch++) {
                chunks += (chunkMask >> ch & 0x1);
                extended += (extendedChunkMask >> ch & 0x1);
            }

            int length = 8192 * chunks + 256 + 2048 * extended;
            if (skylight) {
                length += 2048 * chunks;
            }

            byte[] dat = new byte[length];
            System.arraycopy(inflated, pos, dat, 0, length);

            ParsedChunkData chunkData = NetUtil.dataToChunks(new NetworkChunkData(chunkMask, extendedChunkMask, true, skylight, dat));
            this.x[count] = x;
            this.z[count] = z;
            this.chunks[count] = chunkData.getChunks();
            this.biomeData[count] = chunkData.getBiomes();
            pos += length;
        }
    }

    public void write(NetOut out)
            throws IOException {
        int[] chunkMask = new int[this.chunks.length];
        int[] extendedChunkMask = new int[this.chunks.length];

        int pos = 0;
        byte[] bytes = new byte[0];
        boolean skylight = false;
        for (int count = 0; count < this.chunks.length; count++) {
            Chunk[] column = this.chunks[count];

            NetworkChunkData netData = NetUtil.chunksToData(new ParsedChunkData(column, this.biomeData[count]));
            if (bytes.length < pos + netData.getData().length) {
                byte[] newArray = new byte[pos + netData.getData().length];
                System.arraycopy(bytes, 0, newArray, 0, bytes.length);
                bytes = newArray;
            }

            if (netData.hasSkyLight()) {
                skylight = true;
            }

            System.arraycopy(netData.getData(), 0, bytes, pos, netData.getData().length);
            pos += netData.getData().length;

            chunkMask[count] = netData.getMask();
            extendedChunkMask[count] = netData.getExtendedMask();
        }

        Deflater deflater = new Deflater(-1);
        byte[] deflatedData = new byte[pos];
        int deflatedLength = pos;
        try {
            deflater.setInput(bytes, 0, pos);
            deflater.finish();
            deflatedLength = deflater.deflate(deflatedData);
        } finally {
            deflater.end();
        }

        out.writeShort(this.chunks.length);
        out.writeInt(deflatedLength);
        out.writeBoolean(skylight);
        out.writeBytes(deflatedData, deflatedLength);
        for (int count = 0; count < this.chunks.length; count++) {
            out.writeInt(this.x[count]);
            out.writeInt(this.z[count]);
            out.writeShort((short) (chunkMask[count] & 0xFFFF));
            out.writeShort((short) (extendedChunkMask[count] & 0xFFFF));
        }
    }

    public boolean isPriority() {
        return false;
    }
}