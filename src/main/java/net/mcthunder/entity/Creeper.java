package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Creeper extends LivingEntity {
    private byte fuse;
    private boolean powered;

    public Creeper(Location location) {
        super(location);
        this.type = EntityType.CREEPER;
        this.powered = false;
        this.fuse = (byte) -1;
        this.metadata.setMetadata(16, this.fuse);
        this.metadata.setMetadata(17, (byte) (this.powered ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}