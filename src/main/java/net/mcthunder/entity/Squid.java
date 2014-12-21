package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Squid extends LivingEntity {
    public Squid(Location location) {
        super(location);
        this.type = EntityType.SQUID;
    }

    @Override
    public void ai() {

    }
}