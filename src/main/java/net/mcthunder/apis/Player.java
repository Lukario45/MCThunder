package net.mcthunder.apis;

import org.spacehq.mc.auth.GameProfile;
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

    public Player(Server server, Session session, GameProfile profile, int entityID, int heldItem) {
        this.server = server;
        this.session = session;
        this.gameProfile = profile;
        this.entityID = entityID;
        this.heldItem = heldItem;

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
}
