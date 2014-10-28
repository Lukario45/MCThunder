package net.mcthunder.world;

import org.spacehq.mc.protocol.data.game.Chunk;

/**
 * Created by Kevin on 10/21/2014.
 */
public class Column {
    private byte[] biomeData;
    private Chunk[] chunks;
    private int x;
    private int z;

    public Column(long XZ, Chunk[] chunks, byte[] biomeData) {
        this.x = (int) (XZ >> 32);
        this.z = (int) XZ;
        this.chunks = chunks;
        this.biomeData = biomeData;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public byte[] getBiomes() {
        return this.biomeData;
    }
}