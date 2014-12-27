package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Ghast extends LivingEntity {
    private boolean attacking;

    public Ghast(Location location) {
        super(location);
        this.type = EntityType.GHAST;
        this.metadata.setMetadata(16, (byte) ((this.attacking = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setAttacking(boolean attacking) {
        this.metadata.setMetadata(16, (byte) ((this.attacking = attacking) ? 1 : 0));
        updateMetadata();
    }

    public boolean isAttacking() {
        return this.attacking;
    }
}