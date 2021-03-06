package net.mcthunder.world;

import com.Lukario45.NBTFile.NBTFile;
import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.block.Chest;
import net.mcthunder.block.Sign;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import net.mcthunder.world.generator.Generation;
import org.spacehq.mc.protocol.data.game.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.world.WorldType;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.Lukario45.NBTFile.Utilities.*;
import static net.mcthunder.api.Utils.getLong;
import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/19/2014.
 */
public class World {
    private String name, path;
    private long seed;
    private int dimension;
    private boolean hardcore, generateStructures, dimensionRead = false;
    private Location spawn;
    private Difficulty difficulty;
    private HashMap<Long, Region> regionHashMap = new HashMap<>();
    private HashMap<Long, Column> columnHashMap = new HashMap<>();
    private ConcurrentHashMap<Integer, Entity> loadedEntities = new ConcurrentHashMap<>();
    private HashMap<Location, Chest> chestData = new HashMap<>();
    private ArrayList<Sign> signs = new ArrayList<>();//TEMP arraylist
    private WorldType worldType;

    public World(String name) {
        this(name, "worlds/" + name);
    }

    public World(String name, String path) {
        this.name = name;
        this.path = path;
        this.dimension = 0;
        try {
            CompoundTag tag;
            try {
                 tag = NBTIO.readFile(new File(this.path + "/level.dat")); //MCThunder Gen worlds ONLY
            } catch(EOFException e){ //for NON MCThunder gen worlds
                tag = NBTIO.readFile(new File(this.path + "/level.dat"));
            }
            CompoundTag data = tag.get("Data");
            IntTag xTag = data.get("SpawnX");
            IntTag yTag = data.get("SpawnY");
            IntTag zTag = data.get("SpawnZ");
            int di = 2;
            try {
                ByteTag dif = data.get("Difficulty");
                if (dif != null)
                    di = Byte.toUnsignedInt(dif.getValue());
            } catch (Exception e) {//Older Map compatibility
                IntTag dif = data.get("Difficulty");
                if (dif != null)
                    di = dif.getValue();
            }
            CompoundTag gamerules = data.get("GameRules");
            //tellConsole(LoggingLevel.DEBUG, String.valueOf(xTag.getValue()) + String.valueOf(yTag.getValue()) + String.valueOf(zTag.getValue()));
            this.worldType = fromName(data.get("generatorName").getValue().toString());
            this.seed = (long) data.get("RandomSeed").getValue();
            this.hardcore = (byte) data.get("hardcore").getValue() == 1;
            this.difficulty = difFromName(di);
            this.generateStructures = (byte) data.get("MapFeatures").getValue() == 1;
            if (gamerules.get("commandBlockOutput") != null)
                GameRule.commandBlockOutput.setEnabled(Boolean.valueOf(gamerules.get("commandBlockOutput").getValue().toString()));
            if (gamerules.get("doDaylightCycle") != null)
                GameRule.doDaylightCycle.setEnabled(Boolean.valueOf(gamerules.get("doDaylightCycle").getValue().toString()));
            if (gamerules.get("doFireTick") != null)
                GameRule.doFireTick.setEnabled(Boolean.valueOf(gamerules.get("doFireTick").getValue().toString()));
            if (gamerules.get("doMobLoot") != null)
                GameRule.doMobLoot.setEnabled(Boolean.valueOf(gamerules.get("doMobLoot").getValue().toString()));
            if (gamerules.get("doMobSpawning") != null)
                GameRule.doMobSpawning.setEnabled(Boolean.valueOf(gamerules.get("doMobSpawning").getValue().toString()));
            if (gamerules.get("doTileDrops") != null)
                GameRule.doTileDrops.setEnabled(Boolean.valueOf(gamerules.get("doTileDrops").getValue().toString()));
            if (gamerules.get("keepInventory") != null)
                GameRule.keepInventory.setEnabled(Boolean.valueOf(gamerules.get("keepInventory").getValue().toString()));
            if (gamerules.get("logAdminCommands") != null)
                GameRule.logAdminCommands.setEnabled(Boolean.valueOf(gamerules.get("logAdminCommands").getValue().toString()));
            if (gamerules.get("mobGriefing") != null)
                GameRule.mobGriefing.setEnabled(Boolean.valueOf(gamerules.get("mobGriefing").getValue().toString()));
            if (gamerules.get("naturalRegeneration") != null)
                GameRule.naturalRegeneration.setEnabled(Boolean.valueOf(gamerules.get("naturalRegeneration").getValue().toString()));
            if (gamerules.get("randomTickSpeed") != null) {
                try {
                    GameRule.randomTickSpeed.setTickSpeed(Integer.parseInt((String) gamerules.get("randomTickSpeed").getValue()));
                } catch (Exception e) {//Older Map compatibility
                    GameRule.randomTickSpeed.setTickSpeed((Integer) gamerules.get("randomTickSpeed").getValue());
                }
            }
            if (gamerules.get("sendCommandFeedback") != null)
                GameRule.sendCommandFeedback.setEnabled(Boolean.valueOf(gamerules.get("sendCommandFeedback").getValue().toString()));
            if (gamerules.get("showDeathMessages") != null)
                GameRule.showDeathMessages.setEnabled(Boolean.valueOf(gamerules.get("showDeathMessages").getValue().toString()));
            this.spawn = new Location(this, xTag.getValue(), yTag.getValue(), zTag.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WorldType fromName(String name) {
        return name.equals("largeBiomes") ? WorldType.LARGE_BIOMES : WorldType.valueOf(name.toUpperCase());
    }

    private Difficulty difFromName(int dif) {
        switch(dif) {
            case 0:
                return Difficulty.PEACEFUL;
            case 1:
                return Difficulty.EASY;
            case 2:
                return Difficulty.NORMAL;
            case 3:
                return Difficulty.HARD;
            default:
                return Difficulty.NORMAL;
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean checkRegion(Long l) {//Why does this not just return the first if statements value
        if (this.regionHashMap.containsKey(l)) {
            return true;
        } else {
            return false;
        }
    }

    public void setDimension(int dimension) {
        if (this.dimensionRead)
            return;
        this.dimension = dimension;
        this.dimensionRead = true;
    }

    public void loadAround(Location loc, int distance) {
        int x = (int)loc.getX() >> 4;
        int z = (int)loc.getZ() >> 4;
        for(int xAdd = -distance; xAdd < distance; xAdd++)
            for(int zAdd = -distance; zAdd < distance; zAdd++)
                getRegion(getLong((x + xAdd) >> 5, (z + zAdd) >> 5)).readChunk(getLong(x + xAdd, z + zAdd));
    }

    public long getSeed() {
        return this.seed;
    }

    public void addRegion(long l) {
        this.regionHashMap.put(l, new Region(this, l));
    }

    public void addColumn(Column c) {
        this.columnHashMap.put(c.getLong(), c);
    }

    public void addNewColum(Column c){
        Generation.getGenerationHashMap().put(c.getLong(), c);
    }

    public void unloadColumn(Column c) {
        if (c == null)
            return;
        long l = c.getLong();
        if (!isColumnLoaded(l))
            return;//Already unloaded
        for (Player p : MCThunder.getPlayers())
            if (p.isColumnLoaded(l) || p.isColumnPreLoaded(l))
                return;
        long reg = getLong(c.getX() >> 5, c.getZ() >> 5);
        if (this.regionHashMap.containsKey(reg)) {//Should contain... given how else would it have been loaded
            this.regionHashMap.get(reg).saveChunk(l);
            this.columnHashMap.remove(l);
            //tellConsole(LoggingLevel.DEBUG, "Unloaded column x: " + c.getX() + ", z: " + c.getZ());
        }
    }

    public void unloadColumn(long l) {
        unloadColumn(getColumn(l));
    }

    public boolean isColumnLoaded(long l) {
        return this.columnHashMap.containsKey(l);
    }

    public Collection<Column> getAllColumns() {
        return this.columnHashMap.values();
    }

    public Column getColumn(long l) {
        return this.columnHashMap.get(l);
    }

    public Column getNewColum(long l){
        return Generation.getGenerationHashMap().get(l);
    }

    public Region getRegion(long l) {
        if (!this.regionHashMap.containsKey(l))
            addRegion(l);
        return this.regionHashMap.get(l);
    }

    public void loadWorld() {
        loadAround(this.spawn, MCThunder.maxRenderDistance());
        tellConsole(LoggingLevel.INFO, "Finished loading " + this.name + ".");
    }

    public void unloadWorld() {
        Collection<Long> temp = ((HashMap<Long,Column>)this.columnHashMap.clone()).keySet();
        for(long l : temp)
            unloadColumn(l);
    }

    public boolean getGameRuleValue(String gameRule) {
        GameRule r = GameRule.fromString(gameRule);
        return r != null && r.isEnabled();
    }

    public int getTickSpeed() {
        return GameRule.randomTickSpeed.getTickSpeed();
    }

    public Location getSpawnLocation() {
        return this.spawn.clone();
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void removeEntity(int e) {
        this.loadedEntities.remove(e);
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public int getDimension() {
        return this.dimension;
    }

    public boolean isHardcore() {
        return this.hardcore;
    }

    public boolean canGenerateStructures() {
        return this.generateStructures;
    }

    public Collection<Entity> getEntities() {
        return this.loadedEntities.values();
    }

    public Entity getEntityFromID(int ID){
        return this.loadedEntities.get(ID);
    }

    public void loadEntity(Entity e) {
        if (e == null)
            return;
        long chunk = e.getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(this) && p.isColumnLoaded(chunk))
                for (Packet packet : e.getPackets())
                    p.sendPacket(packet);
        this.loadedEntities.put(e.getEntityID(), e);
    }

    public void addSign(Sign s) {
        s.sendPacket();
        this.signs.add(s);
    }

    public void registerChest(Chest c) {
        this.chestData.put(c.getLocation(), c);
    }

    public Chest getChest(Location l) {
        for (Location i : this.chestData.keySet())
            if (i.equals(l))
                return this.chestData.get(i);
        return this.chestData.get(l);
    }

    public String getPath() {
        return this.path;
    }

    public Collection<Sign> getSigns() {
        return this.signs;
    }

    public static void newWorld(String name){
        NBTFile level = new NBTFile("worlds/" + name + "/level.dat", "", true);

        try {
            level.createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CompoundTag data = new CompoundTag("Data");
        data.put(makeIntTag("SpawnX", 0));
        data.put(makeIntTag("SpawnY", 5));
        data.put(makeIntTag("SpawnZ", 0));
        data.put(makeStringTag("LevelName", name));
        CompoundTag gameRules = new CompoundTag("GameRules");
        gameRules.put(makeStringTag(GameRule.commandBlockOutput.name(), "true"));
        gameRules.put(makeStringTag(GameRule.doDaylightCycle.name(), "true"));
        gameRules.put(makeStringTag(GameRule.doFireTick.name(),"true"));
        gameRules.put(makeStringTag(GameRule.doMobLoot.name(),"true"));
        gameRules.put(makeStringTag(GameRule.doMobSpawning.name(),"true"));
        gameRules.put(makeStringTag(GameRule.doTileDrops.name(),"true"));
        gameRules.put(makeStringTag(GameRule.keepInventory.name(),"false"));
        gameRules.put(makeStringTag(GameRule.logAdminCommands.name(),"true"));
        gameRules.put(makeStringTag(GameRule.mobGriefing.name(),"true"));
        gameRules.put(makeStringTag(GameRule.naturalRegeneration.name(),"true"));
        gameRules.put(makeStringTag(GameRule.randomTickSpeed.name(),"3"));
        gameRules.put(makeStringTag(GameRule.sendCommandFeedback.name(),"true"));
        gameRules.put(makeStringTag(GameRule.showDeathMessages.name(),"true"));
        data.put(gameRules);
        data.put(makeStringTag("generatorName","FLAT"));
        data.put(makeLongTag("RandomSeed", (long) 0000000)); //Procedural Generation
        data.put(makeByteTag("hardcore", (byte) 1));
        data.put(makeByteTag("MapFeatures", (byte) 1));
        
        try {
            level.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum GameRule {
        commandBlockOutput(true),
        doDaylightCycle(true),
        doFireTick(true),
        doMobLoot(true),
        doMobSpawning(true),
        doTileDrops(true),
        keepInventory(false),
        logAdminCommands(true),
        mobGriefing(true),
        naturalRegeneration(true),
        randomTickSpeed(3),
        sendCommandFeedback(true),
        showDeathMessages(true);

        private boolean enabled;
        private int tickSpeed;

        public static GameRule fromString(String name) {
            if (name.equals("commandBlockOutput"))
                return commandBlockOutput;
            if (name.equals("doDaylightCycle"))
                return doDaylightCycle;
            if (name.equals("doFireTick"))
                return doFireTick;
            if (name.equals("doMobLoot"))
                return doMobLoot;
            if (name.equals("doMobSpawning"))
                return doMobSpawning;
            if (name.equals("doTileDrops"))
                return doTileDrops;
            if (name.equals("keepInventory"))
                return keepInventory;
            if (name.equals("logAdminCommands"))
                return logAdminCommands;
            if (name.equals("mobGriefing"))
                return mobGriefing;
            if (name.equals("naturalRegeneration"))
                return naturalRegeneration;
            if (name.equals("randomTickSpeed"))
                return randomTickSpeed;
            if (name.equals("sendCommandFeedback"))
                return sendCommandFeedback;
            if (name.equals("showDeathMessages"))
                return showDeathMessages;
            return null;
        }

        private GameRule(boolean enabled) {
            this.enabled = enabled;
        }

        private GameRule(int tickSpeed) {
            this.tickSpeed = tickSpeed;
            this.enabled = true;
        }

        public void setEnabled(boolean enabled) {
            if (!this.equals(GameRule.randomTickSpeed))
                this.enabled = enabled;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public int getTickSpeed() {
            return this.tickSpeed;
        }

        public void setTickSpeed(int speed) {
            if (this.equals(GameRule.randomTickSpeed))
                this.tickSpeed = speed;
        }

    }
}