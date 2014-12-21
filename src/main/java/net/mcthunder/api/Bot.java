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
    private final int entityID;
    private MetadataMap metadata;
    private GameProfile botProfile;
    private boolean hasAI = false;
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
        this.metadata = new MetadataMap();
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ON_FIRE, false);//on fire
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, false);//sneaking
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, false);//sprinting
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ARM_UP, false);//Eating, drinking, blocking
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.INVISIBLE, false);//invisible
        this.metadata.setMetadata(1, (short) 0);//airLeft
        this.metadata.setMetadata(2, this.name);
        this.metadata.setMetadata(3, (byte) 1);//Always show name
        this.metadata.setMetadata(6, (float) 20);//health
        this.metadata.setMetadata(7, 0);//potionColor
        this.metadata.setMetadata(8, (byte) 0);//potion ambient = false
        this.metadata.setMetadata(9, (byte) 0);//arrows in bot
        this.metadata.setMetadata(15, (byte) (this.hasAI ? 1 : 0));
        //this.metadata.setMetadata(10, (byte) 0);//Unsigned byte for skin flags TODO: Figure out what to put here
        this.metadata.setBit(16, 0x02, false);//TODO: Read cape of the one whose name bot has
        this.metadata.setMetadata(17, (float) 0);//absorption
        this.metadata.setMetadata(18, 0);//score
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
            ServerSpawnPlayerPacket spawnPlayerPacket = (ServerSpawnPlayerPacket) getPacket();
            long chunk = getChunk();
            for (Player pl : MCThunder.getPlayers()) {
                pl.sendPacket(destroyEntitiesPacket);
                if (pl.getWorld().equals(getWorld()) && pl.isColumnLoaded(chunk))
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
        return new ServerSpawnPlayerPacket(this.entityID, this.uuid, getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch(), 0, getMetadata().getMetadataArray());
    }

    public World getWorld() {
        return getLocation().getWorld();
    }

    public abstract void unload();

    public abstract void load();

    public long getChunk() {
        if (this.location == null)
            return 0;
        return Utils.getLong((int) this.location.getX() >> 4, (int) this.location.getZ() >> 4);
    }

    public MetadataMap getMetadata() {
        return this.metadata;
    }
}