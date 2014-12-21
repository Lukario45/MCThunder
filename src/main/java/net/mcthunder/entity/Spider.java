package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Spider extends LivingEntity {
    private boolean climbing;

    public Spider(Location location) {
        super(location);
        this.type = EntityType.SPIDER;
        this.climbing = false;
        this.metadata.setMetadata(16, (byte) (this.climbing ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}