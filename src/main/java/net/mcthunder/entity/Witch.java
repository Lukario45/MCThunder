package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Witch extends LivingEntity {
    private boolean aggressive;

    public Witch(Location location) {
        super(location);
        this.type = EntityType.WITCH;
        this.aggressive = false;
        this.metadata.setMetadata(21, (byte) (this.aggressive ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}