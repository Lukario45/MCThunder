package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.*;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.inventory.ChestInventory;
import net.mcthunder.inventory.Inventory;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.inventory.PlayerInventory;
import net.mcthunder.world.Column;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.auth.properties.PropertyMap;
import org.spacehq.mc.auth.util.Base64;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.packet.Packet;

import java.util.ArrayList;
import java.util.UUID;

import static net.mcthunder.api.Utils.getLong;

/**
 * Created by Kevin on 10/14/2014.
 */
public final class Player extends LivingEntity {
    private final UUID uuid;
    private final GameProfile origProfile;
    private final String name;
    private final Property origSkin;
    private final String capeURL = null;
    private ArrayList<Long> loadedColumns = new ArrayList<>(), northColumns = new ArrayList<>(), eastColumns = new ArrayList<>(),
            southColumns = new ArrayList<>(), westColumns = new ArrayList<>();
    private int viewDistance = 9, slot = 36, ping, score = 0, hunger = 20;
    private Inventory openInventory = null, inv, enderchest;
    private boolean moveable = false, hideCape = false;
    private GameMode gamemode = GameMode.CREATIVE;
    private String displayName, appended = "";
    private Location bedSpawn = null;
    private byte skinFlags = 127;
    private GameProfile profile;
    private UUID lastPmPerson;
    private Session session;
    private UUID skinUUID;
    private Property skin;

    public Player(Session session) {
        super(null);
        this.type = EntityType.PLAYER;
        this.session = session;
        this.profile = getSession().getFlag(ProtocolConstants.PROFILE_KEY);
        this.origProfile = this.profile;
        this.uuid = getGameProfile().getId();
        this.metadata.setMetadata(2, this.name = getGameProfile().getName());
        this.metadata.setMetadata(3, (byte) 1);//Always show name
        this.displayName = this.name;
        this.inv = new PlayerInventory(this.name, this);
        this.enderchest = new ChestInventory("EnderChest");
        this.ping = getSession().getFlag(ProtocolConstants.PING_KEY);
        this.skinUUID = this.uuid;
        this.origSkin = getGameProfile().getProperties().get("textures");
        this.skin = this.origSkin;
        this.maxHealth = 20;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(10, this.skinFlags);
        this.metadata.setBit(16, 0x02, this.hideCape);
        this.metadata.setMetadata(17, (float) (this.activeEffects.containsKey(PotionEffectType.ABSORPTION) ? this.activeEffects.get(PotionEffectType.ABSORPTION).getAmplifier() : 0));
        this.metadata.setMetadata(18, this.score);
        readFromFile();
    }

