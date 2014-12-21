package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Skeleton extends LivingEntity {
    private byte skeletonType;//0 normal, 1 wither

    public Skeleton(Location location) {
        super(location);
        this.type = EntityType.SKELETON;
        this.skeletonType = (byte) 0;
        this.metadata.setMetadata(13, this.skeletonType);
    }

    @Override
    public void ai() {

    }
}