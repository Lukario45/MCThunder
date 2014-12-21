package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Pig extends Ageable {
    private boolean hasSaddle;

    public Pig(Location location) {
        super(location);
        this.type = EntityType.PIG;
        this.hasSaddle = false;
        this.metadata.setMetadata(16, (byte) (this.hasSaddle ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}