    public void readFromFile() {//TODO: Read other entity things once I make variables to store them
        PlayerProfileHandler profileHandler = MCThunder.getProfileHandler();
        profileHandler.checkPlayer(this);
        Location l = null;
        ListTag pos = (ListTag) profileHandler.getAttribute(this, "Pos");
        StringTag w = (StringTag) profileHandler.getAttribute(this, "SpawnWorld");
        if (pos != null) {
            l = new Location(w != null ? MCThunder.getWorld(w.getValue()) : MCThunder.getWorld(MCThunder.getConfig().getWorldName()), ((DoubleTag) pos.get(0)).getValue(),
                    ((DoubleTag) pos.get(1)).getValue(), ((DoubleTag) pos.get(2)).getValue());
            ListTag rotation = (ListTag) profileHandler.getAttribute(this, "Rotation");
            if (rotation != null) {
                l.setYaw(((FloatTag) rotation.get(0)).getValue());
                l.setPitch(((FloatTag) rotation.get(1)).getValue());
            }
        }
        setLocation((l == null || l.getWorld() == null) ? MCThunder.getWorld(MCThunder.getConfig().getWorldName()).getSpawnLocation() : l);
        ListTag motion = (ListTag) profileHandler.getAttribute(this, "Motion");
        if (motion != null && this.location != null) {
            DoubleTag dX = motion.get(0);
            DoubleTag dY = motion.get(1);
            DoubleTag dZ = motion.get(2);
            this.location.setVector(new Vector(dX.getValue(), dY.getValue(), dZ.getValue()));
        }
        ShortTag fire = (ShortTag) profileHandler.getAttribute(this, "Fire");
        if (fire != null)
            this.fireTicks = fire.getValue();
        ShortTag air = (ShortTag) profileHandler.getAttribute(this, "Air");
        if (air != null)
            this.airLeft = air.getValue();
        ByteTag onGround = (ByteTag) profileHandler.getAttribute(this, "OnGround");//1 true, 0 false
        this.onGround = onGround != null && onGround.getValue() == (byte) 1;
        IntTag dim = (IntTag) profileHandler.getAttribute(this, "Dimension");
        if (dim != null)
            getWorld().setDimension(dim.getValue());//-1 nether, 0 overworld, 1 end
        FloatTag healF = (FloatTag) profileHandler.getAttribute(this, "HealF");
        ShortTag health = (ShortTag) profileHandler.getAttribute(this, "Health");
        this.maxHealth = healF == null ? health == null ? 20 : health.getValue() : healF.getValue();
        ListTag activeEffects = (ListTag) profileHandler.getAttribute(this, "ActiveEffects");
        if (activeEffects != null)
            for (int j = 0; j < activeEffects.size(); j++) {
                CompoundTag activeEffect = activeEffects.get(j);
                ByteTag effectID = activeEffect.get("Id");
                ByteTag amplifier = activeEffect.get("Amplifier");
                IntTag duration = activeEffect.get("Duration");
                ByteTag ambient = activeEffect.get("Ambient");//1 true, 0 false
                ByteTag showParticles = activeEffect.get("ShowParticles");//1 true, 0 false
                if (effectID == null || amplifier == null || duration == null)
                    continue;
                PotionEffect potion = new PotionEffect(PotionEffectType.fromID(effectID.getValue()), duration.getValue(), amplifier.getValue());
                potion.setAmbient(ambient != null && ambient.getValue() == (byte) 1);
                potion.setShowParticles(showParticles != null && showParticles.getValue() == (byte) 1);
                this.activeEffects.put(this.latest = potion.getType(), potion);
            }
        IntTag gm = (IntTag) profileHandler.getAttribute(this, "playerGameType");
        if (gm != null)
            this.gamemode = gm.getValue() == 1 ? GameMode.CREATIVE : gm.getValue() == 2 ? GameMode.ADVENTURE : gm.getValue() == 3 ? GameMode.ADVENTURE : GameMode.SURVIVAL;
        IntTag score = (IntTag) profileHandler.getAttribute(this, "Score");
        if (score != null)
            this.score = score.getValue();
        IntTag slot = (IntTag) profileHandler.getAttribute(this, "SelectedItemSlot");
        if (slot != null)
            this.slot = slot.getValue() + 36;
        IntTag x = (IntTag) profileHandler.getAttribute(this, "SpawnX");
        IntTag y = (IntTag) profileHandler.getAttribute(this, "SpawnY");
        IntTag z = (IntTag) profileHandler.getAttribute(this, "SpawnZ");
        StringTag bw = (StringTag) profileHandler.getAttribute(this, "BedSpawnWorld");
        if (x != null && y != null && z != null)
            this.bedSpawn = new Location(bw != null ? MCThunder.getWorld(bw.getValue()) : MCThunder.getWorld(MCThunder.getConfig().getWorldName()), x.getValue(),
                    y.getValue(), z.getValue());
        ByteTag spawnForced = (ByteTag) profileHandler.getAttribute(this, "SpawnForced");//1 true 0 false
        ByteTag sleeping = (ByteTag) profileHandler.getAttribute(this, "Sleeping");//1 true 0 false
        ShortTag sleepTimer = (ShortTag) profileHandler.getAttribute(this, "SleepTimer");
        IntTag foodLevel = (IntTag) profileHandler.getAttribute(this, "foodLevel");
        if (foodLevel != null)
            this.hunger = foodLevel.getValue();
        FloatTag foodExhaustionLevel = (FloatTag) profileHandler.getAttribute(this, "foodExhaustionLevel");
        FloatTag foodSaturationLevel = (FloatTag) profileHandler.getAttribute(this, "foodSaturationLevel");
        IntTag foodTickTimer = (IntTag) profileHandler.getAttribute(this, "foodTickTimer");
        IntTag xpLevel = (IntTag) profileHandler.getAttribute(this, "XpLevel");
        FloatTag xpP = (FloatTag) profileHandler.getAttribute(this, "XpP");
        IntTag xpTotal = (IntTag) profileHandler.getAttribute(this, "XpTotal");
        IntTag xpSeed = (IntTag) profileHandler.getAttribute(this, "XpSeed");
        this.inv.setItems((ListTag) profileHandler.getAttribute(this, "Inventory"));
        this.enderchest.setItems((ListTag) profileHandler.getAttribute(this, "EnderItems"));
        CompoundTag abilities = (CompoundTag) profileHandler.getAttribute(this, "abilities");
        if (abilities != null) {
            FloatTag walkSpeed = abilities.get("walkSpeed");
            FloatTag flySpeed = abilities.get("flySpeed");
            ByteTag mayfly = abilities.get("mayfly");//1 true 0 false
            ByteTag flying = abilities.get("flying");//1 true 0 false
            ByteTag invulnerable = abilities.get("invulnerable");//1 true 0 false
            ByteTag mayBuild = abilities.get("mayBuild");//1 true 0 false
            ByteTag instabuild = abilities.get("instabuild");//1 true 0 false
        }
        StringTag skinUUID = (StringTag) profileHandler.getAttribute(this, "SkinUUID");
        if (skinUUID != null)
            this.profile.getProperties().put("textures", this.skin = Utils.getSkin(this.skinUUID = UUID.fromString(skinUUID.getValue())));
        StringTag displayName = (StringTag) profileHandler.getAttribute(this, "DisplayName");
        if (displayName != null)
            this.displayName = displayName.getValue();
    }

