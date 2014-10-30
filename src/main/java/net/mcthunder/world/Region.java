package net.mcthunder.world;

import net.mcthunder.api.Direction;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Kevin on 10/21/2014.
 */
public class Region {
    private int x;
    private int z;
    private World world;

    public Region(World w, long region) {
        this.x = (int) (region >> 32);
        this.z = (int) region;
        this.world = w;
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
            for (int i = 0; i < sections.size(); i++) { //Loop through all 16 chunks verticle fashion
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
            Column c = new Column(l, chunks, biomes.getValue());
            world.addColumn(c);
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