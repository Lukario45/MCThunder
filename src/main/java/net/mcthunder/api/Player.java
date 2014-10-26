package net.mcthunder.api;

import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.world.Column;
import net.mcthunder.world.Region;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import java.util.HashMap;
import static net.mcthunder.api.Utils.getLong;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Player {
    private HashMap<Long, Column> columnHashMap = new HashMap<>();
    private int entityID;
    private int heldItem;
    private GameProfile gameProfile;
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
        this.entityID = entityID;
        this.heldItem = heldItem;
        this.metadata = new MetadataMap();
    }

    public void loadChunks(int distance) {
        int x = (int)getLocation().getX() / 16;
        int z = (int)getLocation().getZ() / 16;
        for(int xAdd = -distance; xAdd < distance; xAdd++)
            for(int zAdd = -distance; zAdd < distance; zAdd++) {
                int regionX = (x + xAdd) >> 5;
                int regionZ = (z + zAdd) >> 5;
                long reg = getLong(regionX, regionZ);
                Region r = getWorld().getRegion(reg);
                if (r != null)
                    r.readChunk(getLong(x + xAdd, z + zAdd), this);
            }
    }

    public boolean isColumnLoaded(Long l) {
        return this.columnHashMap.containsKey(l);
    }

    public void addColumn(Column c) {
        this.columnHashMap.put(getLong(c.getX(), c.getZ()), c);
        getSession().send(new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks(), new byte[256]));
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
        this.location.setWorld(w);
    }

    public String getAppended() {
        return this.appended;
    }

    public void setAppended(String toAppend) {
        this.appended = toAppend;
    }
}