    public void loadChunks(Direction d) {
        if(d == null) {
            loadDir(Direction.NORTH);
            loadDir(Direction.EAST);
            loadDir(Direction.SOUTH);
            loadDir(Direction.WEST);
            updateDir(Direction.NORTH);
            updateDir(Direction.EAST);
            updateDir(Direction.SOUTH);
            updateDir(Direction.WEST);
        } else if (d.equals(Direction.NORTH_EAST)) {
            loadDir(Direction.NORTH);
            loadDir(Direction.EAST);
            updateDir(Direction.NORTH);
            updateDir(Direction.EAST);
        } else if (d.equals(Direction.SOUTH_EAST)) {
            loadDir(Direction.SOUTH);
            loadDir(Direction.EAST);
            updateDir(Direction.SOUTH);
            updateDir(Direction.EAST);
        } else if (d.equals(Direction.SOUTH_WEST)) {
            loadDir(Direction.SOUTH);
            loadDir(Direction.WEST);
            updateDir(Direction.SOUTH);
            updateDir(Direction.WEST);
        } else if (d.equals(Direction.NORTH_WEST)) {
            loadDir(Direction.NORTH);
            loadDir(Direction.WEST);
            updateDir(Direction.NORTH);
            updateDir(Direction.WEST);
        } else {
            loadDir(d);
            updateDir(d);
        }
    }

    private void loadDir(Direction d) {
        if (!preLoaded(d)) {//if not loaded then load it
            int x = (int) getLocation().getX() >> 4;
            int z = (int) getLocation().getZ() >> 4;
            int xMod = d.equals(Direction.EAST) ? 1 : d.equals(Direction.WEST) ? -1 : 0;
            int zMod = d.equals(Direction.SOUTH) ? 1 : d.equals(Direction.NORTH) ? -1 : 0;
            for (int xAdd = -getView() + xMod; xAdd < getView() + xMod; xAdd++)
                for (int zAdd = -getView() + zMod; zAdd < getView() + zMod; zAdd++)
                    getWorld().getRegion(getLong((x + xAdd) >> 5, (z + zAdd) >> 5)).readChunk(getLong(x + xAdd, z + zAdd), this, d, false);
        }
        sendColumns(d.equals(Direction.NORTH) ? this.northColumns : d.equals(Direction.EAST) ? this.eastColumns : d.equals(Direction.SOUTH) ?
                this.southColumns : d.equals(Direction.WEST) ? this.westColumns : null);
    }

