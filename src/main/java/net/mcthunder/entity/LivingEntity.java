package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public abstract class LivingEntity extends Entity {//TODO: set default healths for each
    private boolean alwaysShowName, hasAI, potionAmbient;
    protected float health;
    private int potionColor;
    private byte arrows;

    public LivingEntity(Location location) {
        super(location);
        this.alwaysShowName = false;
        this.health = 20;
        this.hasAI = false;
        this.potionColor = 0;
        this.potionAmbient = false;
        this.arrows = 0;
        this.metadata.setMetadata(2, this.customName);
        this.metadata.setMetadata(3, (byte) (this.alwaysShowName ? 1 : 0));
        this.metadata.setMetadata(6, this.health);
        this.metadata.setMetadata(7, this.potionColor);
        this.metadata.setMetadata(8, (byte) (this.potionAmbient ? 1 : 0));
        this.metadata.setMetadata(9, this.arrows);
        this.metadata.setMetadata(15, (byte) (this.hasAI ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.valueOf(this.type.getName()), this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), 0, 0, 0, 0, getMetadata().getMetadataArray());
    }

    public abstract void ai();
}