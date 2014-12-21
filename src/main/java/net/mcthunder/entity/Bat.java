package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Bat extends LivingEntity {
    private boolean hanging;
    public Bat(Location location) {
        super(location);
        this.type = EntityType.BAT;
        this.hanging = false;
        this.metadata.setMetadata(16, (byte) (this.hanging ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}