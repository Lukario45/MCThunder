package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Pig extends Ageable {
    private boolean hasSaddle;

    public Pig(Location location) {
        super(location);
        this.type = EntityType.PIG;
        this.metadata.setMetadata(16, (byte) ((this.hasSaddle = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setHasSaddle(boolean hasSaddle) {
        this.metadata.setMetadata(16, (byte) ((this.hasSaddle = hasSaddle) ? 1 : 0));
        updateMetadata();
    }

    public boolean hasSaddle() {
        return this.hasSaddle;
    }
}