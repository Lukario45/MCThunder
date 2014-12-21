package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class SnowGolem extends LivingEntity {
    public SnowGolem(Location location) {
        super(location);
        this.type = EntityType.SNOW_GOLEM;
    }

    @Override
    public void ai() {

    }
}