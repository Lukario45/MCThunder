package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Witch extends LivingEntity {
    private boolean aggressive;

    public Witch(Location location) {
        super(location);
        this.type = EntityType.WITCH;
        this.metadata.setMetadata(21, (byte) ((this.aggressive = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setAggressive(boolean aggressive) {
        this.metadata.setMetadata(21, (byte) ((this.aggressive = aggressive) ? 1 : 0));
        updateMetadata();
    }

    public boolean isAggressive() {
        return this.aggressive;
    }
}