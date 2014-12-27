package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Ocelot extends Tameable {
    private byte catType;

    public Ocelot(Location location) {
        super(location);
        this.type = EntityType.OCELOT;
        this.metadata.setMetadata(18, this.catType = (byte) 0);
    }

    @Override
    public void ai() {

    }

    public void setCatType(byte catType) {
        this.metadata.setMetadata(18, this.catType = catType);
        updateMetadata();
    }

    public byte getCatType() {
        return this.catType;
    }
}