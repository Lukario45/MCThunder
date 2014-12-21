package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Blaze extends LivingEntity {
    public Blaze(Location location) {
        super(location);
        this.type = EntityType.BLAZE;
        this.metadata.setMetadata(16, (byte) (this.fireTicks > 0 ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}