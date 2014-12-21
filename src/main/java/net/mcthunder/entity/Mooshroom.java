package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Mooshroom extends Cow {
    public Mooshroom(Location location) {
        super(location);
        this.type = EntityType.MOOSHROOM;
    }

    @Override
    public void ai() {

    }
}