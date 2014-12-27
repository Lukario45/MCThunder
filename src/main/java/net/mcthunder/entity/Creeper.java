package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Creeper extends LivingEntity {
    private byte fuse;
    private boolean powered;

    public Creeper(Location location) {
        super(location);
        this.type = EntityType.CREEPER;
        this.metadata.setMetadata(16, this.fuse = (byte) -1);
        this.metadata.setMetadata(17, (byte) ((this.powered = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setFuse(byte fuse) {
        this.metadata.setMetadata(16, this.fuse = fuse);
        updateMetadata();
    }

    public byte getFuse() {
        return this.fuse;
    }

    public void setPowered(boolean powered) {
        this.metadata.setMetadata(17, (byte) ((this.powered = powered) ? 1 : 0));
        updateMetadata();
    }

    public boolean isPowered() {
        return this.powered;
    }
}