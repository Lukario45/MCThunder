package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class CaveSpider extends LivingEntity {
    private boolean climbing;

    public CaveSpider(Location location) {//Extends spider for practical purposes
        super(location);
        this.type = EntityType.CAVESPIDER;
        this.climbing = false;
        this.metadata.setMetadata(16, (byte) (this.climbing ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}