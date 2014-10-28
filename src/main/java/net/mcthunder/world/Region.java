package net.mcthunder.world;

import net.mcthunder.api.Direction;
import net.mcthunder.api.Player;
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

/**
 * Created by Kevin on 10/21/2014.
 */
public class Region {
    private int x;
    private int z;
    private Chunk[] chunks;
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
        if (world.isColumnLoaded(l))
            return;
        int x = (int) (l >> 32);
        int z = (int) l;
        while (x < 0)
            x += 32;
        while (z < 0)
            z += 32;
        if (x > 32 || z > 32)
            return;
        File region = new File("worlds/" + world.getName() + "/region/r." + this.x + "." + this.z + ".mca");
        RegionFile regionFile = new RegionFile(region);
        DataInputStream in = regionFile.getChunkDataInputStream(x, z);
        if (in == null) {//Chunk needs to be created

        } else {
            Tag tag = null;
            try {
                tag = NBTIO.readTag(regionFile.getChunkDataInputStream(x, z));
                //tellConsole(LoggingLevel.DEBUG, "Ran " + x + " " + z);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] light = new byte[4096];
            Arrays.fill(light, (byte) 15);
            chunks = new Chunk[16];
            CompoundTag compoundTag = (CompoundTag) tag;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            IntTag xPosTag = level.get("xPos");
            IntTag zPosTag = level.get("zPos");
            int xPos = xPosTag.getValue();
            int zPos = zPosTag.getValue();
            chunkInt = 0;
            CompoundTag chunkz;
            ByteArrayTag blocks;
            ByteArrayTag blockLight;
            ByteArrayTag skyLight;
            ByteArrayTag biomes = level.get("Biomes");
            int counterRan = 0;

            for (int i = 0; i < sections.size(); i++) { //Loop through all 16 chunks (verticle fashion
                chunkz = sections.get(chunkInt);
                blocks = chunkz.get("Blocks");
                blockLight = chunkz.get("BlockLight");
                skyLight = chunkz.get("SkyLight");
                ShortArray3d block = new ShortArray3d(4096);
                NibbleArray3d blocklight = new NibbleArray3d(4096); //Create our blocklight array
                NibbleArray3d skylight = new NibbleArray3d(4096); //Create our skylight array
                int spot = 0;
                int ran = 0;
                for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                    for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                        for (int cX = 0; cX < 16; cX++) { //Loop through x
                            if (ran == 2048)
                                ran = 0;
                            //tellConsole(LoggingLevel.DEBUG,"Run " + ran );
                            block.setBlock(cX, cY, cZ, blocks.getValue(spot));
                            blocklight.set(cX, cY, cZ, blockLight.getValue(ran));
                            //blocklight.fill(blockLight.getValue(spot));
                            skylight.set(cX, cY, cZ, skyLight.getValue(ran));
                            ran++;

                            counterRan++;
                            spot++;
                        }
                Chunk chunk = new Chunk(block, blocklight, skylight);
                chunks[i] = chunk;
                chunkInt++;
                //tellConsole(LoggingLevel.DEBUG, "RAN " + counterRan + " CHUNK INT " + chunkInt);
                counterRan = 0;
            }
            Column c = new Column(getLong(xPos, zPos), chunks, biomes.getValue());
            world.addColumn(c);
            //tellConsole(LoggingLevel.DEBUG, "CONFIRM " + xPos + " " + zPos);
        }
    }

    public void readChunk(long l, Player p, Direction dir, boolean removeOld) {
        if (p.isColumnLoaded(l) && !removeOld)
            return;
        if (world.isColumnLoaded(l)) {
            p.addColumn(l, dir, removeOld);
            return;
        }
        int x = (int) (l >> 32);
        int z = (int) l;
        while (x < 0)
            x += 32;
        while (z < 0)
            z += 32;
        if (x > 32 || z > 32)
            return;
        File region = new File("worlds/" + world.getName() + "/region/r." + this.x + "." + this.z + ".mca");
        RegionFile regionFile = new RegionFile(region);
        DataInputStream in = regionFile.getChunkDataInputStream(x, z);
        if (in == null) {//Chunk needs to be created

        } else {
            Tag tag = null;
            try {
                tag = NBTIO.readTag(regionFile.getChunkDataInputStream(x, z));
                //tellConsole(LoggingLevel.DEBUG, "Ran " + x + " " + z);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] light = new byte[4096];
            Arrays.fill(light, (byte) 15);
            chunks = new Chunk[16];
            CompoundTag compoundTag = (CompoundTag) tag;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            IntTag xPosTag = level.get("xPos");
            IntTag zPosTag = level.get("zPos");
            int xPos = xPosTag.getValue();
            int zPos = zPosTag.getValue();
            chunkInt = 0;
            CompoundTag chunkz;
            ByteArrayTag blocks;
            ByteArrayTag blockLight;
            ByteArrayTag skyLight;
            ByteArrayTag biomes = level.get("Biomes");
            int counterRan = 0;

            for (int i = 0; i < sections.size(); i++) { //Loop through all 16 chunks (verticle fashion
                chunkz = sections.get(chunkInt);
                blocks = chunkz.get("Blocks");
                blockLight = chunkz.get("BlockLight");
                skyLight = chunkz.get("SkyLight");
                ShortArray3d block = new ShortArray3d(4096);
                NibbleArray3d blocklight = new NibbleArray3d(4096); //Create our blocklight array
                NibbleArray3d skylight = new NibbleArray3d(4096); //Create our skylight array
                int spot = 0;
                int ran = 0;
                for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                    for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                        for (int cX = 0; cX < 16; cX++) { //Loop through x
                            if (ran == 2048)
                                ran = 0;
                            //tellConsole(LoggingLevel.DEBUG,"Run " + ran );
                            block.setBlock(cX, cY, cZ, blocks.getValue(spot));
                            blocklight.set(cX, cY, cZ, blockLight.getValue(ran));
                            //blocklight.fill(blockLight.getValue(spot));
                            skylight.set(cX, cY, cZ, skyLight.getValue(ran));
                            ran++;

                            counterRan++;
                            spot++;
                        }
                Chunk chunk = new Chunk(block, blocklight, skylight);
                chunks[i] = chunk;
                chunkInt++;
                //tellConsole(LoggingLevel.DEBUG, "RAN " + counterRan + " CHUNK INT " + chunkInt);
                counterRan = 0;
            }
            Column c = new Column(getLong(xPos, zPos), chunks, biomes.getValue());
            if (!world.isColumnLoaded(getLong(xPos, zPos))) {
                world.addColumn(c);
                p.addColumn(getLong(xPos, zPos), dir, removeOld);
            }
            //tellConsole(LoggingLevel.DEBUG, "CONFIRM " + xPos + " " + zPos);
        }
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}