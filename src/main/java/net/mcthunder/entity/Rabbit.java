package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Rabbit extends Ageable {
    private byte rabbitType;

    public Rabbit(Location location) {
        super(location);
        this.type = EntityType.RABBIT;
        this.rabbitType = (byte) 0;
        this.metadata.setMetadata(18, this.rabbitType);
    }

    @Override
    public void ai() {

    }
}
