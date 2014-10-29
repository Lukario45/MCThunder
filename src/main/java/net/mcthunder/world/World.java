package net.mcthunder.world;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;

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
    private Location spawn;
    private int chunkInt;
    private HashMap<Long, Region> regionHashMap;
    private HashMap<Long, Column> columnHashMap;


    public World(String name/*, long seed*/) {
        this.name = name;
        //this.seed = seed;
        world = new File("worlds/" + name + "/level.dat");
        //this.chunks = chunks;
        columnHashMap = new HashMap<Long, Column>();
        regionHashMap = new HashMap<Long, Region>();
        try {
            CompoundTag tag = NBTIO.readFile(world);
            CompoundTag data = tag.get("Data");
            IntTag xTag = data.get("SpawnX");
            IntTag yTag = data.get("SpawnY");
            IntTag zTag = data.get("SpawnZ");
            tellConsole(LoggingLevel.DEBUG, String.valueOf(xTag.getValue()) + String.valueOf(yTag.getValue()) + String.valueOf(zTag.getValue()));
            spawn = new Location(this, xTag.getValue(), yTag.getValue(), zTag.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public void addAllRegions() {
        File regions = new File("worlds/" + name + "/region/");
        File[] files = regions.listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".mca")) {
                String[] regionName = f.getName().split("\\.");
                int x = Integer.parseInt(regionName[1]);
                //tellConsole(LoggingLevel.DEBUG, String.valueOf(x));
                int z = Integer.parseInt(regionName[2]);
                //tellConsole(LoggingLevel.DEBUG, String.valueOf(z));
                addRegion(getLong(x, z));
            } else {

            }
        }


    }

    public boolean checkRegion(Long l) {//Why does this not just return the first if statements value
        if (regionHashMap.containsKey(l)) {
            return true;
        } else {
            return false;
        }
    }

    public void loadAround(Location loc, int distance) {
        int x = (int)loc.getX() >> 4;
        int z = (int)loc.getZ() >> 4;
        for(int xAdd = -distance; xAdd < distance; xAdd++)
            for(int zAdd = -distance; zAdd < distance; zAdd++) {
                int regionX = (x + xAdd) >> 5;
                int regionZ = (z + zAdd) >> 5;
                long reg = getLong(regionX, regionZ);
                //tellConsole(LoggingLevel.DEBUG, "rX: " + regionX + ", rZ: " + regionZ);
                if (regionHashMap.containsKey(reg)) {
                    regionHashMap.get(reg).readChunk(getLong(x + xAdd, z + zAdd));
                    //tellConsole(LoggingLevel.DEBUG, "cX: " + (x + xAdd) + ", cZ: " + (z + zAdd));
                }
        }
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
        columnHashMap.put(getLong(c.getX(), c.getZ()), c);
        //tellConsole(LoggingLevel.DEBUG, "NEW CHUNK TO COLUMN");
    }

    public void unloadColumn(Column c) {
        if (c == null)
            return;
        long l = getLong(c.getX(), c.getZ());
        for (Player p : MCThunder.playerHashMap.values())
            if (p.isColumnLoaded(l) || p.isColumnPreLoaded(l))
                return;
        //Todo: actually unload column if it passes the checks above
        //tellConsole(LoggingLevel.DEBUG, "Unloaded column x: " + c.getX() + ", z: " + c.getZ());
    }

    public void unloadColumn(long l) {
        unloadColumn(getColumn(l));
    }

    public boolean isColumnLoaded(long l) {
        return columnHashMap.containsKey(l);
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

    public Column getColumn(long l) {
        return columnHashMap.get(l);
    }

    public Region getRegion(long l) {
        return regionHashMap.get(l);
    }

    public void loadAllRegions() {
        for (Region r : regionHashMap.values()) {
            //r.loadRegion();
        }
    }


    public void loadWorld() {
        addAllRegions();
        //loadAllRegions();
        loadAround(spawn, 9);
        tellConsole(LoggingLevel.INFO, "FNNISHED LOADING WORLD");

    }

    public void unloadWorld() {

    }

    public Location getSpawnLocation() {
        return this.spawn;
    }

    public void sendColumns(Player p, Column[] columns) {
        for (Column c : columns)
            p.getSession().send(new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks(), new byte[256]));
    }
}