package net.mcthunder.api;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.ProfileTexture;
import org.spacehq.mc.auth.SessionService;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.auth.util.Base64;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.security.PublicKey;
import java.util.UUID;

import static net.mcthunder.api.Utils.tellConsole;

public abstract class Bot {
    protected MessageFormat format = new MessageFormat();
    private int entityID;
    private MetadataMap metadata;
    private GameProfile botProfile;
    private byte skinFlags;
    private String capeURL;
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
        this.entityID = Entity.getNextID();
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
        this.skinFlags = (byte) (1);
        this.skinFlags |= 1 << 1;
        this.skinFlags |= 1 << 2;
        this.skinFlags |= 1 << 3;
        this.skinFlags |= 1 << 4;
        this.skinFlags |= 1 << 5;
        this.skinFlags |= 1 << 6;
        this.metadata.setMetadata(10, this.skinFlags);//Unsigned byte for skin flags
        this.metadata.setMetadata(15, (byte) (this.hasAI ? 1 : 0));
        this.metadata.setBit(16, 0x02, false);//TODO: Read cape of the one whose name bot has
        this.metadata.setMetadata(17, (float) 0);//absorption
        this.metadata.setMetadata(18, 0);//score
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
            this.entityID = Entity.getNextID();
            ServerSpawnPlayerPacket spawnPlayerPacket = (ServerSpawnPlayerPacket) getPacket();
            long chunk = getChunk();
            for (Player pl : MCThunder.getPlayers()) {
                pl.sendPacket(destroyEntitiesPacket);
                if (pl.getWorld().equals(getWorld()) && pl.isColumnLoaded(chunk))
                    pl.sendPacket(spawnPlayerPacket);
            }
        }
    }

    public void setCape(String url) {
        try {//http://textures.minecraft.net/texture/3f688e0e699b3d9fe448b5bb50a3a288f9c589762b3dae8308842122dcb81
            this.capeURL = url;
            byte[] bytes = Base64.decode(this.skin.getValue().getBytes());
            String value = new String(bytes);
            int s = value.indexOf("CAPE");
            if (s == -1)
                value = value.substring(0, value.length() - 2) + ",\"CAPE\":{\"url\":\"" + this.capeURL + "\"}}}";
            else
                value = value.substring(0, s - 1) + ",\"CAPE\":{\"url\":\"" + this.capeURL + "\"}}}";
            setSkin(new Property("textures", new String(Base64.encode(value.getBytes())), this.skin.getSignature()));
        } catch (IOException e) {
            e.printStackTrace();
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
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(packet);
    }

    public Location getLocation() {
        return this.location.clone();
    }

    public void setLocation(Location location) {
        this.location = location.clone();
    }

    public Packet getPacket() {
        return new ServerSpawnPlayerPacket(this.entityID, this.uuid, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), 0, getMetadata().getMetadataArray());
    }

    public World getWorld() {
        return this.location.getWorld();
    }

    public abstract void unload();

    public abstract void load();

    public long getChunk() {
        return this.location == null ? 0 : Utils.getLong((int) this.location.getX() >> 4, (int) this.location.getZ() >> 4);
    }

    public MetadataMap getMetadata() {
        return this.metadata;
    }

    public int getEntityID() {
        return this.entityID;
    }
}