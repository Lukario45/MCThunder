package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Guardian extends LivingEntity {
    private boolean isElder;

    public Guardian(Location location) {
        super(location);
        this.isElder = false;
        this.type = EntityType.GUARDIAN;
    }

    @Override
    public void ai() {

    }
}