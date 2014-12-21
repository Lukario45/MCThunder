package net.mcthunder.entity;

import net.mcthunder.api.Location;

public abstract class Tameable extends Ageable {
    private boolean sitting, tame;
    private String ownerName;

    public Tameable(Location location) {
        super(location);
        this.sitting = false;
        this.tame = false;
        this.ownerName = "";
        this.metadata.setBit(16, 0x01, this.sitting);
        this.metadata.setBit(16, 0x04, this.tame);
        this.metadata.setMetadata(17, this.ownerName);
    }
}