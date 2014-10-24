package net.mcthunder.world;

import net.mcthunder.api.Location;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.Position;
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
    private Position spawnPosition;
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
            spawnPosition = new Position(xTag.getValue(), yTag.getValue(), zTag.getValue());

        } catch (IOException e) {
            e.printStackTrace();
        }
        spawn = new Location(this, spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());

        loadAround(spawnPosition, 9);
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

    public boolean checkRegion(Long l) {
        if (regionHashMap.containsKey(l)) {
            return true;
        } else {
            return false;
        }
    }

    public void loadAround(Position p, int distance) {
        /**int chunkX = p.getX() >> 4;
         int chunkZ = p.getZ() >>4;
         /*for(int xAdd = -distance; xAdd < distance; xAdd++)
            for(int zAdd = -distance; zAdd < distance; zAdd++) {
                File temp = new File("worlds/" + name + "/region/r." + (x + xAdd) + "." + (z + zAdd) + ".mca");
                if (temp.exists()) {
                    tellConsole(LoggingLevel.DEBUG, "x: " + (x + xAdd) + ", z: " + (z + zAdd));
                    addRegion(getLong(x + xAdd, z + zAdd));
                } else {//Create the chunk

                }
         }*//**

         //Check if regions are loaded
         int[] data = new int[4];
         data[0] = chunkX + distance;//topX
         data [1] = chunkX - distance;//bottomX
         data [2] = chunkZ + distance;//topZ
         data [3] = chunkZ - distance;//bottomZ
         int i = 0;
         Region[] regions = new Region[4];
         for (int d: data){

         //for (int i = 0; i==3;i++ ){
         tellConsole(LoggingLevel.DEBUG, String.valueOf(i));
         if (i <= 1 ){
         if (!checkRegion(getLong(d >> 5,chunkZ >> 5))){
         addRegion(getLong(d >> 5,chunkZ >> 5));
         regions[i] =regionHashMap.get(getLong(d >> 5,chunkZ >> 5));
         } else {
         regions[i] = regionHashMap.get(getLong(d >> 5,chunkZ >> 5));
         }

         } else if (i >= 2){
         if (!checkRegion(getLong(chunkX,d))){
         addRegion(getLong(chunkX,d));
         regions[i] = regionHashMap.get(getLong(chunkX >> 5, d >> 5));
         } else {
         regions[i] = regionHashMap.get(getLong(chunkX >> 5, d >> 5));
         }
            }
         i++;
         regions[0].readChunk(getLong(chunkX,chunkZ));
         } for (Region r: regions){
         boolean readingChunks = true;
         while(readingChunks){


         }
         }

         */
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
        //tellConsole(LoggingLevel.DEBUG, "NEW CHUNK TO COLUMN");
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

    public void loadAllRegions() {
        for (Region r : regionHashMap.values()) {
            //r.loadRegion();
        }
    }


    public void loadWorld() {
        addAllRegions();
        loadAllRegions();
        tellConsole(LoggingLevel.INFO, "FNNISHED LOADING WORLD");

    }
    public void unloadWorld() {

    }

    public Position getSpawnPosition() {
        return this.spawnPosition;
    }

    public void sendColumns(Player p, Column[] columns) {
        for (Column c : columns) {
            ServerChunkDataPacket packet = new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks(), new byte[256]);
            p.getSession().send(packet);
        }
    }
}