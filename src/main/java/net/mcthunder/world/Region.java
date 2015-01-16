package net.mcthunder.world;

import net.mcthunder.api.Direction;
import net.mcthunder.api.Location;
import net.mcthunder.block.Chest;
import net.mcthunder.block.Sign;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Kevin on 10/21/2014.
 */
public class Region {
    private int x;
    private int z;
    private World world;
    private RegionFile regionFile = null;
    private boolean invalid;

    public Region(World w, long region) {
        this.x = (int) (region >> 32);
        this.z = (int) region;
        this.world = w;
        File f = new File(this.world.getPath() + "/region/r." + this.x + "." + this.z + ".mca");
        this.invalid = !f.exists();
        if (this.invalid) {
            //Create the region file
            //Once this if statement actually does things the invalid boolean can be removed
        }
        this.regionFile = new RegionFile(f);
    }

    public void saveChunk(long l) {
        if (this.invalid)
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
        DataOutputStream out = this.regionFile.getChunkDataOutputStream(x, z);
        DataInputStream in = this.regionFile.getChunkDataInputStream(x, z);
        if (in != null && out != null) {
            CompoundTag compoundTag = null;
            try {
                compoundTag = (CompoundTag) NBTIO.readTag(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (compoundTag == null)
                return;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            Column c = this.world.getColumn(l);
            Chunk[] chunks = c.getChunks();
            Map<String,Tag> values = compoundTag.getValue();
            Map<String,Tag> levelInfo = level.getValue();
            ArrayList<Tag> newSections = new ArrayList<>();
            for (int i = 0; i < sections.size(); i++) {//Loop through all 16 chunks
                CompoundTag chunkz = sections.get(i);
                Map<String,Tag> cv = chunkz.getValue();
                ByteArrayTag blocks = chunkz.get("Blocks");
                ByteArrayTag blockLight = chunkz.get("BlockLight");
                ByteArrayTag skyLight = chunkz.get("SkyLight");
                ByteArrayTag data = chunkz.get("Data");
                //ByteArrayTag add = chunkz.get("Add");
                if (chunks[i] != null) {
                    if (chunks[i].getBlockLight() != null)
                        blockLight.setValue(chunks[i].getBlockLight().getData());
                    if (chunks[i].getSkyLight() != null)
                        skyLight.setValue(chunks[i].getSkyLight().getData());
                    if(chunks[i].getBlocks() != null)
                        for (int cX = 0; cX < 16; cX++) //Loop through x
                            for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                                for (int cZ = 0; cZ < 16; cZ++) { //Loop through z
                                    int index = 256*cY + 16*cZ + cX;
                                    int id = chunks[i].getBlocks().getBlock(cX, cY, cZ);//TODO: Use add where needed
                                    if (id > 128)
                                        id -= 256;
                                    blocks.setValue(index, Byte.parseByte(id + ""));
                                    if(index%2 == 0)
                                        data.setValue(index/2, (byte) ((getValue(data, index + 1) << 4) | chunks[i].getBlocks().getData(cX, cY, cZ)));
                                    else
                                        data.setValue(index/2, (byte) ((chunks[i].getBlocks().getData(cX, cY, cZ) << 4) | getValue(data, index - 1)));
                                }
                }
                cv.put("Blocks", blocks);
                cv.put("BlockLight", blockLight);
                cv.put("SkyLight", skyLight);
                cv.put("Data", data);
                //cv.put("Add", add);//How to calculate from value if even needed
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
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readChunk(long l) {
        if (this.invalid || this.world.isColumnLoaded(l))
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
            CompoundTag compoundTag = null;
            try {
                compoundTag = (CompoundTag) NBTIO.readTag(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (compoundTag == null)
                return;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            ByteArrayTag biomes = level.get("Biomes");
            Chunk[] chunks = new Chunk[16];
            for (int i = 0; i < sections.size(); i++) {//Loop through all 16 chunks
                CompoundTag chunkz = sections.get(i);
                ByteArrayTag blocks = chunkz.get("Blocks");
                ByteArrayTag blockLight = chunkz.get("BlockLight");
                ByteArrayTag skyLight = chunkz.get("SkyLight");
                ByteArrayTag data = chunkz.get("Data");
                ByteArrayTag add = chunkz.get("Add");
                ShortArray3d block = new ShortArray3d(4096);
                for (int cX = 0; cX < 16; cX++) //Loop through x
                    for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                        for (int cZ = 0; cZ < 16; cZ++) { //Loop through z
                            int index = 256*cY + 16*cZ + cX;
                            int id = blocks.getValue(index) + (add != null ? getValue(add, index) << 8 : 0);
                            block.setBlockAndData(cX, cY, cZ, id + (id < 0 ? 256 : 0), getValue(data, index));
                        }
                chunks[i] = new Chunk(block, new NibbleArray3d(blockLight.getValue()), new NibbleArray3d(skyLight.getValue()));
            }
            this.world.addColumn(new Column(l, chunks, biomes.getValue()));
            ListTag entities = level.get("Entities");
            for (int i = 0; i < entities.size(); i++)
                this.world.loadEntity(Entity.fromTag(this.world, (CompoundTag) entities.get(i)));
            ListTag tileEntities = level.get("TileEntities");
            for (int i = 0; i < tileEntities.size(); i++) {//TODO: Create a tile entity object for each to clean this file up even more
                CompoundTag tile = tileEntities.get(i);
                StringTag id = tile.get("id");
                IntTag tileX = tile.get("x");
                IntTag tileY = tile.get("y");
                IntTag tileZ = tile.get("z");
                Location loc = (tileX == null || tileY == null || tileZ == null) ? null : new Location(this.world, tileX.getValue(), tileY.getValue(), tileZ.getValue());
                if (id.getValue().equals("Airportal")) {//End Portal

                } else if (id.getValue().equals("Banner")) {
                    IntTag base = tile.get("Base");
                    ListTag patterns = tile.get("Patterns");
                    if (patterns != null)
                        for (int j = 0; j < patterns.size(); j++) {
                            CompoundTag pattern = patterns.get(j);
                            IntTag color = pattern.get("Color");
                            StringTag location = pattern.get("Pattern");
                        }
                } else if (id.getValue().equals("Beacon")) {
                    StringTag lock = tile.get("Lock");
                    IntTag levels = tile.get("Levels");
                    IntTag primary = tile.get("Primary");
                    IntTag secondary = tile.get("Secondary");
                } else if (id.getValue().equals("Cauldron")) {//Brewing Stand
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ListTag items = tile.get("Items");//4 slots
                    IntTag brewTime = tile.get("BrewTime");
                } else if (id.getValue().equals("Chest")) {//Chest and Trapped Chest
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ListTag items = tile.get("Items");//27 slots
                    this.world.registerChest(new Chest(loc, items, customName == null ? null : customName.getValue()));
                } else if (id.getValue().equals("Comparator")) {
                    IntTag outputSignal = tile.get("OutputSignal");
                } else if (id.getValue().equals("Control")) {//Command Block
                    StringTag customName = tile.get("CustomName");
                    StringTag command = tile.get("Command");
                    IntTag successCount = tile.get("SuccessCount");
                    StringTag lastOutput = tile.get("LastOutput");
                    ByteTag trackOutput = tile.get("TrackOutput");//1 true, 0 false
                    CompoundTag commandStats = tile.get("CommandStats");
                    StringTag successCountName = commandStats.get("SuccessCountName");
                    StringTag successCountObjective = commandStats.get("SuccessCountObjective");
                    StringTag affectedBlocksName = commandStats.get("AffectedBlocksName");
                    StringTag affectedBlocksObjective = commandStats.get("AffectedBlocksObjective");
                    StringTag affectedEntitiesName = commandStats.get("AffectedEntitiesName");
                    StringTag affectedEntitiesObjective = commandStats.get("AffectedEntitiesObjective");
                    StringTag affectedItemsName = commandStats.get("AffectedItemsName");
                    StringTag affectedItemsObjective = commandStats.get("AffectedItemsObjective");
                    StringTag queryResultName = commandStats.get("QueryResultName");
                    StringTag queryResultObjective = commandStats.get("QueryResultObjective");
                } else if (id.getValue().equals("DLDetector")) {//Daylight Sensor

                } else if (id.getValue().equals("Dropper") || id.getValue().equals("Trap")) {//Trap = dispenser
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ListTag items = tile.get("Items");//9 slots
                } else if (id.getValue().equals("EnchantTable")) {
                    StringTag customName = tile.get("CustomName");
                } else if (id.getValue().equals("EnderChest")) {

                } else if (id.getValue().equals("FlowerPot")) {
                    StringTag item = tile.get("Item");
                    IntTag data = tile.get("Data");
                } else if (id.getValue().equals("Furnace")) {
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ShortTag burnTime = tile.get("BurnTime");
                    ShortTag cookTime = tile.get("CookTime");
                    ShortTag cookTimeTotal = tile.get("CookTimeTotal");
                    ListTag items = tile.get("Items");//3 slots
                } else if (id.getValue().equals("Hopper")) {
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ListTag items = tile.get("Items");//5 slots
                    IntTag transferCooldown = tile.get("TransferCooldown");
                } else if (id.getValue().equals("MobSpawner")) {
                    ListTag spawnPotentials = tile.get("SpawnPotentials");
                    if(spawnPotentials != null)
                        for (int j = 0; j < spawnPotentials.size(); j++) {
                            CompoundTag spawnPotential = spawnPotentials.get(j);
                            StringTag type = spawnPotential.get("Type");
                            IntTag weight = spawnPotential.get("Weight");
                            CompoundTag properties = spawnPotential.get("Properties");
                        }
                    StringTag entityID = tile.get("EntityId");
                    CompoundTag spawnData = tile.get("SpawnData");
                    ShortTag spawnCount = tile.get("SpawnCount");
                    ShortTag spawnRange = tile.get("SpawnRange");
                    ShortTag delay = tile.get("Delay");
                    ShortTag minSpawnDelay = tile.get("MinSpawnDelay");
                    ShortTag maxSpawnDelay = tile.get("MaxSpawnDelay");
                    ShortTag maxNearbyEntities = tile.get("MaxNearbyEntities");
                    ShortTag requiredPlayerRange = tile.get("RequiredPlayerRange");
                } else if (id.getValue().equals("Music")) {//Note Block
                    ByteTag note = tile.get("note");
                } else if (id.getValue().equals("Piston")) {//Piston Moving
                    IntTag blockId = tile.get("blockId");
                    IntTag blockData = tile.get("blockData");
                    IntTag facing = tile.get("facing");
                    FloatTag progress = tile.get("progress");
                    ByteTag extending = tile.get("extending");//1 true, 0 false
                } else if (id.getValue().equals("RecordPlayer")) {//Jukebox
                    IntTag record = tile.get("Record");
                    CompoundTag recordItem = tile.get("RecordItem");
                } else if (id.getValue().equals("Sign")) {
                    StringTag line1 = tile.get("Text1");
                    StringTag line2 = tile.get("Text2");
                    StringTag line3 = tile.get("Text3");
                    StringTag line4 = tile.get("Text4");
                    String[] lines = new String[4];
                    lines[0] = line1.getValue();
                    lines[1] = line2.getValue();
                    lines[2] = line3.getValue();
                    lines[3] = line4.getValue();
                    this.world.addSign(new Sign(loc, lines));
                } else if (id.getValue().equals("Skull")) {//Head
                    ByteTag skullType = tile.get("SkullType");
                    StringTag extraType = tile.get("ExtraType");//Legacy support for pre 1.8
                    ByteTag rotation = tile.get("Rot");
                    CompoundTag owner = tile.get("Owner");
                    StringTag ownerUUID = (owner == null ? null : (StringTag) owner.get("Id"));
                    UUID uuid = ownerUUID != null ? UUID.fromString(ownerUUID.getValue()) : null;
                    StringTag name = (owner == null ? null : (StringTag) owner.get("Name"));
                    CompoundTag properties = (owner == null ? null : (CompoundTag) owner.get("Properties"));
                    ListTag textures = (properties == null ? null : (ListTag) properties.get("textures"));
                    if (textures != null)
                        for (int j = 0; j < textures.size(); j++) {
                            CompoundTag texture = textures.get(j);
                            StringTag signature = texture.get("Signature");
                            StringTag value = texture.get("Value");
                        }
                }
            }
        }
    }

    public void readChunk(long l, Player p, Direction dir, boolean removeOld) {
        if (this.invalid)
            return;
        if (p.isColumnLoaded(l) && !removeOld)
            return;
        if (this.world.isColumnLoaded(l)) {
            p.addColumn(l, dir, removeOld);
            return;
        }
        readChunk(l);
        if (this.world.isColumnLoaded(l))
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