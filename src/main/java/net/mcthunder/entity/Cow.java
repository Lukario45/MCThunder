package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Cow extends Ageable {
    public Cow(Location location) {
        super(location);
        this.type = EntityType.COW;
    }

    @Override
    public void ai() {

    }
}