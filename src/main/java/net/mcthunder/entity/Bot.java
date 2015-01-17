package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.MessageFormat;
import net.mcthunder.api.PotionEffectType;
import net.mcthunder.api.Utils;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.auth.util.Base64;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

import java.util.UUID;

public abstract class Bot extends LivingEntity {
    private boolean entitySpawned = false, hideCape = false;
    private String name, capeURL = null;
    private GameProfile botProfile;
    private Property skin = null;
    private byte skinFlags = 127;
    private UUID skinUUID, uuid;

    public Bot(String name) {
        super(null);
        this.uuid = UUID.randomUUID();
        this.name = name;
        String uncoloredName = MessageFormat.formatMessage(this.name).getFullText().trim();
        this.metadata.setMetadata(2, uncoloredName.length() > 16 ? uncoloredName.substring(0, 16) : uncoloredName);
        this.metadata.setMetadata(3, (byte) 1);//Always show name
        this.skinUUID = Utils.getUUIDfromString(uncoloredName);
        if (this.skinUUID == null)
            this.skinUUID = this.uuid;
        this.botProfile = new GameProfile(this.uuid, uncoloredName.length() > 16 ? uncoloredName.substring(0, 16) : uncoloredName);
        setSkin(this.skinUUID);
        this.metadata.setMetadata(10, this.skinFlags);
        this.metadata.setBit(16, 0x02, this.hideCape);
        this.metadata.setMetadata(17, (float) (this.activeEffects.containsKey(PotionEffectType.ABSORPTION) ? this.activeEffects.get(PotionEffectType.ABSORPTION).getAmplifier() : 0));
        this.metadata.setMetadata(18, 0);//score
    }

    public PlayerListEntry getListEntry() {
        return new PlayerListEntry(getGameProfile(), GameMode.CREATIVE, 0, MessageFormat.formatMessage(this.name));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        if (this.name == null)
            return;
        String nick = MessageFormat.formatMessage(this.name = newName).getFullText().trim();
        if (nick.length() < 17) {
            super.setCustomName(nick);
            this.metadata.setMetadata(2, nick);
            this.botProfile = new GameProfile(this.uuid, nick);
            this.botProfile.getProperties().put("textures", this.skin);
            updateMetadata();
        }
        MCThunder.getEntryListHandler().refresh(this);
        if (nick.length() < 17) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
            this.entityID = getNextID();
            long chunk = getChunk();
            for (Player pl : MCThunder.getPlayers()) {
                pl.sendPacket(destroyEntitiesPacket);
                if (pl.getWorld().equals(getWorld()) && pl.isColumnLoaded(chunk))
                    for (Packet packet : getPackets())
                        pl.sendPacket(packet);
            }
        }
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
                value = value.substring(0, (s == -1 ? value.length() : s) - 2) + ",\"CAPE\":{\"url\":\"" + this.capeURL + "\"}}}";
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

    public Packet getPacket() {
        return new ServerSpawnPlayerPacket(this.entityID, this.uuid, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), 0, getMetadata().getMetadataArray());
    }

    public void showCape(boolean show) {
        this.metadata.setBit(16, 0x02, this.hideCape = !show);
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 1 : this.skinFlags & ~1));
        updateMetadata();
    }

    public void showJacket(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? (this.skinFlags | 2) : this.skinFlags & ~2));
        updateMetadata();
    }

    public void showLeftSleeve(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 4 : this.skinFlags & ~4));
        updateMetadata();
    }

    public void showRightSleeve(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 8 : this.skinFlags & ~8));
        updateMetadata();
    }

    public void showLeftPantLeg(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 16 : this.skinFlags & ~16));
        updateMetadata();
    }

    public void showRightPantLeg(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 32 : this.skinFlags & ~32));
        updateMetadata();
    }

    public void showHat(boolean show) {
        this.metadata.setMetadata(10, this.skinFlags = (byte) (show ? this.skinFlags | 64 : this.skinFlags & ~64));
        updateMetadata();
    }

    public abstract void unload();

    public abstract void load();

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}