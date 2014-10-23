package net.mcthunder.api;

import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static net.mcthunder.api.Utils.getLong;
import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/19/2014.
 */
public class World {
    private File world;
    private String name;
    private long seed;
    private Chunk[] chunks;
    private Position spawnPosition;
    private int chunkInt;
    private HashMap<Long, Region> regionHashMap;
    private HashMap<Long, Column> columnHashMap;


    public World(String name/*, long seed*/) {
        this.name = name;
        //this.seed = seed;
        world = new File("worlds/" + name + "/level.dat");
        //this.chunks = chunks;
        columnHashMap = new HashMap<Long, Column>();
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

    public void addRegion(long l) {
        regionHashMap.put(l, new Region(this, l));
    }

    public void addColumn(Column c) {
        long l = getLong(c.getX(), c.getZ());
        columnHashMap.put(l, c);
        tellConsole(LoggingLevel.DEBUG, "NEW CHUNK TO COLUMN");
    }

    public Column[] getAllColumnsAsArray() {
        Column[] cArray = new Column[columnHashMap.size()];
        int i = 0;
        for (Column c : columnHashMap.values()) {
            cArray[i] = c;

            i++;
        }
        return cArray;
    }

    public Region getRegion(long l) {
        Region r = regionHashMap.get(l);
        return r;
    }


    public void loadWorld() throws IOException {

    }
    public void unloadWorld() {

    }

    public void sendColumns(Player p, Column[] columns) {
        for (Column c : columns) {
            ServerChunkDataPacket packet = new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks(), new byte[256]);
            p.getSession().send(packet);
        }
    }
}
