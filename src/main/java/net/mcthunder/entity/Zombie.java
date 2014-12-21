package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Zombie extends LivingEntity {
    private boolean child, villager, converting;

    public Zombie(Location location) {
        super(location);
        this.type = EntityType.ZOMBIE;
        this.child = false;
        this.villager = false;
        this.converting = false;
        this.metadata.setMetadata(12, (byte) (this.child ? 1 : 0));
        this.metadata.setMetadata(13, (byte) (this.villager ? 1 : 0));
        this.metadata.setMetadata(14, (byte) (this.converting ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}