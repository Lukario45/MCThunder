package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class IronGolem extends LivingEntity {
    private boolean playerCreated;

    public IronGolem(Location location) {
        super(location);
        this.type = EntityType.IRON_GOLEM;
        this.metadata.setMetadata(16, (byte) ((this.playerCreated = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setPlayerCreated(boolean playerCreated) {
        this.metadata.setMetadata(16, (byte) ((this.playerCreated = playerCreated) ? 1 : 0));
        updateMetadata();
    }

    public boolean isPlayerCreated() {
        return this.playerCreated;
    }
}