    private boolean preLoaded(Direction d) {
        if (d.equals(Direction.NORTH))
            return !this.northColumns.isEmpty();
        if (d.equals(Direction.EAST))
            return !this.eastColumns.isEmpty();
        if (d.equals(Direction.SOUTH))
            return !this.southColumns.isEmpty();
        return d.equals(Direction.WEST) && !this.westColumns.isEmpty();
    }

    private void updateDir(Direction d) {
        int x = (int)this.location.getX() >> 4;
        int z = (int)this.location.getZ() >> 4;
        ArrayList<Long> temp = null;
        if (d.equals(Direction.NORTH)) {
            temp = (ArrayList<Long>) this.southColumns.clone();
            this.northColumns.clear();
            this.southColumns.clear();
            for(int xAdd = -getView(); xAdd < getView(); xAdd++) {
                getWorld().getRegion(getLong((x + xAdd) >> 5, (z - getView() - 1) >> 5)).readChunk(getLong(x + xAdd, z - getView() - 1), this, d, false);
                getWorld().getRegion(getLong((x + xAdd) >> 5, (z + getView()) >> 5)).readChunk(getLong(x + xAdd, z + getView()), this, Direction.SOUTH, true);
            }
        } else if (d.equals(Direction.EAST)) {
            temp = (ArrayList<Long>) this.westColumns.clone();
            this.eastColumns.clear();
            this.westColumns.clear();
            for(int zAdd = -getView(); zAdd < getView(); zAdd++) {
                getWorld().getRegion(getLong((x + getView() + 1) >> 5, (z + zAdd) >> 5)).readChunk(getLong(x + getView() + 1, z + zAdd), this, d, false);
                getWorld().getRegion(getLong((x - getView()) >> 5, (z + zAdd) >> 5)).readChunk(getLong(x - getView(), z + zAdd), this, Direction.WEST, true);
            }
        } else if (d.equals(Direction.SOUTH)) {
            temp = (ArrayList<Long>) this.northColumns.clone();
            this.southColumns.clear();
            this.northColumns.clear();
            for(int xAdd = -getView(); xAdd < getView(); xAdd++) {
                getWorld().getRegion(getLong((x + xAdd) >> 5, (z + getView() + 1) >> 5)).readChunk(getLong(x + xAdd, z + getView() + 1), this, d, false);
                getWorld().getRegion(getLong((x + xAdd) >> 5, (z - getView()) >> 5)).readChunk(getLong(x + xAdd, z - getView()), this, Direction.NORTH, true);
            }
        } else if (d.equals(Direction.WEST)) {
            temp = (ArrayList<Long>) this.eastColumns.clone();
            this.westColumns.clear();
            this.eastColumns.clear();
            for(int zAdd = -getView(); zAdd < getView(); zAdd++) {
                getWorld().getRegion(getLong((x - getView() - 1) >> 5, (z + zAdd) >> 5)).readChunk(getLong(x - getView() - 1, z + zAdd), this, d, false);
                getWorld().getRegion(getLong((x + getView()) >> 5, (z + zAdd) >> 5)).readChunk(getLong(x + getView(), z + zAdd), this, Direction.EAST, true);
            }
        }
        if (temp != null)
            unloadColumn(temp);
    }

    private void unloadColumn(ArrayList<Long> temp) {
        for (long l : temp) {
            getWorld().unloadColumn(l);
            /*for (Player player1 : MCThunder.getPlayers())
                if (player1.getWorld().equals(getWorld()) && l == player1.getChunk() && !player1.getUniqueID().equals(getUniqueID()))
                    sendPacket(new ServerDestroyEntitiesPacket(player1.getEntityID()));
            for (Bot b : MCThunder.getBots())
                if (b.getWorld().equals(getWorld()) && l == b.getChunk())
                    sendPacket(new ServerDestroyEntitiesPacket(b.getEntityID()));
            for (Entity e : getWorld().getEntities())
                if (l == e.getChunk())
                    sendPacket(new ServerDestroyEntitiesPacket(e.getEntityID()));*/
            //Only should destroy on worldchange
        }
    }

