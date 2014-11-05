package net.mcthunder.world;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static net.mcthunder.api.Utils.getLong;
import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/19/2014.
 */
public class World {
    private String name;
    private long seed;
    private int dimension;
    private boolean hardcore;
    private boolean generateStructures;
    private Location spawn;
    private Difficulty difficulty;
    private HashMap<Long, Region> regionHashMap;
    private HashMap<Long, Column> columnHashMap;
    private WorldType worldType;

    public World(String name) {
        this.name = name;
        this.dimension = 0;
        this.columnHashMap = new HashMap<>();
        this.regionHashMap = new HashMap<>();
        try {
            CompoundTag tag = NBTIO.readFile(new File("worlds/" + this.name + "/level.dat"));
            CompoundTag data = tag.get("Data");
            IntTag xTag = data.get("SpawnX");
            IntTag yTag = data.get("SpawnY");
            IntTag zTag = data.get("SpawnZ");
            IntTag dif = data.get("Difficulty");
            CompoundTag gamerules = data.get("GameRules");
            //tellConsole(LoggingLevel.DEBUG, String.valueOf(xTag.getValue()) + String.valueOf(yTag.getValue()) + String.valueOf(zTag.getValue()));
            this.worldType = fromName(data.get("generatorName").getValue().toString());
            this.seed = (long) data.get("RandomSeed").getValue();
            this.hardcore = (byte) data.get("hardcore").getValue() == 1;
            this.difficulty = difFromName(dif != null ? dif.getValue() : 2);
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
            if (gamerules.get("randomTickSpeed") != null)
                GameRule.randomTickSpeed.setTickSpeed((Integer) gamerules.get("randomTickSpeed").getValue());
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
        if (name.equals("largeBiomes"))
            return WorldType.LARGE_BIOMES;
        return WorldType.valueOf(name.toUpperCase());
    }

    private Difficulty difFromName(int dif) {
        if (dif == 0)
            return Difficulty.PEACEFUL;
        if (dif == 1)
            return Difficulty.EASY;
        if (dif == 2)
            return Difficulty.NORMAL;
        if (dif == 3)
            return Difficulty.HARD;
        return Difficulty.NORMAL;
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

    public void unloadColumn(Column c) {
        if (c == null)
            return;
        long l = c.getLong();
        if (!isColumnLoaded(l))
            return;//Already unloaded
        for (Player p : MCThunder.playerHashMap.values())
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

    public Column[] getAllColumnsAsArray() {
        return (Column[]) this.columnHashMap.values().toArray();
    }

    public Column getColumn(long l) {
        return this.columnHashMap.get(l);
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
        HashMap<Long, Column> temp = (HashMap<Long, Column>) this.columnHashMap.clone();
        for(long l : temp.keySet())
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
        return this.spawn;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
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