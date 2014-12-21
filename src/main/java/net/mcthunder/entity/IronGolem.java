package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class IronGolem extends LivingEntity {
    private boolean playerCreated;

    public IronGolem(Location location) {
        super(location);
        this.type = EntityType.IRON_GOLEM;
        this.playerCreated = false;
        this.metadata.setMetadata(16, (byte) (this.playerCreated ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}