package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class MagmaCube extends Slime {
    public MagmaCube(Location location) {
        super(location);
        this.type = EntityType.MAGMA_CUBE;
    }

    @Override
    public void ai() {

    }
}