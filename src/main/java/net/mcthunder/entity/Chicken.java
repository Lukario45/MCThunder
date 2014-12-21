package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Chicken extends LivingEntity {
    public Chicken(Location location) {
        super(location);
        this.type = EntityType.CHICKEN;
    }

    @Override
    public void ai() {

    }
}