package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Ocelot extends Tameable {
    private byte catType;

    public Ocelot(Location location) {
        super(location);
        this.type = EntityType.OCELOT;
        this.catType = (byte) 0;
        this.metadata.setMetadata(18, this.catType);
    }

    @Override
    public void ai() {

    }
}