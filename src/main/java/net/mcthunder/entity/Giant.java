package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Giant extends Zombie {
    public Giant(Location location) {
        super(location);
        this.type = EntityType.GIANT;
    }
}