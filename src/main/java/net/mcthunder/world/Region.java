package net.mcthunder.world;

import net.mcthunder.api.Direction;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 10/21/2014.
 */
public class Region {
    private int x;
    private int z;
    private World world;
    private RegionFile regionFile = null;

    public Region(World w, long region) {
        this.x = (int) (region >> 32);
        this.z = (int) region;
        this.world = w;
        File f = new File("worlds/" + this.world.getName() + "/region/r." + this.x + "." + this.z + ".mca");
        if (!f.exists()) {
            //Create the region file
        }
        this.regionFile = new RegionFile(f);
    }

    public void saveChunk(long l) {
        //TODO: actually save chunk
        int x = (int) (l >> 32);
        int z = (int) l;
        while (x < 0)
            x += 32;
        while (x > 32)
            x -= 32;
        while (z < 0)
            z += 32;
        while (z > 32)
            z -= 32;
        if (x > 32 || z > 32 || x < 0 || z < 0)
            return;
        DataOutputStream out = regionFile.getChunkDataOutputStream(x, z);
        if(out != null) {
            DataInputStream in = this.regionFile.getChunkDataInputStream(x, z);
            Tag tag = null;
            if (in != null) {
                try {
                    tag = NBTIO.readTag(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CompoundTag compoundTag = (CompoundTag) tag;
                if (compoundTag == null)
                    return;
                CompoundTag level = compoundTag.get("Level");
                ListTag sections = level.get("Sections");
                //ByteArrayTag biomes = level.get("Biomes");
                Column c = this.world.getColumn(l);
                Chunk[] chunks = c.getChunks();
                Map<String,Tag> values = compoundTag.getValue();
                Map<String,Tag> levelInfo = level.getValue();
                ArrayList<Tag> newSections = new ArrayList<>();
                for (int i = 0; i < chunks.length; i++) {//Loop through all 16 chunks in a verticle fashion
                    CompoundTag chunkz = sections.get(i);
                    ByteArrayTag blocks = chunkz.get("Blocks");
                    ByteArrayTag blockLight = chunkz.get("BlockLight");
                    ByteArrayTag skyLight = chunkz.get("SkyLight");
                    ByteArrayTag data = chunkz.get("Data");
                    //ByteArrayTag add = chunkz.get("Add");
                    for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                        for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                            for (int cX = 0; cX < 16; cX++) { //Loop through x
                                int index = 256*cY + 16*cZ + cX;
                                blocks.setValue(index, (byte) chunks[i].getBlocks().getBlock(cX, cY, cZ));
                                data.setValue(index/2, (byte) chunks[i].getBlocks().getData(cX, cY, cZ));
                                blockLight.setValue(index/2, (byte) chunks[i].getBlockLight().get(cX, cY, cZ));
                                skyLight.setValue(index/2, (byte) chunks[i].getSkyLight().get(cX, cY, cZ));
                            }
                    Map<String,Tag> cv = chunkz.getValue();
                    cv.put("Blocks", blocks);
                    cv.put("BlockLight", blockLight);
                    cv.put("SkyLight", skyLight);
                    cv.put("Data", data);
                    //cv.put("Add", add);
                    chunkz.setValue(cv);
                    newSections.add(chunkz);
                }
                sections.setValue(newSections);
                levelInfo.put("Sections", sections);
                level.setValue(levelInfo);
                values.put("Level", level);
                compoundTag.setValue(values);
                try {
                    NBTIO.writeTag(out, compoundTag);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readChunk(long l) {
        if (world.isColumnLoaded(l))
            return;
        int x = (int) (l >> 32);
        int z = (int) l;
        while (x < 0)
            x += 32;
        while (x > 32)
            x -= 32;
        while (z < 0)
            z += 32;
        while (z > 32)
            z -= 32;
        if (x > 32 || z > 32 || x < 0 || z < 0)
            return;
        DataInputStream in = this.regionFile.getChunkDataInputStream(x, z);
        if (in == null) {//Chunk needs to be created or is corrupted and should be regenerated

        } else {
            Tag tag = null;
            try {
                tag = NBTIO.readTag(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CompoundTag compoundTag = (CompoundTag) tag;
            if (compoundTag == null)
                return;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            ByteArrayTag biomes = level.get("Biomes");
            Chunk[] chunks = new Chunk[16];
            for (int i = 0; i < sections.size(); i++) {//Loop through all 16 chunks in a verticle fashion
                CompoundTag chunkz = sections.get(i);
                ByteArrayTag blocks = chunkz.get("Blocks");
                ByteArrayTag blockLight = chunkz.get("BlockLight");
                ByteArrayTag skyLight = chunkz.get("SkyLight");
                ByteArrayTag data = chunkz.get("Data");
                ByteArrayTag add = chunkz.get("Add");
                ShortArray3d block = new ShortArray3d(4096);
                NibbleArray3d blocklight = new NibbleArray3d(4096);
                NibbleArray3d skylight = new NibbleArray3d(4096);
                for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                    for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                        for (int cX = 0; cX < 16; cX++) { //Loop through x
                            int index = 256*cY + 16*cZ + cX;
                            block.setBlockAndData(cX, cY, cZ, blocks.getValue(index) + (add != null ? getValue(add, index) << 8 : 0), getValue(data, index));
                            blocklight.set(cX, cY, cZ, getValue(blockLight, index));
                            skylight.set(cX, cY, cZ, getValue(skyLight, index));
                        }
                chunks[i] = new Chunk(block, blocklight, skylight);
            }
            this.world.addColumn(new Column(l, chunks, biomes.getValue()));
        }
    }

    public void readChunk(long l, Player p, Direction dir, boolean removeOld) {
        if (p.isColumnLoaded(l) && !removeOld)
            return;
        if (world.isColumnLoaded(l)) {
            p.addColumn(l, dir, removeOld);
            return;
        }
        readChunk(l);
        if (world.isColumnLoaded(l))
            p.addColumn(l, dir, removeOld);
    }

    private int getValue(ByteArrayTag array, int index) {//From pseudo-code on minecraft wiki Chunk Format page
        return (index%2 == 0 ? array.getValue(index/2) : array.getValue(index/2) >> 4)&0x0F;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}