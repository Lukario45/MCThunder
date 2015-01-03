package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Wolf extends Tameable {
    private boolean angry, begging;
    private byte collarColor;

    public Wolf(Location location) {
        super(location);
        this.type = EntityType.WOLF;
        this.metadata.setBit(16, 0x02, this.angry = false);
        this.metadata.setMetadata(18, this.health);
        this.metadata.setMetadata(19, (byte) ((this.begging = false) ? 1 : 0));
        this.metadata.setMetadata(20, this.collarColor = MetadataConstants.ColorFlags.RED);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.WOLF, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setAggressive(boolean angry) {
        this.metadata.setBit(16, 0x02, this.angry = angry);
        updateMetadata();
    }

    public boolean isAggressive() {
        return this.angry;
    }

    public void setHealth(float health) {
        this.health = health;
        this.metadata.setMetadata(6, this.health);
        this.metadata.setMetadata(18, this.health);
        updateMetadata();
    }

    public float getHealth() {
        return this.health;
    }

    public void setBegging(boolean begging) {
        this.metadata.setMetadata(19, (byte) ((this.begging = begging) ? 1 : 0));
        updateMetadata();
    }

    public boolean isBegging() {
        return this.begging;
    }

    public void setCollarColor(byte color) {
        this.metadata.setMetadata(20, this.collarColor = color);
        updateMetadata();
    }

    public byte getCollarColor() {
        return this.collarColor;
    }
}