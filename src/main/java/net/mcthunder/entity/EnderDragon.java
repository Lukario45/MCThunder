package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class EnderDragon extends LivingEntity {
    public EnderDragon(Location location) {
        super(location);
        this.type = EntityType.ENDER_DRAGON;
    }

    @Override
    public void ai() {

    }
}