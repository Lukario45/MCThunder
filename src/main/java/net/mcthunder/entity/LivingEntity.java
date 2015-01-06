package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.PotionEffect;
import net.mcthunder.api.PotionEffectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.*;
import org.spacehq.packetlib.packet.Packet;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public abstract class LivingEntity extends Entity {//TODO: set default max healths for each living entity type
    protected HashMap<PotionEffectType, PotionEffect> activeEffects = new HashMap<>();
    private PotionEffectType latest;
    private boolean alwaysShowName, hasAI;
    protected float health, maxHealth;
    private byte arrows;

    public LivingEntity(Location location) {
        super(location);
        this.maxHealth = 20;
        this.latest = null;
        this.metadata.setMetadata(2, this.customName);
        this.metadata.setMetadata(3, (byte) ((this.alwaysShowName = false) ? 1 : 0));
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(7, 0);//No color until a potion is set
        this.metadata.setMetadata(8, (byte) 0);//False until potion effects are set
        this.metadata.setMetadata(9, this.arrows = (byte) 0);
        this.metadata.setMetadata(15, (byte) ((this.hasAI = false) ? 1 : 0));
    }

    public abstract void ai();

    public void setCustomName(String name) {
        this.metadata.setMetadata(2, this.customName = name);
        updateMetadata();
    }

    public String getName() {
        return this.customName;
    }

    public void setAlwaysShowName(boolean show) {
        this.metadata.setMetadata(3, (byte) ((this.alwaysShowName = show) ? 1 : 0));
        updateMetadata();
    }

    public boolean alwaysShowName() {
        return this.alwaysShowName;
    }

    public void setHealth(float health) {
        this.metadata.setMetadata(6, this.health = health);
        updateMetadata();
    }

    public float getHealth() {
        return this.health;
    }

    public void addPotionEffect(PotionEffect p) {
        this.latest = p.getType();
        this.activeEffects.put(p.getType(), p);
        this.metadata.setMetadata(7, p.getType().getColor());
        this.metadata.setMetadata(8, (byte) (p.isAmbient() ? 1 : 0));
        updateMetadata();
        //TODO: Have the duration tick down and then run out also for all potion effect stuff have it actually send effects and things to player
    }

    public void removePotionEffect(PotionEffectType p) {
        this.activeEffects.remove(p);
        if (this.latest != null && p.equals(this.latest)) {
            if (this.activeEffects.isEmpty()) {
                this.latest = null;
                this.metadata.setMetadata(7, 0);
                this.metadata.setMetadata(8, (byte) 0);
                updateMetadata();
                return;
            }
            this.latest = (PotionEffectType) this.activeEffects.keySet().toArray()[0];
            this.metadata.setMetadata(7, this.latest.getColor());
            this.metadata.setMetadata(8, (byte) (this.activeEffects.get(this.latest).isAmbient() ? 1 : 0));
            updateMetadata();
        }
    }

    public void clearPotionEffects() {
        this.activeEffects.clear();
        this.latest = null;
        this.metadata.setMetadata(7, 0);
        this.metadata.setMetadata(8, (byte) 0);
        updateMetadata();
    }

    public Collection<PotionEffect> getActiveEffects() {
        return this.activeEffects.values();
    }

    protected void moveForward(double distance) {
        Location old = getLocation();
        this.location.setX(this.location.getX() - distance * Math.sin(Math.toRadians(this.location.getYaw())));
        this.location.setZ(this.location.getZ() + distance * Math.cos(Math.toRadians(this.location.getYaw())));
        Packet update;
        int packed = (int) (distance * 32);
        if (packed > Byte.MAX_VALUE || packed < Byte.MIN_VALUE)
            update = new ServerEntityTeleportPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), isOnGround());
        else
            update = new ServerEntityPositionPacket(this.entityID, this.location.getX() - old.getX(), 0, this.location.getZ() - old.getZ(), isOnGround());
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(update);
    }

    protected void moveBackward(double distance) {
        moveForward(-distance);
    }

    protected void moveRight(double distance) {
        Location old = getLocation();
        this.location.setX(this.location.getX() - distance * Math.cos(Math.toRadians(this.location.getYaw())));
        this.location.setZ(this.location.getZ() + distance * Math.sin(Math.toRadians(this.location.getYaw())));
        Packet update;
        int packed = (int) (distance * 32);
        if (packed > Byte.MAX_VALUE || packed < Byte.MIN_VALUE)
            update = new ServerEntityTeleportPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), isOnGround());
        else
            update = new ServerEntityPositionPacket(this.entityID, this.location.getX() - old.getX(), 0, this.location.getZ() - old.getZ(), isOnGround());
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(update);
    }

    protected void moveLeft(double distance) {
        moveRight(-distance);
    }

    protected void moveUp(double distance) {
        Location old = getLocation();
        this.location.setY(this.location.getY() + distance);
        Packet update;
        int packed = (int) (distance * 32);
        if (packed > Byte.MAX_VALUE || packed < Byte.MIN_VALUE)
            update = new ServerEntityTeleportPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), isOnGround());
        else
            update = new ServerEntityPositionPacket(this.entityID, 0, this.location.getY() - old.getY(), 0, isOnGround());
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(update);
    }

    protected void moveDown(double distance) {
        moveUp(-distance);
    }

    protected void turnRight(double angle) {
        this.location.setYaw((float) ((this.location.getYaw() + angle) % 360));
        Collection<Packet> packets = Arrays.asList(new ServerEntityRotationPacket(this.entityID, this.location.getYaw(), this.location.getPitch(), isOnGround()),
                new ServerEntityHeadLookPacket(this.entityID, this.location.getYaw()));
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                for (Packet update : packets)
                    p.sendPacket(update);
    }

    protected void turnLeft(double angle) {
        turnRight(-angle);
    }

    protected void lookUp(double angle) {
        this.location.setPitch((float) ((this.location.getPitch() + 90 + angle) % 180) - 90);
        Packet update = new ServerEntityRotationPacket(this.entityID, this.location.getYaw(), this.location.getPitch(), isOnGround());
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(update);
    }

    protected void lookDown(double angle) {
        lookUp(-angle);
    }

    public Collection<Packet> getPackets() {
        return Arrays.asList(getPacket(), new ServerEntityMetadataPacket(this.entityID, getMetadata().getMetadataArray()),
                new ServerEntityPositionRotationPacket(this.entityID, 0, 0, 0, this.location.getYaw(), this.location.getPitch(), isOnGround()),
                new ServerEntityHeadLookPacket(this.entityID, this.location.getYaw()));
    }
}