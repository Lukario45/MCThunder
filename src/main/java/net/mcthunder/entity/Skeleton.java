package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Skeleton extends LivingEntity {
    private byte skeletonType;//0 normal, 1 wither

    public Skeleton(Location location) {
        super(location);
        this.type = EntityType.SKELETON;
        this.metadata.setMetadata(13, this.skeletonType = (byte) 0);
    }

    @Override
    public void ai() {

    }

    public void setSkeletonType(byte skeletonType) {
        this.metadata.setMetadata(13, this.skeletonType = skeletonType);
        updateMetadata();
    }

    public byte getSkeletonType() {
        return this.skeletonType;
    }
}