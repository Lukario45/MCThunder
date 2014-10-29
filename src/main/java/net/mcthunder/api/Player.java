package net.mcthunder.api;

import net.mcthunder.MCThunder;
import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.world.Column;
import net.mcthunder.world.Region;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.UUID;

import static net.mcthunder.api.Utils.*;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Player {
    private ArrayList<Long> loadedColumns = new ArrayList<>();
    private ArrayList<Long> northColumns = new ArrayList<>();
    private ArrayList<Long> eastColumns = new ArrayList<>();
    private ArrayList<Long> southColumns = new ArrayList<>();
    private ArrayList<Long> westColumns = new ArrayList<>();
    private int viewDistance = 9;
    private int entityID;
    private int heldItem;
    private GameProfile gameProfile;
    private final UUID uuid;
    private final String name;
    private String displayName;
    private GameMode gamemode;
    private Session session;
    private Server server;
    private MetadataMap metadata;
    private Location location;
    private boolean onGround;
    private boolean sneaking;
    private boolean sprinting;
    private ServerChatHandler chatHandler;
    private Player lastPmPerson;
    private String appended = "";

    public Player(Server server, Session session, GameProfile profile, int entityID, int heldItem, EntityMetadata metadata) {
        this.chatHandler = new ServerChatHandler();
        this.server = server;
        this.session = session;
        this.gameProfile = profile;
        this.uuid = this.gameProfile.getId();
        this.name = this.gameProfile.getName();
        this.displayName = this.name;
        this.entityID = entityID;
        this.heldItem = heldItem;
        this.metadata = new MetadataMap();
        this.gamemode = GameMode.CREATIVE;
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
                for (int zAdd = -getView() + zMod; zAdd < getView() + zMod; zAdd++) {
                    Region r = getWorld().getRegion(getLong((x + xAdd) >> 5, (z + zAdd) >> 5));
                    if (r != null)
                        r.readChunk(getLong(x + xAdd, z + zAdd), this, d, false);
                }
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
        if (d.equals(Direction.NORTH)) {
            for (long l : this.southColumns)
                getWorld().unloadColumn(l);
            this.northColumns.clear();
            this.southColumns.clear();
            for(int xAdd = -getView(); xAdd < getView(); xAdd++) {
                Region r = getWorld().getRegion(getLong((x + xAdd) >> 5, (z - getView() - 1) >> 5));
                if (r != null)
                    r.readChunk(getLong(x + xAdd, z - getView() - 1), this, d, false);
                Region rOld = getWorld().getRegion(getLong((x + xAdd) >> 5, (z + getView()) >> 5));
                if (rOld != null)
                    rOld.readChunk(getLong(x + xAdd, z + getView()), this, Direction.SOUTH, true);
            }
        } else if (d.equals(Direction.EAST)) {
            for (long l : this.westColumns)
                getWorld().unloadColumn(l);
            this.eastColumns.clear();
            this.westColumns.clear();
            for(int zAdd = -getView(); zAdd < getView(); zAdd++) {
                Region r = getWorld().getRegion(getLong((x + getView() + 1) >> 5, (z + zAdd) >> 5));
                if (r != null)
                    r.readChunk(getLong(x + getView() + 1, z + zAdd), this, d, false);
                Region rOld = getWorld().getRegion(getLong((x - getView()) >> 5, (z + zAdd) >> 5));
                if (rOld != null)
                    rOld.readChunk(getLong(x - getView(), z + zAdd), this, Direction.WEST, true);
            }
        } else if (d.equals(Direction.SOUTH)) {
            for (long l : this.northColumns)
                getWorld().unloadColumn(l);
            this.southColumns.clear();
            this.northColumns.clear();
            for(int xAdd = -getView(); xAdd < getView(); xAdd++) {
                Region r = getWorld().getRegion(getLong((x + xAdd) >> 5, (z + getView() + 1) >> 5));
                if (r != null)
                    r.readChunk(getLong(x + xAdd, z - getView() - 1), this, d, false);
                Region rOld = getWorld().getRegion(getLong((x + xAdd) >> 5, (z - getView()) >> 5));
                if (rOld != null)
                    rOld.readChunk(getLong(x + xAdd, z - getView()), this, Direction.NORTH, true);
            }
        } else if (d.equals(Direction.WEST)) {
            for (long l : this.eastColumns)
                getWorld().unloadColumn(l);
            this.westColumns.clear();
            this.eastColumns.clear();
            for(int zAdd = -getView(); zAdd < getView(); zAdd++) {
                Region r = getWorld().getRegion(getLong((x - getView() - 1) >> 5, (z + zAdd) >> 5));
                if (r != null)
                    r.readChunk(getLong(x - getView() - 1, z + zAdd), this, d, false);
                Region rOld = getWorld().getRegion(getLong((x + getView()) >> 5, (z + zAdd) >> 5));
                if (rOld != null)
                    rOld.readChunk(getLong(x + getView(), z + zAdd), this, Direction.EAST, true);
            }
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
            getSession().send(new ServerChunkDataPacket(getWorld().getColumn(l).getX(), getWorld().getColumn(l).getZ()));
        }
    }

    public void refreshColumn(Column c) {
        getSession().send(new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks()));
    }

    private void sendColumns(Direction d) {
        /*int[] x = null;
        int[] z = null;
        Chunk[][] chunks = null;
        byte[][] biomeData = null;
        int size = 0;
        if (d.equals(Direction.NORTH))
            size = this.northColumns.size();
        else if (d.equals(Direction.EAST))
            size = this.eastColumns.size();
        else if (d.equals(Direction.SOUTH))
            size = this.southColumns.size();
        else if (d.equals(Direction.WEST))
            size = this.westColumns.size();
        x = new int[size];
        z = new int[size];
        chunks = new Chunk[size][];
        biomeData = new byte[size][];*/
        if (d.equals(Direction.NORTH)) {
            for (int i = 0; i < this.northColumns.size(); i++) {
                long pos = this.northColumns.get(i);
                if (!isColumnLoaded(pos)) {
                    /*x[i] = getWorld().getColumn(pos).getX();
                    z[i] = getWorld().getColumn(pos).getZ();
                    chunks[i] = getWorld().getColumn(pos).getChunks();
                    biomeData[i] = getWorld().getColumn(pos).getBiomes();*/
                    getSession().send(new ServerChunkDataPacket(getWorld().getColumn(pos).getX(), getWorld().getColumn(pos).getZ(), getWorld().getColumn(pos).getChunks(), getWorld().getColumn(pos).getBiomes()));
                    this.loadedColumns.add(pos);
                }
            }
        } else if (d.equals(Direction.EAST)) {
            for (int i = 0; i < this.eastColumns.size(); i++) {
                long pos = this.eastColumns.get(i);
                if (!isColumnLoaded(pos)) {
                    /*x[i] = getWorld().getColumn(pos).getX();
                    z[i] = getWorld().getColumn(pos).getZ();
                    chunks[i] = getWorld().getColumn(pos).getChunks();
                    biomeData[i] = getWorld().getColumn(pos).getBiomes();*/
                    getSession().send(new ServerChunkDataPacket(getWorld().getColumn(pos).getX(), getWorld().getColumn(pos).getZ(), getWorld().getColumn(pos).getChunks(), getWorld().getColumn(pos).getBiomes()));
                    this.loadedColumns.add(pos);
                }
            }
        } else if (d.equals(Direction.SOUTH)) {
            for (int i = 0; i < this.southColumns.size(); i++) {
                long pos = this.southColumns.get(i);
                if (!isColumnLoaded(pos)) {
                    /*x[i] = getWorld().getColumn(pos).getX();
                    z[i] = getWorld().getColumn(pos).getZ();
                    chunks[i] = getWorld().getColumn(pos).getChunks();
                    biomeData[i] = getWorld().getColumn(pos).getBiomes();*/
                    getSession().send(new ServerChunkDataPacket(getWorld().getColumn(pos).getX(), getWorld().getColumn(pos).getZ(), getWorld().getColumn(pos).getChunks(), getWorld().getColumn(pos).getBiomes()));
                    this.loadedColumns.add(pos);
                }
            }
        } else if (d.equals(Direction.WEST)) {
            for (int i = 0; i < this.westColumns.size(); i++) {
                long pos = this.westColumns.get(i);
                if (!isColumnLoaded(pos)) {
                    /*x[i] = getWorld().getColumn(pos).getX();
                    z[i] = getWorld().getColumn(pos).getZ();
                    chunks[i] = getWorld().getColumn(pos).getChunks();
                    biomeData[i] = getWorld().getColumn(pos).getBiomes();*/
                    getSession().send(new ServerChunkDataPacket(getWorld().getColumn(pos).getX(), getWorld().getColumn(pos).getZ(), getWorld().getColumn(pos).getChunks(), getWorld().getColumn(pos).getBiomes()));
                    this.loadedColumns.add(pos);
                }
            }
        }
        //getSession().send(new ServerMultiChunkDataPacket(x, z, chunks, biomeData));
    }

    public int getView() {
        return this.viewDistance;
    }

    public void setView(int distance) {
        if (MCThunder.maxRenderDistance() < distance)
            distance = MCThunder.maxRenderDistance();
        this.viewDistance = distance;
    }

    public int getHeldItem() {
        return this.heldItem;
    }

    public void setHeldItem(int newItem) {
        this.heldItem = newItem;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public void setEntityID(int newID) {
        this.entityID = newID;
    }

    public Server getServer() {
        return this.server;
    }

    public Session getSession() {
        return this.session;
    }

    public GameProfile gameProfile() {
        return this.gameProfile;
    }

    public MetadataMap getMetadata() {
        return this.metadata;
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

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, sneaking);
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, sneaking);
    }

    public GameMode getGameMode() {
        return this.gamemode;
    }

    public ServerChatHandler getChatHandler() {
        return this.chatHandler;
    }

    public void sendMessage(String message) {
        this.chatHandler.sendPrivateMessage(this.session, message);
    }

    public Player getLastPmPerson() {
        return this.lastPmPerson;
    }

    public void setLastPmPerson(Player lastPmPerson) {
        this.lastPmPerson = lastPmPerson;
    }

    public World getWorld() {
        return this.location.getWorld();
    }

    public void setWorld(World w) {
        this.location.setWorld(w);//Also will need to remove loaded chunks and load new ones
    }

    public String getAppended() {
        return this.appended;
    }

    public void setAppended(String toAppend) {
        this.appended = toAppend;
    }
}
