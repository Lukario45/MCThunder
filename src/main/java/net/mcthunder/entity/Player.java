package net.mcthunder.entity;

import com.Lukario45.NBTFile.NBTFile;
import net.mcthunder.MCThunder;
import net.mcthunder.api.*;
import net.mcthunder.inventory.ChestInventory;
import net.mcthunder.inventory.Inventory;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.inventory.PlayerInventory;
import net.mcthunder.world.Column;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.packet.Packet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import static net.mcthunder.api.Utils.getLong;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Player extends LivingEntity {
    private final UUID uuid;
    private final String name;
    private final Property origSkin;
    private GameProfile profile;
    private UUID skinUUID;
    private Property skin;
    private NBTFile playerFile;
    private Inventory openInventory;
    private HashMap<PotionEffectType, PotionEffect> activeEffects = new HashMap<>();
    private ArrayList<Long> loadedColumns = new ArrayList<>();
    private ArrayList<Long> northColumns = new ArrayList<>();
    private ArrayList<Long> eastColumns = new ArrayList<>();
    private ArrayList<Long> southColumns = new ArrayList<>();
    private ArrayList<Long> westColumns = new ArrayList<>();
    private Inventory inv;
    private int viewDistance = 9;
    private int slot;
    private int ping;
    private String displayName;
    private GameMode gamemode;
    private Session session;
    private boolean moveable;
    private boolean hideCape;
    private float absorption;
    private int score;
    private Player lastPmPerson;
    private String appended = "";

    public Player(Session session) {
        super(null);
        this.session = session;
        this.profile = getSession().getFlag(ProtocolConstants.PROFILE_KEY);
        this.uuid = getGameProfile().getId();
        this.name = getGameProfile().getName();
        this.slot = 36;
        this.displayName = this.name;
        this.gamemode = GameMode.CREATIVE;
        this.moveable = false;
        this.inv = new PlayerInventory(44, this.name, this);
        this.ping = getSession().getFlag(ProtocolConstants.PING_KEY);
        this.playerFile = new NBTFile(new File("PlayerFiles", this.uuid + ".dat"), "Player");
        this.skinUUID = this.uuid;
        this.origSkin = getGameProfile().getProperties().get("textures");
        this.skin = this.origSkin;
        this.openInventory = null;
        this.metadata.setMetadata(2, this.name);
        this.metadata.setMetadata(3, (byte) 1);//Always show name
        this.metadata.setMetadata(10, (byte) 0);//Unsigned byte for skin flags TODO: Figure out what to put here
        this.metadata.setMetadata(15, (byte) 1);//Assuming player has a brain
        this.metadata.setBit(16, 0x02, this.hideCape = false);
        this.metadata.setMetadata(17, this.absorption = 0);
        this.metadata.setMetadata(18, this.score = 0);
    }

    public NBTFile getPlayerFile() {
        return this.playerFile;
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
        if (!preLoaded(d)) {
            int xMod = 0;
            int zMod = 0;
            if (d.equals(Direction.NORTH))
                zMod = -1;
            else if (d.equals(Direction.EAST))
                xMod = 1;
            else if (d.equals(Direction.SOUTH))
                zMod = 1;
            else if (d.equals(Direction.WEST))
                xMod = -1;
            int x = (int) getLocation().getX() >> 4;
            int z = (int) getLocation().getZ() >> 4;
            for (int xAdd = -getView() + xMod; xAdd < getView() + xMod; xAdd++)
                for (int zAdd = -getView() + zMod; zAdd < getView() + zMod; zAdd++)
                    getWorld().getRegion(getLong((x + xAdd) >> 5, (z + zAdd) >> 5)).readChunk(getLong(x + xAdd, z + zAdd), this, d, false);
        }
        sendColumns(d);
    }

    private boolean preLoaded(Direction d) {
        if (d.equals(Direction.NORTH))
            return !this.northColumns.isEmpty();
        else if (d.equals(Direction.EAST))
            return !this.eastColumns.isEmpty();
        else if (d.equals(Direction.SOUTH))
            return !this.southColumns.isEmpty();
        else if (d.equals(Direction.WEST))
            return !this.westColumns.isEmpty();
        return false;
    }

    private void updateDir(Direction d) {
        int x = (int)getLocation().getX() >> 4;
        int z = (int)getLocation().getZ() >> 4;
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
            for (Player player1 : MCThunder.getPlayers())
                if (player1.getWorld().equals(getWorld()) && l == player1.getChunk() && !player1.getUniqueID().equals(getUniqueID()))
                    sendPacket(new ServerDestroyEntitiesPacket(player1.getEntityID()));
            for (Bot b : MCThunder.getBots())
                if (b.getWorld().equals(getWorld()) && l == b.getChunk())
                    sendPacket(new ServerDestroyEntitiesPacket(b.getEntityID()));
            for (Entity e : getWorld().getEntities())
                if (l == e.getChunk())
                    sendPacket(new ServerDestroyEntitiesPacket(e.getEntityID()));
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
                sendPacket(player1.getPacket());
        for (Bot b : MCThunder.getBots())
            if (b.getWorld().equals(getWorld()) && isColumnLoaded(b.getChunk()))
                sendPacket(b.getPacket());
        for (Entity e : getWorld().getEntities())
            if (isColumnLoaded(e.getChunk()))
                for (Packet packet : e.getPackets())
                    sendPacket(packet);
    }

    private void sendColumns(Direction d) {
        if (d.equals(Direction.NORTH))
            sendColumns(this.northColumns);
        else if (d.equals(Direction.EAST))
            sendColumns(this.eastColumns);
        else if (d.equals(Direction.SOUTH))
            sendColumns(this.southColumns);
        else if (d.equals(Direction.WEST))
            sendColumns(this.westColumns);
    }

    private void sendColumns(ArrayList<Long> columns) {
        for (long column : columns)
            if (!isColumnLoaded(column)) {
                Column c = getWorld().getColumn(column);
                sendPacket(new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks(), c.getBiomes()));
                for (Player player1 : MCThunder.getPlayers())
                    if (player1.getWorld().equals(getWorld()) && column == player1.getChunk() && !player1.getUniqueID().equals(getUniqueID()))
                        sendPacket(player1.getPacket());
                for (Bot b : MCThunder.getBots())
                    if (b.getWorld().equals(getWorld()) && column == b.getChunk())
                        sendPacket(b.getPacket());
                for (Entity e : getWorld().getEntities())
                    if (column == e.getChunk())
                        for (Packet packet : e.getPackets())
                            sendPacket(packet);
                this.loadedColumns.add(column);
            }
    }

    public void teleport(Location l) {
        long chunk = getChunk();
        if (!l.getWorld().equals(getWorld())) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
            Packet respawn = getPacket();
            if (respawn != null)
                for (Player p : MCThunder.getPlayers()) {
                    p.sendPacket(destroyEntitiesPacket);
                    if (p.getWorld().equals(l.getWorld()) && p.isColumnLoaded(chunk))
                        p.sendPacket(respawn);
                }
            sendPacket(new ServerRespawnPacket(l.getWorld().getDimension(), l.getWorld().getDifficulty(), getGameMode(), l.getWorld().getWorldType()));
        } else {
            ServerEntityTeleportPacket packet = new ServerEntityTeleportPacket(getEntityID(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), isOnGround());
            for (Player p : MCThunder.getPlayers())
                if (p.getWorld().equals(l.getWorld()) && p.isColumnLoaded(chunk))
                    p.sendPacket(packet);
        }
        setLocation(l);
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
        return profile;
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

    public String getDisplayName() {
        return this.displayName;
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

    public void sendMessage(String message) {
        sendPacket(new ServerChatPacket(new MessageFormat().formatMessage(message)));
    }

    public Player getLastPmPerson() {
        return this.lastPmPerson;
    }

    public void setLastPmPerson(Player lastPmPerson) {
        this.lastPmPerson = lastPmPerson;
    }

    public void setWorld(World w) {
        super.setWorld(w);
        sendPacket(new ServerRespawnPacket(getWorld().getDimension(), getWorld().getDifficulty(), getGameMode(), getWorld().getWorldType()));
        sendPacket(new ServerPlayerPositionRotationPacket(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch()));
        sendPacket(new ServerSpawnPositionPacket(this.location.getPosition()));
        //Also will need to remove loaded chunks and load new ones
    }

    public String getAppended() {
        return this.appended;
    }

    public void setAppended(String toAppend) {
        this.appended = toAppend;
    }

    public void addPotionEffect(PotionEffect p) {
        this.activeEffects.put(p.getType(), p);
        //TODO: Have the duration tick down and then run out also for all potion effect stuff have it actually send effects and things to player
    }

    public void removePotionEffect(PotionEffectType p) {
        this.activeEffects.remove(p);
    }

    public void clearPotionEffects() {
        this.activeEffects.clear();
    }

    public Collection<PotionEffect> getActiveEffects() {
        return this.activeEffects.values();
    }

    public void sendPacket(Packet p) {
        if (p == null)
            return;
        getSession().send(p);
    }

    public PlayerListEntry getListEntry() {
        return new PlayerListEntry(getGameProfile(), getGameMode(), getPing(), Message.fromString(getName()));
    }

    protected void setSkin(Property p) {
        if (p == null || !p.getName().equalsIgnoreCase("textures"))
            return;
        this.skin = p;
        this.profile.getProperties().put("textures", this.skin);
        MCThunder.getEntryListHandler().refresh(this);
        ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
        ServerSpawnPlayerPacket spawnPlayerPacket = (ServerSpawnPlayerPacket) getPacket();
        for (Player pl : MCThunder.getPlayers())
            if (!pl.getUniqueID().equals(getUniqueID())) {
                pl.sendPacket(destroyEntitiesPacket);
                if (pl.getWorld().equals(getWorld()))
                    pl.sendPacket(spawnPlayerPacket);
            }
        sendPacket(new ServerRespawnPacket(getWorld().getDimension(), getWorld().getDifficulty(), getGameMode(), getWorld().getWorldType()));
        sendPacket(new ServerPlayerPositionRotationPacket(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch()));
        sendPacket(new ServerSpawnPositionPacket(this.location.getPosition()));
    }

    public void setSkin(String name) {
        setSkin(Utils.getUUIDfromString(name));
    }

    public void setSkin(UUID uuid) {
        this.skinUUID = uuid;
        setSkin(Utils.getSkin(this.skinUUID));
    }

    public void removeSkin() {
        setSkin(this.origSkin);
    }

    public void openInventory(Inventory inv) {
        this.openInventory = inv;
        sendPacket(this.openInventory.getView());
        for (int i = 0; i < this.openInventory.getItems().length; i++)
            sendPacket(new ServerSetSlotPacket(this.openInventory.getID(), i, this.openInventory.getItemAt(i).getIS()));
    }

    public Inventory getEnderChest() {
        Inventory e = new ChestInventory("EnderChest");
        //TODO: Retrieve enderchest data
        return e;
    }

    @Override
    public void ai() {
        //Do nothing as the player themselves is the intelligence
    }
}