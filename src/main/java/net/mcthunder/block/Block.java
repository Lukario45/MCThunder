package net.mcthunder.block;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Direction;
import net.mcthunder.api.Location;
import net.mcthunder.api.Player;
import net.mcthunder.material.Material;
import net.mcthunder.world.Column;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;

import static net.mcthunder.api.Utils.getLong;

public class Block {
    private Location loc;
    private Material type;
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
        if (isInvalid()) {
            this.type = null;
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
        this.type = Material.fromID(blocks.getBlock(this.blockX, this.blockY, this.blockZ));
        this.data = (short) blocks.getData(this.blockX, this.blockY, this.blockZ);
    }

    public Block getRelative(Direction d, int distance) {
        if (distance <= 0 || d == null)
            return this;
        int tempX = (int)this.loc.getX();
        int tempY = (int)this.loc.getY();
        int tempZ = (int)this.loc.getZ();
        if (d.equals(Direction.NORTH))
            tempX -= distance;
        else if (d.equals(Direction.SOUTH))
            tempX += distance;
        else if (d.equals(Direction.UP))
            tempY += distance;
        else if (d.equals(Direction.DOWN))
            tempY -= distance;
        else if (d.equals(Direction.EAST))
            tempZ -= distance;
        else if (d.equals(Direction.WEST))
            tempZ += distance;
        return new Block(new Location(this.loc.getWorld(), tempX, tempY, tempZ));
    }

    public Block getRelative(Direction d) {
        return getRelative(d, 1);
    }

    public Material getType() {
        return this.type;
    }

    public Location getLocation(){
        return this.loc;
    }

    public void setType(Material type, short data) {
        if (type == null)
            type = Material.AIR;
        this.data = data;
        this.type = type;
        if (isInvalid())
            return;
        Column column = this.loc.getWorld().getColumn(getLong(this.columnX, this.columnZ));
        Chunk[] chunks = column.getChunks();
        ShortArray3d blocks = chunks[this.chunkY] != null ? chunks[this.chunkY].getBlocks() : new ShortArray3d(4096);
        NibbleArray3d blockLight = chunks[this.chunkY] != null ? chunks[this.chunkY].getBlockLight() : new NibbleArray3d(4096);
        NibbleArray3d skyLight = chunks[this.chunkY] != null ? chunks[this.chunkY].getSkyLight() : new NibbleArray3d(4096);
        blocks.setBlockAndData(this.blockX, this.blockY, this.blockZ, this.type.getID(), this.data);
        Block above = getRelative(Direction.UP);
        blockLight.set(this.blockX, this.blockY, this.blockZ, this.type.getLightLevel());
        skyLight.set(this.blockX, this.blockY, this.blockZ, above.getSkyLight());
        chunks[this.chunkY] = new Chunk(blocks, blockLight, skyLight);
        Column c = new Column(getLong(this.columnX, this.columnZ), chunks, column.getBiomes());//Should be correct biomes ;_;
        this.loc.getWorld().addColumn(c);
        for (Player p : MCThunder.playerHashMap.values())
            if (p.getWorld().equals(this.loc.getWorld()) && p.isColumnLoaded(getLong(this.columnX, this.columnZ)))
                p.refreshColumn(c);
    }

    public int getSkyLight() {
        if (isInvalid())
            return 0;
        //15 sun
        //12 sun during rain or snow
        //10 sun during thunderstorm
        //4 moon
        return this.loc.getWorld().getColumn(getLong(this.columnX, this.columnZ)).getChunks()[this.chunkY].getSkyLight().get(this.blockX, this.blockY, this.blockZ);
    }

    public int getLightLevel() {
        if (isInvalid())
            return 0;
        return this.loc.getWorld().getColumn(getLong(this.columnX, this.columnZ)).getChunks()[this.chunkY].getBlockLight().get(this.blockX, this.blockY, this.blockZ);
    }

    public void setTypeID(int id) {
        setType(Material.fromID(id), (short)0);
    }

    public void setType(Material type) {
        setType(type, (short)0);
    }

    public short getData() {
        return this.data;
    }

    private boolean isInvalid() {
        return this.loc == null || this.chunkY < 0 || this.chunkY > 15 || this.blockX < 0 || this.blockX > 15 || this.blockY < 0 ||
                this.blockY > 15 || this.blockZ < 0 || this.blockZ > 15;
    }
}