    public boolean isColumnLoaded(long l) {
        return this.loadedColumns.contains(l);
    }

    public boolean isColumnPreLoaded(long l) {
        return this.northColumns.contains(l) || this.eastColumns.contains(l) || this.southColumns.contains(l) || this.westColumns.contains(l);
    }

    public void addColumn(long l, Direction d, boolean removeOld) {
        if (d.equals(Direction.NORTH) && !this.northColumns.contains(l))
            this.northColumns.add(l);
        else if (d.equals(Direction.EAST) && !this.eastColumns.contains(l))
            this.eastColumns.add(l);
        else if (d.equals(Direction.SOUTH) && !this.southColumns.contains(l))
            this.southColumns.add(l);
        else if (d.equals(Direction.WEST) && !this.westColumns.contains(l))
            this.westColumns.add(l);
        if (removeOld && isColumnLoaded(l)) {
            this.loadedColumns.remove(l);
            sendPacket(new ServerChunkDataPacket(getWorld().getColumn(l).getX(), getWorld().getColumn(l).getZ()));
        }
    }

    public void refreshColumn(Column c) {
        sendPacket(new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks(), c.getBiomes()));
        for (Player player1 : MCThunder.getPlayers())
            if (player1.getWorld().equals(getWorld()) && isColumnLoaded(player1.getChunk()) && !player1.getUniqueID().equals(getUniqueID()))
                for (Packet packet : player1.getPackets())
                    sendPacket(packet);
        for (Bot b : MCThunder.getBots())
            if (b.getWorld().equals(getWorld()) && isColumnLoaded(b.getChunk()))
                for (Packet p : b.getPackets())
                    sendPacket(p);
        for (Entity e : getWorld().getEntities())
            if (isColumnLoaded(e.getChunk()))
                for (Packet packet : e.getPackets())
                    sendPacket(packet);
    }

    private void sendColumns(ArrayList<Long> columns) {
        if (columns != null)
            for (long column : columns)
                if (!isColumnLoaded(column)) {
                    Column c = getWorld().getColumn(column);
                    if (c != null) {
                        sendPacket(new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks(), c.getBiomes()));
                        for (Player player1 : MCThunder.getPlayers())
                            if (player1.getWorld().equals(getWorld()) && column == player1.getChunk() && !player1.getUniqueID().equals(getUniqueID()))
                                for (Packet packet : player1.getPackets())
                                    sendPacket(packet);
                        for (Bot b : MCThunder.getBots())
                            if (b.getWorld().equals(getWorld()) && column == b.getChunk())
                                for (Packet p : b.getPackets())
                                    sendPacket(p);
                        for (Entity e : getWorld().getEntities())
                            if (column == e.getChunk())
                                for (Packet packet : e.getPackets())
                                    sendPacket(packet);
                    }
                    this.loadedColumns.add(column);//Technically is loaded for other purposes
                }
    }

    public void teleport(Location l) {
        toggleMoveable();
        if (!l.getWorld().equals(getWorld())) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
            setLocation(l);
            long chunk = getChunk();
            this.entityID = getNextID();//Is this needed? depends on how when going back and forth between worlds we do it
            for (Player p : MCThunder.getPlayers()) {
                p.sendPacket(destroyEntitiesPacket);
                if (p.getWorld().equals(l.getWorld()) && p.isColumnLoaded(chunk))
                    for (Packet packet : getPackets())
                        p.sendPacket(packet);
            }
            this.northColumns.clear();
            this.eastColumns.clear();
            this.westColumns.clear();
            this.southColumns.clear();
            this.loadedColumns.clear();
            sendPacket(new ServerRespawnPacket(getWorld().getDimension(), getWorld().getDifficulty(), getGameMode(), getWorld().getWorldType()));
            updateInventory();
        } else {//TODO: Unload and the old chunks if tp is to far
            setLocation(l);
            ServerEntityTeleportPacket packet = new ServerEntityTeleportPacket(getEntityID(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), isOnGround());
            long chunk = getChunk();
            for (Player p : MCThunder.getPlayers())
                if (p.getWorld().equals(l.getWorld()) && p.isColumnLoaded(chunk))
                    p.sendPacket(packet);
        }
        loadChunks(null);//Load the needed chunks
        toggleMoveable();
        sendPacket(new ServerPlayerPositionRotationPacket(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch()));
        sendPacket(new ServerSpawnPositionPacket(this.location.getPosition()));
    }

    public int getView() {
        return this.viewDistance;
    }

    public void setView(int distance) {
        this.viewDistance = MCThunder.maxRenderDistance() < distance ? MCThunder.maxRenderDistance() : distance;
    }

    public ItemStack getHeldItem() {
        return this.inv.getItemAt(this.slot);
    }

    public Session getSession() {
        return this.session;
    }

    public GameProfile getGameProfile() {
        return this.profile;
    }

    public void disconnect(String reason) {
        getSession().disconnect(reason);
    }

    public int getPing() {
        return this.ping;
    }

    public UUID getUniqueID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {//TODO: Config value for symbol in front of nickname
        return (this.displayName.equals(this.name) ? "" : "~") + this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? this.name : displayName;
        String uncolored = MessageFormat.formatMessage(getDisplayName()).getFullText().trim();
        if (uncolored.length() < 17) {
            super.setCustomName(uncolored);
            this.metadata.setMetadata(2, uncolored);
            PropertyMap properties = (PropertyMap) this.profile.getProperties().clone();
            this.profile = this.displayName == null ? this.origProfile : new GameProfile(this.uuid, uncolored);
            for (String key : properties.keySet())
                this.profile.getProperties().put(key, properties.get(key));
            updateMetadata();
        }
        MCThunder.getEntryListHandler().refresh(this);
        if (uncolored.length() < 17) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
            this.entityID = getNextID();
            long chunk = getChunk();
            for (Player pl : MCThunder.getPlayers())
                if (!pl.getUniqueID().equals(getUniqueID())) {
                    pl.sendPacket(destroyEntitiesPacket);
                    if (pl.getWorld().equals(getWorld()) && pl.isColumnLoaded(chunk))
                        for (Packet packet : getPackets())
                            pl.sendPacket(packet);
                }
            sendPacket(new ServerRespawnPacket(getWorld().getDimension(), getWorld().getDifficulty(), getGameMode(), getWorld().getWorldType()));
            updateInventory();
            sendPacket(new ServerPlayerPositionRotationPacket(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch()));
            sendPacket(new ServerSpawnPositionPacket(this.location.getPosition()));
        }
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public void toggleMoveable() {
        this.moveable = !this.moveable;
    }

    public boolean isMoveable() {
        return this.moveable;
    }

    public Packet getPacket() {
        return new ServerSpawnPlayerPacket(getEntityID(), getUniqueID(), this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), getHeldItem().getType().getID(), getMetadata().getMetadataArray());
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public GameMode getGameMode() {
        return this.gamemode;
    }

    public void setGameMode(GameMode gm) {
        this.gamemode = gm;
        sendPacket(new ServerRespawnPacket(getWorld().getDimension(), getWorld().getDifficulty(), getGameMode(), getWorld().getWorldType()));
        updateInventory();
        sendPacket(new ServerPlayerPositionRotationPacket(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch()));
        sendPacket(new ServerSpawnPositionPacket(this.location.getPosition()));
    }

    public void sendMessage(String message) {
        sendPacket(new ServerChatPacket(MessageFormat.formatMessage(message)));
    }

    public Player getLastPmPerson() {
        return MCThunder.getPlayer(this.lastPmPerson);
    }

    public void setLastPmPerson(Player lastPmPerson) {
        this.lastPmPerson = lastPmPerson.getUniqueID();
    }

    public void unloadChunks() {
        unloadColumn(this.northColumns);
        unloadColumn(this.eastColumns);
        unloadColumn(this.westColumns);
        unloadColumn(this.southColumns);
        unloadColumn(this.loadedColumns);
    }

    public void setWorld(World w) {
        this.northColumns.clear();
        this.eastColumns.clear();
        this.westColumns.clear();
        this.southColumns.clear();
        this.loadedColumns.clear();
        super.setWorld(w);
        sendPacket(new ServerRespawnPacket(getWorld().getDimension(), getWorld().getDifficulty(), getGameMode(), getWorld().getWorldType()));
        updateInventory();
        sendPacket(new ServerPlayerPositionRotationPacket(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch()));
        sendPacket(new ServerSpawnPositionPacket(this.location.getPosition()));
        loadChunks(null);
    }

    public String getAppended() {
        return this.appended;
    }

    public void setAppended(String toAppend) {
        this.appended = toAppend;
    }

    public void sendPacket(Packet p) {
        if (p != null)
            getSession().send(p);
    }

    public PlayerListEntry getListEntry() {
        return new PlayerListEntry(getGameProfile(), getGameMode(), getPing(), MessageFormat.formatMessage(getDisplayName()));
    }

    protected void setSkin(Property p) {
        if (p == null || !p.getName().equalsIgnoreCase("textures"))
            return;
        this.skin = p;
        if (this.capeURL != null)//This does not work because signature is not valid after it is changed
            try {//TODO: Try to come up with a way to get this to work, that does not require the private mc auth key
                byte[] bytes = Base64.decode(this.skin.getValue().getBytes("UTF-8"));
                String value = new String(bytes);
                int s = value.indexOf("CAPE");
                value = value.substring(0, (s == -1 ? value.length() : s) - 2) + ",\"CAPE\":{\"url\":\"" + this.capeURL + "\"}}}";
                this.skin = new Property("textures", new String(Base64.encode(value.getBytes("UTF-8"))), this.skin.getSignature());
            } catch (Exception e) {
                e.printStackTrace();
            }
        this.profile.getProperties().put("textures", this.skin);
        MCThunder.getEntryListHandler().refresh(this);
        ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
        this.entityID = getNextID();
        long chunk = getChunk();
        for (Player pl : MCThunder.getPlayers())
            if (!pl.getUniqueID().equals(getUniqueID())) {
                pl.sendPacket(destroyEntitiesPacket);
                if (pl.getWorld().equals(getWorld()) && pl.isColumnLoaded(chunk))
                    for (Packet packet : getPackets())
                        pl.sendPacket(packet);
            }
        sendPacket(new ServerRespawnPacket(getWorld().getDimension(), getWorld().getDifficulty(), getGameMode(), getWorld().getWorldType()));
        updateInventory();
        sendPacket(new ServerPlayerPositionRotationPacket(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch()));
        sendPacket(new ServerSpawnPositionPacket(this.location.getPosition()));
    }

    public void setSkin(String name) {
        setSkin(Utils.getUUIDfromString(name));
    }

    public void setSkin(UUID uuid) {
        setSkin(Utils.getSkin(this.skinUUID = uuid));
    }

    public void removeSkin() {
        setSkin(this.origSkin);
    }

    public void openInventory(Inventory inv) {
        if (inv == null)
            return;
        this.openInventory = inv;
        sendPacket(this.openInventory.getView());
        for (int i = 0; i < this.openInventory.getSize(); i++)
            sendPacket(new ServerSetSlotPacket(this.openInventory.getID(), i, this.openInventory.getItemAt(i).getItemStack()));
    }

    public void updateInventory() {
        for (int i = 0; i < this.inv.getSize(); i++)
            sendPacket(new ServerSetSlotPacket(0, i, this.inv.getItemAt(i).getItemStack()));
    }

    public Inventory getOpenInventory() {
        return this.openInventory;
    }

    public Inventory getEnderChest() {
        return this.enderchest;
    }

    public void showCape(boolean show) {
        this.metadata.setBit(16, 0x02, this.hideCape = !show);
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 1 : this.skinFlags & ~1));
        updateMetadata();
    }

    public void showJacket(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 2 : this.skinFlags & ~2));
        updateMetadata();
    }

    public void showLeftSleeve(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 4 : this.skinFlags & ~4));
        updateMetadata();
    }

    public void showRightSleeve(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 8 : this.skinFlags & ~8));
        updateMetadata();
    }

    public void showLeftPantLeg(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 16 : this.skinFlags & ~16));
        updateMetadata();
    }

    public void showRightPantLeg(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 32 : this.skinFlags & ~32));
        updateMetadata();
    }

    public void showHat(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 64 : this.skinFlags & ~64));
        updateMetadata();
    }

    @Override
    public void ai() { }//Do nothing as the player themselves is the intelligence

    public CompoundTag getNBT() {//TODO: Correct the values that are just 0 to be based on variables
        CompoundTag nbt = super.getNBT();
        nbt.remove("id");
        nbt.remove("CustomName");
        nbt.remove("CustomNameVisible");
        nbt.remove("Equipment");
        nbt.remove("DropChances");
        nbt.remove("CanPickUpLoot");
        nbt.remove("PersistenceRequired");
        nbt.remove("Leashed");
        nbt.remove("Leash");
        nbt.put(new IntTag("playerGameType", this.gamemode.equals(GameMode.CREATIVE) ? 1 : this.gamemode.equals(GameMode.ADVENTURE) ? 2 :
                this.gamemode.equals(GameMode.SPECTATOR) ? 3 : 0));
        nbt.put(new IntTag("Score", this.score));
        nbt.put(new IntTag("SelectedItemSlot", this.slot - 36));
        CompoundTag selectedItem = new CompoundTag("SelectedItem");
        selectedItem.put(new ByteTag("Count", (byte) this.getHeldItem().getAmount()));
        selectedItem.put(new ShortTag("Damage", this.getHeldItem().getType().getData()));
        selectedItem.put(new StringTag("id", "minecraft:" + this.getHeldItem().getType().getParent().getName().toLowerCase()));
        nbt.put(selectedItem);
        if (this.bedSpawn != null) {
            nbt.put(new IntTag("SpawnX", (int) this.bedSpawn.getX()));
            nbt.put(new IntTag("SpawnY", (int) this.bedSpawn.getY()));
            nbt.put(new IntTag("SpawnZ", (int) this.bedSpawn.getZ()));
        }
        nbt.put(new ByteTag("SpawnForced", (byte) 0));
        nbt.put(new ByteTag("Sleeping", (byte) 0));
        nbt.put(new ShortTag("SleepTimer", (short) 0));
        nbt.put(new IntTag("foodLevel", this.hunger));
        nbt.put(new FloatTag("foodExhaustionLevel", 0));
        nbt.put(new FloatTag("foodSaturationLevel", 0));
        nbt.put(new IntTag("foodTickTimer", 0));
        nbt.put(new IntTag("XpLevel", 0));
        nbt.put(new FloatTag("XpP", 0));
        nbt.put(new IntTag("XpTotal", 0));
        nbt.put(new IntTag("XpSeed", 0));
        nbt.put(this.inv.getItemList("Inventory"));
        nbt.put(this.enderchest.getItemList("EnderItems"));
        CompoundTag abilities = new CompoundTag("abilities");
        abilities.put(new FloatTag("walkSpeed", (float) 0.1));
        abilities.put(new FloatTag("flySpeed", (float) 0.05));
        abilities.put(new ByteTag("mayfly", (byte) 0));
        abilities.put(new ByteTag("flying", (byte) 0));
        abilities.put(new ByteTag("invulnerable", (byte) 0));
        abilities.put(new ByteTag("mayBuild", (byte) 0));
        abilities.put(new ByteTag("instabuild", (byte) 0));
        nbt.put(abilities);
        nbt.put(new StringTag("SpawnWorld", getWorld().getName()));
        nbt.put(new StringTag("BedSpawnWorld", getWorld().getName()));
        nbt.put(new StringTag("UUID", this.uuid.toString()));//Overrides entity tag with this name
        nbt.put(new StringTag("SkinUUID", this.skinUUID.toString()));
        nbt.put(new StringTag("DisplayName", this.displayName));
        return nbt;
    }

    public void saveToFile() {
        PlayerProfileHandler profileHandler = MCThunder.getProfileHandler();
        CompoundTag nbt = getNBT();
        for (Tag value : nbt.getValue().values())
            profileHandler.changeAttribute(this, value);
    }
}