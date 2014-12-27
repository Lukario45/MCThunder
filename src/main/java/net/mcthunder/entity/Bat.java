package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Bat extends LivingEntity {
    private boolean hanging;

    public Bat(Location location) {
        super(location);
        this.type = EntityType.BAT;
        this.metadata.setMetadata(16, (byte) ((this.hanging = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setHanging(boolean hanging) {
        this.metadata.setMetadata(16, (byte) ((this.hanging = hanging) ? 1 : 0));
        updateMetadata();
    }

    public boolean isHanging() {
        return this.hanging;
    }
}