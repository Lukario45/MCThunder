package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Rabbit extends Ageable {
    private byte rabbitType;

    public Rabbit(Location location) {
        super(location);
        this.type = EntityType.RABBIT;
        this.metadata.setMetadata(18, this.rabbitType = (byte) 0);
    }

    @Override
    public void ai() {

    }

    public void setRabbitType(byte rabbitType) {
        this.metadata.setMetadata(18, this.rabbitType = rabbitType);
        updateMetadata();
    }

    public byte getRabbitType() {
        return this.rabbitType;
    }
}