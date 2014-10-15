package net.mcthunder.apis;

import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Player {
    private int entityID;
    private int heldItem;
    private GameProfile gameProfile;
    private Session session;
    private Server server;
    private EntityMetadata[] metadata;
    private List<EntityMetadata> metadataList;
    //private EntityMetadata metadata;

    public Player(Server server, Session session, GameProfile profile, int entityID, int heldItem, EntityMetadata metadata) {
        this.metadataList = new ArrayList<>();


        this.server = server;
        this.session = session;
        this.gameProfile = profile;
        this.entityID = entityID;
        this.heldItem = heldItem;
        this.metadataList.add(metadata);


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
        return this.getSession();
    }

    public GameProfile gameProfile() {
        return this.gameProfile;
    }

    public EntityMetadata[] getMetadata() {
        this.metadata = metadataList.toArray(new EntityMetadata[metadataList.size()]);
        return this.metadata;
    }

    public void addMetadata(EntityMetadata metadata) {
        this.metadataList.add(metadata);

    }
}
