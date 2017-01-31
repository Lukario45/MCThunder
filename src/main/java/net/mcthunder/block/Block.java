package net.mcthunder.block;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Direction;
import net.mcthunder.api.Location;
import net.mcthunder.entity.Player;
import net.mcthunder.world.Column;
import org.spacehq.mc.protocol.data.game.chunk.Chunk;
import org.spacehq.mc.protocol.data.game.chunk.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.world.block.BlockChangeRecord;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;

import static net.mcthunder.api.Utils.getLong;

//import org.spacehq.mc.protocol.data.game.chunk.ShortArray3d;

public class Block {
    private Location loc;
    private Material type;
    private int columnX, columnZ, chunkY, blockX, blockY, blockZ;

    public Block(Location loc) {
        this.loc = loc;
        if (this.loc == null)
            return;
        this.loc.setX((int) this.loc.getX());
        this.loc.setY((int) this.loc.getY());
        this.loc.setZ((int) this.loc.getZ());
        int columnX = (int)this.loc.getX() >> 4;
        int columnZ = (int)this.loc.getZ() >> 4;
        int chunkY = (int)this.loc.getY() >> 4;
        int blockX = (int)this.loc.getX() % 16;
        int blockY = (int)this.loc.getY() % 16;
        int blockZ = (int)this.loc.getZ() % 16;
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
        this.columnX = columnX;
        this.columnZ = columnZ;
        this.chunkY = chunkY;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        if (isInvalid()) {
            this.type = Material.AIR;
            return;
        }
        Chunk[] chunks = loc.getWorld().getColumn(getLong(this.columnX, this.columnZ)).getChunks();
        ShortArray3d blocks = chunks[this.chunkY] != null ? chunks[this.chunkY].getBlocks() : new ShortArray3d(4096);
        this.type = Material.fromData(Material.fromID(blocks.getBlock(this.blockX, this.blockY, this.blockZ)), (short) blocks.getData(this.blockX, this.blockY, this.blockZ));
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
        return this.loc == null ? null : this.loc.clone();
    }

    public void setType(Material type) {
        if (type == null)
            type = Material.AIR;
        this.type = type;
        if (isInvalid())
            return;
        if (this.type.getParent().equals(Material.CHEST) || this.type.getParent().equals(Material.TRAPPED_CHEST))
            this.loc.getWorld().registerChest(new Chest(this.loc, null, null));
        Column column = this.loc.getWorld().getColumn(getLong(this.columnX, this.columnZ));
        Chunk[] chunks = column.getChunks();
        ShortArray3d blocks = chunks[this.chunkY] != null ? chunks[this.chunkY].getBlocks() : new ShortArray3d(4096);
        NibbleArray3d blockLight = chunks[this.chunkY] != null ? chunks[this.chunkY].getBlockLight() : new NibbleArray3d(4096);
        NibbleArray3d skyLight = chunks[this.chunkY] != null ? chunks[this.chunkY].getSkyLight() : new NibbleArray3d(4096);
        blocks.setBlockAndData(this.blockX, this.blockY, this.blockZ, this.type.getID(), this.type.getData());
        Block above = getRelative(Direction.UP);
        blockLight.set(this.blockX, this.blockY, this.blockZ, this.type.getLightLevel());
        skyLight.set(this.blockX, this.blockY, this.blockZ, above.getSkyLight());
        chunks[this.chunkY] = new Chunk(blocks, blockLight, skyLight);
        Column c = new Column(getLong(this.columnX, this.columnZ), chunks, column.getBiomes());//Should be correct biomes ;_;
        this.loc.getWorld().addColumn(c);
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(this.loc.getWorld()) && p.isColumnLoaded(getLong(this.columnX, this.columnZ)))
                p.sendPacket(new ServerBlockChangePacket(new BlockChangeRecord(this.loc.getPosition(), this.type.getID() << 4 | this.type.getData())));
        updatePhys();
    }

    public int getSkyLight() {
        if (isInvalid())
            return 0;
        //15 sun
        //12 sun during rain or snow
        //10 sun during thunderstorm
        //4 moon
        Chunk c = this.loc.getWorld().getColumn(getLong(this.columnX, this.columnZ)).getChunks()[this.chunkY];
        return c == null ? 0 : c.getSkyLight().get(this.blockX, this.blockY, this.blockZ);
    }

    public int getLightLevel() {
        if (isInvalid())
            return 0;
        return this.loc.getWorld().getColumn(getLong(this.columnX, this.columnZ)).getChunks()[this.chunkY].getBlockLight().get(this.blockX, this.blockY, this.blockZ);
    }

    private boolean isInvalid() {
        return this.loc == null || this.chunkY < 0 || this.chunkY > 15 || this.blockX < 0 || this.blockX > 15 || this.blockY < 0 ||
                this.blockY > 15 || this.blockZ < 0 || this.blockZ > 15 || !this.loc.getWorld().isColumnLoaded(getLong(this.columnX, this.columnZ));
    }

    private void updatePhys() {//TODO: Replace with a physics engine instead of this temporary thing used for testing
        if (this.type.getParent().equals(Material.WATER)) {//Todo: add a tick delay instead of it being instant
            int height = this.type.equals(Material.WATER) ? 8 : Integer.parseInt(this.type.getName().split("_")[1]);
            Block north = getRelative(Direction.NORTH);
            Block east = getRelative(Direction.EAST);
            Block south = getRelative(Direction.SOUTH);
            Block west = getRelative(Direction.WEST);
            Block down = getRelative(Direction.DOWN);
            //TODO: Check these for other things water can go through
            if (down.getType().equals(Material.AIR))
                down.setType(Material.WATER_8_8);
            if (height != 1 && !down.getType().equals(Material.WATER)) {
                Material newMat = Material.fromString("WATER_" + (height - 1) + "_8");
                if (north.getType().equals(Material.AIR))
                    north.setType(newMat);
                if (east.getType().equals(Material.AIR))
                    east.setType(newMat);
                if (south.getType().equals(Material.AIR))
                    south.setType(newMat);
                if (west.getType().equals(Material.AIR))
                    west.setType(newMat);
            }
        }
    }
}
