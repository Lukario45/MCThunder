package net.mcthunder.api;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.auth.util.Base64;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.packetlib.packet.Packet;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public abstract class Bot {
    private int entityID;
    private MetadataMap metadata;
    private GameProfile botProfile;
    private byte skinFlags;
    private String capeURL = null;
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
        String temp = MessageFormat.formatMessage(this.name).getFullText().trim();
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
        this.metadata.setBit(16, 0x02, false);
        this.metadata.setMetadata(17, (float) 0);//absorption
        this.metadata.setMetadata(18, 0);//score
    }

    public PlayerListEntry getListEntry() {
        return new PlayerListEntry(getGameProfile(), GameMode.CREATIVE, 0, MessageFormat.formatMessage(this.name));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
        this.botProfile = new GameProfile(this.uuid, MessageFormat.formatMessage(this.name).getFullText().trim());
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
        if (this.capeURL != null)//This does not work because signature is not valid after it is changed
            try {//TODO: Try to come up with a way to get this to work, that does not require the private mc auth key
                byte[] bytes = Base64.decode(this.skin.getValue().getBytes("UTF-8"));
                String value = new String(bytes);
                int s = value.indexOf("CAPE");
                if (s == -1)
                    value = value.substring(0, value.length() - 2) + ",\"CAPE\":{\"url\":\"" + this.capeURL + "\"}}}";
                else
                    value = value.substring(0, s - 2) + ",\"CAPE\":{\"url\":\"" + this.capeURL + "\"}}}";
                this.skin = new Property("textures", new String(Base64.encode(value.getBytes("UTF-8"))), this.skin.getSignature());
            } catch (Exception e) {
                e.printStackTrace();
            }
        this.botProfile.getProperties().put("textures", this.skin);
        MCThunder.getEntryListHandler().refresh(this);
        if (isEntitySpawned()) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(this.entityID);
            this.entityID = Entity.getNextID();
            long chunk = getChunk();
            for (Player pl : MCThunder.getPlayers()) {
                pl.sendPacket(destroyEntitiesPacket);
                if (pl.getWorld().equals(getWorld()) && pl.isColumnLoaded(chunk))
                    for (Packet packet : getPackets())
                        pl.sendPacket(packet);
            }
        }
    }

    @Deprecated
    public void setCape(String url) {
        this.capeURL = url;
        setSkin(this.skin);
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
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                for (Packet packet : getPackets())
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

    public Collection<Packet> getPackets() {
        return Arrays.asList(getPacket(), new ServerEntityMetadataPacket(this.entityID, getMetadata().getMetadataArray()),
                new ServerEntityTeleportPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch(), true));
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