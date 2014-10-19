package net.mcthunder.api;

import org.spacehq.mc.protocol.data.game.Chunk;

/**
 * Created by Kevin on 10/19/2014.
 */
public class World {
    private String name;
    private long seed;
    private Chunk[] chunks;

    public World(String name, long seed, Chunk[] chunks) {
        this.name = name;
        this.seed = seed;
        this.chunks = chunks;
    }

    public String getName() {
        return this.name;
    }

    public long getSeed() {
        return this.seed;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public void loadWorld() {

    }

    public void unloadWorld() {

    }
}
