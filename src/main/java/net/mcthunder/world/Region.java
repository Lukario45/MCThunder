package net.mcthunder.world;

import net.mcthunder.api.LoggingLevel;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.ShortArray3d;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static net.mcthunder.api.Utils.getLong;
import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/21/2014.
 */
public class Region {
    private int x;
    private int z;
    private Chunk[] chunks;
    private Position spawnPosition;
    private int chunkInt;
    private HashMap<Long, Region> regionHashMap;
    private World world;


    public Region(World w, long region) {
        this.x = (int) (region >> 32);
        this.z = (int) region;
        this.world = w;

    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public void readChunk(long l) {
        File region = new File("worlds/" + world.getName() + "/region/r." + x + "." + z + ".mca");
        RegionFile regionFile = new RegionFile(region);
        int x = (int) (l >> 32);
        int z = (int) l;
        while (x < 0)
            x += 32;
        while (z < 0)
            z += 32;
        if (x > 32 || z > 32)
            return;
        byte[] light = new byte[4096];
        Arrays.fill(light, (byte) 15);
        Tag tag = null;
        chunks = new Chunk[16];
        DataInputStream in = regionFile.getChunkDataInputStream(x, z);
        if (in == null) {//Chunk needs to be created

        } else {
            try {
                tag = NBTIO.readTag(regionFile.getChunkDataInputStream(x, z));
                //tellConsole(LoggingLevel.DEBUG, "Ran " + x + " " + z);
            } catch (IOException e) {
                e.printStackTrace();
            }

            CompoundTag compoundTag = (CompoundTag) tag;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            chunkInt = 0;
            CompoundTag chunkz;
            ByteArrayTag blocks;
            int counterRan = 0;

            for (int i = 0; i < sections.size(); i++) { //Loop through all 16 chunks (verticle fashion
                chunkz = sections.get(chunkInt);
                blocks = chunkz.get("Blocks");
                ShortArray3d block = new ShortArray3d(4096);
                NibbleArray3d blocklight = new NibbleArray3d(light); //Create our blocklight array
                NibbleArray3d skylight = new NibbleArray3d(light); //Create our skylight array
                int spot = 0;
                for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                    for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                        for (int cX = 0; cX < 16; cX++) { //Loop through x
                            block.setBlock(cX, cY, cZ, blocks.getValue(spot));
                            counterRan++;
                            spot++;
                        }
                Chunk chunk = new Chunk(block, blocklight, skylight);
                chunks[i] = chunk;
                chunkInt++;
                //tellConsole(LoggingLevel.DEBUG, "RAN " + counterRan + " CHUNK INT " + chunkInt);
                counterRan = 0;
            }
            IntTag xPosTag = level.get("xPos");
            IntTag zPosTag = level.get("zPos");
            int xPos = xPosTag.getValue();
            int zPos = zPosTag.getValue();
            Column c = new Column(getLong(xPos, zPos), chunks);
            world.addColumn(c);
            //tellConsole(LoggingLevel.DEBUG, "CONFIRM " + xPos + " " + zPos);
        }
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public void loadRegion() {
        File region = new File("worlds/" + world.getName() + "/region/r." + x + "." + z + ".mca");
        if (region.exists()) {
            int chunkX = 0;
            int chunkZ = 0;
            RegionFile regionFile = new RegionFile(region);
            byte[] light = new byte[4096]; //Create a light array of bytes (actually nibbles) (should this be 2048)
            Arrays.fill(light, (byte) 15); //

            Tag tag = null;
            boolean readAllChunks = false;
            while (!readAllChunks) {
                chunks = new Chunk[16];
                DataInputStream in = regionFile.getChunkDataInputStream(chunkX, chunkZ);
                if (in == null) {

                } else {
                    try {
                        tag = NBTIO.readTag(regionFile.getChunkDataInputStream(chunkX, chunkZ));
                        //tellConsole(LoggingLevel.DEBUG, "Ran " + chunkX + " " + chunkZ);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    CompoundTag compoundTag = (CompoundTag) tag;
                    CompoundTag level = compoundTag.get("Level");
                    ListTag sections = level.get("Sections");
                    chunkInt = 0;
                    CompoundTag chunkz;
                    ByteArrayTag blocks;
                    int counterRan = 0;
                    for (int i = 0; i < sections.size(); i++) { //Loop through all 16 chunks (verticle fashion
                        chunkz = sections.get(chunkInt);
                        blocks = chunkz.get("Blocks");
                        ShortArray3d block = new ShortArray3d(4096);
                        NibbleArray3d blocklight = new NibbleArray3d(light); //Create our blocklight array
                        NibbleArray3d skylight = new NibbleArray3d(light); //Create our skylight array
                        int spot = 0;
                        for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                            for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                                for (int cX = 0; cX < 16; cX++) { //Loop through x
                                    block.setBlock(cX, cY, cZ, blocks.getValue(spot));
                                    counterRan++;
                                    spot++;
                                }
                        Chunk chunk = new Chunk(block, blocklight, skylight);
                        chunks[i] = chunk;
                        chunkInt++;
                        //tellConsole(LoggingLevel.DEBUG, "RAN " + counterRan + " CHUNK INT " + chunkInt);
                        counterRan = 0;
                    }
                    IntTag xPosTag = level.get("xPos");
                    IntTag zPosTag = level.get("zPos");
                    int xPos = xPosTag.getValue();
                    int zPos = zPosTag.getValue();
                    Column c = new Column(getLong(xPos, zPos), chunks);
                    world.addColumn(c);
                    tellConsole(LoggingLevel.DEBUG, "CONFIRM " + xPos + " " + zPos);
                }
                if (chunkZ == 31 && chunkX == 31)
                    readAllChunks = true;

                if (chunkX == 31) {
                    chunkX = 0;
                    chunkZ++;
                } else
                    chunkX++;
            }
            //tellConsole(LoggingLevel.DEBUG, "FINNISHED");
        }
    }
}