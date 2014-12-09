package net.mcthunder.api;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Player;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.packetlib.packet.Packet;

import java.util.UUID;

public abstract class Bot {
    protected MessageFormat format = new MessageFormat();
    private int entityID;
    private GameProfile botProfile;
    private boolean entitySpawned = false;
    private Property skin = null;
    private Location location;
    private String name;
    private UUID skinUUID;
    private UUID uuid;

    public Bot(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        String temp = format.formatMessage(this.name).getFullText().trim();
        this.skinUUID = Utils.getUUIDfromString(temp);
        if (this.skinUUID == null)
            this.skinUUID = this.uuid;
        this.botProfile = new GameProfile(this.uuid, temp);
        this.entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
        setSkin(this.skinUUID);
        load();
    }

    public PlayerListEntry getListEntry() {
        return new PlayerListEntry(getGameProfile(), GameMode.CREATIVE, 0, format.formatMessage(this.name));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
        this.botProfile = new GameProfile(this.uuid, format.formatMessage(this.name).getFullText().trim());
        this.botProfile.getProperties().put("textures", this.skin);
    }

    public GameProfile getGameProfile() {
        return this.botProfile;
    }

    public boolean isEntitySpawned() {
        return this.entitySpawned;
    }

    protected void setSkin(Property p) {
        if (p == null || !p.getName().equalsIgnoreCase("textures"))
            return;
        this.skin = p;
        this.botProfile.getProperties().put("textures", this.skin);
        MCThunder.getEntryListHandler().refresh(this);
        if (isEntitySpawned()) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(this.entityID);
            this.entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
            ServerSpawnPlayerPacket spawnPlayerPacket = (ServerSpawnPlayerPacket) getPacket();
            for (Player pl : MCThunder.getPlayers()) {
                pl.sendPacket(destroyEntitiesPacket);
                if (pl.getWorld().equals(getWorld()))
                    pl.sendPacket(spawnPlayerPacket);
            }
        }
    }

    public void setSkin(String name) {
        setSkin(Utils.getUUIDfromString(name));
    }

    public void setSkin(UUID uuid) {
        this.skinUUID = uuid;
        setSkin(Utils.getSkin(this.skinUUID));
    }

    public void spawn(Location l) {
        if (isEntitySpawned())
            return;
        this.entitySpawned = true;
        this.location = l;
        ServerSpawnPlayerPacket packet = (ServerSpawnPlayerPacket) getPacket();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()))
                p.sendPacket(packet);
    }

    public Location getLocation() {
        return this.location.clone();
    }

    public void setLocation(Location location) {
        this.location = location.clone();
    }

    public Packet getPacket() {
        return new ServerSpawnPlayerPacket(this.entityID, this.uuid, getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch(), 0, new MetadataMap().getMetadataArray());
    }

    public World getWorld() {
        return getLocation().getWorld();
    }

    public abstract void unload();

    public abstract void load();
}