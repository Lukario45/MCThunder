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
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.packet.Packet;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static net.mcthunder.api.Utils.getLong;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Player extends LivingEntity {
    private final UUID uuid;
    private final GameProfile origProfile;
    private final String name;
    private final Property origSkin;
    private final String capeURL = null;
    private GameProfile profile;
    private UUID skinUUID;
    private Property skin;
    private byte skinFlags;
    private NBTFile playerFile;
    private Inventory openInventory = null;
    private ArrayList<Long> loadedColumns = new ArrayList<>();
    private ArrayList<Long> northColumns = new ArrayList<>();
    private ArrayList<Long> eastColumns = new ArrayList<>();
    private ArrayList<Long> southColumns = new ArrayList<>();
    private ArrayList<Long> westColumns = new ArrayList<>();
    private Inventory inv;
    private int viewDistance = 9;
    private int slot, ping, score;
    private String displayName;
    private GameMode gamemode;
    private Session session;
    private boolean moveable, hideCape;
    private Player lastPmPerson;
    private String appended = "";

    public Player(Session session) {
        super(null);
        this.session = session;
        this.profile = getSession().getFlag(ProtocolConstants.PROFILE_KEY);
        this.origProfile = this.profile;
        this.uuid = getGameProfile().getId();
        this.metadata.setMetadata(2, this.name = getGameProfile().getName());
        this.metadata.setMetadata(3, (byte) 1);//Always show name
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
        this.metadata.setMetadata(10, this.skinFlags = (byte) 127);
        this.metadata.setMetadata(15, (byte) 1);//Assumes the player has a brain
        this.metadata.setBit(16, 0x02, this.hideCape = false);
        this.metadata.setMetadata(17, (float) (this.activeEffects.containsKey(PotionEffectType.ABSORPTION) ? this.activeEffects.get(PotionEffectType.ABSORPTION).getAmplifier() : 0));
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
                    this.loadedColumns.add(column);
                }
    }

    public void teleport(Location l) {
        long chunk = getChunk();
        if (!l.getWorld().equals(getWorld())) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
            this.entityID = getNextID();//Is this needed? depends on how when going back and forth between worlds we do it
            for (Player p : MCThunder.getPlayers()) {
                p.sendPacket(destroyEntitiesPacket);
                if (p.getWorld().equals(l.getWorld()) && p.isColumnLoaded(chunk))
                    for (Packet packet : getPackets())
                        p.sendPacket(packet);
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

    public void setDisplayName(String displayName) {//TODO: Save nick to file
        this.displayName = displayName == null ? this.name : displayName;
        PropertyMap properties = (PropertyMap) this.profile.getProperties().clone();
        this.profile = this.displayName == null ? this.origProfile : new GameProfile(this.uuid, MessageFormat.formatMessage(getDisplayName()).getFullText().trim());
        for (String key : properties.keySet())
            this.profile.getProperties().put(key, properties.get(key));
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
        sendPacket(new ServerPlayerPositionRotationPacket(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch()));
        sendPacket(new ServerSpawnPositionPacket(this.location.getPosition()));
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
        sendPacket(new ServerChatPacket(MessageFormat.formatMessage(message)));
    }

    public Player getLastPmPerson() {
        return this.lastPmPerson;
    }

    public void setLastPmPerson(Player lastPmPerson) {
        this.lastPmPerson = lastPmPerson;
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
        for (int i = 0; i < this.openInventory.getItems().length; i++)
            sendPacket(new ServerSetSlotPacket(this.openInventory.getID(), i, this.openInventory.getItemAt(i).getIS()));
    }

    public Inventory getEnderChest() {
        Inventory e = new ChestInventory("EnderChest");
        //TODO: Retrieve enderchest data
        return e;
    }

    public void showCape(boolean show) {
        this.metadata.setBit(16, 0x02, this.hideCape = !show);
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 1 : this.skinFlags & ~1));
        updateMetadata();
    }

    public void showJacket(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? (this.skinFlags | 2) : this.skinFlags & ~2));
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
}