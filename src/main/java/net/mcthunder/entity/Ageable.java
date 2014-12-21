package net.mcthunder.entity;

import net.mcthunder.api.Location;

public abstract class Ageable extends LivingEntity {
    private byte age;

    public Ageable(Location location) {
        super(location);
        this.age = 1;
        this.metadata.setMetadata(12, this.age);
    }
}