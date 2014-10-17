package net.mcthunder.apis;

import net.mcthunder.handlers.ServerChatHandler;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Player {
    private int entityID;
    private int heldItem;
    private GameProfile gameProfile;
    private Session session;
    private Server server;
    private MetadataMap metadata;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;
    private boolean onGround;
	private boolean sneaking;
	private boolean sprinting;
    private ServerChatHandler chatHandler;

    public Player(Server server, Session session, GameProfile profile, int entityID, int heldItem, EntityMetadata metadata) {
        this.chatHandler = new ServerChatHandler();
        this.server = server;
        this.session = session;
        this.gameProfile = profile;
        this.entityID = entityID;
        this.heldItem = heldItem;
		this.metadata = new MetadataMap();
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

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return (float) this.yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return (float) this.pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
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

    public void sendMessageToPlayer(String message) {
        this.chatHandler.sendPrivateMessage(this.session, message);
    }
}