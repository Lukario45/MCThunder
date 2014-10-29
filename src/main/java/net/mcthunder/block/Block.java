package net.mcthunder.block;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.Player;
import net.mcthunder.world.Column;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;

import static net.mcthunder.api.Utils.getLong;

public class Block {
    private Location loc;
    private int type;
    private short data;
    private int columnX, columnZ, chunkY, blockX, blockY, blockZ;

    public Block(Location loc) {
        this.loc = loc;
        if (this.loc == null)
            return;
        int columnX = (int)this.loc.getX() >> 4;
        int columnZ = (int)this.loc.getZ() >> 4;
        int chunkY = (int)this.loc.getY() >> 4;
        int blockX = (int)this.loc.getX()%16;
        int blockY = (int)this.loc.getY()%16;
        int blockZ = (int)this.loc.getZ()%16;
        if (blockX < 0)
            blockX += 16;
        if (blockX > 15) {
            blockX = blockX % 16;
            columnX++;
        }
        if (blockY < 0)
            blockY += 16;
        if (blockY > 15) {
            blockY = blockY % 16;
            chunkY++;
        }
        if (blockZ < 0)
            blockZ += 16;
        if (blockZ > 15) {
            blockZ = blockZ % 16;
            columnZ++;
        }
        if (chunkY == -1) {
            this.type = 0;
            this.data = 0;
            return;
        }
        this.columnX = columnX;
        this.columnZ = columnZ;
        this.chunkY = chunkY;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        Chunk[] chunks = loc.getWorld().getColumn(getLong(this.columnX, this.columnZ)).getChunks();
        ShortArray3d blocks = chunks[this.chunkY] != null ? chunks[this.chunkY].getBlocks() : new ShortArray3d(4096);
        this.type = blocks.getBlock(this.blockX, this.blockY, this.blockZ);
        this.data = (short) blocks.getData(this.blockX, this.blockY, this.blockZ);
    }

    public int getType() {
        return this.type;
    }

    public Location getLocation(){
        return this.loc;
    }

    public void setType(int type, short data) {
        this.type = type;
        this.data = data;
        Column column = loc.getWorld().getColumn(getLong(this.columnX, this.columnZ));
        Chunk[] chunks = column.getChunks();
        ShortArray3d blocks = chunks[this.chunkY] != null ? chunks[this.chunkY].getBlocks() : new ShortArray3d(4096);
        NibbleArray3d blockLight = chunks[this.chunkY] != null ? chunks[this.chunkY].getBlockLight() : new NibbleArray3d(4096);
        NibbleArray3d skyLight = chunks[this.chunkY] != null ? chunks[this.chunkY].getSkyLight() : new NibbleArray3d(4096);
        blocks.setBlockAndData((int)this.blockX, (int)this.blockY, (int)this.blockZ, this.type, this.data);
        chunks[this.chunkY] = new Chunk(blocks, blockLight, skyLight);
        Column c = new Column(getLong(this.columnX, this.columnZ), chunks, column.getBiomes());//Should be correct biomes ;_;
        this.loc.getWorld().addColumn(c);
        for (Player p : MCThunder.playerHashMap.values())
            if (p.isColumnLoaded(getLong(this.columnX, this.columnZ)))
                p.refreshColumn(c);
    }

    public void setType(int type) {
        setType(type, (short)0);
    }

    public short getData() {
        return this.data;
    }

    public boolean isLiquid() {
        return this.type == 8 || this.type == 9 || this.type == 10 || this.type == 11;
    }

    public boolean isLongGrass() {
        return this.type == 31 || this.type == 175;
    }

    public boolean isPlant() {
        return this.type == 6 || this.type == 37 || this.type == 38 || this.type == 39 || this.type == 40 || this.type == 59 ||
                this.type == 81 || this.type == 83 || this.type == 86 || this.type == 103 || this.type == 104 || this.type == 105 ||
                this.type == 141 || this.type == 142 || this.type == 175 || this.type == 251 || isLongGrass();
    }
}