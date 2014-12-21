package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Ghast extends LivingEntity {
    private boolean attacking;

    public Ghast(Location location) {
        super(location);
        this.type = EntityType.GHAST;
        this.attacking = false;
        this.metadata.setMetadata(16, (byte) (this.attacking ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}