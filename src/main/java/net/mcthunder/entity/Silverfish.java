package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Silverfish extends LivingEntity {
    public Silverfish(Location location) {
        super(location);
        this.type = EntityType.SILVERFISH;
    }

    @Override
    public void ai() {

    }
}