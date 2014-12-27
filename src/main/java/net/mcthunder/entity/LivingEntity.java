package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public abstract class LivingEntity extends Entity {//TODO: set default max healths for each living entity type
    private boolean alwaysShowName, hasAI, potionAmbient;
    protected float health, maxHealth;
    private int potionColor;
    private byte arrows;

    public LivingEntity(Location location) {
        super(location);
        this.maxHealth = 20;
        this.metadata.setMetadata(2, this.customName);
        this.metadata.setMetadata(3, (byte) ((this.alwaysShowName = false) ? 1 : 0));
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(7, this.potionColor = 0);//TODO: Base off of active effects
        this.metadata.setMetadata(8, (byte) ((this.potionAmbient = false) ? 1 : 0));
        this.metadata.setMetadata(9, this.arrows = (byte) 0);
        this.metadata.setMetadata(15, (byte) ((this.hasAI = false) ? 1 : 0));
    }

    public Packet getPacket() {//TODO: For efficiency move into each own class so will not have to rely on MobType.valueOf, Also add headYaw and mot instead of using 0's
        return new ServerSpawnMobPacket(this.entityID, MobType.valueOf(this.type.getName()), this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), 0, 0, 0, 0, getMetadata().getMetadataArray());
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
}