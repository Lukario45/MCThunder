package net.mcthunder.world;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
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
    private Difficulty difficulty;
    private HashMap<Long, Region> regionHashMap;
    private HashMap<Long, Column> columnHashMap;
    private WorldType worldType;

    public World(String name) {
        this.name = name;
        this.world = new File("worlds/" + name + "/level.dat");
        //this.chunks = chunks;
        this.columnHashMap = new HashMap<>();
        this.regionHashMap = new HashMap<>();
        try {
            CompoundTag tag = NBTIO.readFile(world);
            CompoundTag data = tag.get("Data");
            IntTag xTag = data.get("SpawnX");
            IntTag yTag = data.get("SpawnY");
            IntTag zTag = data.get("SpawnZ");
            IntTag dif = data.get("Difficulty");
            //tellConsole(LoggingLevel.DEBUG, String.valueOf(xTag.getValue()) + String.valueOf(yTag.getValue()) + String.valueOf(zTag.getValue()));
            this.worldType = fromName(data.get("generatorName").getValue().toString());
            this.seed = (long) data.get("RandomSeed").getValue();
            this.difficulty = difFromName(dif != null ? dif.getValue() : 2);
            this.spawn = new Location(this, xTag.getValue(), yTag.getValue(), zTag.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WorldType fromName(String name) {
        if (name.equals("largeBiomes"))
            return WorldType.LARGE_BIOMES;
        return WorldType.valueOf(name.toUpperCase());
    }

    private Difficulty difFromName(int dif) {
        if (dif == 0)
            return Difficulty.PEACEFUL;
        if (dif == 1)
            return Difficulty.EASY;
        if (dif == 2)
            return Difficulty.NORMAL;
        if (dif == 3)
            return Difficulty.HARD;
        return Difficulty.NORMAL;
    }

    public String getName() {
        return this.name;
    }

    public void addAllRegions() {
        File[] files = new File("worlds/" + this.name + "/region/").listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".mca")) {
                String[] regionName = f.getName().split("\\.");
                addRegion(getLong(Integer.parseInt(regionName[1]), Integer.parseInt(regionName[2])));
            } else {

            }
        }


    }

    public boolean checkRegion(Long l) {//Why does this not just return the first if statements value
        if (this.regionHashMap.containsKey(l)) {
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
                if (this.regionHashMap.containsKey(reg))
                    this.regionHashMap.get(reg).readChunk(getLong(x + xAdd, z + zAdd));
        }
    }

    public long getSeed() {
        return this.seed;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public void addRegion(long l) {
        this.regionHashMap.put(l, new Region(this, l));
    }

    public void addColumn(Column c) {
        this.columnHashMap.put(getLong(c.getX(), c.getZ()), c);
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
        return this.columnHashMap.containsKey(l);
    }

    public Column[] getAllColumnsAsArray() {
        Column[] cArray = new Column[this.columnHashMap.size()];
        int i = 0;
        for (Column c : this.columnHashMap.values()) {
            cArray[i] = c;
            i++;
        }
        return cArray;
    }

    public Column getColumn(long l) {
        return this.columnHashMap.get(l);
    }

    public Region getRegion(long l) {
        return this.regionHashMap.get(l);
    }

    public void loadWorld() {
        addAllRegions();
        loadAround(this.spawn, 9);
        tellConsole(LoggingLevel.INFO, "FNNISHED LOADING WORLD");

    }

    public void unloadWorld() {

    }

    public Location getSpawnLocation() {
        return this.spawn;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }
}