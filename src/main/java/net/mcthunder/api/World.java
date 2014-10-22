package net.mcthunder.api;

import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.ShortArray3d;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.ByteArrayTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.opennbt.tag.builtin.Tag;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

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

    public World(String name/*, long seed*/) {
        this.name = name;
        //this.seed = seed;
        world = new File("worlds/" + name + "/level.dat");
        //this.chunks = chunks;
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
        regionHashMap.put(l, new Region(l));
    }

    public Region getRegion(long l) {
        Region r = regionHashMap.get(l);
        return r;
    }


    public void loadWorld() throws IOException {
        File region = new File("worlds/" + name + "/region/r.0.0.mca");
        if (region.exists()) {
            RegionFile regionFile = new RegionFile(region);
            byte[] light = new byte[4096]; //Create a light array of bytes (actually nibbles) (should this be 2048)
            Arrays.fill(light, (byte) 15); //
            chunks = new Chunk[16];
            Tag tag = NBTIO.readTag(regionFile.getChunkDataInputStream(0, 0));
            CompoundTag compoundTag = (CompoundTag) tag;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            chunkInt = 0;
            CompoundTag chunkz;
            //tellConsole(LoggingLevel.DEBUG,"Got Chunk " + chunkInt );
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
                tellConsole(LoggingLevel.DEBUG, "RAN " + counterRan + " CHUNK INT " + chunkInt);
                counterRan = 0;
            }
        }
    }
    public void unloadWorld() {

    }
}
