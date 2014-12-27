package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;

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