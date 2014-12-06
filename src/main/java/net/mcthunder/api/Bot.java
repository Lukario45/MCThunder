package net.mcthunder.api;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Player;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.packetlib.packet.Packet;

import java.util.UUID;

import static net.mcthunder.api.Utils.tellConsole;

public abstract class Bot {
    private final int entityID;
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
        this.skinUUID = Utils.getUUIDfromString(this.name);
        if (this.skinUUID == null)
            this.skinUUID = this.uuid;
        this.botProfile = new GameProfile(this.uuid, this.name);
        this.entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
        setSkin(this.uuid);
        load();
    }

    public PlayerListEntry getListEntry() {
        return new PlayerListEntry(getGameProfile(), GameMode.CREATIVE, 0, Message.fromString(this.name));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
        this.botProfile = new GameProfile(this.uuid, this.name);
        this.botProfile.getProperties().put("textures", this.skin);
    }

    public GameProfile getGameProfile() {
        return this.botProfile;
    }

    public boolean isEntitySpawned() {
        return this.entitySpawned;
    }

    public void setSkin(Property p) {
        if (p == null || !p.getName().equalsIgnoreCase("textures"))
            return;
        this.skin = p;
        this.botProfile.getProperties().put("textures", this.skin);
    }

    public void setSkin(String name) {
        setSkin(Utils.getUUIDfromString(name));
    }

    public void setSkin(UUID uuid) {
        setSkin(Utils.getSkin(uuid));
    }

    public void spawn(Location l) {//TODO: Will also need to send to people when they login also
        if (isEntitySpawned())
            return;
        this.entitySpawned = true;
        this.location = l;
        Packet packet = getPacket();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(this.location.getWorld()))
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