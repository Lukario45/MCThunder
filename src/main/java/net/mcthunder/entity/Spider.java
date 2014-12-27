package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Spider extends LivingEntity {
    private boolean climbing;

    public Spider(Location location) {
        super(location);
        this.type = EntityType.SPIDER;
        this.metadata.setMetadata(16, (byte) ((this.climbing = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setClimbing(boolean climbing) {
        this.metadata.setMetadata(16, (byte) ((this.climbing = climbing) ? 1 : 0));
        updateMetadata();
    }

    public boolean isClimbing() {
        return this.climbing;
    }
}