package net.mcthunder.packet.essentials;

import net.mcthunder.game.essentials.Chunk;

public class ParsedChunkData {
    private Chunk[] chunks;
    private byte[] biomes;

    public ParsedChunkData(Chunk[] chunks, byte[] biomes) {
        this.chunks = chunks;
        this.biomes = biomes;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public byte[] getBiomes() {
        return this.biomes;
    }
}